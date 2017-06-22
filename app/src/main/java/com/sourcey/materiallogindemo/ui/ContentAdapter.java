package com.sourcey.materiallogindemo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import com.sourcey.materiallogindemo.R;

/**
 * recycler view adapter
 */
public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
	private final LayoutInflater layoutInflater;
	private final List<ContentAdapterModel> items;

	private int day;
	private int hour;

	public ContentAdapter(@NonNull Context contenxt, List<ContentAdapterModel> items , int day) {
		layoutInflater = LayoutInflater.from(contenxt);
		this.items = items != null ? items : Collections.<ContentAdapterModel>emptyList();
		this.day = day;
		this.hour = 0;

		// work great in case getItemId() below returns unique id
		setHasStableIds(true);
	}

	@Override
	public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
		View view = layoutInflater.inflate(viewType, parent, false);
		ViewHolder viewHolder;

		switch (viewType) {
			case R.layout.content_ad_cell:
				// if ad - use directly ViewHolder, no need to create a new class for it
				viewHolder = new ViewHolder(view) {
				};
				break;
			default:
				// regular monday cell
				viewHolder = new ContentCellHolder(view, this.day , this.hour , items.get(hour).note);
				this.hour = this.hour + 1;
				break;
		}
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		if (holder instanceof ContentCellHolder) {
			// only this type of holder needs to update
			((ContentCellHolder) holder).update(items.get(position));
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public int getItemViewType(final int position) {
		// ContentAdapterModel.layoutId is the unique view type since this is an unique id through the whole app
		return items.get(position).layoutId;
	}

	@Override
	public long getItemId(final int position) {
		// by using 'position' as a unique item id, we call setHasStableIds(true); in constructor which boosts scrolling
		return position;
	}
}