package com.example.user.FitLife.models;

import com.jjoe64.graphview.series.DataPointInterface;

import java.util.HashMap;

/**
 * Created by MohammadrezaAsgari on 02/08/2017.
 */

public class HistoryListItem<T> implements DataPointInterface {

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

	@Override
	public double getX() {
		return (double) mEndDate;
	}

	@Override
	public double getY() {
		return Double.parseDouble(mValue.toString());
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
