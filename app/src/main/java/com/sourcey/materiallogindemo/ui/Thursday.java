package com.sourcey.materiallogindemo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Application;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.database.DatabaseHelper;
import com.sourcey.materiallogindemo.database.Model;
import com.sourcey.materiallogindemo.database.NotesTable;

import java.util.LinkedList;
import java.util.List;

/**
 * Present monday page for the view pager
 */
public final class Thursday extends LinearLayout {
	public final RecyclerView recyclerView;
	public final TextView dayTitle;

	public Thursday(final Context context) {
		this(context, null, 0);
	}

	public Thursday(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Thursday(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// inflate and add synced
		LayoutInflater.from(context).inflate(R.layout.content_, this, true);

		Activity host = (Activity) context;

		final String userName = ((Application) host.getApplication()).getUser();

		DatabaseHelper databaseHelper = ((Application) host.getApplication()).getDatabaseHelper();

		List<Model> noteList = databaseHelper.getDayNotes(NotesTable.NAME,userName, 3);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		dayTitle = (TextView) findViewById(R.id.dayTitle);
		dayTitle.setText("Thursday");
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setItemViewCacheSize(24);

		// get items
		List<ContentAdapterModel> items = getItems(noteList);

		// create a new instnace of the adapter and then supply the adapter to the recylcer view
		ContentAdapter contentAdapter = new ContentAdapter(context, items , 3);
		recyclerView.setAdapter(contentAdapter);
	}

	@Override
	protected void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// changind columns to 1 or 2 in case portrait or landscape
		int columns = Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation ? 2 : 1;
		((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(columns);
	}

	/**
	 * Create a dummy data store - list of ContentAdapterModel items to supply to ContentAdapter
	 * this needs to happen from a remote service or (preferably) from in-memory cache (data store) or eventually
	 * directly from a local database table result
	 *
	 * @return list of items
	 */
	private List<ContentAdapterModel> getItems(final List<Model> list) {



		return new LinkedList<ContentAdapterModel>() {{

			NotesTable.Row row = null;

			for (int i = 0; i < 24 && null != list; i++) {

				row = (NotesTable.Row) list.get(i);


				add(new ContentAdapterModel(R.layout.content_cell, 3,i, row.note));
			}
		}};
	}
}