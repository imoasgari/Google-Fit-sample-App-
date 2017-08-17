package com.example.user.FitLife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.user.FitLife.activity.datarequester.CaloriesDataRequester;
import com.example.user.FitLife.activity.datarequester.DataRequester;
import com.example.user.FitLife.activity.datarequester.DistanceDataRequester;
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
	private int mHistorySteps;
	private int mSensorSteps;
	private Utils mUtils = new Utils();


	public BuildFitnessClient(FragmentActivity context) {
		mContext = context;
		mDataRequesterMap.put(0, new CaloriesDataRequester().setClient(this));
		mDataRequesterMap.put(1, new DistanceDataRequester().setClient(this));
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

	public void initClient() {
		mUtils.initTimes();
		//check if the client is null || is disconnected
		if (mClient == null || !mClient.isConnected()) {


			// Create the Google API Client
			mClient = new GoogleApiClient.Builder(mContext)
				.addApi(Fitness.HISTORY_API)
				.addApi(Fitness.SENSORS_API)
				.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
				.addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
				.addConnectionCallbacks(
					new GoogleApiClient.ConnectionCallbacks() {

						@Override
						public void onConnected(Bundle bundle) {
							sensorStepCount();
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

	public List<HistoryListItem> getCalories(DataReadResult dataReadResult, Range range) {
		float calories;
		List<HistoryListItem> caloriesList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						calories = dataPoint.getValue(field).asFloat();
						caloriesList.add(new HistoryListItem<>(calories, range.ordinal(), dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
					}
				}
			}
		}
		return caloriesList;
	}

	public void getStepsForToday(DailyTotalResult dailyTotalResult, Range range) {
		int total;
		List<HistoryListItem> stepList = new ArrayList<>();
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
				stepList.add(new HistoryListItem<>(total, range.ordinal(), Utils.getStartTime(), Utils.getEndTime()));
				mHistoryListener.onTodayStepsUpdatedForHistory(stepList);
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

	public List<HistoryListItem> getSteps(DataReadResult dataReadResult, Range range) {
		int steps;
		List<HistoryListItem> stepsList = new ArrayList<>();
		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						steps = dataPoint.getValue(field).asInt();
						stepsList.add(new HistoryListItem<>(steps, range.ordinal(), dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));

					}
				}
			}
		}
		return stepsList;
	}

	public List<HistoryListItem> getDistance(DataReadResult dataReadResult, Range range) {
		float distance;
		List<HistoryListItem> historyListItems = new ArrayList<>();
		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				if (dataSets.size() != 0) {
					for (DataPoint dataPoint : dataSet.getDataPoints()) {
						for (Field field : dataPoint.getDataType().getFields()) {
							distance = dataPoint.getValue(field).asFloat();
							distance /= 1000;
							historyListItems.add(new HistoryListItem<>(distance, range.ordinal(), dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
						}
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

	public enum Range {
		DAILY(0),
		WEEKLY(1),
		MONTHLY(2);
		private int mInt;

		Range(int anInt) {
			mInt = anInt;
		}

		public int getIndex() {
			return mInt;
		}
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

	public void onTodayCaloriesForHistory(List<HistoryListItem> calories) {
		mHistoryListener.onTodayCaloriesUpdatedForHistory(calories);
	}


	public void onTodayDistanceForHistory(List<HistoryListItem> distance) {
		mHistoryListener.onTodayDistanceUpdatedForHistory(distance);
	}

}