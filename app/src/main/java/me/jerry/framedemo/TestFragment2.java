package me.jerry.framedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import me.jerry.framework.android.FragmentFrame;
import me.jerry.framework.android.WholeAppCompatActivity;
import me.jerry.framework.annotation.AutoFindView;

/**
 * Created by Jerry on 2017/8/21.
 */

public class TestFragment2 extends FragmentFrame implements View.OnClickListener {
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_add;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_delete;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button bt_modify;
    @AutoFindView(listeners = View.OnClickListener.class)
    Button btQuery;
    @AutoFindView(value = me.jerry.framework.R.id.action_bar_back, listeners = View.OnClickListener.class)
    ImageView ivBack;
    @Nullable
    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("fragment2", "oncreateView");
        return inflater.inflate(R.layout.activity_test2, null);
    }


    @Override
    public void onClick(View v) {
        if(R.id.bt_modify == v.getId()) {
//            Dialog dialog = new Dialog(getActivity(), R.style.AppTheme);
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_demo);
//            dialog.setTitle("title area");
//            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//            params.width = 200;
//            params.height = 200;
//            params.x = 0;
//            params.y = 0;
//            params.gravity = Gravity.TOP | Gravity.LEFT;
//            dialog.getWindow().setAttributes(params);
//            dialog.show();

//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    while(true) {
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        new Handler(Looper.getMainLooper()).post(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    WindowManager wm = (WindowManager) getActivity().getApplication().getSystemService(Context.WINDOW_SERVICE);
//                                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_PHONE);
//                                    params.width = 200;
//                                    params.height = 200;
//                                    params.gravity = Gravity.CENTER;
//                                    wm.addView(LayoutInflater.from(getActivity().getApplication()).inflate(R.layout.dialog_demo, null), params);
//                                }
//                            }
//                        );
//                    }
//                }
//            }.start();
            Toast.makeText(getActivity().getApplicationContext(), "listening", Toast.LENGTH_SHORT).show();
        } else if(me.jerry.framework.R.id.action_bar_back == v.getId()) {
            ((WholeAppCompatActivity)getActivity()).finishFragment(this);
        } else if(R.id.bt_delete == v.getId()) {
        }
    }
}
