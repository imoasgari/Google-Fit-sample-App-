package com.example.user.FitLife.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.user.FitLife.R;
import com.example.user.FitLife.adapter.HistoryAdapter;
import com.example.user.FitLife.models.HistoryListItem;

/**
 * Created by MohammadrezaAsgari on 11/08/2017.
 */

public class WeeklyViewHolder extends BaseViewHolder {
	public static final int LAYOUT = R.layout.item_history;
	private TextView mDate;
	private TextView mStats;

	public WeeklyViewHolder(View itemView) {
		super(itemView);
		findViews(itemView);
	}

	@Override
	protected void findViews(View view) {
		mDate = itemView.findViewById(R.id.view_date);
		mStats = itemView.findViewById(R.id.view_stats);
	}

	@Override
	public void onBind(HistoryListItem data) {
		if (data.getType() == HistoryListItem.Type.WEEKLY) {
			mStats.setText(data.getValue().toString());
			mDate.setText(HistoryAdapter.Utils.getDateFormatForWeek(data.getStartDate(), data.getEndDate()));
		}
	}
}
