package com.example.user.testgooglefit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by user on 13/07/2017.
 */

public class PermissionManager {

	public static final int MY_PERMISSION_FINE_LOCATION = 101;

	public static boolean checkPermissions(final Activity activity, final String permission, final int requestCode) {
		boolean isPermissionRequired = ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
		if (isPermissionRequired) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				new AlertDialog.Builder(activity)
					.setMessage(R.string.permission_access_text)
					.setTitle(R.string.permission_access_title)
					.setPositiveButton(R.string.permission_access_allow, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							requestPermissionAccess(activity, new String[]{permission}, requestCode);
						}
					})
					.create()
					.show();
			} else {
				requestPermissionAccess(activity, new String[]{permission}, requestCode);
			}
		}
		return isPermissionRequired;
	}

	public static void requestPermissionAccess(Activity activity, String[] permission, int requestCode) {
		ActivityCompat.requestPermissions(activity, permission, requestCode);
	}

}
