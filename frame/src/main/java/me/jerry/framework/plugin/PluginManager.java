package me.jerry.framework.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2017/8/24.
 */

public class PluginManager {
    private static final String TAG = PluginManager.class.getName();
    public final static String PLUGIN_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin";
    public final static String MAIN_CLASS = ".plugin.Main";
    public final static String MAIN_METHOD = "plugin_main";
    public final static String LOAD_FUNC_METHOD = "load_func";
    public static List<Method> loadFuncMethods = new ArrayList<Method>();
    public final static List<Plugin> pluginList = new ArrayList<>();

//    public final static PluginActionListener listener = new PluginActionListener() {
//        @Override
//        public void onLoadFunc(Builder builder) {
//            for(Method method : loadFuncMethods) {
//                try {
//                    method.invoke(null, builder);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                    Log.w(TAG, "鎻掍欢涓叆鍙ｆ柟娉曡闂潈闄愪笉瓒�");
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                    Log.w(TAG, "鎻掍欢涓叆鍙ｆ柟娉曞弬鏁版牸寮忛敊璇�");
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                    Log.w(TAG, "鎻掍欢涓叆鍙ｆ柟娉曞繀椤绘槸闈欐�佹柟娉�");
//                }
//            }
//        }
//    };


    public static void loadPlugins(Context context) {
        File odex = context.getDir("plugin_dex", Context.MODE_PRIVATE);
        File pluginRoot = new File(PLUGIN_DIR);
        List<File> plugins = new ArrayList<File>();
        Resources resources = null;
        if(pluginRoot.exists() && pluginRoot.isDirectory()) {
            loadPluginFiles(pluginRoot, plugins);
            for(File plugin : plugins) {
                try {
                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
                    addAssetPath.invoke(assetManager, plugin.getAbsolutePath());
                    resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                DexClassLoader dexClassLoader = new DexClassLoader(plugin.getAbsolutePath(), odex.getAbsolutePath(), null, context.getClassLoader());
                try {
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{plugin.toURI().toURL()}, context.getClassLoader());
                    Class<?> clazz = classLoader.loadClass(context.getPackageName() + MAIN_CLASS);
                    Method method = clazz.getDeclaredMethod(MAIN_METHOD, Object[].class);
                    Object object = method.invoke(null, new Object[]{new Object[]{context, resources}});
                    if(object instanceof Plugin) {
                        ((Plugin) object).classLoader = classLoader;
                        pluginList.add((Plugin)object);
                    }
//                    loadFuncMethods.add(clazz.getDeclaredMethod(LOAD_FUNC_METHOD, MenuPage.Builder.class));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Log.w(TAG, "class " + context.getPackageName() + MAIN_CLASS + " not found");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Log.w(TAG, "method " + MAIN_METHOD + " not found");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.w(TAG, "method " + MAIN_METHOD + " is not access.");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Log.w(TAG, "arguments is invalid in method " + MAIN_METHOD);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Log.w(TAG, "method " + MAIN_METHOD + " may be not static");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private static void loadPluginFiles(File dir, List<File> plugins) {
        File[] files = dir.listFiles();
        for(File file : files) {
            if(file.isFile() && (file.getAbsolutePath().endsWith(".zip") || file.getAbsolutePath().endsWith(".apk") || file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".aar") || file.getAbsolutePath().endsWith(".dex"))) {
                plugins.add(file);
            } else if(file.isDirectory()) {
                loadPluginFiles(file, plugins);
            }
        }
    }

//    public static interface PluginActionListener{
//        public void onLoadFunc(MenuPage.Builder builder);
//    }
}
