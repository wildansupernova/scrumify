package crystal.scrumify.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static String[] allPermissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * check if some permissions is allowed by user
     * @param context context where the method is called
     * @param activity activity where the method is called
     * @param permissions the permissions to be checked
     * @return true if all permission are allowed
     */
    public static boolean isPermissionGranted(Context context, Activity activity, String... permissions){
        try {
            for (String s : permissions){
                if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, permissions, ConstantUtils.REQ_PERMISSION);
                    return false;
                }
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

