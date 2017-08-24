package com.example.user.FitLife.presenter;

import com.example.user.FitLife.BuildFitnessClient;
import com.example.user.FitLife.activity.AnalyticsFragment;
import com.example.user.FitLife.models.HistoryListItem;

import java.util.List;

/**
 * Created by MohammadrezaAsgari on 21/08/2017.
 */

public class AnalyticsPresenter implements HistoryListener {

	private AnalyticsFragment mView;
	private BuildFitnessClient mClient;

	public AnalyticsPresenter(AnalyticsFragment analyticsFragment, BuildFitnessClient client) {
		mView = analyticsFragment;
		mClient = client;
	}

	public void onViewReady() {
		mClient.initClient();
		mClient.setHistoryListener(this);
	}

	@Override
	public void onLastWeekDistanceUpdated(List<HistoryListItem> lastWeekDistance) {
		mView.lastWeekDistance(lastWeekDistance);
	}

	@Override
	public void onLastWeekCaloriesUpdated(List<HistoryListItem> lastWeekCalories) {
		mView.lastWeekCalories(lastWeekCalories);
	}

	@Override
	public void onLastWeekStepsUpdated(List<HistoryListItem> totalLastWeekSteps) {
		mView.lastWeekSteps(totalLastWeekSteps);
	}

	@Override
	public void onLastMonthDistanceUpdated(List<HistoryListItem> lastMonthDistance) {
		mView.lastMonthDistance(lastMonthDistance);
	}

	@Override
	public void onLastMonthCaloriesUpdated(List<HistoryListItem> lastMonthCalories) {
		mView.lastMonthCalories(lastMonthCalories);
	}

	@Override
	public void onLastMonthStepsUpdated(List<HistoryListItem> totalStepsLastMonth) {
		mView.lastMonthSteps(totalStepsLastMonth);
	}

	@Override
	public void onTodayCaloriesUpdatedForHistory(List<HistoryListItem> calories) {
		mView.todayCalories(calories);
	}

	@Override
	public void onTodayDistanceUpdatedForHistory(List<HistoryListItem> distance) {
		mView.todayDistance(distance);
	}

	@Override
	public void onTodayStepsUpdatedForHistory(List<HistoryListItem> steps) {
		mView.todaySteps(steps);
	}

	public void requestDataFor(int position, int selectedItemPosition) {
		mClient.requestDataFor(position,selectedItemPosition);
	}
}
