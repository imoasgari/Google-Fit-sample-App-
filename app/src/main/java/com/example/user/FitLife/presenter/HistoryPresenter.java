package com.example.user.FitLife.presenter;

import com.example.user.FitLife.BuildFitnessClient;
import com.example.user.FitLife.ShowFitnessDataInterface;
import com.example.user.FitLife.activity.HistoryFragment;
import com.example.user.FitLife.models.HistoryListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryPresenter implements HistoryListener {

	private HistoryFragment mView;
	private BuildFitnessClient mClient;

	public HistoryPresenter(HistoryFragment historyFragment, BuildFitnessClient client) {
		mView = historyFragment;
		mClient = client;
	}


	public void onViewReady() {
		mClient.initClient();
		mClient.setHistoryListener(this);
	}

	public void requestDataFor(int type, int range){
		mClient.requestDataFor(type, range);
	}

	public void clientManagement() {
		mClient.manageClients();
	}

	@Override
	public void onLastMonthDistanceUpdated(List<HistoryListItem> lastMonthDistance) {
		mView.displayLastMonthDistance(lastMonthDistance);
	}

	@Override
	public void onLastMonthCaloriesUpdated(List<HistoryListItem> lastMonthCalories) {
		mView.displayLastMonthCalories(lastMonthCalories);
	}

	@Override
	public void onLastMonthStepsUpdated(List<HistoryListItem> totalStepsLastMonth) {
		mView.displayLastMonthSteps(totalStepsLastMonth);
	}

	@Override
	public void onLastWeekDistanceUpdated(List<HistoryListItem> lastWeekDistance) {
		mView.displayLastWeekDistance(lastWeekDistance);
	}

	@Override
	public void onLastWeekCaloriesUpdated(List<HistoryListItem> lastWeekCalories) {
		mView.displayLastWeekCalories(lastWeekCalories);
	}

	@Override
	public void onLastWeekStepsUpdated(List<HistoryListItem> totalLastWeekSteps) {
		mView.displayLastWeekSteps(totalLastWeekSteps);
	}


//	public List<HistoryListItem> getDataGroupedByWeek(List<HistoryListItem> dataGroupedByDays) {
//		List<HistoryListItem> dataGroupedByWeek = new ArrayList<>();
//		int i = 0;
//		for(HistoryListItem dayItem : dataGroupedByDays) {
//			dataGroupedByWeek.add(new HistoryListItem());
//
//		}
//
//	}
}
