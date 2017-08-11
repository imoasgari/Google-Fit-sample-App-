package com.example.user.FitLife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.FitLife.R;
import com.example.user.FitLife.adapter.viewholder.BaseViewHolder;
import com.example.user.FitLife.adapter.viewholder.DailyViewHolder;
import com.example.user.FitLife.adapter.viewholder.MonthlyViewHolder;
import com.example.user.FitLife.adapter.viewholder.WeeklyViewHolder;
import com.example.user.FitLife.models.HistoryListItem;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

	public static String DATE_FORMAT = "LLLL";
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
				return new DailyViewHolder(contactWeekView);
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
		private static HistoryListItem S_ITEM;

		public static String getMonthDate(long milliSeconds, String dateFormat) {
			// Create a DateFormatter object for displaying date in specified format.
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliSeconds);
			return formatter.format(calendar.getTime());

		}

//		public static String[] getMonthDate(Long historyListItem) {
//			DateTime dateTime = new DateTime();
//			String[] month = new String[]{};
//			historyListItem = S_ITEM.getEndDate();
//
//			for (int i = 0; i < historyListItem; i++) {
//				month[i] = String.valueOf(i);
//			}
//
//
//			return dateTime(month);
//		}

	}
}
