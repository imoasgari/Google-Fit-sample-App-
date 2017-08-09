package com.example.user.FitLife;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.FitLife.models.HistoryListItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

	public static String DATE_FORMAT = "LLLL";
	private Context mContext;
	private List<HistoryListItem> mHistoryListItem;

	public HistoryAdapter(Context context, List<HistoryListItem> historyListItem) {
		mHistoryListItem = historyListItem;
		mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View contactView = inflater.inflate(ViewHolder.LAYOUT, parent, false);
		return new ViewHolder(contactView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		HistoryListItem historyListItem = mHistoryListItem.get(position);
		holder.mStats.setText(historyListItem.getValue().toString());
		holder.mDate.setText(Utils.getMonthDate(historyListItem.getEndDate(), DATE_FORMAT));

	}

	@Override
	public int getItemCount() {
		return mHistoryListItem.size();
	}

	public void onNewData(List<HistoryListItem> valuesListFloat) {
		mHistoryListItem.clear();
		mHistoryListItem.addAll(valuesListFloat);
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private static int LAYOUT = R.layout.items_history;
		private TextView mDate;
		private TextView mStats;

		public ViewHolder(View itemView) {
			super(itemView);
			findViews(itemView);
		}

		private void findViews(View itemView) {
			mDate = itemView.findViewById(R.id.view_date);
			mStats = itemView.findViewById(R.id.view_stats);
		}
	}

	public static class Utils {

		public static String getMonthDate(long milliSeconds, String dateFormat) {
			// Create a DateFormatter object for displaying date in specified format.
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds);
			return formatter.format(calendar.getTime());
		}

	}
}
