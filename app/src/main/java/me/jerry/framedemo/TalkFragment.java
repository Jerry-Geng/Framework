package me.jerry.framedemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import me.jerry.framework.android.FragmentFrame;
import me.jerry.framework.android.WholeAppCompatActivity;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.killerengine.transmit.bluetooth.BluetoothManager;
import me.jerry.killerengine.transmit.bluetooth.BluetoothResponse;
import me.jerry.killerengine.transmit.bluetooth.OnReceiveListener;

/**
 * Created by Jerry on 2017/8/31.
 */

public class TalkFragment extends FragmentFrame implements View.OnClickListener {
    @AutoFindView
    private EditText et_msg;
    @AutoFindView
    private TextView tv_received;
    @AutoFindView(listeners = View.OnClickListener.class)
    private Button bt_send;

    private BluetoothManager bluetoothManager;
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_talk, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothManager = BluetoothManager.getCurrentBM();
        if(bluetoothManager != null) {
            bluetoothManager.setmOnReceiveListener(new OnReceiveListener() {
                @Override
                public void onReceive(BluetoothResponse responseEntity) {
                    try {
                        tv_received.setText(responseEntity.getSrcMac() + ">>>" + responseEntity.getDstMac() + ":::" + new String((byte[])responseEntity.getData(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ((WholeAppCompatActivity)getActivity()).finishFragment(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(et_msg.length() > 0) {
            try {
                bluetoothManager.sendMessage(et_msg.getText().toString());
                et_msg.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            bluetoothManager.sendMessage(BluetoothManager.MESSAGE_EXIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
