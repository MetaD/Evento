package com.summer.evento;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    Button createEventBtn;
    Button browseEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Parse.initialize(this, "qs8WgPqX4Gowi1Bo1n1qaTxea0ThJ5ZLYEMei0wm", "poSff8kszhh252fsCGasestrtFqWE4aO8acm36Wm");

        createEventBtn = (Button) findViewById(R.id.btn_create_event);
        browseEventBtn = (Button) findViewById(R.id.btn_whats_happen);

        registerForContextMenu(createEventBtn);

        browseEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseEvent = new Intent(MainActivity.this, BrowseEvents.class);
                startActivity(browseEvent);
            }
        });
        
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	  openContextMenu(view);
            }
        });
    }


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.create_event_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}


    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.parse:
            Intent createEvent_fromImg = new Intent(MainActivity.this, CreateEvent_fromImg.class);
            startActivity(createEvent_fromImg);
    		break;
    	case R.id.type:
            Intent createEvent = new Intent(MainActivity.this, CreateEvent.class);
            startActivity(createEvent);
            break;
    	}
		return super.onContextItemSelected(item);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
