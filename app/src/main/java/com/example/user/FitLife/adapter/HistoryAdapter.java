package com.example.user.FitLife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.FitLife.adapter.viewholder.BaseViewHolder;
import com.example.user.FitLife.adapter.viewholder.DailyViewHolder;
import com.example.user.FitLife.adapter.viewholder.MonthlyViewHolder;
import com.example.user.FitLife.adapter.viewholder.WeeklyViewHolder;
import com.example.user.FitLife.models.HistoryListItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

	private Context mContext;
	private List<HistoryListItem> mHistoryListItem;

	public HistoryAdapter(Context context, List<HistoryListItem> historyListItem) {
		mHistoryListItem = historyListItem;
		mContext = context;
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		switch (HistoryListItem.Type.fromId(viewType)) {
			case DAILY:
				View contactView = inflater.inflate(DailyViewHolder.LAYOUT, parent, false);
				return new DailyViewHolder(contactView);
			case WEEKLY:
				View contactWeekView = inflater.inflate(WeeklyViewHolder.LAYOUT, parent, false);
				return new WeeklyViewHolder(contactWeekView);
			case MONTHLY:
				View contactMonthView = inflater.inflate(MonthlyViewHolder.LAYOUT, parent, false);
				return new MonthlyViewHolder(contactMonthView);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		holder.onBind(mHistoryListItem.get(position));
	}

	@Override
	public int getItemCount() {
		return mHistoryListItem.size();
	}

	@Override
	public int getItemViewType(int position) {
		return mHistoryListItem.get(position).getType().ordinal();
	}

	public void onNewData(List<HistoryListItem> valuesListFloat) {
		mHistoryListItem.clear();
		mHistoryListItem.addAll(valuesListFloat);
		notifyDataSetChanged();
	}


	public static class Utils {
		private static String DATE_FORMAT_FOR_MONTH = "LLLL";
		private static String DATE_FORMAT_FOR_WEEK = "MMMM d";
		private static String DATE_FORMAT_FOR_DAY = "EEEE";

		public static String getMonthDate(long milliSeconds) {
			// Create a DateFormatter object for displaying date in specified format.
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_FOR_MONTH);

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds);
			return formatter.format(calendar.getTime());
		}

		public static String getDateFormatForDay(long milliSeconds) {
			String today = "Today";
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(milliSeconds);
			// Create a DateFormatter object for displaying date in specified format.
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_FOR_DAY);

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			if (calendar.compareTo(cal) == 1) {
				return today;
			} else {
				calendar.setTimeInMillis(milliSeconds);
				return formatter.format(calendar.getTime());
			}


		}

		public static String getDateFormatForWeek(long startDate, long endDate) {
			String startOfTheWeek;

			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_FOR_WEEK);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startDate);
			startOfTheWeek = formatter.format(calendar.getTime());

			return startOfTheWeek;
		}

	}
}
