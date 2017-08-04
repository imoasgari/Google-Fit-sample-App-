package com.example.user.FitLife.models;

/**
 * Created by user on 02/08/2017.
 */

public class HistoryListItem<T> {

	private long mMStartDate;
	private long mEndDate;
	private T mValue;


	public HistoryListItem(T value, long startDate, long endDate) {
		mMStartDate = startDate;
		mEndDate = endDate;
		mValue = value;
	}

	public long getStartDate() {
		return mMStartDate;
	}

	public long getEndDate() {
		return mEndDate;
	}

	public T getValue() {
		return mValue;
	}
}
