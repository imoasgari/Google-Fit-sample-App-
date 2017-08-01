package com.example.user.testgooglefit.presenter;

import com.example.user.testgooglefit.BuildFitnessClient;
import com.example.user.testgooglefit.ShowFitnessDataInterface;
import com.example.user.testgooglefit.activity.DashboardFragment;


/**
 * Created by user on 17/07/2017.
 */

public class DashboardPresenter implements ShowFitnessDataInterface {

	private DashboardFragment mView;
	private BuildFitnessClient mClient;

	public DashboardPresenter(DashboardFragment dashboardActivity, BuildFitnessClient client) {
		mView = dashboardActivity;
		mClient = client;

	}

	@Override
	public void onTodayStepUpdated(int finalSteps) {
		mView.displayTodaySteps(finalSteps);
	}

	@Override
	public void onTodayDistanceUpdated(float todayDistance) {
		mView.displayTodayTotalDistance(todayDistance);
	}

	@Override
	public void onTodayCalories(float todayCalories) {
		mView.displayTodayCalories(todayCalories);

	}

	@Override
	public void onConnectionFailed() {

	}

	@Override
	public void showSensorStepsOnCircle(int sensorSteps) {
		mView.displaySensorStepsOnCircle(sensorSteps);
	}

//	public void onStopHasCalled() {
//		if (mClient != null) {
//			mClient.onStopCalled();
//			mClient.manageClients();
//		}
//	}

	public void onViewReady() {
		mClient.setListener(this);
		mClient.initClient(BuildFitnessClient.Client.DAILY);
	}

}
