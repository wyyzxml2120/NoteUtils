package com.pjw.noteutils.data;

import android.content.Context;
import android.content.SharedPreferences;

public class NotePreference {

    private static final String SHARED_PATH = "note_utils";
    private static SharedPreferences sharedPreferences;
    private Context con;
    public NotePreference(Context context)
    {
        con = context;
    }
    private String preferenceName="dnnoteutils";

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void write(String key,String value)
    {
        SharedPreferences userInfo = con.getSharedPreferences(preferenceName, 0);
        userInfo.edit().putString(key, value).apply();
    }

    public String read(String key)
    {
        SharedPreferences userInfo = con.getSharedPreferences(preferenceName,0);
        return userInfo.getString(key, "");
    }

    public void writeInt(String key, int value) {
        SharedPreferences userInfo = con.getSharedPreferences(preferenceName, 0);
        userInfo.edit().putInt(key, value).apply();
    }

    public Integer readInt(String key) {
        SharedPreferences userInfo = con.getSharedPreferences(preferenceName, 0);
        return userInfo.getInt(key, 0);
    }

}
