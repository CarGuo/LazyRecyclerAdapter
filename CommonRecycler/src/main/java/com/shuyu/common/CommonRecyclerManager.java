package com.shuyu.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * Created by Guo on 2015/11/23.
 * 管理器
 */
public class CommonRecyclerManager {

    public HashMap<Integer, String> hashMap = new HashMap<Integer, String>();

    public RecyclerBaseHolder getViewTypeHolder(Context context, ViewGroup parent, int layoutId) {
        View v = LayoutInflater.from(context).inflate(layoutId, parent, false);
        String className = hashMap.get(layoutId);
        try {
            Class c = Class.forName(className);
            Constructor object = c.getDeclaredConstructor(new Class[]{Context.class, View.class});
            object.setAccessible(true);

            return (RecyclerBaseHolder) object.newInstance(context, v);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addType(int layoutId, String ClassName) {
        if (!hashMap.containsKey(layoutId)) {
            hashMap.put(layoutId, ClassName);
        }
    }

}


