package com.summer.evento;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDetails extends Activity {
	
	Button addToMyCalendar;
	ParseObject event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		
		Intent prevIntent = getIntent();
		String eventID = prevIntent.getStringExtra("eventID");
		TextView title = (TextView)findViewById(R.id.detail_title);
		TextView date = (TextView)findViewById(R.id.detail_date);
		TextView time = (TextView)findViewById(R.id.detail_time);
		TextView location = (TextView)findViewById(R.id.detail_location);
		TextView description = (TextView)findViewById(R.id.detail_description);
		
		// Retrieve info from Parse database
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("objectId", eventID);
		try {
			List<ParseObject> eventList = query.find();
			event = eventList.get(0);
			title.setText(event.getString("Title"));
			String dateString = DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(event.getDate("startTime"));
			String startString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(event.getDate("startTime"));
			String endString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US).format(event.getDate("endTime"));
			date.setText(dateString);
			time.setText(startString + " - " + endString);
			location.setText(event.getString("Location"));
			description.setText(event.getString("Description"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addToMyCalendar = (Button)findViewById(R.id.sync);
		
		addToMyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTimeZone(TimeZone.getTimeZone("EST"));
                beginTime.setTime(event.getDate("startTime"));
                Calendar endTime = Calendar.getInstance();
                endTime.setTimeZone(TimeZone.getTimeZone("EST"));
                endTime.setTime(event.getDate("endTime"));
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(Events.TITLE, event.getString("Title"))
                        .putExtra(Events.DESCRIPTION, event.getString("Description"))
                        .putExtra(Events.EVENT_LOCATION, event.getString("Location"))
                        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

                startActivity(intent);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
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
