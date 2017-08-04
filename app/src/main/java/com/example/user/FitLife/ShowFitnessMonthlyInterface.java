package com.example.user.FitLife;

import com.example.user.FitLife.models.HistoryListItem;

import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public interface ShowFitnessMonthlyInterface {
	void onLastMonthDistanceUpdated(List<HistoryListItem> lastMonthDistance);

	void onLastMonthCaloriesUpdated(List<HistoryListItem> lastMonthCalories);

	void onLastMonthStepsUpdated(List<HistoryListItem> totalStepsLastMonth);
}
