package com.example.user.testgooglefit.models;

/**
 * Created by user on 01/08/2017.
 */

public class Distance {

	private float mDistance;
	private long mStartDate;
	private long mEndDate;

	public float getDistance() {
		return mDistance;
	}

	public long getStartDate() {
		return mStartDate;
	}

	public long getEndDate() {
		return mEndDate;
	}

	public Distance(float distance, long startDate, long endDate) {
		mDistance = distance;
		mStartDate = startDate;
		mEndDate = endDate;

	}
}
