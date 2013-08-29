package com.chenyc.timeaccount;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chenyc.timeaccount.activity.R;
import com.chenyc.timeaccount.view.CalendarView;

public class CalendarBrowse extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CalendarView calView = new CalendarView(this);
		setContentView(calView);
		calView.requestFocus();
		setTitle(getString(R.string.app_name) + "-"
				+ getString(R.string.view_account));

	}

	public void showTimeItemList(int year, int month, int day) {
		Intent k = new Intent(this, TimeItemList.class);
		k.putExtra("cal", new GregorianCalendar(year,month,day));
		startActivity(k);
	}

}
