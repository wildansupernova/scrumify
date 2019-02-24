package crystal.scrumify.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import crystal.scrumify.R;
import crystal.scrumify.utils.FileUtils;
import crystal.scrumify.utils.PdfCreator;

public class RecordActivity extends BaseActivity implements SensorEventListener {

    /*** XML View Component ***/
    private LinearLayout recordLayout;
    private LinearLayout buttonLayout;
    private TextView newText;
    private FloatingActionButton newButton;
    private FloatingActionButton docButton;

    /*** Sensor ***/
    private SensorManager sensorManager;
    private Sensor proximity;
    private static final int BEST_DISTANCE = 5;
    private static final int GOOD_DISTANCE = 20;

    /*** Context Data ***/
    private boolean isMicClicked;
    private boolean isSensorExist;
    private boolean isSensorPaused;
    private String allText;

    /*** Listener ***/
    private View.OnClickListener newButtonListener = view -> {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            isSensorExist = true;
            startActivityForResult(intent, 9);

            if (!isSensorPaused) {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            }
        } else {
            isSensorExist = false;
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
                        PdfCreator.createPdf(FileUtils.getAppPath(RecordActivity.this) + docName, RecordActivity.this, allText);
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

                    if (isMicClicked) {
                        isMicClicked = false;
                        allText = result.get(0) + ".";
                    } else {
                        allText += "\n\n" + result.get(0) + ".";
                    }
                    newText.setText(allText);
                }

                if (isSensorExist) sensorManager.unregisterListener(this);
                break;
        }
    }

    public RecordActivity() {
        super(R.layout.activity_record);
        isMicClicked = true;
        allText = "Happy Meeting! :)";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorExist) sensorManager.unregisterListener(this, proximity);
        buttonLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSensorPaused = true;
        if (isSensorExist) sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -BEST_DISTANCE && event.values[0] <= BEST_DISTANCE) {
                showSnackBar("BEST", R.color.colorPrimary);
            } else if (event.values[0] >= -GOOD_DISTANCE && event.values[0] <= GOOD_DISTANCE){
                showSnackBar("GOOD", R.color.colorPrimaryDark);
            } else {
                showSnackBar("BAD", R.color.colorAccent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void bindView() {
        recordLayout = findViewById(R.id.record_layout);
        buttonLayout = findViewById(R.id.record_layout_button);
        newText = findViewById(R.id.record_new_text);
        newButton = findViewById(R.id.record_new_button);
        docButton = findViewById(R.id.record_doc_button);
    }

    @Override
    public void setupView() {
        newText.setText(allText);
    }

    @Override
    public void bindListener() {
        newButton.setOnClickListener(newButtonListener);
        docButton.setOnClickListener(docButtonListener);
    }

    @Override
    public void unbindListener() {
        newButton.setOnClickListener(null);
        docButton.setOnClickListener(null);
    }

    private void showSnackBar(String text, int color) {
        Snackbar snackbar = Snackbar.make(recordLayout, text, Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        view.setBackgroundResource(color);

        TextView textview = view.findViewById(android.support.design.R.id.snackbar_text);

        textview.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textview.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        buttonLayout.setVisibility(View.GONE);
        snackbar.show();
    }
}
