package com.dimonzz.app_cache;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * This class echoes a string called from JavaScript.
 */
public class AppCache extends CordovaPlugin {

    private static final String LOG_TAG = "AppCacheClear";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("clear")) {
            Log.v(LOG_TAG, "Cordova Android AppCacheClear() called.");
            task(callbackContext);
            return true;
        }
        return false;
    }

    public void task(CallbackContext callbackContext) {
        final AppCache self = this;
        final CallbackContext callback = callbackContext;
        final Context context = cordova.getActivity().getApplicationContext();

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(deleteCache(context) && deleteExternalCache(context)){
                    Log.e(LOG_TAG, "Cache deleted");
                    PluginResult result = new PluginResult(PluginResult.Status.OK);
                    result.setKeepCallback(false);
                    callback.sendPluginResult(result);
                }else{
                    String msg = "Error while clearing app cache";
                    Log.e(LOG_TAG, msg);
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, msg);
                    result.setKeepCallback(false);
                    callback.sendPluginResult(result);
                }
            }
        });
    }

    public static boolean deleteExternalCache(Context context) {
        try {
            File dir = context.getExternalCacheDir();
            deleteDir(dir);
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
    }

    public static boolean deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
