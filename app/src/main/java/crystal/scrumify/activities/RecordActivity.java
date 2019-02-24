package crystal.scrumify.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crystal.scrumify.R;
import crystal.scrumify.models.Record;
import crystal.scrumify.utils.FileUtils;
import crystal.scrumify.utils.PdfCreator;

public class RecordActivity extends BaseActivity {

    /*** XML View Component ***/
    private TextView newText;
    private FloatingActionButton newButton;
    private FloatingActionButton docButton;

    private boolean isClicked;
    private String allText;

    /*** Listener ***/
    private View.OnClickListener newButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 9);
            } else {
                Toast.makeText(RecordActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
            }
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
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String docName = docNameInput.getText().toString().trim();
                            PdfCreator.createPdf(FileUtils.getAppPath(RecordActivity.this) + docName, RecordActivity.this, allText);
                        }
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

                    if (isClicked) {
                        isClicked = false;
                        allText = result.get(0);
                    } else {
                        allText += ".\n\n" + result.get(0);
                    }
                    newText.setText(allText);
                }
                break;
        }
    }

    public RecordActivity() {
        super(R.layout.activity_record);
        isClicked = true;
        allText = "Happy Meeting! :)";
    }

    @Override
    public void bindView() {
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
}
