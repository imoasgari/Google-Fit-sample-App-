package com.example.user.testgooglefit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.user.testgooglefit.models.Calories;
import com.example.user.testgooglefit.models.Distance;
import com.example.user.testgooglefit.models.Steps;
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
import java.util.List;
import java.util.concurrent.TimeUnit;


import static android.content.ContentValues.TAG;

/**
 * Created by user on 07/07/2017.
 */

public class BuildFitnessClient {

	private GoogleApiClient mClient;
	private FragmentActivity mContext;
	private ShowFitnessDataInterface mListener;
	private OnDataPointListener dListener;
	private ShowFitnessWeeklyDataInterface wListener;
	private ShowFitnessMonthlyInterface monthlyListener;
	private long startTime;
	private long endTime;
	private int mHistorySteps;
	private int mSensorSteps;
	private long endWeek;
	private long startWeek;
	private long endMonth;
	private long startMonth;

	public enum Client {
		DAILY,
		HISTORY
	}

	public BuildFitnessClient(FragmentActivity context) {
		mContext = context;
	}

	public void setListener(ShowFitnessDataInterface listener) {
		mListener = listener;
	}

	public void setMonthlyListener(ShowFitnessMonthlyInterface listener) {
		monthlyListener = listener;
	}

	public void setWeeklyListener(ShowFitnessWeeklyDataInterface listener) {
		wListener = listener;
	}

	public void initClient(final Client client) {
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
							if (client == Client.DAILY) {
								initDailyFitnessData();
								//subscribe();
							} else if (client == Client.HISTORY) {
								initHistoryFitnessData();
							}
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

	private void initHistoryFitnessData() {
		setWeekTime();
		setMonthTime();
		distanceCountWeekly();
		caloriesCountWeekly();
		stepCountWeekly();
		distanceCountMonthly();
		caloriesCountMonthly();
		stepCountMonthly();
	}

	private void initDailyFitnessData() {
		setDayTime();
		stepCount();
		caloriesCount();
		distanceCount();
		sensorStepCount();
	}

	private void sensorStepCount() {
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

	private void distanceCount() {
		DataReadRequest readDistanceData = queryFitnessData(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA);
		Fitness.HistoryApi.readData(mClient, readDistanceData).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				mListener.onTodayDistanceUpdated(getDailyDistance(dataReadResult));
			}
		});
	}

	private void distanceCountWeekly() {
		DataReadRequest readDistanceDataWeekly = queryFitnessDataWeekly(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA);
		Fitness.HistoryApi.readData(mClient, readDistanceDataWeekly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				wListener.onLastWeekDistanceUpdated(getDistance(dataReadResult));
			}
		});
	}

	private void distanceCountMonthly() {
		DataReadRequest readDistanceDataMonthly = queryFitnessDataMonthly(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA);
		Fitness.HistoryApi.readData(mClient, readDistanceDataMonthly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				monthlyListener.onLastMonthDistanceUpdated(getDistance(dataReadResult));
			}
		});
	}

	private void caloriesCount() {
		DataReadRequest readCaloriesData = queryFitnessData(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED);
		Fitness.HistoryApi.readData(mClient, readCaloriesData).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				mListener.onTodayCalories(getDailyCalories(dataReadResult));
			}
		});
	}

	private void caloriesCountWeekly() {
		DataReadRequest readCaloriesDataWeekly = queryFitnessDataWeekly(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED);
		Fitness.HistoryApi.readData(mClient, readCaloriesDataWeekly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				wListener.onLastWeekCaloriesUpdated(getCalories(dataReadResult));
			}
		});
	}

	private void caloriesCountMonthly() {
		DataReadRequest readCaloriesDataMonthly = queryFitnessDataMonthly(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED);
		Fitness.HistoryApi.readData(mClient, readCaloriesDataMonthly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				monthlyListener.onLastMonthCaloriesUpdated(getCalories(dataReadResult));
			}
		});
	}

	private void stepCount() {
		Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA).setResultCallback(new ResultCallback<DailyTotalResult>() {
			@Override
			public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
				getStepsForToday(dailyTotalResult);
			}
		});
	}

	private void stepCountWeekly() {
		DataReadRequest readStepsDataWeekly = queryFitnessDataWeekly(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA);
		Fitness.HistoryApi.readData(mClient, readStepsDataWeekly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				wListener.onLastWeekStepsUpdated(getSteps(dataReadResult));
			}
		});
	}

	private void stepCountMonthly() {
		DataReadRequest readStepDataMonthly = queryFitnessDataMonthly(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA);
		Fitness.HistoryApi.readData(mClient, readStepDataMonthly).setResultCallback(new ResultCallback<DataReadResult>() {
			@Override
			public void onResult(@NonNull DataReadResult dataReadResult) {
				monthlyListener.onLastMonthStepsUpdated(getSteps(dataReadResult));
			}
		});
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

	private DataReadRequest queryFitnessData(DataType dataTypeS, DataType dataTypeA) {
		return new DataReadRequest.Builder()
			.aggregate(dataTypeS, dataTypeA)
			.bucketByTime(1, TimeUnit.DAYS)
			.setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
			.build();
	}

	private DataReadRequest queryFitnessDataWeekly(DataType mDataType, DataType nDataType) {
		return new DataReadRequest.Builder()
			.aggregate(mDataType, nDataType)
			.bucketByTime(7, TimeUnit.DAYS)
			.setTimeRange(startWeek, endWeek, TimeUnit.MILLISECONDS)
			.build();
	}

	private DataReadRequest queryFitnessDataMonthly(DataType mDataType, DataType nDataType) {
		return new DataReadRequest.Builder()
			.aggregate(mDataType, nDataType)
			.bucketByTime(30, TimeUnit.DAYS)
			.setTimeRange(startMonth, endMonth, TimeUnit.MILLISECONDS)
			.build();
	}

//	public void subscribe() {
//		Fitness.RecordingApi.subscribe(mClient,DataType.TYPE_CALORIES_EXPENDED).setResultCallback(new ResultCallback<Status>() {
//			@Override
//			public void onResult(@NonNull Status status) {
//				if(status.isSuccess()) {
//					if(status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
//						Log.i("TAG", "Existing subscription for activity detected!");
//					}
//					else {
//						Log.i("TAG", "Successfully subscribed!");
//					}
//				} else {
//					Log.i("TAG", "There was a problem!");
//				}
//			}
//		});
//
//	}

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

	private List<Calories> getCalories(DataReadResult dataReadResult) {
		float calories;
		List<Calories> caloriesList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						calories = dataPoint.getValue(field).asFloat();
						caloriesList.add(new Calories((int)calories, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
					}
				}
			}
		}
//
//		Fitness.RecordingApi.listSubscriptions(mClient,DataType.TYPE_CALORIES_EXPENDED).setResultCallback(new ResultCallback<ListSubscriptionsResult>() {
//			@Override
//			public void onResult(@NonNull ListSubscriptionsResult listSubscriptionsResult) {
//				for (Subscription subscription : listSubscriptionsResult.getSubscriptions()) {
//					DataType dataType = subscription.getDataType();
//					System.out.println("Hello" + dataType.getName());
//
//				}
//			}
//		});
		return caloriesList;
	}

	private void getStepsForToday(DailyTotalResult dailyTotalResult) {
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


	private List<Steps> getSteps(DataReadResult dataReadResult) {
		int steps;
		List<Steps> stepsList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						steps = dataPoint.getValue(field).asInt();
						stepsList.add(new Steps(steps, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));

					}
				}
			}
		}
		return stepsList;
	}

	private List<Distance> getDistance(DataReadResult dataReadResult) {
		float distance;
		List<Distance> distanceList = new ArrayList<>();

		for (Bucket bucket : dataReadResult.getBuckets()) {
			List<DataSet> dataSets = bucket.getDataSets();
			for (DataSet dataSet : dataSets) {
				for (DataPoint dataPoint : dataSet.getDataPoints()) {
					for (Field field : dataPoint.getDataType().getFields()) {
						distance = dataPoint.getValue(field).asFloat();
						distance /= 1000;
						distanceList.add(new Distance(distance, dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
					}
				}
			}
		}
		return distanceList;
	}


	private float getDailyDistance(DataReadResult dataReadResult) {
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

	private float getDailyCalories(DataReadResult dataReadResult) {
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


	private void showSensorSteps(DataPoint dataPoint) {
		for (Field field : dataPoint.getDataType().getFields()) {
			final Value val = dataPoint.getValue(field);
			mSensorSteps = val.asInt();
			mHistorySteps += mSensorSteps;
			mListener.showSensorStepsOnCircle(mHistorySteps);
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
}
