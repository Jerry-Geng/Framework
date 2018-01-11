package me.jerry.framework.plugin;

import me.jerry.framework.android.FragmentFrame;

/**
 * Created by Jerry on 2017/8/24.
 */

public class Plugin {
    public byte[] icon;
    public String title;
    public ClassLoader classLoader;
    public Class<? extends FragmentFrame> firstFragment;
}
