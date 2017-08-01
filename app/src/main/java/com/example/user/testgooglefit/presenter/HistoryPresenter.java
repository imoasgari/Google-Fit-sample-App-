package com.example.user.testgooglefit.presenter;

import com.example.user.testgooglefit.BuildFitnessClient;
import com.example.user.testgooglefit.ShowFitnessMonthlyInterface;
import com.example.user.testgooglefit.ShowFitnessWeeklyDataInterface;
import com.example.user.testgooglefit.activity.HistoryFragment;
import com.example.user.testgooglefit.models.Calories;
import com.example.user.testgooglefit.models.Distance;
import com.example.user.testgooglefit.models.Steps;

import java.util.List;

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
	public void onLastMonthDistanceUpdated(List<Distance> lastMonthDistance) {
		mView.displayLastMonthDistance(lastMonthDistance);
	}

	@Override
	public void onLastMonthCaloriesUpdated(List<Calories> lastMonthCalories) {
		mView.displayLastMonthCalories(lastMonthCalories);
	}

	@Override
	public void onLastMonthStepsUpdated(List<Steps> totalStepsLastMonth) {
		mView.displayLastMonthSteps(totalStepsLastMonth);
	}

	@Override
	public void onLastWeekDistanceUpdated(List<Distance> lastWeekDistance) {
		mView.displayLastWeekDistance(lastWeekDistance);
	}

	@Override
	public void onLastWeekCaloriesUpdated(List<Calories> lastWeekCalories) {
		mView.displayLastWeekCalories(lastWeekCalories);
	}

	@Override
	public void onLastWeekStepsUpdated(List<Steps> totalLastWeekSteps) {
		mView.displayLastWeekSteps(totalLastWeekSteps);
	}

	public void onViewReady() {
		mClient.initClient(BuildFitnessClient.Client.HISTORY);
		mClient.setWeeklyListener(this);
		mClient.setMonthlyListener(this);
	}
}
