package com.example.user.testgooglefit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 26/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder{
		//TextView mName;

		public ViewHolder(View itemView) {
			super(itemView);

			findViews(itemView);
		}

		private void findViews(View itemView) {
			//nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
		}
	}
}
