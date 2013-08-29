package com.chenyc.timeaccount.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * 时间日记项数据提供者
 * 
 * @author chenyc
 * 
 */
public class TimeItemAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_EVENT_TYPE = "event_type";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_HOUR = "hour";
	public static final String KEY_MINUTE = "minute";
	public static final String KEY_DATE = "date";
	public static final String KEY_RATE = "rate";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_TABLE = "time_items";

	private final Context mCtx;

	public TimeItemAdapter(Context mCtx) {
		this.mCtx = mCtx;
	}
	
	

	public SQLiteDatabase getMDb() {
		return mDb;
	}



	public TimeItemAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createTimeItem(long eventType, String content, int hour,
			int minute, String date,int rate) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EVENT_TYPE, eventType);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_HOUR, hour);
		initialValues.put(KEY_MINUTE, minute);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_RATE, rate);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteTimeItem(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllTimeItems() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_EVENT_TYPE, KEY_CONTENT, KEY_HOUR, KEY_MINUTE, KEY_DATE },
				null, null, null, null, null);
	}

	public Cursor fetchTimeItem(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_EVENT_TYPE, KEY_CONTENT, KEY_HOUR, KEY_MINUTE, KEY_DATE,KEY_RATE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateEventType(long rowId, long eventType, String content,
			int hour, int minute, String date,int rate) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EVENT_TYPE, eventType);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_HOUR, hour);
		initialValues.put(KEY_MINUTE, minute);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_RATE, rate);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	public Cursor fetchAllTimeItemsByDate(String selectDate) {
		// String tables = "time_items,event_types";
		// return mDb.query(tables, new String[] { "time_items._id",
		// KEY_EVENT_TYPE, KEY_CONTENT, KEY_HOUR, KEY_MINUTE,
		// KEY_DATE,"event_types.name","hour||':'||minute as count_time"},
		// "event_types._id = time_items.event_type and "+KEY_DATE + "='" +
		// selectDate+"'", null, null, null, null);

		String sql = "select time_items._id,time_items.content,event_types.name,time_items.hour||'小时'||time_items.minute||'分' as count_time,time_items.rate from time_items,event_types "
				+ "where time_items.event_type = event_types._id and time_items.date = ?";
		return mDb.rawQuery(sql, new String[] { selectDate });
	}

}
