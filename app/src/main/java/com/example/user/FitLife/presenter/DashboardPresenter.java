package com.example.user.FitLife.presenter;

import com.example.user.FitLife.BuildFitnessClient;
import com.example.user.FitLife.ShowFitnessDataInterface;
import com.example.user.FitLife.activity.DashboardFragment;
import com.example.user.FitLife.activity.datarequester.CaloriesDataRequester;
import com.example.user.FitLife.activity.datarequester.DistanceDataRequester;
import com.example.user.FitLife.activity.datarequester.StepsDataRequester;
import com.example.user.FitLife.models.HistoryListItem;

import java.util.List;


/**
 * Created by user on 17/07/2017.
 */

public class DashboardPresenter implements ShowFitnessDataInterface , HistoryListener{

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
		mClient.setHistoryListener(this);
		mClient.requestDataFor(0, CaloriesDataRequester.Range.DAILY.getIndex());
		mClient.requestDataFor(1, DistanceDataRequester.Range.DAILY.getIndex());
		mClient.requestDataFor(2, StepsDataRequester.Range.DAILY.getIndex());

	}

	@Override
	public void onLastWeekDistanceUpdated(List<HistoryListItem> lastWeekDistance) {
	}

	@Override
	public void onLastWeekCaloriesUpdated(List<HistoryListItem> lastWeekCalories) {
	}

	@Override
	public void onLastWeekStepsUpdated(List<HistoryListItem> totalLastWeekSteps) {
	}

	@Override
	public void onLastMonthDistanceUpdated(List<HistoryListItem> lastMonthDistance) {
	}

	@Override
	public void onLastMonthCaloriesUpdated(List<HistoryListItem> lastMonthCalories) {
	}

	@Override
	public void onLastMonthStepsUpdated(List<HistoryListItem> totalStepsLastMonth) {
	}

	@Override
	public void onTodayCaloriesUpdatedForHistory(List<HistoryListItem> calories) {
	}

	@Override
	public void onTodayDistanceUpdatedForHistory(List<HistoryListItem> distance) {
	}

	@Override
	public void onTodayStepsUpdatedForHistory(List<HistoryListItem> steps) {
	}
}
