package com.example.user.FitLife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.user.FitLife.activity.datarequester.CaloriesDataRequester;
import com.example.user.FitLife.activity.datarequester.DataRequester;
import com.example.user.FitLife.activity.datarequester.DistastanceDataRequester;
import com.example.user.FitLife.activity.datarequester.StepsDataRequester;
import com.example.user.FitLife.models.HistoryListItem;
import com.example.user.FitLife.presenter.HistoryListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 07/07/2017.
 */

public class BuildFitnessClient {

	private Map<Integer, DataRequester> mDataRequesterMap = new HashMap<>();

	private GoogleApiClient mClient;
	private FragmentActivity mContext;
	private ShowFitnessDataInterface mListener;
	private OnDataPointListener dListener;
	private HistoryListener mHistoryListener;
	private long startTime;
	private long endTime;
	private int mHistorySteps;
	private int mSensorSteps;
	private long endWeek;
	private long startWeek;
	private long endMonth;
	private long startMonth;

	public BuildFitnessClient(FragmentActivity context) {
		mContext = context;
		mDataRequesterMap.put(0, new CaloriesDataRequester().setClient(this));
		mDataRequesterMap.put(1, new DistastanceDataRequester().setClient(this));
		mDataRequesterMap.put(2, new StepsDataRequester().setClient(this));

	}

	public void setListener(ShowFitnessDataInterface listener) {
		mListener = listener;
	}

	public void setHistoryListener(HistoryListener listener) {
		mHistoryListener = listener;
	}

	public HistoryListener getHistoryListener() {
		return mHistoryListener;
	}

	public long getEndWeek() {
		return endWeek;
	}

	public long getStartWeek() {
		return startWeek;
	}

	public long getEndMonth() {
		return endMonth;
	}

	public long getStartMonth() {
		return startMonth;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void initClient() {
		//check if the client is null || is disconnected
		if (mClient == null || !mClient.isConnected()) {


			// Create the Google API Client
			mClient = new GoogleApiClient.Builder(mContext)
				.addApi(Fitness.HISTORY_API)
				.addApi(Fitness.SENSORS_API)
				.addApi(Fitness.RECORDING_API)
				.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
				.addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
				.addConnectionCallbacks(
					new GoogleApiClient.ConnectionCallbacks() {

						@Override
						public void onConnected(Bundle bundle) {
							initTime();
							initDailyFitnessData();
						}

						@Override
						public void onConnectionSuspended(int i) {
							if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
								Log.i(TAG, "Connection lost.  Cause: Network Lost.");
							} else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
								Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
							}
						}
					}
				)
				.enableAutoManage(mContext, 0, new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(ConnectionResult result) {
						mListener.onConnectionFailed();
					}
				})
				.build();
		} else {
			mClient.reconnect();
		}

	}

	private void initTime() {
		setWeekTime();
		setMonthTime();
		setDayTime();
	}

	private void initDailyFitnessData() {
		//stepCount();
		//caloriesCount();
		//distanceCount();
		sensorStepCount();
	}

	public void sensorStepCount() {
		Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
			.setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
			.setDataSourceTypes(DataSource.TYPE_DERIVED)
			.build())
			.setResultCallback(new ResultCallback<DataSourcesResult>() {
				@Override
				public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
					for (DataSource dataSource : dataSourcesResult.getDataSources()) {
						final DataType dataType = dataSource.getDataType();
						registerFitnessDataListener(dataSource, dataType);
					}
				}
			});
	}

//	public void stepCount() {
//		Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA).setResultCallback(new ResultCallback<DailyTotalResult>() {
//			@Override
//			public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
//				getStepsForToday(dailyTotalResult);
//			}
//		});
//	}

	public void requestDataForSteps(DataType dataType, ResultCallback<DailyTotalResult> callback) {
		Fitness.HistoryApi.readDailyTotal(mClient, dataType).setResultCallback(callback);
	}

	public void requestDataFor(int type, int range) {
		mDataRequesterMap.get(type).requestDataFor(range);
	}

	public void requestHistoryData(DataReadRequest readRequest, ResultCallback<DataReadResult> callback) {
		Fitness.HistoryApi.readData(mClient, readRequest).setResultCallback(callback);
	}

	private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
		OnDataPointListener onDataPointListener = new OnDataPointListener() {
			@Override
			public void onDataPoint(DataPoint dataPoint) {
				showSensorSteps(dataPoint);
			}
		};

		Fitness.SensorsApi.add(
			mClient,
			new SensorRequest.Builder()
				.setDataSource(dataSource)
				.setDataType(dataType)
				.setSamplingRate(30, TimeUnit.SECONDS)
				.build(),
			onDataPointListener)
			.setResultCallback(new ResultCallback<Status>() {
				@Override
				public void onResult(@NonNull Status status) {

				}
			});
	}

	public DataReadRequest buildQueryForFitnessData(DataType activityType, DataType populateDataType, int time, long startPeriod, long endPeriod) {
		return new DataReadRequest.Builder()
			.aggregate(activityType, populateDataType)
			.bucketByTime(time, TimeUnit.DAYS)
			.setTimeRange(startPeriod, endPeriod, TimeUnit.MILLISECONDS)
			.build();
	}

	private void setDayTime() {
		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);
		endTime = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		startTime = cal.getTimeInMillis();
	}

	private void setWeekTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		endWeek = calendar.getTimeInMillis();
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		startWeek = calendar.getTimeInMillis();
	}

	private void setMonthTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		endMonth = calendar.getTimeInMillis();
		calendar.add(Calendar.MONTH, -1);
		startMonth = calendar.getTimeInMillis();
	}

	public List<HistoryListItem> getCalories(DataReadResult dataReadResult) {
		float calories;
		List<HistoryListItem> caloriesList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						calories = dataPoint.getValue(field).asFloat();
						caloriesList.add(new HistoryListItem<>(calories, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
					}
				}
			}
		}
		return caloriesList;
	}

	public void getStepsForToday(DailyTotalResult dailyTotalResult) {
		int total;
		if (dailyTotalResult.getStatus().isSuccess()) {
			DataSet dataSet = dailyTotalResult.getTotal();
			if (dataSet != null) {
				if (dataSet.isEmpty()) {
					total = 0;
				} else {
					total = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
				}
				mHistorySteps = total;
				mListener.onTodayStepUpdated(mHistorySteps);
				mListener.showSensorStepsOnCircle(mHistorySteps);
			}
		}
	}

	public void showSensorSteps(DataPoint dataPoint) {
		for (Field field : dataPoint.getDataType().getFields()) {
			final Value val = dataPoint.getValue(field);
			mSensorSteps = val.asInt();
			mHistorySteps += mSensorSteps;
			mListener.showSensorStepsOnCircle(mHistorySteps);
		}
	}

	public float getDailyDistance(DataReadResult dataReadResult) {
		float distance = 0;

		Bucket bucket = dataReadResult.getBuckets().get(0);
		List<DataSet> dataSets = bucket.getDataSets();
		for (DataSet dataSet : dataSets) {
			for (DataPoint dataPoint : dataSet.getDataPoints()) {
				for (Field field : dataPoint.getDataType().getFields()) {
					distance = dataPoint.getValue(field).asFloat();
					distance /= 1000;
				}
			}
		}
		return distance;
	}

	public List<HistoryListItem> getSteps(DataReadResult dataReadResult) {
		int steps;
		List<HistoryListItem> stepsList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						steps = dataPoint.getValue(field).asInt();
						stepsList.add(new HistoryListItem<>(steps, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));

					}
				}
			}
		}
		return stepsList;
	}

	public List<HistoryListItem> getDistance(DataReadResult dataReadResult) {
		float distance;
		List<HistoryListItem> historyListItems = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						distance = dataPoint.getValue(field).asFloat();
						distance /= 1000;
						historyListItems.add(new HistoryListItem<>(distance, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
					}
				}
			}
		}
		return historyListItems;
	}

	public float getDailyCalories(DataReadResult dataReadResult) {
		float calories = 0;

		Bucket bucket = dataReadResult.getBuckets().get(0);
		List<DataSet> dataSets = bucket.getDataSets();
		for (DataSet dataSet : dataSets) {
			for (DataPoint dataPoint : dataSet.getDataPoints()) {
				for (Field field : dataPoint.getDataType().getFields()) {
					calories = dataPoint.getValue(field).asFloat();
				}
			}
		}
		return (int) calories;
	}

	public void onStopCalled() {
		if (mClient != null && mClient.isConnected()) {
			Fitness.SensorsApi.remove(mClient, dListener)
				.setResultCallback(new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						if (status.isSuccess()) {
							mClient.disconnect();
						}
					}
				});
		}
	}

	public void manageClients() {
		mClient.stopAutoManage(mContext);
		mClient.disconnect();
	}

	public void onLastWeekCaloriesUpdated(List<HistoryListItem> calories) {
		mHistoryListener.onLastWeekCaloriesUpdated(calories);
	}

	public void onLastWeekDistanceUpdated(List<HistoryListItem> distance) {
		mHistoryListener.onLastWeekDistanceUpdated(distance);
	}

	public void onLastWeekStepsUpdated(List<HistoryListItem> steps) {
		mHistoryListener.onLastWeekStepsUpdated(steps);
	}

	public void onLastMonthCaloriesUpdated(List<HistoryListItem> calories) {
		mHistoryListener.onLastMonthCaloriesUpdated(calories);
	}

	public void onLastMonthDistanceUpdated(List<HistoryListItem> distance) {
		mHistoryListener.onLastMonthDistanceUpdated(distance);
	}

	public void onLastMonthStepsUpdated(List<HistoryListItem> steps) {
		mHistoryListener.onLastMonthStepsUpdated(steps);
	}

	public void onTodayCaloriesUpdated(float dailyCalories) {
		mListener.onTodayCalories(dailyCalories);
	}

	public void onTodayDistanceUpdated(float dailyDistance) {
		mListener.onTodayDistanceUpdated(dailyDistance);
	}
}