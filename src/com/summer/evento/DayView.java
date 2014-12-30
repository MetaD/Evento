package com.summer.evento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DayView extends Activity {

	List<ListViewItem> items = new ArrayList<DayView.ListViewItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);

		ListView listView = (ListView)findViewById(R.id.eventList);

		Intent prevIntent = getIntent();
		int month = prevIntent.getIntExtra("month", 11);
		int year = prevIntent.getIntExtra("year", 2014);
		int day = prevIntent.getIntExtra("day", 8);

		Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeZone(TimeZone.getTimeZone("EST"));
        beginTime.set(year, month, day, 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeZone(TimeZone.getTimeZone("EST"));
        endTime.set(year, month, day, 23, 59);

        // Retrieve from Parse database
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereGreaterThan("startTime", beginTime.getTime());
		query.whereLessThanOrEqualTo("startTime", endTime.getTime());
		final List<ParseObject> eventList;
		try {
			eventList = query.find();
            Log.d("event", "Retrieved " + eventList.size() + " events");
            if (eventList.size() == 0) {
        		TextView no_event = (TextView)findViewById(R.id.noEventTxt);
        		no_event.setText("Currently No Events Scheduled");
            }
            else {
            	int i = 0;
    			for (final ParseObject element : eventList) {   				
    	            items.add(new ListViewItem() {{
    	    			title = element.getString("Title");
    	    			location = element.getString("Location");
    	    			startTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(element.getDate("startTime"));
    	    		}});
    	         }
    			
    			
    			
    			CustomListViewAdapter adapter = new CustomListViewAdapter(this, items);
    			listView.setAdapter(adapter);
    			
    			

    			listView.setOnItemClickListener(new OnItemClickListener() {
    				   @Override
    				   public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
    					   Intent intent = new Intent(DayView.this, EventDetails.class);
    					   ParseObject item = eventList.get(pos);
    					   intent.putExtra("eventID", item.getObjectId());
    					   startActivity(intent);
    				   }
    				});
            }
		} catch (ParseException e) {
            Log.d("score", "Error: " + e.getMessage());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.day_view, menu);
		return true;
	}

	class ListViewItem {
		public String title;
		public String location;
		public String startTime;
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
