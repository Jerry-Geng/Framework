package me.jerry.framework.plugin;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.dynamicload.internal.DLPluginPackage;
import com.ryg.utils.DLConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.jerry.framework.R;
import me.jerry.framework.android.FragmentFrame;
import me.jerry.framework.annotation.AutoFindView;

/**
 * Created by Jerry on 2017/8/24.
 */

public class PluginListFragment extends FragmentFrame implements AdapterView.OnItemClickListener {
    @AutoFindView(listeners = AdapterView.OnItemClickListener.class)
    private ListView lvPlugin;
    private List<DLPluginPackage> pluginPackageList = new ArrayList<>();

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plugin_list, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        DLPluginManager pm = DLPluginManager.getInstance(getActivity());
        File pluginRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin");
        for(File plugin : pluginRoot.listFiles()) {
            if(plugin.getAbsolutePath().endsWith(".apk")) {
                DLPluginPackage dlPluginPackage = pm.loadApk(plugin.getAbsolutePath());
                if(dlPluginPackage != null) {
                    pluginPackageList.add(dlPluginPackage);
                }
            }
        }
        lvPlugin.setAdapter(new PluginAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DLIntent intent = new DLIntent();
        intent.setClass(getActivity(), DLProxyActivity.class);
        intent.putExtra(DLConstants.EXTRA_PACKAGE, pluginPackageList.get(position).packageName);
        intent.putExtra(DLConstants.EXTRA_CLASS, pluginPackageList.get(position).defaultActivity);
        intent.setPluginClass(pluginPackageList.get(position).defaultActivity);
        intent.setPluginPackage(pluginPackageList.get(position).packageName);
        startActivity(intent);
    }

    public class PluginAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pluginPackageList.size();
        }

        @Override
        public Object getItem(int position) {
            return pluginPackageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_plugins, null);
            TextView title = (TextView) view.findViewById(R.id.tv_plugin_title);
            ImageView icon = (ImageView) view.findViewById(R.id.iv_plugin_icon);
            title.setText(pluginPackageList.get(position).packageInfo.applicationInfo.loadLabel(getActivity().getApplicationContext().getPackageManager()));
            icon.setImageDrawable(pluginPackageList.get(position).packageInfo.applicationInfo.loadIcon(getActivity().getApplicationContext().getPackageManager()));
            return view;
        }
    }
}
