package com.example.user.FitLife.activity.datarequester;

import android.support.annotation.NonNull;

import com.example.user.FitLife.BuildFitnessClient;
import com.example.user.FitLife.Utils;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 08/08/2017.
 */

public class CaloriesDataRequester implements DataRequester {

	private BuildFitnessClient mClient;
	private static int DAYS_OF_WEEK = 7;
	private static int DAYS_OF_MONTH = 30;
	private static int DAY = 1;

	@Override
	public DataRequester setClient(BuildFitnessClient client) {
		mClient = client;
		return this;
	}

	@Override
	public void requestDataFor(int range) {
		Range.get(range).requestDataFor(mClient);

	}

	public enum Range {
		DAILY(0, new RangeDataRequester() {
			@Override
			public void requestDataWithRange(final BuildFitnessClient client) {
				client.requestHistoryData(client.buildQueryForFitnessData(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED,
					DAY, Utils.getStartTime(), Utils.getEndTime()), new ResultCallback<DataReadResult>() {
					@Override
					public void onResult(@NonNull DataReadResult dataReadResult) {
						client.onTodayCaloriesUpdated(client.getDailyCalories(dataReadResult));
						client.onTodayCaloriesForHistory(client.getCalories(dataReadResult, BuildFitnessClient.Range.DAILY));
					}
				});
			}
		}),
		WEEKLY(1, new RangeDataRequester() {
			@Override
			public void requestDataWithRange(final BuildFitnessClient client) {
				client.requestHistoryData(client.buildQueryForFitnessData(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED,
					7, Utils.getStartWeek(), Utils.getEndWeek()),
					new ResultCallback<DataReadResult>() {
						@Override
						public void onResult(@NonNull DataReadResult dataReadResult) {
							client.onLastWeekCaloriesUpdated(client.getCalories(dataReadResult, BuildFitnessClient.Range.WEEKLY));
						}
					});
			}
		}),
		MONTHLY(2, new RangeDataRequester() {
			@Override
			public void requestDataWithRange(final BuildFitnessClient client) {
				client.requestHistoryData(client.buildQueryForFitnessData(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED,
					Calendar.WEEK_OF_MONTH, Utils.getStartMonth(), Utils.getEndMonth()), new ResultCallback<DataReadResult>() {
					@Override
					public void onResult(@NonNull DataReadResult dataReadResult) {
						client.onLastMonthCaloriesUpdated(client.getCalories(dataReadResult, BuildFitnessClient.Range.MONTHLY));
					}
				});
			}
		});

		private int mResourceId;
		private RangeDataRequester mDataRequester;

		Range(int resourceId, RangeDataRequester rangeDataRequester) {
			mResourceId = resourceId;
			mDataRequester = rangeDataRequester;
			Static.sMap.put(mResourceId, this);
		}

		public void requestDataFor(BuildFitnessClient client) {
			mDataRequester.requestDataWithRange(client);
		}

		public static Range get(int range) {
			return Static.sMap.get(range);
		}

		static class Static {

			static Map<Integer, Range> sMap = new HashMap<>();
		}

		private interface RangeDataRequester {

			void requestDataWithRange(BuildFitnessClient client);
		}

		public int getIndex() {
			return mResourceId;
		}
	}
}
