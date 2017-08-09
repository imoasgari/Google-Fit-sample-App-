package com.example.user.FitLife.presenter;

import com.example.user.FitLife.ShowFitnessDataInterface;
import com.example.user.FitLife.models.HistoryListItem;

import java.util.List;

/**
 * Created by user on 08/08/2017.
 */

public interface HistoryListener {

	void onLastWeekDistanceUpdated(List<HistoryListItem> lastWeekDistance);

	void onLastWeekCaloriesUpdated(List<HistoryListItem> lastWeekCalories);

	void onLastWeekStepsUpdated(List<HistoryListItem> totalLastWeekSteps);

	void onLastMonthDistanceUpdated(List<HistoryListItem> lastMonthDistance);

	void onLastMonthCaloriesUpdated(List<HistoryListItem> lastMonthCalories);

	void onLastMonthStepsUpdated(List<HistoryListItem> totalStepsLastMonth);
}
