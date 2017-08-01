package com.example.user.testgooglefit.activity;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.testgooglefit.PermissionManager;
import com.example.user.testgooglefit.R;
import com.example.user.testgooglefit.ShowSensorData;
import com.example.user.testgooglefit.activity.clientprovider.BuildFitnessClientProvider;
import com.example.user.testgooglefit.models.Distance;
import com.example.user.testgooglefit.presenter.DashboardPresenter;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.List;

import static com.example.user.testgooglefit.PermissionManager.MY_PERMISSION_FINE_LOCATION;

public class DashboardFragment extends Fragment implements ShowSensorData {

	private DashboardPresenter mPresenter;
	private static final String TAG = "BasicHistoryApi";
	private TextView mTodaySteps;
	private TextView mDistance;
	private TextView mTodayCalories;
	private TextView textStepsView;
	private TextView textRemainingView;
	private DecoView decoView;
	private DecoView distanceDecoView;
	private DecoView caloriesDecoView;
	private int mBackIndex;
	private int mSeriesIndex;
	private int distanceSeriesIndex;
	private final float mSeriesMax = 10000f;
	private final float caloriesMax = 5000f;
	private final float distanceMax = 10f;
	private int mBackIndexDistance;
	private int mBackIndexCalories;
	private int mSeriesIndexCalories;


	@Override
	public void onAttach(Context context) {
		BuildFitnessClientProvider provider = (BuildFitnessClientProvider) context;
		mPresenter = new DashboardPresenter(this, provider.getFitnessClient());
		super.onAttach(context);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dashboard, viewGroup, false);

		if (!PermissionManager.checkPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, PermissionManager.MY_PERMISSION_FINE_LOCATION)) {
			mPresenter.onViewReady();
		}

		initViews(view);
		decoView = view.findViewById(R.id.dynamicArcView);
		distanceDecoView = view.findViewById(R.id.dynamicArcViewDistance);
		caloriesDecoView = view.findViewById(R.id.dynamicArcViewCalories);
		createBackSeries();
		createDataSeries();
		createEvents();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void createDataSeries() {
		final SeriesItem seriesItem = createSeriesItem("#005E91", mSeriesMax, false);
		seriesItem.addArcSeriesItemListener(new SeriesListener());
		mSeriesIndex = decoView.addSeries(seriesItem);


		final SeriesItem seriesItemDistance = createSeriesItem("#005E91", distanceMax, false);
		seriesItem.addArcSeriesItemListener(new SeriesListener());
		distanceSeriesIndex = distanceDecoView.addSeries(seriesItemDistance);


		final SeriesItem seriesItemCalories = createSeriesItem("#005E91", caloriesMax, false);
		seriesItem.addArcSeriesItemListener(new SeriesListener());
		mSeriesIndexCalories = caloriesDecoView.addSeries(seriesItemCalories);
	}



	private void createBackSeries() {
		SeriesItem seriesItem = createSeriesItem("#FFE2E2E2", mSeriesMax, true);
		mBackIndex = decoView.addSeries(seriesItem);

		SeriesItem seriesItemDistance = createSeriesItem("#FFE2E2E2", distanceMax, true);
		mBackIndexDistance = distanceDecoView.addSeries(seriesItemDistance);

		SeriesItem seriesItemCalories = createSeriesItem("#FFE2E2E2", caloriesMax, true);
		mBackIndexCalories = caloriesDecoView.addSeries(seriesItemCalories);
	}

	private void createEvents() {
		decoView.executeReset();

		decoView.addEvent(new DecoEvent.Builder(mSeriesMax)
			.setIndex(mBackIndex)
			.setDuration(1000)
			.setDelay(100)
			.build());

		distanceDecoView.executeReset();

		distanceDecoView.addEvent(new DecoEvent.Builder(distanceMax)
			.setIndex(mBackIndexDistance)
			.setDuration(1000)
			.setDelay(100)
			.build());

		caloriesDecoView.executeReset();

		caloriesDecoView.addEvent(new DecoEvent.Builder(caloriesMax)
			.setIndex(mBackIndexCalories)
			.setDuration(1000)
			.setDelay(100)
			.build());
	}

	private SeriesItem createSeriesItem(String color, float limit, boolean visibility) {
		return new SeriesItem.Builder(Color.parseColor(color))
			.setRange(0, limit, 0)
			.setInitialVisibility(visibility)
			.build();
	}

	private void initViews(View view) {
		mTodaySteps = view.findViewById(R.id.textSteps);
		mDistance = view.findViewById(R.id.textDistance);
		mTodayCalories = view.findViewById(R.id.textCalories);
//		textStepsView = view.findViewById(R.id.textPercentage);
		//textRemainingView = view.findViewById(R.id.textRemaining);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case MY_PERMISSION_FINE_LOCATION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					mPresenter.onViewReady();
				} else {
					Toast.makeText(getContext(), R.string.location_denied, Toast.LENGTH_SHORT).show();

				}
		}
	}

	public void displayTodaySteps(int finalSteps) {
		mTodaySteps.setText(finalSteps + "");
		decoView.addEvent(new DecoEvent.Builder(finalSteps)
			.setIndex(mSeriesIndex)
			.setDelay(1250)
			.build());
	}

	public void displayTodayTotalDistance(float todayDistance) {
		mDistance.setText(todayDistance + "");
		distanceDecoView.addEvent(new DecoEvent.Builder(todayDistance)
			.setIndex(distanceSeriesIndex)
			.setDelay(1250)
			.build());
	}

	public void displayTodayCalories(float todayCalories) {
		mTodayCalories.setText(todayCalories + "");
		caloriesDecoView.addEvent(new DecoEvent.Builder(todayCalories)
			.setIndex(mSeriesIndexCalories)
			.setDelay(1250)
			.build());
	}

	public void displaySensorStepsOnCircle(int sensorSteps) {
	}

	public class SeriesListener implements SeriesItem.SeriesItemListener {

		@Override
		public void onSeriesItemAnimationProgress(float v, float v1) {

		}

		@Override
		public void onSeriesItemDisplayProgress(float percentComplete) {
			displaySensorStepsOnCircle((int) percentComplete);
		}
	}

}
