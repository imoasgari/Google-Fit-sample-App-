package com.example.user.testgooglefit.presenter;

import com.example.user.testgooglefit.activity.LandingActivity;

/**
 * Created by user on 17/07/2017.
 */

public class LandingPresenter {

	private static final int NO_DELAY = 0;
	private static final int DELAY = 1000;


	private LandingActivity mView;

	public LandingPresenter(LandingActivity landingActivity) {
		mView = landingActivity;
	}

	public void onRequestLocationPermission() {
		mView.requestPermission();
	}

	public void onPermissionGranted(boolean requiredUserInteraction) {
		mView.navigateToDashboard(requiredUserInteraction ? NO_DELAY : DELAY);
	}

	public void onPermissionDenied() {
		mView.retryAccessToLocation();
	}
}
