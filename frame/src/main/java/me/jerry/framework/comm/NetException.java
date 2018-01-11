package me.jerry.framework.comm;

import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import me.jerry.framework.exception.CustomException;

/**网络异常exception
 * @author JerryGeng
 */
public class NetException extends CustomException {

    public final static int ERR_CODE_UNDEFINED = 0;
    public final static int ERR_CODE_TIMEOUT = 1;
    public final static int ERR_HAND_SHAKE = 2;
    public final static int ERR_URL_MALFORMED = 3;
    public final static int ERR_CONNECT = 4;

    public final static int REDIRECT_MULTIPLE_CHOICES = 300;
    public final static int REDIRECT_MOVED_PERMANENTLY = 301;
    public final static int REDIRECT_FOUND = 302;
    public final static int REDIRECT_SEE_OTHER = 303;
    public final static int REDIRECT_NOT_MODIFIED = 304;
    public final static int REDIRECT_USE_PROXY = 305;
    public final static int REDIRECT_UNUSED = 306;
    public final static int REDIRECT_TEMPORARY_REDIRECT = 307;

    public final static int ERR_BAD_REQUEST = 400;
    public final static int ERR_UNAUTHORIZED = 401;
    public final static int ERR_PAYMENT_GRANTED = 402;
    public final static int ERR_FORBIDDEN = 403;
    public final static int ERR_FILE_NOT_FOUND = 404;
    public final static int ERR_METHOD_NOT_ALLOWED = 405;
    public final static int ERR_NOT_ACCEPTABLE = 406;
    public final static int ERR_PROXY_AUTHENTICATION_REQUIRED = 407;
    public final static int ERR_REQUEST_TIMEOUT = 408;
    public final static int ERR_CONFLICT = 409;
    public final static int ERR_GONE = 410;
    public final static int ERR_LENGTH_REQUIRED = 411;
    public final static int ERR_PRECONDITION_FAILED = 412;
    public final static int ERR_REQUEST_ENTITY_TOO_LARGE = 413;
    public final static int ERR_REQUEST_URI_TOO_LARGE = 414;
    public final static int ERR_UNSUPPORTED_MEDIA_TYPE = 415;
    public final static int ERR_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public final static int ERR_EXPECTATION_FAILED = 417;
    public final static int ERR_UNPROCESSABLE_ENTITY = 422;
    public final static int ERR_LOCKED = 423;
    public final static int ERR_FAILED_DEPENDENCY = 424;

    public final static int SERVER_ERR_INTERNAL_SERVER_ERR = 500;
    public final static int SERVER_ERR_NOT_IMPLEMENTED = 501;
    public final static int SERVER_ERR_BAD_GATEWAY = 502;
    public final static int SERVER_ERR_SERVICE_UNAVAILABLE = 503;
    public final static int SERVER_ERR_GATEWAY_TIMEOUT = 504;
    public final static int SERVER_ERR_HTTP_VERSION_NOT_SUPPORTED = 505;
    public final static int SERVER_ERR_INSUFFICIENT_STORAGE = 507;

    private int errCode = ERR_CODE_UNDEFINED;

    private String detailMessage;



    public static final String TIME_OUT = "访问超时";
    public static final String BIND_ERR = "套接字绑定错误";
    public static final String PROTOCOL_ERR = "协议错误";
    public static final String CONNECT_ERR = "网络连接失败";
    public static final String NEED_RETRY = "请重试";
    public static final String MALFORMED_URL = "URL出错";
    public static final String NO_ROUTE_TO_HOST = "无法连接到主机";
    public static final String PORT_UNREACHABLE = "ICMP端口不可达";
    public static final String SOCKET_ERR = "底层协议错误";
    public static final String UNKNOWN_HOST = "无法找到主机";
    public static final String UNKNOWN_SERVICE = "未知服务异常";
    public static final String SSL_HAND_SHAKE_ERR = "SSL握手失败";
    public static final String UNKNOWN_EXCEPTION = "未知通讯错误";


    public NetException(int code, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        if(throwable != null){
            if(throwable instanceof SocketTimeoutException){
                this.detailMessage = TIME_OUT;
            } else if(throwable instanceof java.net.BindException){
                this.detailMessage = BIND_ERR;
            } else if(throwable instanceof java.net.ProtocolException){
                this.detailMessage = PROTOCOL_ERR;
            } else if(throwable instanceof java.net.ConnectException){
                this.detailMessage = CONNECT_ERR;
            } else if(throwable instanceof java.net.HttpRetryException){
                this.detailMessage = NEED_RETRY;
            } else if(throwable instanceof java.net.MalformedURLException){
                this.detailMessage = MALFORMED_URL;
            } else if(throwable instanceof java.net.NoRouteToHostException){
                this.detailMessage = NO_ROUTE_TO_HOST;
            } else if(throwable instanceof java.net.PortUnreachableException){
                this.detailMessage = PORT_UNREACHABLE;
            } else if(throwable instanceof java.net.SocketException){
                this.detailMessage = SOCKET_ERR;
            } else if(throwable instanceof java.net.UnknownHostException){
                this.detailMessage = UNKNOWN_HOST;
            } else if(throwable instanceof java.net.UnknownServiceException){
                this.detailMessage = UNKNOWN_SERVICE;
            } else if(throwable instanceof SSLHandshakeException){
                this.detailMessage = SSL_HAND_SHAKE_ERR;
            } else{
                if(detailMessage != null && detailMessage.length() > 0) {
                    this.detailMessage = UNKNOWN_EXCEPTION + throwable.toString();
                }
            }
        }
    }

    public int getErrCode() {
        return errCode;
    }

    public NetException(int errCode) {
        super((String)null);
        this.errCode = errCode;
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }
}
