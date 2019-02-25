package crystal.scrumify.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import crystal.scrumify.R;
import crystal.scrumify.contracts.OnGetAddressCompleted;
import crystal.scrumify.services.AddressService;
import crystal.scrumify.utils.FileUtils;
import crystal.scrumify.utils.PdfCreator;

public class RecordActivity extends BaseActivity implements SensorEventListener {

    /*** XML View Component ***/
    private TextView newText;
    private FloatingActionButton micButton;
    private FloatingActionButton docButton;
    private TextView sensorInfo;

    /*** Sensor ***/
    private SensorManager sensorManager;
    private Sensor proximity;
    private Sensor ambient;
    private static final int BEST_DISTANCE = 5;
    private static final int GOOD_DISTANCE = 20;

    /*** Context Data ***/
    private boolean isMicClicked;
    private boolean isSensorExist;
    private float temperature;
    private String location;
    private String allText;

    /*** Listener ***/
    private View.OnClickListener micButtonListener = view -> {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 9);

            if (!isSensorExist) {
                isSensorExist = true;
                proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            }
        } else {
            Toast.makeText(RecordActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener docButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View inflater = getLayoutInflater().inflate(R.layout.form_new_document, null);
            final EditText docNameInput = inflater.findViewById(R.id.form_document_name);
            AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);

            builder.setTitle("Generate New Document Now?")
                    .setView(inflater)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String docName = docNameInput.getText().toString().trim();
                        PdfCreator.createPdf(FileUtils.getAppPath(RecordActivity.this) + docName, RecordActivity.this, temperature, location, allText);
                    })
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 9:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (!isMicClicked) {
                        isMicClicked = true;
                        allText = result.get(0) + ".";
                    } else {
                        allText += "\n\n" + result.get(0) + ".";
                    }
                    newText.setText(allText);
                }

                if (isSensorExist) sensorManager.unregisterListener(this, proximity);
                sensorInfo.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public RecordActivity() {
        super(R.layout.activity_record);
        isMicClicked = false;
        isSensorExist = false;
        allText = "Happy Meeting! :)";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AddressService addressService = new AddressService(this, new OnGetAddressCompleted() {
            @Override
            public void onCompleted(String address) {
                location = address;
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ambient = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.unregisterListener(this, proximity);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sensorManager.unregisterListener(this, ambient);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.d(RecordActivity.class.getSimpleName(), "Proximity: " + String.valueOf(event.values[0]));
            if (event.values[0] >= -BEST_DISTANCE && event.values[0] <= BEST_DISTANCE) {
                showProximityInfo("BEST", R.color.colorPrimary);
            } else if (event.values[0] >= -GOOD_DISTANCE && event.values[0] <= GOOD_DISTANCE){
                showProximityInfo("GOOD", R.color.colorPrimaryDark);
            } else {
                showProximityInfo("BAD", R.color.colorAccent);
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            Log.d(RecordActivity.class.getSimpleName(), "Temperature: " + String.valueOf(event.values[0]));
            temperature = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void bindView() {
        newText = findViewById(R.id.record_new_text);
        micButton = findViewById(R.id.record_mic_button);
        docButton = findViewById(R.id.record_doc_button);
        sensorInfo = findViewById(R.id.record_proximity_info);
    }

    @Override
    public void setupView() {
        newText.setText(allText);
        sensorInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void bindListener() {
        micButton.setOnClickListener(micButtonListener);
        docButton.setOnClickListener(docButtonListener);
    }

    @Override
    public void unbindListener() {
        micButton.setOnClickListener(null);
        docButton.setOnClickListener(null);
    }

    private void showProximityInfo(String text, int color) {
        sensorInfo.setText(text);
        sensorInfo.setBackgroundResource(color);
        sensorInfo.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            sensorInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            sensorInfo.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        sensorInfo.setVisibility(View.VISIBLE);
    }
}
