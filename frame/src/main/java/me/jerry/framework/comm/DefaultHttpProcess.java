package me.jerry.framework.comm;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jerry on 2017/8/7.
 */

public class DefaultHttpProcess implements INetProcess {
    @Override
    public byte[] process(CommEntity commEntity) throws NetException {
        URL url = null;
        String requestMethod = "GET";
        switch (commEntity.getRequestType()) {
            case REQUEST_TYPE_DELETE: requestMethod = "DELETE"; break;
            case REQUEST_TYPE_GET: requestMethod = "GET"; break;
            case REQUEST_TYPE_POST: requestMethod = "POST"; break;
            case REQUEST_TYPE_PUT: requestMethod = "PUT"; break;
            case REQUEST_TYPE_HEAD: requestMethod = "HEAD"; break;
            case REQUEST_TYPE_OPTIONS: requestMethod = "OPTIONS"; break;
            case REQUEST_TYPE_PATCH: requestMethod = "PATCH"; break;
            case REQUEST_TYPE_TRACE: requestMethod = "TRACE"; break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        Map<String, Object> params = commEntity.getRequestBean().params;
        if(params != null) {
            Set<String> keys = params.keySet();
            for(String key : keys) {
                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(params.get(key).toString(), "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if(commEntity.getRequestType() == CommEntity.ERequestType.REQUEST_TYPE_GET) {
                url = new URL(commEntity.getRequestBean().url + sb.substring(0, sb.length() - 1));
            } else {
                url = new URL(commEntity.getRequestBean().url);
            }
            Log.i("net process", "url: " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new NetException(NetException.ERR_URL_MALFORMED);
        }
        try {
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setConnectTimeout(commEntity.getConnectTimeout());
            if(!"GET".equals(requestMethod)) {
                httpConnection.setRequestMethod(requestMethod);
            }
            httpConnection.setReadTimeout(commEntity.getRequestTimeout());
            httpConnection.setUseCaches(commEntity.isCacheable());
            httpConnection.setDoInput(true);
            if(commEntity.getRequestType() != CommEntity.ERequestType.REQUEST_TYPE_GET) {
                httpConnection.setDoOutput(true);
            }
            httpConnection.setRequestProperty("Accept-Charset", "utf-8");
//            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Map<String, Object> headers = commEntity.getRequestBean().headers;
            if(headers != null) {
                Set<String> headKeys = headers.keySet();
                for(String key : headKeys) {
                    httpConnection.setRequestProperty(key, headers.get(key).toString());
                }
            }
            if(commEntity.getRequestType() != CommEntity.ERequestType.REQUEST_TYPE_GET) {
                DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());
                outputStream.writeBytes(sb.substring(1, sb.length() - 1));
                outputStream.flush();
                outputStream.close();
            }
            Log.i("http process", "ready to communicate");
            Log.i("net process", "method is " + httpConnection.getRequestMethod());
            int retCode = httpConnection.getResponseCode();
            Log.i("net process", "method is " + httpConnection.getRequestMethod());
            Log.i("http process", "got code: " + retCode);
            if(retCode >= 100 && retCode < 200) {
                // temporary response
                // no handle.
            } else if (retCode >= 200 && retCode < 300) {
                // succeed
                InputStream is = httpConnection.getInputStream();
                byte[] buff = new byte[1024];
                byte[] result = new byte[0];
                int len = 0;
                while((len = is.read(buff)) > 0) {
                    byte[] temp = new byte[result.length + len];
                    System.arraycopy(result, 0, temp, 0, result.length);
                    System.arraycopy(buff, 0, temp, result.length, len);
                    result = temp;
                }
                is.close();
                if(commEntity.getResponseBean() == null) {
                    commEntity.setResponseBean(new CommEntity.ResponseBean());
                    commEntity.getResponseBean().body = result;
                    commEntity.getResponseBean().headers = httpConnection.getHeaderFields();
                }
                return result;
            } else if (retCode >= 300 && retCode < 400) {
                // redirection
                String location = httpConnection.getHeaderField("LOCATION");
                Log.i("netProcess", "redirect to: " + location);
                commEntity.getRequestBean().url = location;
                return process(commEntity);
            } else if (retCode >= 400 && retCode < 500) {
                // bad request
                Log.i("net process", "method is " + httpConnection.getRequestMethod());
                throw new NetException(retCode);
            } else if (retCode >= 500) {
                // server error
                throw new NetException(retCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException(e);
        }
        return new byte[0];
    }

    @Override
    public boolean innerRetry() {
        return false;
    }

    @Override
    public boolean innerCache() {
        return true;
    }
}
