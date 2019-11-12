package com.example.pedometerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    @BindView(R.id.textView_stepNum) TextView textView_stepNum;
    @BindView(R.id.textView_Kcal) TextView textView_Kcal;
    @BindView(R.id.textView_distance) TextView textView_distance;
    @BindView(R.id.LL_communityGo) LinearLayout LL_communitiGo;
    @BindView(R.id.btn_start) Button btn_start;

    String countStep;
    String DetectedStep;
    static final String State_Count = "Counter";
    static final String State_Detect = "Detector";

    boolean isServiceStopped;

    private static final String TAG = "SersorEvent";

    private SensorManager sensorManager;
    private Sensor stepCountSensor;

    IStepCountService binder;

    int stepNum = -1;
    double Kcal = 0;
    double distance = 0;
    long backKeyPressedTime;
/*
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IStepCountService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
*/
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
/*
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null) {
            Toast.makeText(this, "No step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sf = getSharedPreferences("stepData", MODE_PRIVATE);
        stepNum = Integer.parseInt(sf.getString("stepData", "-1"));
        Log.d("WTF", "savedStep : " + Integer.toString(stepNum));
*/

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getBaseContext(), StepCountService.class));
                registerReceiver(broadcastReceiver, new IntentFilter(StepCountService.BROADCAST_ACTION));
                isServiceStopped = false;

            }
        });

        LL_communitiGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
           // round는 소수점 첫째자리 반올림해서 정수로 리턴한다.
           stepNum = (int)event.values[0];
           printUI(stepNum);
       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void printUI(int stepNum) {
        // Math.round( ( ~~ ) * 10 / 100.0)  -> 소수점 2자리.
        Kcal = Math.round((stepNum / 30 *100)) / 100.0;
        distance = Math.round((stepNum / 1538.4 * 100)) / 100.0;

        textView_stepNum.setText(Integer.toString(stepNum));
        textView_Kcal.setText(Double.toString(Kcal));
        textView_distance.setText(Double.toString(distance));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, getString(R.string.RESET_MESSAGE), Toast.LENGTH_SHORT).show();


        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) {
        countStep = intent.getStringExtra("Counted_Step");
        DetectedStep = intent.getStringExtra("Detected_Step");
        Log.d(TAG, String.valueOf(countStep));
        Log.d(TAG, String.valueOf(DetectedStep));

        stepNum = Integer.parseInt(countStep);
        Kcal = Math.round((stepNum / 30 *100)) / 100.0;
        distance = Math.round((stepNum / 1538.4 * 100)) / 100.0;

        textView_stepNum.setText(Integer.toString(stepNum));
        textView_Kcal.setText(Double.toString(Kcal));
        textView_distance.setText(Double.toString(distance));
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, getString(R.string.APP_CLOSE_BACK_BUTTON),Toast.LENGTH_SHORT).show();
        } else {
            AppFinish();
        }
    }

    public void AppFinish() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
