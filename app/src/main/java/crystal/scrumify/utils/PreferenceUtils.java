package crystal.scrumify.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static void setStringPref(Context context, String prefKey, String prefValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(prefKey, prefValue);
        editor.apply();
    }

    private static String getStringPref(Context context, String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(prefKey,"");
    }

    private static void setBooleanPref(Context context, String prefKey, boolean prefValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(prefKey, prefValue);
        editor.apply();
    }

    private static boolean getBooleanPref(Context context, String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(prefKey, false);
    }

    private static void setIntPref(Context context, String prefKey, int prefValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(prefKey, prefValue);
        editor.apply();
    }

    private static int getIntPref(Context context, String prefKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(prefKey, -1);
    }

    public static void setToken(Context context, String token) {
        setStringPref(context, ConstantUtils.PREF_TOKEN_ARG, token);
    }

    public static String getToken(Context context) {
        return getStringPref(context, ConstantUtils.PREF_TOKEN_ARG);
    }

    public static void setName(Context context, String name) {
        setStringPref(context, ConstantUtils.PREF_NAME_ARG, name);
    }

    public static String getName(Context context) {
        return getStringPref(context, ConstantUtils.PREF_NAME_ARG);
    }

    public static void setEmail(Context context, String email) {
        setStringPref(context, ConstantUtils.PREF_EMAIL_ARG, email);
    }

    public static String getEmail(Context context) {
        return getStringPref(context, ConstantUtils.PREF_EMAIL_ARG);
    }

    public static void setUserId(Context context, int userId) {
        setIntPref(context, ConstantUtils.PREF_USER_ID_ARG, userId);
    }

    public static int getUserId(Context context) {
//        return 1;
        return getIntPref(context, ConstantUtils.PREF_USER_ID_ARG);
    }

    public static void setLogin(Context context, boolean isLogin) {
        setBooleanPref(context, ConstantUtils.PREF_IS_LOGIN_ARG, isLogin);
    }

    public static boolean isLogin(Context context) {
        return getBooleanPref(context, ConstantUtils.PREF_IS_LOGIN_ARG);
    }

}
