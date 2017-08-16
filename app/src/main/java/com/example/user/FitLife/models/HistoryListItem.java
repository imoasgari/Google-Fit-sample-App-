package com.example.user.FitLife.models;

import java.util.HashMap;

/**
 * Created by user on 02/08/2017.
 */

public class HistoryListItem<T> {

	private long mStartDate;
	private long mEndDate;
	private T mValue;
	private Type mType;

	public HistoryListItem(T value, int type, long startDate, long endDate) {
		mStartDate = startDate;
		mEndDate = endDate;
		mValue = value;
		mType = Type.fromId(type);
	}

	public long getStartDate() {
		return mStartDate;
	}

	public long getEndDate() {
		return mEndDate;
	}

	public Type getType() {
		return mType;
	}

	public T getValue() {
		return mValue;
	}


	public enum Type {
		DAILY(0),
		WEEKLY(1),
		MONTHLY(2);

		private int mId;

		Type(int sourceId) {
			mId = sourceId;
			Map.sMap.put(mId, this);
		}

		public static Type fromId(int type) {
			return Map.sMap.get(type);
		}

		static class Map {
			static java.util.Map<Integer, Type> sMap = new HashMap<>();
		}
	}
}
