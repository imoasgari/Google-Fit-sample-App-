package com.example.user.testgooglefit.models;

/**
 * Created by user on 31/07/2017.
 */

public class Calories {
	private long mStartDate;
	private long mEndDate;
	private float mCalories;

	public Calories(float calories, long startDate, long endDate) {
		mStartDate = startDate;
		mEndDate = endDate;
		mCalories = calories;
	}

	@Override
	public String toString() {
		return "Calories{" +
			"mStartDate=" + mStartDate +
			", mEndDate=" + mEndDate +
			", mCalories=" + mCalories +
			'}';
	}

	public long getStartDate() {
		return mStartDate;
	}

	public long getEndDate() {
		return mEndDate;
	}

	public float getCalories() {
		return mCalories;
	}
}
