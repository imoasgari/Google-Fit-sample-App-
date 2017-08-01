package com.example.user.testgooglefit;

import com.example.user.testgooglefit.models.Calories;
import com.example.user.testgooglefit.models.Distance;

import java.util.List;

/**
 * Created by user on 07/07/2017.
 */

public interface ShowFitnessDataInterface {

	void onTodayStepUpdated(int finalSteps);

	void onTodayDistanceUpdated(float todayDistance);

	void onTodayCalories(float todayCalories);

	void onConnectionFailed();

	void showSensorStepsOnCircle(int sensorSteps);
}
