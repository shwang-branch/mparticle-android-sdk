package com.mparticle;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sdozor on 3/27/14.
 */
class ProviderPersistence extends JSONObject{

    static final String KEY_PERSISTENCE = "cms";
    private static final String KEY_PERSISTENCE_ID = "id";
    private static final String KEY_PERSISTENCE_ANDROID = "pr";
    private static final String KEY_PERSISTENCE_FILE = "f";
    private static final String KEY_PERSISTENCE_MODE = "m";
    private static final String KEY_PERSISTENCE_KEY_LIST = "ps";
    private static final String KEY_PERSISTENCE_KEY = "k";
    private static final String KEY_PERSISTENCE_TYPE = "t";
    private static final String KEY_PERSISTENCE_MPVAR = "n";
    private static final String KEY_PERSISTENCE_DEFAULT = "d";
    private static final String MPPREFIX = "mp::";

    private static final int PERSISTENCE_TYPE_STRING = 1;
    private static final int PERSISTENCE_TYPE_INT = 2;
    private static final int PERSISTENCE_TYPE_BOOLEAN = 3;
    private static final int PERSISTENCE_TYPE_FLOAT = 4;
    private static final int PERSISTENCE_TYPE_LONG = 5;


    ProviderPersistence(JSONObject config, Context context) throws JSONException{
        super();
        JSONArray configPersistence = config.getJSONArray(KEY_PERSISTENCE);
        for (int i = 0; i < configPersistence.length(); i++){

            JSONObject values = new JSONObject();
            if (configPersistence.getJSONObject(i).has(KEY_PERSISTENCE_ANDROID)){
                JSONArray files = configPersistence.getJSONObject(i).getJSONArray(KEY_PERSISTENCE_ANDROID);

                for (int fileIndex = 0; fileIndex < files.length(); fileIndex++){
                    JSONObject fileObject = files.getJSONObject(fileIndex);
                    SharedPreferences preferences = context.getSharedPreferences(fileObject.getString(KEY_PERSISTENCE_FILE), fileObject.getInt(KEY_PERSISTENCE_MODE));
                    JSONArray fileObjects = fileObject.getJSONArray(KEY_PERSISTENCE_KEY_LIST);
                    SharedPreferences.Editor editor = preferences.edit();
                    for (int keyIndex = 0; keyIndex < fileObjects.length(); keyIndex++){
                        final int type = fileObjects.getJSONObject(keyIndex).getInt(KEY_PERSISTENCE_TYPE);
                        final String key = fileObjects.getJSONObject(keyIndex).getString(KEY_PERSISTENCE_KEY);
                        final String mpKey = fileObjects.getJSONObject(keyIndex).getString(KEY_PERSISTENCE_MPVAR);
                        final String mpPersistenceKey = MPPREFIX + mpKey;
                        boolean contains = preferences.contains(key);
                        boolean containsMpKey = preferences.contains(mpPersistenceKey);
                        Object defaultValue;
                        switch (type) {
                            case PERSISTENCE_TYPE_STRING:
                                defaultValue = fileObjects.getJSONObject(keyIndex).getString(KEY_PERSISTENCE_DEFAULT);
                                if (contains) {
                                    values.put(mpKey, preferences.getString(key, (String) defaultValue));
                                } else {
                                    if (!containsMpKey){
                                        editor.putString(mpPersistenceKey , (String) defaultValue);
                                    }
                                    values.put(mpKey, preferences.getString(mpPersistenceKey, (String) defaultValue));
                                }
                                break;
                            case PERSISTENCE_TYPE_INT:
                                defaultValue = fileObjects.getJSONObject(keyIndex).getInt(KEY_PERSISTENCE_DEFAULT);
                                if (contains) {
                                    values.put(mpKey, preferences.getInt(key, (Integer) defaultValue));
                                } else {
                                    if (!containsMpKey){
                                        editor.putInt(mpPersistenceKey, (Integer) defaultValue);
                                    }
                                    values.put(mpKey, preferences.getInt(mpPersistenceKey, (Integer) defaultValue));
                                }
                                break;
                            case PERSISTENCE_TYPE_BOOLEAN:
                                defaultValue = fileObjects.getJSONObject(keyIndex).getBoolean(KEY_PERSISTENCE_DEFAULT);
                                if (contains) {
                                    values.put(mpKey, preferences.getBoolean(key, (Boolean) defaultValue));
                                } else {
                                    if (!containsMpKey){
                                        editor.putBoolean(mpPersistenceKey, (Boolean) defaultValue);
                                    }
                                    values.put(mpKey, preferences.getBoolean(mpPersistenceKey, (Boolean) defaultValue));
                                }
                                break;
                            case PERSISTENCE_TYPE_FLOAT:
                                defaultValue = fileObjects.getJSONObject(keyIndex).getDouble(KEY_PERSISTENCE_DEFAULT);
                                if (contains) {
                                    values.put(mpKey, preferences.getFloat(key, (Float) defaultValue));
                                } else {
                                    if (!containsMpKey){
                                        editor.putFloat(mpPersistenceKey, (Float) defaultValue);
                                    }
                                    values.put(mpKey, preferences.getFloat(mpPersistenceKey, (Float) defaultValue));
                                }
                                break;
                            case PERSISTENCE_TYPE_LONG:
                                defaultValue = fileObjects.getJSONObject(keyIndex).getLong(KEY_PERSISTENCE_DEFAULT);
                                if (contains) {
                                    values.put(mpKey, preferences.getLong(key, (Long) defaultValue));
                                } else {
                                    if (!containsMpKey){
                                        editor.putLong(mpPersistenceKey, (Long) defaultValue);
                                    }
                                    values.put(mpKey, preferences.getLong(mpPersistenceKey, (Long) defaultValue));
                                }
                                break;
                        }
                        editor.commit();
                    }

                }
            }
            put(Integer.toString(configPersistence.getJSONObject(i).getInt(KEY_PERSISTENCE_ID)), values);

        }

    }
}