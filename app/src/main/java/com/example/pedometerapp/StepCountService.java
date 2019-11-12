package com.example.pedometerapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class StepCountService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepCountSensor;
    Sensor stepDetectorSensor;

    int currentStepsDetected;

    int stepNum;
    int newStepCounter;

    boolean serviceStopped;

    NotificationManager notificationManager;

    Intent intent;
    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = "com.websmithing.yusuf.mybroadcast";

    private final Handler handler = new Handler();


    @Override
    public void onCreate() {
        super.onCreate();


        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service", "Start");

        showNotification();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCountSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);

        currentStepsDetected = 0;
        stepNum = 0;
        newStepCounter = 0;

        serviceStopped = false;

        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStopped = true;

        dismissNotification();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int)event.values[0];
            Log.d("WTF", "stepNum : "+ stepNum);

            if(stepNum == 0) {
                stepNum = (int)event.values[0];
            }
            newStepCounter = countSteps - stepNum;
        }

        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int)event.values[0];
            currentStepsDetected += detectSteps;
        }
        Log.v("Service Counter", String.valueOf(newStepCounter));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void showNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default");
        notificationBuilder.setContentTitle("Pedometer");
        notificationBuilder.setContentText("Pedometer session is running in the background.");
        notificationBuilder.setSmallIcon(R.mipmap.sneaker);
        notificationBuilder.setColor(Color.parseColor("#6600cc"));
        int colorLED = Color.argb(255, 0, 255, 0);
        notificationBuilder.setLights(colorLED, 500, 500);
        // To  make sure that the Notification LED is triggered.
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setOngoing(true);

        //Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,new Intent(),0);
        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);


        //notificationManager.createNotificationChannel(new NotificationChannel("default", "defaultChannel", NotificationManager.IMPORTANCE_DEFAULT));


        Log.d(TAG, "notiM : " + notificationManager);
        Log.d(TAG, "notiB : " + notificationBuilder);
        notificationManager.notify(1, notificationBuilder.build());


    }

    private void dismissNotification() {
        notificationManager.cancel(0);
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if(!serviceStopped) {
                broadcastSensorValue();
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void broadcastSensorValue() {
        Log.d(TAG, "Data to Activity");
        // add step counter to intent.
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        // add step detector to intent.
        intent.putExtra("Detected_Step_Int", currentStepsDetected);
        intent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }


}
