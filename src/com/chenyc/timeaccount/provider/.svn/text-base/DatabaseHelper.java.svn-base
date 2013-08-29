package com.chenyc.timeaccount.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "timeaccount";
	public static final int DATABASE_VERSION = 7;

	private static final String EVENT_TYPE_TABLE_CREATE = "create table event_types (_id integer primary key autoincrement, "
			+ "name text not null);";

	private static final String TIME_ITEM_TABLE_CREATE = "create table time_items (_id integer primary key autoincrement, "
			+ "event_type integer not null,content text not null,hour integer not null,minute integer not null,"
			+ "date text not null,rate integer);";

	private static final String SYNC_LOG_TABLE_CREATE = "create table sync_logs (_id integer primary key autoincrement,"
			+ "type text not null,method text not null,pid integer not null,state integer not null);";

	private static final String INTROSPECTION_TABLE_CREATE = "create table introspections (_id integer primary key autoincrement," +
			"date text not null,introspection text not null);";

	private static final String INDEX_1 = "create index t_index_1 on time_items(event_type);";
	private static final String INDEX_2 = "create index t_index_2 on time_items(date);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(EVENT_TYPE_TABLE_CREATE);
		db.execSQL(TIME_ITEM_TABLE_CREATE);
		db.execSQL(SYNC_LOG_TABLE_CREATE);
		db.execSQL(INTROSPECTION_TABLE_CREATE);
		db.execSQL(INDEX_1);
		db.execSQL(INDEX_2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		 Log.w("TimeAccount", "Upgrading database from version " + oldVersion
//		 + " to " + newVersion + ", which will destroy all old data");
//		 db.execSQL("DROP TABLE IF EXISTS event_types");
//		 db.execSQL("DROP TABLE IF EXISTS time_items");
//		 db.execSQL("DROP TABLE IF EXISTS sync_logs");
//		 db.execSQL("DROP TABLE IF EXISTS instrospections");
//		 onCreate(db);
	}

}