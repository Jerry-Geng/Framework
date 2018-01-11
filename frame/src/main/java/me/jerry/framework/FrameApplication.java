package me.jerry.framework;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jerry on 2017/8/11.
 */

public class FrameApplication extends Application {
    public static Context mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("frameApplication", "onCreate");
        Toast.makeText(this, "application onCreate", Toast.LENGTH_LONG).show();
        mApp = this;
    }
}
