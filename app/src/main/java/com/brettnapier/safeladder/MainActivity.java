package com.brettnapier.safeladder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvHeading;
    TextView tvRoll;
    TextView tvPitch;
    TextView tvSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvHeading = (TextView) findViewById( R.id.tvHeading );
        tvPitch = (TextView) findViewById( R.id. tv_pitch );
        //tvRoll = (TextView) findViewById( R.id.tvRoll );
        tvSafe = (TextView) findViewById( R.id.tv_safe );

        //have the system call us on mag/accel changes
        SensorManager sensorManager = (SensorManager) getSystemService( SENSOR_SERVICE );

        //for accelerometer
        sensorManager.registerListener( mySensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER ),
                SensorManager.SENSOR_DELAY_UI );

        //for magnometer
        sensorManager.registerListener( mySensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD ),
                SensorManager.SENSOR_DELAY_UI );
    }

    //remember sensor readings
    float[] accelReadings = new float[3];
    float[] magReadings = new float[3];

    SensorEventListener mySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //which sensor caused the event?
            if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ){
                //accelerometer
                accelReadings = event.values;
            }
            else{
                //magnometer
                magReadings = event.values;
            }

            //take the readings and convert them into the orientation
            float[] rotationMatrix =  new float[9];
            float[] inclinationMatrix = new float[9];

            //take sensor data and convert to rotation matrix and incination matrix
            SensorManager.getRotationMatrix( rotationMatrix, inclinationMatrix, accelReadings, magReadings );
            float[] orientation = new float[3]; //final orientation values
            SensorManager.getOrientation(rotationMatrix, orientation); //actually gives us heading and pitch/roll

            double pitchAngle = Math.round( Math.toDegrees( orientation[1] ) );

            if( ( pitchAngle>=70 && pitchAngle<=75 ) || ( pitchAngle <= -70 && pitchAngle >= -75 ) ){
                tvSafe.setText( "Safe angle" );
            }
            else{
                tvSafe.setText( "Not a safe angle!" );
            }

            //display results, orientation[0],orientation[1], orientation[2] are all in radians by default
           //tvHeading.setText( "Heading: " + Math.toDegrees( orientation[0] ) );
            tvPitch.setText( "Pitch: " + Math.toDegrees( orientation[1] ) );
            //tvRoll.setText( "Roll: " + Math.toDegrees( orientation[2] ) );
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
