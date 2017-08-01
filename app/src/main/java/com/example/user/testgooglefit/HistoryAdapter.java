package com.example.user.testgooglefit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.testgooglefit.models.Calories;
import com.example.user.testgooglefit.models.Distance;
import com.example.user.testgooglefit.models.Steps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

	public static String DATE_FORMAT = "dd:mm:yy";
	private List<Calories> mCalories;
	private List<Distance> mDistances;
	private List<Steps> mSteps;
	private Context mContext;

	public HistoryAdapter(List<Calories> calories, List<Distance> distances, List<Steps> steps, Context context) {
		mCalories = calories;
		mDistances = distances;
		mSteps = steps;
		mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View contactView = inflater.inflate(R.layout. , parent, false);
		ViewHolder viewHolder = new ViewHolder(contactView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// set the data to the views
		//holder.nameTextView.setText(Utils.formatDate(calories.getStartDate()));
		Calories calories = mCalories.get(position);
		Distance distance = mDistances.get(position);
		Steps steps = mSteps.get(position);
	}

	// create an Utils class with a static method for formating the date

	@Override
	public int getItemCount() {
		return mCalories.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder{
		// private static int LAYOUT = R.layout.history_list_item;
		// create history_list_item in layout folder
		//private TextView mName;

		public ViewHolder(View itemView) {
			super(itemView);

			findViews(itemView);
		}

		private void findViews(View itemView) {
			// find the views
			//nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
		}
	}

	public class Utils {

		public String getDate(long milliSeconds, String dateFormat)
		{
			// Create a DateFormatter object for displaying date in specified format.
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds);
			return formatter.format(calendar.getTime());
		}
	}
}
