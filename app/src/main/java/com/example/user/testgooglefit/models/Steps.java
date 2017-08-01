package com.example.user.testgooglefit.models;

/**
 * Created by user on 26/07/2017.
 */

public class Steps {
	private long mStartDate;
	private long mEndDate;
	private int mSteps;

	public Steps(int steps, long startDate, long endDate) {
		mStartDate = startDate;
		mEndDate = endDate;
		mSteps = steps;
	}

	public long getStartDate() {
		return mStartDate;
	}

	public long getEndDate() {
		return mEndDate;
	}

	public int getSteps() {
		return mSteps;
	}
}
