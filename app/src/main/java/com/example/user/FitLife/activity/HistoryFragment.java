package com.example.user.FitLife.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.FitLife.adapter.HistoryAdapter;
import com.example.user.FitLife.R;
import com.example.user.FitLife.activity.clientprovider.BuildFitnessClientProvider;
import com.example.user.FitLife.models.HistoryListItem;
import com.example.user.FitLife.presenter.HistoryPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 26/07/2017.
 */

public class HistoryFragment extends Fragment {

	private HistoryPresenter mHistoryPresenter;
	private Spinner dataSpinner;
	private Spinner timeSpinner;
	private RecyclerView mRecyclerView;
	private HistoryAdapter mHistoryAdapter;
	private List<HistoryListItem> mData = new ArrayList<>();


	@Override
	public void onAttach(Context context) {
		BuildFitnessClientProvider provider = (BuildFitnessClientProvider) context;
		mHistoryPresenter = new HistoryPresenter(this, provider.getFitnessClient());
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, container, false);
		mHistoryPresenter.onViewReady();

		mRecyclerView = view.findViewById(R.id.my_recycler_view);
		mHistoryAdapter = new HistoryAdapter(getActivity(), mData);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setAdapter(mHistoryAdapter);


		dataSpinner = view.findViewById(R.id.data_spinner);
		timeSpinner = view.findViewById(R.id.time_spinner);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, getResources().getStringArray(R.array.data_view_array));
		dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		dataSpinner.setAdapter(dataAdapter);

		ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, getResources().getStringArray(R.array.time_view_array));
		timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		timeSpinner.setAdapter(timeAdapter);

		dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				mHistoryPresenter.requestDataFor(position, timeSpinner.getSelectedItemPosition());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				mHistoryPresenter.requestDataFor(dataSpinner.getSelectedItemPosition(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});


		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHistoryPresenter.clientManagement();
	}

	public void displayLastMonthDistance(List<HistoryListItem> lastMonthDistance) {
		mHistoryAdapter.onNewData(lastMonthDistance);
	}

	public void displayLastMonthCalories(List<HistoryListItem> lastMonthCalories) {
		mHistoryAdapter.onNewData(lastMonthCalories);
	}

	public void displayLastMonthSteps(List<HistoryListItem> totalStepsLastMonth) {
		mHistoryAdapter.onNewData(totalStepsLastMonth);
	}

	public void displayLastWeekDistance(List<HistoryListItem> lastWeekDistance) {
		mHistoryAdapter.onNewData(lastWeekDistance);

	}

	public void displayLastWeekCalories(List<HistoryListItem> lastWeekCalories) {
		mHistoryAdapter.onNewData(lastWeekCalories);

	}

	public void displayLastWeekSteps(List<HistoryListItem> totalLastWeekSteps) {
		mHistoryAdapter.onNewData(totalLastWeekSteps);

	}

	public List<HistoryListItem> getData() {
		return mData;
	}

	public void displayTodayCalories(List<HistoryListItem> calories) {
		mHistoryAdapter.onNewData(calories);
	}

	public void displayTodayDistance(List<HistoryListItem> distance) {
		mHistoryAdapter.onNewData(distance);
	}

	public void displayTodaySteps(List<HistoryListItem> steps) {
		mHistoryAdapter.onNewData(steps);
	}
}
