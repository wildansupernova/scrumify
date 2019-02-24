package crystal.scrumify.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import crystal.scrumify.R;
import crystal.scrumify.contracts.OnGetLocation;
import crystal.scrumify.contracts.OnTaskCompleted;
import crystal.scrumify.services.AddressService;
import crystal.scrumify.services.FetchAddress;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class LocationActivity extends AppCompatActivity {

    TextView latitudeText, longitudeText;
    ProgressBar progressBar;
    AddressService addressService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_location);

        latitudeText = findViewById(R.id.latitude);
        longitudeText = findViewById(R.id.longitude);
        progressBar = findViewById(R.id.progressBar);

        addressService = new AddressService(this, new OnGetLocation() {
            @Override
            public void onGetLocation(Location location) {
                progressBar.setVisibility(View.INVISIBLE);
                latitudeText.setText("Latitude: " + location.getLatitude());
                longitudeText.setText("Longitude: " + String.valueOf(location.getLongitude()));
            }
        });
    }

}
