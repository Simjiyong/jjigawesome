package kr.co.company.jjigawesome;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by BHY on 2017. 12. 7..
 */

public class SPtoObject {
    public SPtoObject(){};

    public static int saveObject(SharedPreferences mPrefs, Object object, String string){
        if(object!=null && mPrefs!=null) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.clear();
            Gson gson = new Gson();
            String json = gson.toJson(object);
            prefsEditor.putString(string, json);
            prefsEditor.commit();

            return 1;
        }
        else{
            return 0;
        }
    }

    public static Object loadObject(SharedPreferences mPrefs, String string, Type typeofO){
        Gson gson = new Gson();
        String json = mPrefs.getString(string, "");
        return gson.fromJson(json, typeofO);
    }
}
