package com.example.user.FitLife.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.FitLife.R;
import com.example.user.FitLife.Utils;
import com.example.user.FitLife.activity.clientprovider.BuildFitnessClientProvider;
import com.example.user.FitLife.adapter.HistoryAdapter;
import com.example.user.FitLife.models.HistoryListItem;
import com.example.user.FitLife.presenter.AnalyticsPresenter;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

/**
 * Created by MohammadrezaAsgari on 21/08/2017.
 */

public class AnalyticsFragment extends Fragment {

	private AnalyticsPresenter mPresenter;
	private LineGraphSeries<DataPoint> mSeries;
	private GraphView mGraphView;
	private Spinner dataSpinner;
	private Spinner timeSpinner;
	private DataPoint[] values;


	@Override
	public void onAttach(Context context) {
		BuildFitnessClientProvider provider = (BuildFitnessClientProvider) context;
		mPresenter = new AnalyticsPresenter(this, provider.getFitnessClient());
		super.onAttach(context);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_analytics, container, false);
		mPresenter.onViewReady();

		mGraphView = view.findViewById(R.id.graph);
		mGraphView.getViewport().setScalable(true);
		mGraphView.getViewport().setScalableY(true);

		dataSpinner = view.findViewById(R.id.data_spinner);
		timeSpinner = view.findViewById(R.id.time_spinner);

		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, getResources().getStringArray(R.array.data_view_array));
		dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		dataSpinner.setAdapter(dataAdapter);

		final ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, getResources().getStringArray(R.array.time_view_array));
		timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		timeSpinner.setAdapter(timeAdapter);

		dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				mPresenter.requestDataFor(position, timeSpinner.getSelectedItemPosition());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				mPresenter.requestDataFor(dataSpinner.getSelectedItemPosition(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		return view;
	}


	public void lastWeekSteps(List<HistoryListItem> totalLastWeekSteps) {
		mSeries = new LineGraphSeries<>(generateData(totalLastWeekSteps));
		mGraphView.addSeries(mSeries);
		mGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					return super.formatLabel(value, isValueX);
				} else {
					for (int i = 0; i < values.length; i++) {
						return Utils.getDateFormatForWeek(value);
					}
				}
				return null;
			}
		});
	}

	private DataPoint[] generateData(List<HistoryListItem> totalListValues) {
		values = new DataPoint[totalListValues.size()];
		for (int i = 0; i < totalListValues.size(); i++) {
			HistoryListItem item = totalListValues.get(i);
			values[i] = new DataPoint(item.getX(), item.getY());
		}

		return values;
	}

	public void lastWeekDistance(List<HistoryListItem> lastWeekDistance) {
		mSeries = new LineGraphSeries<>(generateData(lastWeekDistance));
		mGraphView.addSeries(mSeries);
		mGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					return super.formatLabel(value, isValueX);
				} else {
					for (int i = 0; i < values.length; i++) {
						return Utils.getDateFormatForWeek(value);
					}
				}
				return null;
			}
		});
	}

	public void lastWeekCalories(List<HistoryListItem> lastWeekCalories) {
		mSeries = new LineGraphSeries<>(generateData(lastWeekCalories));
		mGraphView.addSeries(mSeries);
	}

	public void lastMonthDistance(List<HistoryListItem> lastMonthDistance) {
		mSeries = new LineGraphSeries<>(generateData(lastMonthDistance));
		mGraphView.addSeries(mSeries);
	}

	public void lastMonthCalories(List<HistoryListItem> lastMonthCalories) {
		mSeries = new LineGraphSeries<>(generateData(lastMonthCalories));
		mGraphView.addSeries(mSeries);
	}

	public void lastMonthSteps(List<HistoryListItem> totalStepsLastMonth) {
		mSeries = new LineGraphSeries<>(generateData(totalStepsLastMonth));
		mGraphView.addSeries(mSeries);
	}

	public void todayCalories(List<HistoryListItem> calories) {
		mSeries = new LineGraphSeries<>(generateData(calories));
		mGraphView.addSeries(mSeries);
	}

	public void todayDistance(List<HistoryListItem> distance) {
		mSeries = new LineGraphSeries<>(generateData(distance));
		mGraphView.addSeries(mSeries);
	}

	public void todaySteps(List<HistoryListItem> steps) {
		mSeries = new LineGraphSeries<>(generateData(steps));
		mGraphView.addSeries(mSeries);
	}
}
