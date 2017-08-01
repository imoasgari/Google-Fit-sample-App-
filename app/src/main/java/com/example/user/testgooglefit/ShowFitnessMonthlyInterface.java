package com.example.user.testgooglefit;

/**
 * Created by user on 26/07/2017.
 */

public interface ShowFitnessMonthlyInterface {
	void onLastMonthDistanceUpdated(float lastMonthDistance);

	void onLastMonthCaloriesUpdated(float lastMonthCalories);

	void onLastMonthStepsUpdated(int totalStepsLastMonth);
}
