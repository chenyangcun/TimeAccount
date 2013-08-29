package com.chenyc.timeaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.provider.EventTypeAdapter;
import com.chenyc.timeaccount.provider.SyncLogAdapter;
import com.chenyc.timeaccount.provider.TimeItemAdapter;

public class TimeItemEdit extends Activity {

	private Spinner mSpinner;
	private TimeItemAdapter mDbHelper;
	private EditText mContentText;
	private Long mRowId;
	private int mHour;
	private int mMinute;
	private EventTypeAdapter eventTypeProvider;
	private Cursor eventItemCursor;
	private String mSelectDate;
	private EditText mEditHour;
	private EditText mEditMinute;
	private RatingBar mRatingBar;
	private int mRate;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeitem_edit);

		mSelectDate = savedInstanceState != null ? savedInstanceState
				.getString(TimeItemAdapter.KEY_DATE) : null;

		if (mSelectDate == null) {
			Bundle extras = getIntent().getExtras();
			mSelectDate = extras != null ? extras
					.getString(TimeItemAdapter.KEY_DATE) : null;
		}

		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.today_account) + "[" + mSelectDate + "]");

		// 处理事件类型下拉框
		mSpinner = (Spinner) findViewById(R.id.event_type_spinner);
		eventTypeProvider = new EventTypeAdapter(this);
		eventTypeProvider.open();
		eventItemCursor = eventTypeProvider.fetchAllEventTypes();
		startManagingCursor(eventItemCursor);
		String[] from = new String[] { EventTypeAdapter.KEY_NAME };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, eventItemCursor, from, to);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);

		// 初始化TimeItemProvider
		mDbHelper = new TimeItemAdapter(this);
		mDbHelper.open();

		// 定义控件

		mContentText = (EditText) findViewById(R.id.content);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		mEditHour = (EditText) findViewById(R.id.hour);
		mEditMinute = (EditText) findViewById(R.id.minute);
		mRatingBar = (RatingBar) findViewById(R.id.ratingbar);

		// 获取数据
		mRowId = savedInstanceState != null ? savedInstanceState
				.getLong(TimeItemAdapter.KEY_ROWID) : null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras
					.getLong(TimeItemAdapter.KEY_ROWID) : null;
		}

		populateFields();

		// 设置按钮事件
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String content = mContentText.getText().toString();
				long eventTypeId = mSpinner.getSelectedItemId();
				String strHour = mEditHour.getText().toString();
				String strMinute = mEditMinute.getText().toString();
				if ("".equals(strHour)) {
					strHour = "0";
				}

				if ("".equals(strMinute)) {
					strMinute = "0";
				}
				mHour = Integer.valueOf(strHour);
				mMinute = Integer.valueOf(strMinute);
				mRate = (int) mRatingBar.getRating();

				String alertString = "";

				if ("".equals(mSelectDate)) {
					alertString = "日期不能为空！";
				} else if ("".equals(content.trim())) {
					alertString = "内容不能为空！";
				} else if (eventTypeId == 0) {
					alertString = "类型不能为空！";
				} else if (mHour == 0 && mMinute == 0) {
					alertString = "耗时不能为0小时0分！";
				}

				if (!"".equals(alertString)) {
					new AlertDialog.Builder(TimeItemEdit.this).setTitle("提示")
							.setMessage(alertString).setPositiveButton("确定",
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

		if (mRowId != null && mRowId != 0) {
			Cursor timeItem = mDbHelper.fetchTimeItem(mRowId);
			startManagingCursor(timeItem);
			mContentText.setText(timeItem.getString(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_CONTENT)));

			long eventTypeId = timeItem.getLong(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_EVENT_TYPE));

			mSpinner.setSelection(findEventTypeIdPosition(eventTypeId));

			mEditHour.setText(timeItem.getString(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_HOUR)));

			mEditMinute.setText(timeItem.getString(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_MINUTE)));

			mSelectDate = timeItem.getString(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_DATE));

			mRatingBar.setRating(timeItem.getInt(timeItem
					.getColumnIndexOrThrow(TimeItemAdapter.KEY_RATE)));

		}
	}

	private int findEventTypeIdPosition(long id) {
		eventItemCursor.moveToFirst();
		while (!eventItemCursor.isAfterLast()) {
			if (eventItemCursor.getLong(eventItemCursor
					.getColumnIndex(EventTypeAdapter.KEY_ROWID)) == id) {
				return eventItemCursor.getPosition();
			}
			eventItemCursor.moveToNext();
		}
		return 0;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(TimeItemAdapter.KEY_ROWID, mRowId);
		outState.putString(TimeItemAdapter.KEY_DATE, mSelectDate);
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
		String content = mContentText.getText().toString();
		long eventTypeId = mSpinner.getSelectedItemId();
		String strHour = mEditHour.getText().toString();
		String strMinute = mEditMinute.getText().toString();
		if ("".equals(strHour)) {
			strHour = "0";
		}

		if ("".equals(strMinute)) {
			strMinute = "0";
		}
		mHour = Integer.valueOf(strHour);
		mMinute = Integer.valueOf(strMinute);
		mRate = (int) mRatingBar.getRating();

		if ("".equals(content.trim()) || eventTypeId == 0) {
			return;
		}

		if (mHour == 0 && mMinute == 0) {
			return;
		}

		if (mRowId == null || mRowId == 0) {
			long id = mDbHelper.createTimeItem(eventTypeId, content, mHour,
					mMinute, mSelectDate, mRate);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateEventType(mRowId, eventTypeId, content, mHour,
					mMinute, mSelectDate, mRate);
		}
		
		SyncLogAdapter.log(mDbHelper.getMDb(),"timeitem", "update", mRowId);
	}
}
