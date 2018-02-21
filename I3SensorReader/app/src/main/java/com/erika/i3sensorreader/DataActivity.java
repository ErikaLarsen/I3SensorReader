package com.erika.i3sensorreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class DataActivity extends AppCompatActivity {
    private static final String TAG = "DataActivity";

    private MqttAndroidClient client;
    private String clientId;
    private String imscBroker = "tcp://dsicloud3.usc.edu:1880";
    private MqttConnectOptions options;
    private String topic = "USC/UPC/sensor1";
    private String mBroker;
    private String username = "erikalar";
    private String password = "clntcp";

    String mSensorName;
    private TextView nameView;
    private TextView soundView;
    private TextView temperatureView;
    private TextView vibrationView;
    private TextView lightView;
    private TextView humidityView;
    private TextView gasMq5View;
    private TextView gasMq3View;
    private TextView gasMq2View;
    private TextView gasMq9View;
    private TextView tempIntView;
    private TextView pirView;
    private TextView idView;
    private ViewGroup sensorLayout;

    private SharedPreferences mPrefs;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: "+"starting");

        mPrefs = getSharedPreferences("stored_data",0);

        initializeViews();
        setBroker();

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), mBroker, clientId);
        options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);

        Log.d("DataActivity","client created: "+client.toString());


        try {
            IMqttToken token = client.connect(options);
            Log.d("DataActivity","client connecting");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess connected!");

                    client.setCallback(new MqttCallback() {

                        @Override
                        public void connectionLost(Throwable cause) {
                            Log.d("DataActivity", "connection lost");
                            nameView.setText("connection lost");
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message){
                            String messageString = new String(message.getPayload());
                            Log.d(TAG, "Incoming topic: " + topic);
                            Log.d(TAG, "Incoming message: " + messageString);
                            parseMessage(messageString);

                            SharedPreferences.Editor ed = mPrefs.edit();
                            ed.putString("sensor_data_1", messageString);
                            ed.commit();

                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });




                    int qos = 0;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        Log.d("DataActivity", "subscribing to " +topic);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                                Log.d(TAG, "onSuccess subscribed!");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Log.d(TAG, "onFailure failed to subscribe");

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure failed to connect");

                }
            });
            Log.d("DataActivity", "setConnectCallback");


        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void initializeViews(){

        nameView = (TextView) findViewById(R.id.name);
        soundView = (TextView) findViewById(R.id.sound_data);
        temperatureView = (TextView) findViewById(R.id.temperature_data);
        vibrationView = (TextView) findViewById(R.id.vibration_data);
        lightView = (TextView) findViewById(R.id.light_data);
        humidityView = (TextView) findViewById(R.id.humidity_data);
        gasMq5View = (TextView) findViewById(R.id.gas_mq5_data);
        gasMq3View = (TextView) findViewById(R.id.gas_mq3_data);
        gasMq2View = (TextView) findViewById(R.id.gas_mq2_data);
        gasMq9View = (TextView) findViewById(R.id.gas_mq9_data);
        tempIntView = (TextView) findViewById(R.id.temperature_interior_data);
        pirView = (TextView) findViewById(R.id.pir_data);
        idView = (TextView) findViewById(R.id.id_data);
        sensorLayout = (ViewGroup) findViewById(R.id.data_layout);
        sensorLayout.setVisibility(View.GONE);

        mSensorName = getIntent().getStringExtra("sensor_name");
        nameView.setText(mSensorName + " -- waiting for data.");
        if(mSensorName.equals("Sensor 1")){
            topic = "USC/UPC/sensor1";
        }
        else if(mSensorName.equals("Sensor 2")){
            topic = "USC/UPC/sensor2";
        }
        else if(mSensorName.equals("Sensor 3")){
            topic = "USC/UPC/sensor3";
        }
        else if(mSensorName.equals("Sensor 4")){
            topic = "USC/UPC/sensor4";
        }

    }

    private void setBroker(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String broker = preferences.getString("broker", "dsicloud3.usc.edu");
        String port = preferences.getString("port","xxx");
        String username = preferences.getString("username", "xxx");
        Log.d(TAG, "getSettings: "+port+", "+username);
        if(broker!=null && port!=null){
            mBroker = "tcp://"+broker+":"+port;
            Log.d(TAG, "setBroker: "+mBroker);
        }
        else mBroker = imscBroker;

        mBroker = imscBroker;

    }



    private void parseMessage(String message) {
        JSONObject obj;

        try {
            obj = new JSONObject(message);
        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.d(TAG, "Failed to create JSON object");
            return;
        }

        if (obj != null) {
            setDoubleView(obj,lightView,"light");
            setIntView(obj,soundView,"Sound");
            setDoubleView(obj,temperatureView,"temperature");
            setDoubleView(obj,vibrationView,"vibration");
            setIntView(obj,humidityView,"humidity");
            setIntView(obj,gasMq5View,"gas_mq5");
            setIntView(obj,gasMq3View,"gas_mq3");
            setIntView(obj,gasMq2View,"gas_mq2");
            setIntView(obj,gasMq9View,"gas_mq9");
            setDoubleView(obj,tempIntView,"temperature_interior");
            setIntView(obj,pirView,"pir");
            setIntView(obj,idView,"ID");
        }
        nameView.setText(mSensorName);
        findViewById(R.id.data_layout).setVisibility(ViewGroup.VISIBLE);
    }



    private void setDoubleView(JSONObject obj, TextView textView, String name) {
        try {
            Double data = obj.getDouble(name);
            textView.setText(data.toString());
        } catch (JSONException ex) {
            Log.d(TAG, name+" not found.");
            textView.setText("---");
        }
    }

    private void setIntView(JSONObject obj, TextView textView, String name) {
        try {
            Integer data = obj.getInt(name);
            textView.setText(data.toString());
        } catch (JSONException ex) {
            Log.d(TAG, name+" not found.");
            textView.setText("---");
        }
    }


    protected void onDestroy(){
        Log.d("DataActivity", "onDestroy "+client.toString());
        super.onDestroy();
        try {
            client.disconnect();
            if(client!=null){
                Log.d(TAG, "destroy client.");
                client = null;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
