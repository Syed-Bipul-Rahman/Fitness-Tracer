package com.roomdb.fitnesstracer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //global variable declaration
    private TextView stepCountTextView;
    private OnDataPointListener stepCountListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//initializatins
        stepCountTextView = findViewById(R.id.stepCountTextView);

        //stepcount listener
        stepCountListener = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                int stepCount = dataPoint.getValue(field).asInt();
                updateStepCount(stepCount);
            }
        };
        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .add(new SensorRequest.Builder()
                        .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(), stepCountListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Fitness.getSensorsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .remove(stepCountListener);
    }

    private void updateStepCount(int stepCount) {
        runOnUiThread(() -> stepCountTextView.setText("Step Count: " + stepCount));

    }
}