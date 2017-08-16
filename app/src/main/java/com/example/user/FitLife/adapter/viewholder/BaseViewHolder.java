package com.example.user.FitLife.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.user.FitLife.models.HistoryListItem;

import java.text.ParseException;

/**
 * Created by user on 10/08/2017.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

	public BaseViewHolder(View itemView) {
		super(itemView);
	}

	protected abstract void findViews(View view);

	public abstract void onBind(HistoryListItem data);

}