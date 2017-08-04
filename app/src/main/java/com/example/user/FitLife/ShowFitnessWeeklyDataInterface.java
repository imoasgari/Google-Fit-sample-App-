package com.example.user.FitLife;

import com.example.user.FitLife.models.HistoryListItem;

import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public interface ShowFitnessWeeklyDataInterface {
	void onLastWeekDistanceUpdated(List<HistoryListItem> lastWeekDistance);

	void onLastWeekCaloriesUpdated(List<HistoryListItem> lastWeekCalories);

	void onLastWeekStepsUpdated(List<HistoryListItem> totalLastWeekSteps);
}
