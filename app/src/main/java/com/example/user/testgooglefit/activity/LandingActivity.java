package com.example.user.testgooglefit.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.example.user.testgooglefit.PermissionManager;
import com.example.user.testgooglefit.R;
import com.example.user.testgooglefit.presenter.LandingPresenter;

import static com.example.user.testgooglefit.PermissionManager.MY_PERMISSION_FINE_LOCATION;

/**
 * Created by user on 14/07/2017.
 */

public class LandingActivity extends FragmentActivity {

	private View mEnableButton;

	private LandingPresenter mPresenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
		mPresenter = new LandingPresenter(this);
		if (!PermissionManager.checkPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionManager.MY_PERMISSION_FINE_LOCATION)) {
			mPresenter.onPermissionGranted(false);
		}

		mEnableButton = findViewById(R.id.landing_activity_location_button);
		mEnableButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPresenter.onRequestLocationPermission();
			}

		});

	}

	public void requestPermission() {
		PermissionManager.requestPermissionAccess(LandingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionManager.MY_PERMISSION_FINE_LOCATION);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case MY_PERMISSION_FINE_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					mPresenter.onPermissionGranted(true);
				} else {
					mPresenter.onPermissionDenied();
				}
		}
	}

	public void navigateToDashboard(int delay) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(LandingActivity.this, NavigationActivity.class));
				finish();
			}
		}, delay);
	}

	public void retryAccessToLocation() {
		Toast.makeText(getApplicationContext(), "Location permission is required!", Toast.LENGTH_SHORT).show();
		mEnableButton.setVisibility(View.VISIBLE);
	}
}
