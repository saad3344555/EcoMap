package com.edgeon.ecomapapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

public class LocalDatabase {

    public SharedPreferences sharedpreferences;

    public LocalDatabase() {

    }

    public void clear(Context context) {
        sharedpreferences = context.getSharedPreferences("ecomap_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
    }



    public void setUserEmail(Context context, String email) {
        sharedpreferences = context.getSharedPreferences("ecomap_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }



    public String getUserEmail(Context context) {
        sharedpreferences = context.getSharedPreferences("ecomap_user", Context.MODE_PRIVATE);
        return sharedpreferences.getString("email", " ");
    }

//    // GOOGLE PLAY APP SIGNING SHA-1 KEY:- F8:D4:8F:3F:78:54:A4:88:28:C1:35:8A:C1:0C:F9:D8:21
//    byte[] sha1 = {
//            (byte) 0xFC, 0x3F, (byte) 0xE2, (byte)0xF8, (byte)0xD4, (byte) 0x8F, 0x3F, (byte)0x78, (byte)0x54, (byte)0xA4, (byte)0x88, 0x28, (byte) 0xC1, 0x35, (byte) 0x8A, (byte)0xC1, 0x0C, (byte) 0xF9, (byte)0xD8, (byte)0x21
//    };
//        Log.e("keyhashGooglePlaySignIn",""+Base64.encodeToString(sha1, Base64.NO_WRAP));


}
