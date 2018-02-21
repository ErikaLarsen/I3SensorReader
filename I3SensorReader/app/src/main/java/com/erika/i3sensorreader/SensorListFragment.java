package com.erika.i3sensorreader;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by erikalarsen on 1/12/18.
 */

public class SensorListFragment extends ListFragment {
    private static final String TAG = "SensorListFragment";
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_list, container, false);
        arrayList = new ArrayList<String>();
        arrayList.add("Sensor 1");
        arrayList.add("Sensor 2");
        arrayList.add("Sensor 3");
        arrayList.add("Sensor 4");

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.sensor_list_item, R.id.sensor_id, arrayList);
        listView = view.findViewById(R.id.sensor_list);
        listView.setAdapter(arrayAdapter);




        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: "+view);
                ((MainActivity)getActivity()).startNextActivity(adapterView.getPositionForView(view));
            }
        });
    }
}
