package com.example.user.testgooglefit;

/**
 * Created by user on 26/07/2017.
 */

public interface ShowFitnessWeeklyDataInterface {
	void onLastWeekDistanceUpdated(float lastWeekDistance);

	void onLastWeekCaloriesUpdated(float lastWeekCalories);

	void onLastWeekStepsUpdated(int totalLastWeekSteps);
}
