package com.example.user.FitLife.presenter;

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

	void onTodayCaloriesUpdatedForHistory(List<HistoryListItem> calories);

	void onTodayDistanceUpdatedForHistory(List<HistoryListItem> distance);

	void onTodayStepsUpdatedForHistory(List<HistoryListItem> steps);
}
