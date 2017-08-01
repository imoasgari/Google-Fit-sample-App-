package com.example.user.testgooglefit.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.testgooglefit.BuildFitnessClient;
import com.example.user.testgooglefit.R;
import com.example.user.testgooglefit.activity.clientprovider.BuildFitnessClientProvider;
import com.example.user.testgooglefit.presenter.HistoryPresenter;


/**
 * Created by user on 26/07/2017.
 */

public class HistoryFragment extends Fragment {

	private HistoryPresenter mHistoryPresenter;
	private Spinner dataSpinner;
	private Spinner timeSpinner;
	private RecyclerView mRecyclerView;

	@Override
	public void onAttach(Context context) {
		BuildFitnessClientProvider provider = (BuildFitnessClientProvider) context;
		mHistoryPresenter = new HistoryPresenter(this, provider.getFitnessClient());
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, container, false );
		mHistoryPresenter.onViewReady();

		mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

//		dataSpinner = view.findViewById(R.id.data_spinner);
//		timeSpinner = view.findViewById(R.id.time_spinner);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.time_view_array, android.R.layout.simple_dropdown_item_1line);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		ArrayAdapter<CharSequence> dataAdaptor = ArrayAdapter.createFromResource(this.getActivity(), R.array.data_view_array, android.R.layout.simple_dropdown_item_1line);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		timeSpinner.setAdapter(adapter);
//		timeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//			}
//		});
//		dataSpinner.setAdapter(dataAdaptor);
//		dataSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//			}
//		});

		return view;
	}

	public void displayLastMonthDistance(float lastMonthDistance) {
	}

	public void displayLastMonthCalories(float lastMonthCalories) {
	}

	public void displayLastMonthSteps(int totalStepsLastMonth) {
	}

	public void displayLastWeekDistance(float lastWeekDistance) {
	}

	public void displayLastWeekCalories(float lastWeekCalories) {
	}

	public void displayLastWeekSteps(int totalLastWeekSteps) {
	}

}
