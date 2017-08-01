package com.example.user.testgooglefit.models;

/**
 * Created by user on 31/07/2017.
 */

public class DailyCalories {
	private long mDate;
	private float mCalories;

	public DailyCalories(long date, float calories) {
		mDate = date;
		mCalories = calories;
	}

	public long getDate() {
		return mDate;
	}

	public float getCalories() {
		return mCalories;
	}
}
