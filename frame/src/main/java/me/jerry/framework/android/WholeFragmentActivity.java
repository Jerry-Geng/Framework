package me.jerry.framework.android;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import me.jerry.framework.R;
import me.jerry.framework.annotation.AutoFindView;

/**
 * Created by Jerry on 2017/8/21.
 */

public class WholeFragmentActivity extends FragmentActivityFrame implements IWholeActivity {
    private List<Class<? extends FragmentFrame>> list;

    public final static String TAG_MAIN = "main";

    @AutoFindView
    public FrameLayout fragment_main;

    private FragmentStack stack = new FragmentStack();

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.F);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment;
    }

    @Override
    protected void loadData() {
        list = FragmentManifest.findFragments(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState, PersistableBundle persistentState) {
        if(list != null && !list.isEmpty()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            try {
                FragmentFrame fragmentFrame = list.get(0).newInstance();
                fragmentFrame.setMainFragment(true);
                transaction.add(R.id.fragment_main, fragmentFrame, TAG_MAIN);
                transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);
                transaction.commit();
                stack.pushFragment(fragmentFrame);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "no main fragment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startFragment(Class<? extends FragmentFrame> clazz, int callType, Bundle args) {
        try {
            FragmentFrame fragmentFrame = null;
            boolean attatched = false;
            boolean clear_top = false;
            FragmentFrame current = stack.getTop();
            switch (callType) {
                case FragmentFrame.CALL_TYPE_CREATE_NEW:
                    fragmentFrame = clazz.newInstance(); break;
                case FragmentFrame.CALL_TYPE_BRING_TO_FRONT:
                    int count = stack.count();
                    for(int i = count - 1; i >= 0; i --) {
                        FragmentFrame temp = stack.getFragment(i);
                        if(temp.getClass() == clazz) {
                            stack.removeFromStack(temp);
                            fragmentFrame = temp;
                            break;
                        }
                    }
                    if(fragmentFrame == null) {
                        fragmentFrame = clazz.newInstance();
                    } else {
                        // mark attached, don't need to push the arguments.
                        attatched = true;
                    }
                    break;
                case FragmentFrame.CALL_TYPE_CLEAR_TOP:
                    count = stack.count();
                    for(int i = count - 1; i >= 0; i --) {
                        FragmentFrame temp = stack.getFragment(i);
                        if(temp.getClass() == clazz) {

                            if(stack.contains(temp)) {
                                FragmentFrame f;
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                while((f = stack.popFragment()) != temp) {
                                    transaction.remove(f);
                                    if(f == current) {
                                        // mark top removed, don't need to hide top.
                                        clear_top = true;
                                    }
                                }
                                transaction.commit();
                                fragmentFrame = temp;
                            }
                            break;
                        }
                    }
                    if(fragmentFrame == null) {
                        fragmentFrame = clazz.newInstance();
                    } else {
                        // mark attached, don't need to push the arguments.
                        attatched = true;
                    }
                    break;
            }
            if(!attatched) {
                fragmentFrame.setArguments(args);
            } else {
                // always bring the found fragment to top.
                fragment_main.removeView(fragmentFrame.getView());
                fragment_main.addView(fragmentFrame.getView(), fragment_main.getChildCount());
            }
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);
            if(current != null) {
                if(!clear_top) {
                    transaction.hide(current);
                }
            }
            if(!attatched) {
                // if the fragment is not attached, it means it is new, add it.
                transaction.add(R.id.fragment_main, fragmentFrame, clazz.getName());
            } else {
                // if the fragment is attached, make it visible.
                transaction.show(fragmentFrame);
            }
            transaction.commit();
            stack.pushFragment(fragmentFrame);
            Log.i("put fragment", "put success: " + clazz.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishFragment(FragmentFrame fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit);
        transaction.remove(fragment);
        stack.removeFromStack(fragment);
        if(stack.getTop() != null) {
            transaction.show(stack.getTop());
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(!stack.isEmpty() && stack.getTop() != stack.getBottom()) {
                finishFragment(stack.getTop());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
