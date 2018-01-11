package me.jerry.framework.annotation;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.jerry.framework.exception.AnnotationException;
import me.jerry.framework.utils.ReflactUtils;
import me.jerry.framework.utils.StringUtils;

/**
 * Created by Jerry on 2017/8/15.
 */

public class AutoFindViewSolution implements AnnotationSolver {
    private Object obj;
    private Context context;
    private Object viewHolder;
    private Object listener;

    /**
     *
     * @param viewDeclarer find view area. we will auto find the views declared in this object
     * @param context android context.
     * @param viewHolder which declared method findViewById, like view or activity.
     * @param listener the object implements all the listeners declared in the annotation.
     */
    public AutoFindViewSolution(Object viewDeclarer, Context context, Object viewHolder, Object listener) {
        obj = viewDeclarer;
        this.context = context;
        this.viewHolder = viewHolder;
        this.listener = listener;
    }

    @Override
    public void solve() {
        Log.i("auto find view", "solve start");
        Field[] fields = ReflactUtils.getFieldsWithSuper(obj.getClass(), Object.class);
        for(Field field : fields) {
            if(View.class.isAssignableFrom(field.getType())) {
                AutoFindView annotation = (AutoFindView) field.getAnnotation(AutoFindView.class);
                if(annotation != null) {
                    int id = annotation.value();
                    if(id == -1) {

                        try{
                            id = Class.forName(context.getPackageName() + ".R$id").getDeclaredField(StringUtils.upTo_(field.getName())).getInt(null);
                        } catch (IllegalAccessException e) {
                        } catch (NoSuchFieldException e) {
                            try {
                                id = Class.forName(context.getPackageName() + ".R$id").getDeclaredField(field.getName()).getInt(null);
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            } catch (NoSuchFieldException e1) {
                                e1.printStackTrace();
                                throw new AnnotationException("the auto find view id is not defined and the view's name cannot be found in R.id.class after 'upTo_' translate or not.");
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                                throw new AnnotationException("R.class not found, package: " + context.getPackageName());
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new AnnotationException("R.class not found, package: " + context.getPackageName());
                        }
                    }
                    field.setAccessible(true);
                    View view = null;
                    try {
                        Method method = null;
                        Class clazz = viewHolder.getClass();
                        NoSuchMethodException exception = null;
                        while (clazz != Object.class) {
                            try {
                                method = clazz.getDeclaredMethod("findViewById", int.class);
                                if(method != null) {
                                    break;
                                }
                            } catch (NoSuchMethodException e) {
                                clazz = clazz.getSuperclass();
                                exception = e;
                                continue;
                            }
                        }
                        if(method == null) {
                            throw exception;
                        }
                        view = (View) method.invoke(viewHolder, id);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new AnnotationException("method findViewById in viewholder is not accessable");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        throw new AnnotationException("not found method findViewById in viewholder" + viewHolder.getClass().toString());
                    }
                    if(view != null) {
                        Log.i("auto find view", "view found");
                        try {
                            field.set(obj, view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Class[] listenerTypes = annotation.listeners();
                        List<Method[]> methods = new ArrayList<>();
                        List<Class> clazzs = new ArrayList<>();
                        clazzs.add(view.getClass());
                        while(!clazzs.get(0).equals(View.class)) {
                            clazzs.add(0, clazzs.get(0).getSuperclass());
                        }
                        for(Class clazz : clazzs) {
                            methods.add(clazz.getDeclaredMethods());
                        }
                        for(Method[] methods1 : methods) {
                            for(Method method : methods1) {
                                for(Class listenerType : listenerTypes) {
                                    if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(listenerType)) {
                                        try {
                                            method.setAccessible(true);
                                            method.invoke(view, listener);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                            throw new AnnotationException("the setListener method not reached");
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                            throw new AnnotationException("invoke error");
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Log.i("auto find view", "can not find the view");
                    }
                }
            }
        }
    }
}
