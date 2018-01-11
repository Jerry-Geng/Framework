package me.jerry.framedemo;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import me.jerry.framework.android.AdapterWithViewFrame;
import me.jerry.framework.android.FragmentFrame;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.killerengine.transmit.bluetooth.BluetoothManager;
import me.jerry.killerengine.transmit.bluetooth.BluetoothResponse;
import me.jerry.killerengine.transmit.bluetooth.OnConnectListener;
import me.jerry.killerengine.transmit.bluetooth.OnReceiveListener;

/**
 * Created by Jerry on 2017/8/30.
 */

public class ListFragment extends FragmentFrame implements AdapterView.OnItemClickListener, View.OnClickListener {
    @AutoFindView(listeners = AdapterView.OnItemClickListener.class)
    private ListView lv_bd;
    @AutoFindView(listeners = View.OnClickListener.class)
    private Button btSend;
    private BluetoothManager bm = null;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice bd = (BluetoothDevice) lv_bd.getAdapter().getItem(position);
        BluetoothManager bm = new BluetoothManager(new OnReceiveListener() {
            @Override
            public void onReceive(BluetoothResponse responseEntity) {

            }

        }, new Handler(Looper.getMainLooper()));
        bm.connect(bd, new OnConnectListener() {
            @Override
            public boolean onConnected(BluetoothManager bluetoothManager, Handler mHandler) {
                ListFragment.this.bm = bluetoothManager;
                Toast.makeText(getActivity().getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                startFragment(TalkFragment.class, CALL_TYPE_CREATE_NEW, null);
                return true;
            }

            @Override
            public void onConnectedErr(String errMsg, BluetoothManager bluetoothManager) {
                Toast.makeText(getActivity().getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
                if(errMsg.equals(BluetoothManager.MESSAGE_EXIT) || errMsg.equals(BluetoothManager.MESSAGE_FORCE_CLOSE)) {
                    startFragment(ListFragment.class, CALL_TYPE_CLEAR_TOP, null);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothManager.initBluetooth(new OnConnectListener() {
            boolean returnvalue = false;
            @Override
            public boolean onConnected(final BluetoothManager bluetoothManager, Handler mHandler) {
                final ConditionVariable cv = new ConditionVariable();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setMessage("A request from " + bluetoothManager.getClientName());
                        builder.setTitle("Bluetooth Request");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startFragment(TalkFragment.class, CALL_TYPE_CREATE_NEW, null);
                                returnvalue = true;
                                cv.open();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                returnvalue = false;
                                cv.open();
                            }
                        });
                        builder.create().show();
                    }
                });
                cv.block();
                return returnvalue;
            }

            @Override
            public void onConnectedErr(String errMsg, BluetoothManager bluetoothManager) {
                Toast.makeText(getActivity().getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
                if(errMsg.equals(BluetoothManager.MESSAGE_EXIT) || errMsg.equals(BluetoothManager.MESSAGE_FORCE_CLOSE)) {
                    startFragment(ListFragment.class, CALL_TYPE_CLEAR_TOP, null);
                }
            }
        }, null, new OnReceiveListener() {
            @Override
            public void onReceive(BluetoothResponse responseEntity) {
                Toast.makeText(getActivity().getApplicationContext(), responseEntity.getDstMac() + ":::" + responseEntity.getSrcMac(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        lv_bd.setAdapter(new BdAdapter(BluetoothManager.getBluetoothDevices(), lv_bd));
    }

    @Override
    public void onClick(View v) {
        try {
            bm.sendMessage("hello, 世界");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class BdAdapter extends AdapterWithViewFrame<BluetoothDevice> {

        public BdAdapter(List<BluetoothDevice> dataList, AdapterView listView) {
            super(dataList, listView);
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public void bindValueToView(IViewHolder viewHolder, BluetoothDevice data, int position) {
            ViewHolder viewHolder1 = (ViewHolder) viewHolder;
            viewHolder1.tv_bd_name.setText(data.getName());
            viewHolder1.tv_bd_status.setText("" + data.getBondState());
        }

        @Override
        protected int getLayoutId() {
            return R.layout.adapter_bluetooth;
        }

        @Override
        protected IViewHolder getViewHolder() {
            return new ViewHolder();
        }

        public static class ViewHolder implements IViewHolder {
            @AutoFindView
            public TextView tv_bd_name;
            @AutoFindView
            public TextView tv_bd_status;
        }
    }
}
