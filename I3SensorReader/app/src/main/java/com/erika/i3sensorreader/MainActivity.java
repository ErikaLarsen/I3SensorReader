package com.erika.i3sensorreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    static final String NO_DATA = "No stored data.";
    private String mStoredData;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences mPrefs = getSharedPreferences("stored_data",0);
        mStoredData = mPrefs.getString("sensor_data_1", NO_DATA);
        Log.d(TAG, "onCreate: stored data -- "+mStoredData);

        Log.d(TAG, "onCreate: starting");
        //setup viewpager with sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),new SensorListFragment(), new SensorMapFragment());
        mViewPager.setAdapter(mSectionPageAdapter);
        TabLayout tablayout = (TabLayout)findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startNextActivity(int i){
        String message;

        switch (i){
            case 0: message = "Sensor 1"; break;
            case 1: message = "Sensor 2"; break;
            case 2: message = "Sensor 3"; break;
            case 3: message = "Sensor 4"; break;
            default: message = "Sensor Data"; break;
        }

        Intent intent = new Intent(this, DataActivity.class);
        intent.putExtra("sensor_name", message);
        startActivity(intent);
    }

    public void startNextActivity(String sensorName){
        Intent intent = new Intent(this, DataActivity.class);
        intent.putExtra("sensor_name", sensorName);
        startActivity(intent);
    }


}
