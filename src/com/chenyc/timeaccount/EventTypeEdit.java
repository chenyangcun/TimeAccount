package com.chenyc.timeaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.provider.EventTypeAdapter;
import com.chenyc.timeaccount.provider.SyncLogAdapter;

public class EventTypeEdit extends Activity {
	private EditText mNameText;
	private Long mRowId;

	private EventTypeAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventtype_edit);
		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.type_manage));

		mDbHelper = new EventTypeAdapter(this);
		mDbHelper.open();

		mNameText = (EditText) findViewById(R.id.name);

		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = savedInstanceState != null ? savedInstanceState
				.getLong(EventTypeAdapter.KEY_ROWID) : null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras
					.getLong(EventTypeAdapter.KEY_ROWID) : null;
		}

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String name = mNameText.getText().toString();
				if ("".equals(name.trim())) {
					new AlertDialog.Builder(EventTypeEdit.this).setTitle("提示")
							.setMessage("事件类型不能为空！").setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface,
												int i) {
											return;
										}
									}).show();
					return;
				}
				setResult(RESULT_OK);
				finish();
			}

		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor note = mDbHelper.fetchEventType(mRowId);
			startManagingCursor(note);
			mNameText.setText(note.getString(note
					.getColumnIndexOrThrow(EventTypeAdapter.KEY_NAME)));

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(EventTypeAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String name = mNameText.getText().toString();

		if ("".equals(name.trim())) {
			return;
		}

		if (mRowId == null) {
			long id = mDbHelper.createEventType(name);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateEventType(mRowId, name);
		}
		SyncLogAdapter.log(mDbHelper.getMDb(), "eventtype", "update", mRowId);
	}

}
