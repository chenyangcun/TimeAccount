package com.chenyc.timeaccount.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class TimeAccountProvider extends ContentProvider {

	private static final UriMatcher sUriMatcher;

	private DatabaseHelper mDbHelper;

	private static final String EVENT_TYPE_TABLE_NAME = "event_types";
	private static final String TIME_ITEM_TABLE_NAME = "time_items";
	private static final String SYNC_LOG_TABLE_NAME = "sync_logs";
	private static final String INTROSPECTION_TABLE_NAME = "introspections";

	private static HashMap<String, String> sEventTypesProjectionMap;
	private static HashMap<String, String> sTimeItemsProjectionMap;
	private static HashMap<String, String> sSyncLogsProjectionMap;
	private static HashMap<String, String> sIntrosProjectionMap;

	private static final int EVENT_TYPES = 1;
	private static final int EVENT_TYPE_ID = 2;
	private static final int TIME_ITEMS = 3;
	private static final int TIME_ITEM_ID = 4;
	private static final int SYNC_LOGS = 5;
	private static final int SYNC_LOG_ID = 6;
	private static final int INTROSPECTIONS = 7;
	private static final int INTROSPECTION_ID = 8;

	private static final String EVENT_TYPE_CONTENT_TYPE = "vnd.chenyc.cursor.dir/vnd.account.eventtype";
	private static final String TIME_ITEM_CONTENT_TYPE = "vnd.chenyc.cursor.dir/vnd.account.timeitem";
	private static final String SYNC_LOG_CONTENT_TYPE = "vnd.chenyc.cursor.dir/vnd.account.synclog";
	private static final String INTROSPECTION_CONTENT_TYPE = "vnd.chenyc.cursor.dir/vnd.account.introspection";

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "eventtypes", EVENT_TYPES);
		sUriMatcher
				.addURI(TimeAccount.AUTHORITY, "eventtypes/#", EVENT_TYPE_ID);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "timeitems", TIME_ITEMS);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "timeitems/#", TIME_ITEM_ID);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "synclogs", SYNC_LOGS);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "synclogs/#", SYNC_LOG_ID);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "introspections", INTROSPECTIONS);
		sUriMatcher.addURI(TimeAccount.AUTHORITY, "introspections/#", INTROSPECTION_ID);

		sEventTypesProjectionMap = new HashMap<String, String>();
		sEventTypesProjectionMap.put(EventTypeAdapter.KEY_ROWID,
				EventTypeAdapter.KEY_ROWID);
		sEventTypesProjectionMap.put(EventTypeAdapter.KEY_NAME,
				EventTypeAdapter.KEY_NAME);

		sTimeItemsProjectionMap = new HashMap<String, String>();
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_ROWID,
				TimeItemAdapter.KEY_ROWID);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_CONTENT,
				TimeItemAdapter.KEY_CONTENT);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_DATE,
				TimeItemAdapter.KEY_DATE);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_EVENT_TYPE,
				TimeItemAdapter.KEY_EVENT_TYPE);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_HOUR,
				TimeItemAdapter.KEY_HOUR);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_MINUTE,
				TimeItemAdapter.KEY_MINUTE);
		sTimeItemsProjectionMap.put(TimeItemAdapter.KEY_RATE,
				TimeItemAdapter.KEY_RATE);

		sSyncLogsProjectionMap = new HashMap<String, String>();
		sSyncLogsProjectionMap.put(SyncLogAdapter.KEY_ROWID,
				SyncLogAdapter.KEY_ROWID);
		sSyncLogsProjectionMap.put(SyncLogAdapter.KEY_METHOD,
				SyncLogAdapter.KEY_METHOD);
		sSyncLogsProjectionMap.put(SyncLogAdapter.KEY_PID,
				SyncLogAdapter.KEY_PID);
		sSyncLogsProjectionMap.put(SyncLogAdapter.KEY_STATE,
				SyncLogAdapter.KEY_STATE);
		sSyncLogsProjectionMap.put(SyncLogAdapter.KEY_TYPE,
				SyncLogAdapter.KEY_TYPE);

		sIntrosProjectionMap = new HashMap<String, String>();
		sIntrosProjectionMap.put(Introspection.KEY_ROWID, Introspection.KEY_ROWID);
		sIntrosProjectionMap.put(Introspection.KEY_DATE, Introspection.KEY_DATE);
		sIntrosProjectionMap.put(Introspection.KEY_INTRO, Introspection.KEY_INTRO);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		case EVENT_TYPES:
			qb.setTables(EVENT_TYPE_TABLE_NAME);
			qb.setProjectionMap(sEventTypesProjectionMap);
			break;
		case EVENT_TYPE_ID:
			qb.setTables(EVENT_TYPE_TABLE_NAME);
			qb.setProjectionMap(sEventTypesProjectionMap);
			qb.appendWhere(EventTypeAdapter.KEY_ROWID + "="
					+ uri.getPathSegments().get(1));
			break;
		case TIME_ITEMS:
			qb.setTables(TIME_ITEM_TABLE_NAME);
			qb.setProjectionMap(sTimeItemsProjectionMap);
			break;
		case TIME_ITEM_ID:
			qb.setTables(TIME_ITEM_TABLE_NAME);
			qb.setProjectionMap(sTimeItemsProjectionMap);
			qb.appendWhere(TimeItemAdapter.KEY_ROWID + "="
					+ uri.getPathSegments().get(1));
			break;
		case SYNC_LOGS:
			qb.setTables(SYNC_LOG_TABLE_NAME);
			qb.setProjectionMap(sSyncLogsProjectionMap);
			break;
		case SYNC_LOG_ID:
			qb.setTables(SYNC_LOG_TABLE_NAME);
			qb.setProjectionMap(sSyncLogsProjectionMap);
			qb.appendWhere(SyncLogAdapter.KEY_ROWID + "="
					+ uri.getPathSegments().get(1));
			break;
		case INTROSPECTIONS:
			qb.setTables(INTROSPECTION_TABLE_NAME);
			qb.setProjectionMap(sIntrosProjectionMap);
			break;
		case INTROSPECTION_ID:
			qb.setTables(INTROSPECTION_TABLE_NAME);
			qb.setProjectionMap(sIntrosProjectionMap);
			qb.appendWhere(Introspection.KEY_ROWID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case EVENT_TYPES:
		case EVENT_TYPE_ID:
			return EVENT_TYPE_CONTENT_TYPE;

		case TIME_ITEMS:
		case TIME_ITEM_ID:
			return TIME_ITEM_CONTENT_TYPE;

		case SYNC_LOGS:
		case SYNC_LOG_ID:
			return SYNC_LOG_CONTENT_TYPE;
			
		case INTROSPECTIONS:
		case INTROSPECTION_ID:
			return INTROSPECTION_CONTENT_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case EVENT_TYPES:
			count = db.delete(EVENT_TYPE_TABLE_NAME, where, whereArgs);
			break;
		case EVENT_TYPE_ID:
			String id = uri.getPathSegments().get(1);
			count = db.delete(EVENT_TYPE_TABLE_NAME,
					EventTypeAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TIME_ITEMS:
			count = db.delete(TIME_ITEM_TABLE_NAME, where, whereArgs);
			break;

		case TIME_ITEM_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(TIME_ITEM_TABLE_NAME,
					TimeItemAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
		case SYNC_LOGS:
			count = db.delete(SYNC_LOG_TABLE_NAME, where, whereArgs);
			break;
		case SYNC_LOG_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(SYNC_LOG_TABLE_NAME,
					SyncLogAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
			
		case INTROSPECTIONS:
			count = db.delete(INTROSPECTION_TABLE_NAME, where, whereArgs);
			break;
		case INTROSPECTION_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(INTROSPECTION_TABLE_NAME,
					Introspection.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long rowId = 0;
		Uri contentUri;
		switch (sUriMatcher.match(uri)) {
		case EVENT_TYPES:
			rowId = db.insert(EVENT_TYPE_TABLE_NAME, "null", initialValues);
			contentUri = TimeAccount.EVENT_TYPE_CONTENT_URI;

			break;
		case TIME_ITEMS:
			rowId = db.insert(TIME_ITEM_TABLE_NAME, "null", initialValues);
			contentUri = TimeAccount.TIME_ITEM_CONTENT_URI;
			break;
		case SYNC_LOGS:
			rowId = db.insert(SYNC_LOG_TABLE_NAME, "null", initialValues);
			contentUri = TimeAccount.SYNC_LOG_CONTENT_URI;
			break;
		case INTROSPECTIONS:
			rowId = db.insert(INTROSPECTION_TABLE_NAME, "null", initialValues);
			contentUri = TimeAccount.INTROSPECTION_CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (rowId > 0) {
			Uri returnUri = ContentUris.withAppendedId(contentUri, rowId);
			getContext().getContentResolver().notifyChange(returnUri, null);
			return returnUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String id;
		int count;
		switch (sUriMatcher.match(uri)) {
		case EVENT_TYPES:
			count = db.update(EVENT_TYPE_TABLE_NAME, values, where, whereArgs);
			break;
		case EVENT_TYPE_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(EVENT_TYPE_TABLE_NAME, values,
					EventTypeAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case TIME_ITEMS:
			count = db.update(TIME_ITEM_TABLE_NAME, values, where, whereArgs);
			break;
		case TIME_ITEM_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(TIME_ITEM_TABLE_NAME, values,
					TimeItemAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case SYNC_LOGS:
			count = db.update(SYNC_LOG_TABLE_NAME, values, where, whereArgs);
			break;
		case SYNC_LOG_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(SYNC_LOG_TABLE_NAME, values,
					SyncLogAdapter.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
			
		case INTROSPECTIONS:
			count = db.update(INTROSPECTION_TABLE_NAME, values, where, whereArgs);
			break;
		case INTROSPECTION_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(INTROSPECTION_TABLE_NAME, values,
					Introspection.KEY_ROWID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
