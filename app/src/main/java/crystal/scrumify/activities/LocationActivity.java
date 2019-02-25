package crystal.scrumify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import crystal.scrumify.R;
import crystal.scrumify.contracts.OnGetAddressCompleted;
import crystal.scrumify.services.AddressService;

public class LocationActivity extends AppCompatActivity {

    TextView latitudeText, longitudeText;
    ProgressBar progressBar;
    AddressService addressService;

    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_location);


        latitudeText = findViewById(R.id.latitude);
        longitudeText = findViewById(R.id.longitude);
        progressBar = findViewById(R.id.progressBar);

        addressService = new AddressService(this, address -> {
            progressBar.setVisibility(View.INVISIBLE);
            latitudeText.setText(address);
        });
    }
}
