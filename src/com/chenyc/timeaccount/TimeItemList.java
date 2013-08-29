package com.chenyc.timeaccount;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.provider.Introspection;
import com.chenyc.timeaccount.provider.SyncLogAdapter;
import com.chenyc.timeaccount.provider.TimeAccount;
import com.chenyc.timeaccount.provider.TimeItemAdapter;

public class TimeItemList extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int INTROSPECTION_ID = Menu.FIRST + 1;
	private static final int DELETE_ID = Menu.FIRST + 2;
	private static final int DATE_DIALOG_ID = 1;
	private TimeItemAdapter mDbHelper;
	private TextView mSelectDateView;
	private String mSelectDate;

	private int mYear;
	private int mMonth;
	private int mDay;

	// 处理滑动事件
	private GestureDetector mGestureDetector = new GestureDetector(
			new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {

					//横向
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
						if (e1.getY() > e2.getY()) {
							Calendar cal = new GregorianCalendar(mYear, mMonth,
									mDay);
							cal.add(Calendar.DATE, 1);
							mYear = cal.get(Calendar.YEAR);
							mMonth = cal.get(Calendar.MONTH);
							mDay = cal.get(Calendar.DATE);
							updateDisplay();
							fillData();
						}

						if (e1.getY() < e2.getY()) {
							Calendar cal = new GregorianCalendar(mYear, mMonth,
									mDay);
							cal.add(Calendar.DATE, -1);
							mYear = cal.get(Calendar.YEAR);
							mMonth = cal.get(Calendar.MONTH);
							mDay = cal.get(Calendar.DATE);
							updateDisplay();
							fillData();
						}
					}

					//纵向
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
						if (e1.getX() > e2.getX()) {
							Calendar cal = new GregorianCalendar(mYear, mMonth,
									mDay);
							cal.add(Calendar.DATE, 1);
							mYear = cal.get(Calendar.YEAR);
							mMonth = cal.get(Calendar.MONTH);
							mDay = cal.get(Calendar.DATE);
							updateDisplay();
							fillData();
						}

						if (e1.getX() < e2.getX()) {
							Calendar cal = new GregorianCalendar(mYear, mMonth,
									mDay);
							cal.add(Calendar.DATE, -1);
							mYear = cal.get(Calendar.YEAR);
							mMonth = cal.get(Calendar.MONTH);
							mDay = cal.get(Calendar.DATE);
							updateDisplay();
							fillData();
						}
					}
					return true;
				}

			});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeitems_list);
		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.today_account));

		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return view.onTouchEvent(event);
			}
		});

		// 获取日期并设置
		Bundle extras = getIntent().getExtras();
		Calendar c = (Calendar) (extras != null ? extras.getSerializable("cal")
				: null);
		if (c == null) {
			c = Calendar.getInstance();
		}
		mYear = savedInstanceState != null ? savedInstanceState.getInt("year")
				: -1;
		mMonth = savedInstanceState != null ? savedInstanceState
				.getInt("month") : -1;
		mDay = savedInstanceState != null ? savedInstanceState.getInt("day")
				: -1;
		if (mYear == -1 || mMonth == -1 || mDay == -1) {
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
		}
		updateDisplay();

		// 获取列表数据
		mDbHelper = new TimeItemAdapter(this);
		mDbHelper.open();
		fillData();

		// 注册上下文菜单
		registerForContextMenu(getListView());
	}

	private void updateDisplay() {
		mSelectDateView = (TextView) findViewById(R.id.select_date);
		mSelectDate = "" + mYear + "-";
		if (mMonth + 1 < 10) {
			mSelectDate += "0" + (mMonth + 1) + "-";
		} else {
			mSelectDate += (mMonth + 1) + "-";
		}
		if (mDay < 10) {
			mSelectDate += "0" + mDay;
		} else {
			mSelectDate += mDay;
		}
		mSelectDateView.setText(mSelectDate);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		Cursor timeItemsCursor = mDbHelper.fetchAllTimeItemsByDate(mSelectDate);
		startManagingCursor(timeItemsCursor);

		// Create an array to specify the fields we want to display in the list
		// (only TITLE)
		String[] from = new String[] { "content", "name", "count_time","rate"};

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.row_content, R.id.row_type,
				R.id.row_count_time,R.id.ratingbar };

		// Now create a simple cursor adapter and set it to display

		SimpleCursorAdapter timeItems = new SimpleCursorAdapter(this,
				R.layout.timeitem_row, timeItemsCursor, from, to);
		timeItems.setViewBinder(new SimpleCursorAdapter.ViewBinder(){

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				int rateIndex = cursor.getColumnIndex("rate");
				if(columnIndex == rateIndex)
				{
					RatingBar ratingBar = (RatingBar)view;
					ratingBar.setRating(cursor.getInt(rateIndex));
					return true;
				}
				return false;
			}
		
		});
		
		setListAdapter(timeItems);
		
		TextView introTextView = (TextView) findViewById(R.id.introspection);
		introTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		introTextView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return true;
			}
			
		});
		// 设置反思内容
		introTextView.setText(""); // 先还原为空
		if (mSelectDate != null) {
			Cursor cursor = getContentResolver().query(
					TimeAccount.INTROSPECTION_CONTENT_URI,
					new String[] { Introspection.KEY_INTRO }, "date = ?",
					new String[] { mSelectDate }, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				String introspection = cursor.getString(cursor
						.getColumnIndexOrThrow(Introspection.KEY_INTRO));

				introspection = "反思:\n\t\t" + introspection;
				introTextView.setText(introspection);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_book);
		menu.add(0, INTROSPECTION_ID, 0, R.string.menu_introspection);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createTimeItem();
			return true;
		case INTROSPECTION_ID:
			createInprospection();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void createInprospection() {
		Intent i = new Intent(this, IntrospectionEdit.class);
		i.putExtra(TimeItemAdapter.KEY_DATE, mSelectDate);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	private void createTimeItem() {
		Intent i = new Intent(this, TimeItemEdit.class);
		i.putExtra(TimeItemAdapter.KEY_DATE, mSelectDate);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteTimeItem(info.id);
			fillData();
			SyncLogAdapter.log(mDbHelper.getMDb(), "timeitem", "delete",
					info.id);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, TimeItemEdit.class);
		i.putExtra(TimeItemAdapter.KEY_ROWID, id);
		i.putExtra(TimeItemAdapter.KEY_DATE, mSelectDate);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("year", mYear);
		outState.putInt("month", mMonth);
		outState.putInt("day", mDay);
	}

}
