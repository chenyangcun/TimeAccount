package com.chenyc.timeaccount;

import com.chenyc.timeaccount.activity.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Main extends Activity implements android.view.View.OnClickListener {
	private static final int CONFIG_ID = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		View aboutButton = this.findViewById(R.id.about);
		aboutButton.setOnClickListener(this);

		View typeButton = this.findViewById(R.id.type_manage);
		typeButton.setOnClickListener(this);

		View todayAccount = this.findViewById(R.id.today_account);
		todayAccount.setOnClickListener(this);

		View viewAccount = this.findViewById(R.id.view_account);
		viewAccount.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.type_manage:
			Intent j = new Intent(this, EventTypeList.class);
			startActivity(j);
			break;
		case R.id.today_account:
			Intent k = new Intent(this, TimeItemList.class);
			startActivity(k);
			break;
		case R.id.view_account:
			Intent m = new Intent(this, CalendarBrowse.class);
			startActivity(m);
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, CONFIG_ID, 0, R.string.menu_config);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case CONFIG_ID:
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

}