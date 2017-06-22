package com.sourcey.materiallogindemo.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.Application;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.database.NotesTable;

/**
 * View holder for {@link R.layout#content_ad_cell}
 */
public class ContentCellHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	//public final TextView title;
	//public final TextView description;

	private Button addNote;
	private EditText noteText;


	public int myday;
	public int myhour;
	public String noteTextString;
	public String userName;

	public ContentCellHolder(@NonNull View view , int day , int hour , String text) {
		super(view);


		Activity myActivity = (Activity) view.getContext();
		userName = ((Application) myActivity.getApplication()).getUser();

		noteTextString = text;
		myday = day;
		myhour = hour;

		String buttonString = "";
		if (myhour > 12 )
		{
			buttonString = "Save " + (myhour -12) + "PM";
		}
		else
		{
			buttonString = "Save " + myhour + "AM";
		}


		//title = (TextView) view.findViewById(R.id.title);
		//description = (TextView) view.findViewById(R.id.description);

		addNote = (Button) view.findViewById(R.id.addnote);
		addNote.setText(buttonString);
		addNote.setOnClickListener(this);

		noteText = (EditText) view.findViewById(R.id.noteText);
		noteText.setText(text);
		noteText.requestFocus();

	}

	public ContentCellHolder(View itemView) {
		super(itemView);
	}

	public void update(ContentAdapterModel model) {
		if (model == null || model.layoutId != R.layout.content_cell) {
			return;
		}
		//noteText.setText(model.title);
		//description.setText(model.description);

		//int yalla= model.day;
		//noteText.setText("hey");
	}

	/*private NotesTable.Row toNoteModel(String firstName , String lastName) {
		Bundle bundle = new Bundle();
		bundle.putInt(BaseColumns._ID, 0);
		bundle.putString(NotesTable.NOTE, noteText.getText().toString());
		bundle.putString(NotesTable.FIRST_NAME, firstName);
		bundle.putString(NotesTable.LAST_NAME, lastName);

		return new NotesTable.Row(bundle);
	}*/

	@Override
	public void onClick(final View view) {


		Activity myActivity = (Activity) view.getContext();
		((Application) myActivity.getApplication()).
				getDatabaseHelper().
				updateNote(NotesTable.NAME , userName , myday , myhour , noteText.getText().toString() );

		//noteText.setHint(myhour + "");

		Toast
			.makeText(view.getContext(), "Saved", Toast.LENGTH_SHORT)
			.show();
	}
}
