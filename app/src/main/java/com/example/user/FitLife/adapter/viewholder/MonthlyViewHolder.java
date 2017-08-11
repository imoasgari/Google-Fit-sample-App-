package com.example.user.FitLife.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.user.FitLife.R;
import com.example.user.FitLife.models.HistoryListItem;

/**
 * Created by user on 11/08/2017.
 */

public class MonthlyViewHolder extends BaseViewHolder {

	public static final int LAYOUT = R.layout.item_history;
	private TextView mDate;
	private TextView mStats;

	public MonthlyViewHolder(View itemView) {
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
		if (data.getType() == HistoryListItem.Type.MONTHLY) {
			mStats.setText(data.getValue().toString());
		}
	}
}
