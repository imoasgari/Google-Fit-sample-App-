package com.example.user.testgooglefit;

import com.example.user.testgooglefit.models.Calories;
import com.example.user.testgooglefit.models.Distance;
import com.example.user.testgooglefit.models.Steps;

import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public interface ShowFitnessWeeklyDataInterface {
	void onLastWeekDistanceUpdated(List<Distance> lastWeekDistance);

	void onLastWeekCaloriesUpdated(List<Calories> lastWeekCalories);

	void onLastWeekStepsUpdated(List<Steps> totalLastWeekSteps);
}
