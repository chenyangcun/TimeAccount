package com.chenyc.timeaccount;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.provider.EventTypeAdapter;
import com.chenyc.timeaccount.provider.SyncLogAdapter;
import com.chenyc.timeaccount.provider.TimeAccount;

public class EventTypeList extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private EventTypeAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventtypes_list);
		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.type_manage));
		mDbHelper = new EventTypeAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		Cursor eventTypesCursor = mDbHelper.fetchAllEventTypes();
		startManagingCursor(eventTypesCursor);

		// Create an array to specify the fields we want to display in the list
		// (only TITLE)
		String[] from = new String[] { EventTypeAdapter.KEY_NAME };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { android.R.id.text1 };

		// Now create a simple cursor adapter and set it to display

		SimpleCursorAdapter eventTypes = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, eventTypesCursor, from, to);
		setListAdapter(eventTypes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createEventType();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void createEventType() {
		Intent i = new Intent(this, EventTypeEdit.class);
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

			if (useByTimeItem(info.id)) {
				new AlertDialog.Builder(this).setTitle("提示").setMessage(
						"事件类型已经被引用，不能删除！").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								return;
							}
						}).show();
				return true;
			}

			mDbHelper.deleteEventType(info.id);
			SyncLogAdapter.log(mDbHelper.getMDb(), "eventtype", "delete",
					info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private boolean useByTimeItem(long id) {

		Cursor mCursor = getContentResolver().query(
				TimeAccount.TIME_ITEM_CONTENT_URI,
				new String[] { "event_type" }, "event_type = ?",
				new String[] { id + "" }, null);
		if (mCursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, EventTypeEdit.class);
		i.putExtra(EventTypeAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		fillData();

	}

}
