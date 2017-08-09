package com.example.user.FitLife.presenter;

import com.example.user.FitLife.BuildFitnessClient;
import com.example.user.FitLife.ShowFitnessDataInterface;
import com.example.user.FitLife.activity.DashboardFragment;


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

	public void onViewReady() {
		mClient.setListener(this);
		mClient.initClient();
	}

}