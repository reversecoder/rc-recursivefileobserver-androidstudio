package com.reversecoder.recursivefileobserver.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Md. Rashadul Alam
 */
public class RuntimePermissionManager {

    public static final int REQUEST_CODE_PERMISSION=420;

    public static ArrayList<String> getAllPermissions(Context context) {
        ArrayList<String> allPermissions = new ArrayList<String>();
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            allPermissions = new ArrayList<String>(Arrays.asList(pi.requestedPermissions));
        } catch (Exception e) {
            return new ArrayList<String>();
        }
        return allPermissions;
    }

    public static boolean isAllPermissionsGranted(Context context) {
        ArrayList<String> mPermissions = getAllPermissions(context);

        for (int i = 0; i < mPermissions.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, mPermissions.get(i));
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<String> getAllUnGrantedPermissions(Context context) {
        ArrayList<String> mPermissions = getAllPermissions(context);
        ArrayList<String> listPermissionsNeeded = new ArrayList<String>();
        for (int i = 0; i < mPermissions.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, mPermissions.get(i));
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(mPermissions.get(i));
            }
        }

        return listPermissionsNeeded;
    }

    public static ArrayList<String> getAllGrantedPermissions(Context context) {
        ArrayList<String> mPermissions = getAllPermissions(context);
        ArrayList<String> grantedPermissions = new ArrayList<String>();
        for (int i = 0; i < mPermissions.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, mPermissions.get(i));
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(mPermissions.get(i));
            }
        }

        return grantedPermissions;
    }
}
