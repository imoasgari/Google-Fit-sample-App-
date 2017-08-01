package com.example.user.testgooglefit.presenter;

import com.example.user.testgooglefit.BuildFitnessClient;
import com.example.user.testgooglefit.ShowFitnessMonthlyInterface;
import com.example.user.testgooglefit.ShowFitnessWeeklyDataInterface;
import com.example.user.testgooglefit.activity.HistoryFragment;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryPresenter implements ShowFitnessWeeklyDataInterface, ShowFitnessMonthlyInterface {

	private HistoryFragment mView;
	private BuildFitnessClient mClient;

	public HistoryPresenter(HistoryFragment historyFragment, BuildFitnessClient client) {
		mView = historyFragment;
		mClient = client;
	}
	@Override
	public void onLastMonthDistanceUpdated(float lastMonthDistance) {
		mView.displayLastMonthDistance(lastMonthDistance);
	}

	@Override
	public void onLastMonthCaloriesUpdated(float lastMonthCalories) {
		mView.displayLastMonthCalories(lastMonthCalories);
	}

	@Override
	public void onLastMonthStepsUpdated(int totalStepsLastMonth) {
		mView.displayLastMonthSteps(totalStepsLastMonth);
	}

	@Override
	public void onLastWeekDistanceUpdated(float lastWeekDistance) {
		mView.displayLastWeekDistance(lastWeekDistance);
	}

	@Override
	public void onLastWeekCaloriesUpdated(float lastWeekCalories) {
		mView.displayLastWeekCalories(lastWeekCalories);
	}

	@Override
	public void onLastWeekStepsUpdated(int totalLastWeekSteps) {
		mView.displayLastWeekSteps(totalLastWeekSteps);
	}

	public void onViewReady() {
		mClient.initClient(BuildFitnessClient.Client.HISTORY);
		mClient.setWeeklyListener(this);
		mClient.setMonthlyListener(this);
	}
}
