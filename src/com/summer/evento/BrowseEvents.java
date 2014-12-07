package com.summer.evento;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
//import android.webkit.WebSettings;
//import android.webkit.WebView;

public class BrowseEvents extends FragmentActivity {
	//ViewPager viewPager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_events);
		
		CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                 Intent dayView = new Intent(BrowseEvents.this, DayView.class);
                 dayView.putExtra("year", year);
                 dayView.putExtra("month", month);
                 dayView.putExtra("day", dayOfMonth);
                 startActivity(dayView);
            }
        });
		/*
		viewPager = (ViewPager) findViewById(R.id.month_pager);
		FragmentManager fragmentManager = getSupportFragmentManager();
		viewPager.setAdapter(new MyAdapter(fragmentManager));
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.browse_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
/*
class MyAdapter extends FragmentPagerAdapter {
	
	public MyAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem (int i) {
		Fragment fragment = null;
		if (i == 0) {
			fragment = new Last_month();
		}
		if (i == 1) {
			fragment = new Curr_month();
		}
		if (i == 2) {
			fragment = new Next_month();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle (int position) {
		if (position == 0)
			return "November";
		if (position == 1)
			return "December";
		if (position == 2)
			return "January";
		return null;
	}
}
*/