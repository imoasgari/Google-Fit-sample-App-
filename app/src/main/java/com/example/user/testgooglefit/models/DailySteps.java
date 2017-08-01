package com.example.user.testgooglefit.models;

/**
 * Created by user on 26/07/2017.
 */

public class DailySteps {
	private String mDate;
	private int mSteps;

	public DailySteps(String date, int steps) {
		mDate = date;
		mSteps = steps;
	}

	public int getSteps() {
		return mSteps;
	}

	public void setSteps(int steps) {
		mSteps = steps;
	}
}
