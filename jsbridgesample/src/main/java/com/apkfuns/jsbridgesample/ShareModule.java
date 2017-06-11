package com.apkfuns.jsbridgesample;

import android.util.Log;
import android.widget.Toast;

import com.apkfuns.jsbridge.JsBridge;
import com.apkfuns.jsbridge.module.JBArray;
import com.apkfuns.jsbridge.module.JBCallback;
import com.apkfuns.jsbridge.module.JSBridgeMethod;
import com.apkfuns.jsbridge.module.JsModule;
import com.apkfuns.jsbridge.module.JBMap;
import com.apkfuns.jsbridge.module.WritableJBArray;
import com.apkfuns.jsbridge.module.WritableJBMap;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by pengwei on 2017/5/27.
 */

public class ShareModule extends JsModule {
    @Override
    public String getModuleName() {
        return "share";
    }

    @JSBridgeMethod(methodName = "hiShare")
    public void share(float platform, String msg, final JBCallback success, final JBCallback failure) {
        Toast.makeText(getContext(), "abc", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        success.apply("ret = " + msg, true);
    }

    @JSBridgeMethod
    public void ajax(final JBMap dataMap) {
        OkHttpUtils.get()
                .url(dataMap.getString("url") + "?" + dataMap.getString("data"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dataMap.getCallback("error").apply(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dataMap.getCallback("success").apply(response);
                    }
                });
    }

    @JSBridgeMethod
    public void test(JBArray array) {
        for (int i=0;i<array.size();i++) {
            String output = "" + array.get(i);
            if (array.get(i) != null) {
                output += "##" + array.get(i).getClass();
            }
            Log.d(JsBridge.TAG, output);
        }
        array.getCallback(4).apply("xxx");
        Log.d(JsBridge.TAG, "ret=" + array.getMap(5).getInt("a"));
        array.getMap(5).getCallback("b").apply();
    }

    @JSBridgeMethod
    public void testReturn(JBCallback callback) {
        WritableJBArray jbArray = WritableJBArray.create();
        jbArray.pushInt(1);
        jbArray.pushInt(2);
        jbArray.pushInt(3);
        WritableJBMap jbMap = WritableJBMap.create();
        jbMap.putString("a","hello");
        jbMap.putString("b","world");
        callback.apply(jbArray, jbMap);
        Log.d(JsBridge.TAG, jbMap.toString() + "\n" + jbArray.toString());
        Log.d(JsBridge.TAG, getContext() + "#" + getWebViewObject());
    }

    @JSBridgeMethod
    public int version() {
        return 10;
    }
}
