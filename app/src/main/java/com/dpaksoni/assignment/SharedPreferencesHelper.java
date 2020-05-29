package com.dpaksoni.assignment;

/**
 * Created by dsoni on 13/2/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Helper class for accessing or setting values in shared-preference
 */
public class SharedPreferencesHelper {
    private final String TAG = "SHARED_PREF_HELPER";
    private String pref;
    private Context context;

    public SharedPreferencesHelper(Context context, String prefFile) {
        this.context = context;
        this.pref = prefFile;
    }

    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public void setString(String key, String value){
        try {
            Log.d(TAG, "setString ["+key+" = "+value+"]");
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(key, value);
            editor.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * Set a integer shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public void setInt(String key, int value){
        try {
            Log.d(TAG, "setInt ["+key+" = "+value+"]");
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(key, value);
            editor.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    public void setLong(String key, long value){
        try {
            Log.d(TAG, "setLong ["+key+" = "+value+"]");
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(key, value);
            editor.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * Set a Boolean shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public void setBoolean(String key, boolean value){
        try {
            Log.d(TAG, "setBoolean ["+key+" = "+value+"]");
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    /**
     * Get a string shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public String getString(String key, String defValue){
        try {
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            String returnVal = settings.getString(key, defValue);
            Log.d(TAG, "getSharedPreferenceString ["+key+" = "+returnVal+"]");
            return returnVal;
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return defValue;
        }
    }

    /**
     * Get a integer shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public int getInt(String key, int defValue){
        try {
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            int returnVal = settings.getInt(key, defValue);
            Log.d(TAG, "getSharedPreferenceInt ["+key+" = "+returnVal+"]");
            return returnVal;
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return defValue;
        }
    }

    public long getLong(String key, long defValue){
        try {
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            long returnVal = settings.getLong(key, defValue);
            Log.d(TAG, "getSharedPreferenceLong ["+key+" = "+returnVal+"]");
            return returnVal;
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return defValue;
        }
    }

    /**
     * Get a boolean shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public boolean getBoolean(String key, boolean defValue){
        try {
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            boolean returnVal = settings.getBoolean(key, defValue);
            Log.d(TAG, "getSharedPreferenceBoolean ["+key+" = "+returnVal+"]");
            return returnVal;
        }
        catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return defValue;
        }
    }

    public boolean contains(String key) {
        try {
            SharedPreferences settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            boolean returnVal = settings.contains(key);
            Log.d(TAG, "contains ["+key+" = "+returnVal+"]");
            return returnVal;
        }
        catch (Exception e) {
            Log.e(TAG, "contains: Exception", e);
            return false;
        }
    }

    public void remove(String... keys) {
        try {
            if(keys != null && keys.length > 0) {
                SharedPreferences.Editor settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit();

                for(String key : keys) {
                    settings.remove(key);
                }

                settings.commit();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "remove: Exception", e);
        }
    }

    public void remove(String key) {
        try {
            SharedPreferences.Editor settings = context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
            settings.remove(key);
            settings.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "remove: Exception", e);
        }
    }
}