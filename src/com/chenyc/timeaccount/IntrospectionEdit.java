package com.chenyc.timeaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.provider.Introspection;
import com.chenyc.timeaccount.provider.SyncLogAdapter;
import com.chenyc.timeaccount.provider.TimeAccount;
import com.chenyc.timeaccount.provider.TimeItemAdapter;

public class IntrospectionEdit extends Activity {

	private String mSelectDate;

	private EditText mIntrospectionEdit;

	private long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introspection_edit);
		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.menu_introspection));

		TextView mSelectDateView = (TextView) findViewById(R.id.select_date);

		mSelectDate = savedInstanceState != null ? savedInstanceState
				.getString(TimeItemAdapter.KEY_DATE) : null;

		if (mSelectDate == null) {
			Bundle extras = getIntent().getExtras();
			mSelectDate = extras != null ? extras
					.getString(TimeItemAdapter.KEY_DATE) : null;
		}
		mSelectDateView.setText(mSelectDate);

		mIntrospectionEdit = (EditText) findViewById(R.id.introspection);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String introspection = mIntrospectionEdit.getText().toString();
				if (introspection == null || "".equals(introspection.trim())) {
					new AlertDialog.Builder(IntrospectionEdit.this).setTitle(
							"提示").setMessage("反思内容不能为空！").setPositiveButton(
							"确定", new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
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
		if (mSelectDate != null) {
			Cursor cursor = getContentResolver().query(
					TimeAccount.INTROSPECTION_CONTENT_URI,
					new String[] { Introspection.KEY_ROWID,
							Introspection.KEY_INTRO }, "date = ?",
					new String[] { mSelectDate }, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				String introspection = cursor.getString(cursor
						.getColumnIndexOrThrow(Introspection.KEY_INTRO));
				mIntrospectionEdit.setText(introspection);
				mRowId = cursor.getLong(cursor
						.getColumnIndexOrThrow(Introspection.KEY_ROWID));
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Introspection.KEY_ROWID, mSelectDate);
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
		String introspection = mIntrospectionEdit.getText().toString();
		if (mSelectDate != null && introspection != null
				&& !"".equals(introspection.trim())) {
			ContentValues cv = new ContentValues();
			cv.put(Introspection.KEY_DATE, mSelectDate);
			cv.put(Introspection.KEY_INTRO, introspection);
			if (mRowId > 0) {
				cv.put(Introspection.KEY_ROWID, mRowId);
				getContentResolver().update(
						Uri.withAppendedPath(
								TimeAccount.INTROSPECTION_CONTENT_URI, "/"
										+ mRowId), cv, null, null);
			} else {
				Uri uri = getContentResolver().insert(
						TimeAccount.INTROSPECTION_CONTENT_URI, cv);
				String[] uriArray = uri.toString().split("/");
				mRowId = Long.parseLong(uriArray[uriArray.length-1]);
			}
			SyncLogAdapter.log(getContentResolver(), "introspection", "update", mRowId);
		}
	}

}
