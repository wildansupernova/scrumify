package crystal.scrumify.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import crystal.scrumify.contracts.OnGetLocation;

public class AddressService {

    private Context context;
    private GoogleApiClient googleApiClient;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks;
    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener;
    private LocationListener locationListener;

    private LocationRequest locationRequest;
    private OnGetLocation onGetLocation;

    public AddressService (Context context, OnGetLocation onGetLocation) {

        this.context = context;
        this.onGetLocation = onGetLocation;

        setupConnectionCallbacks();
        setupConnectionFailedListener();
        setupLocationChangedListener();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    private void setupConnectionCallbacks() {
        connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                settingRequest();
            }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(context, "Connection suspended!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setupConnectionFailedListener() {
        connectionFailedListener = connectionResult -> {
            Toast.makeText(context, "Connection failed!", Toast.LENGTH_SHORT).show();
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult((Activity) context, 90000);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Location services connection failed!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setupLocationChangedListener() {
        locationListener = location -> onGetLocation.onGetLocation(location);
    }

    private void settingRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        locationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result;
        result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(newResult -> {
            final Status status = newResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    getLocation();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult((Activity) context, 1000);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            onGetLocation.onGetLocation(lastLocation);
        } else {
            Log.i("Current Location", "No data for location found");
            if (!googleApiClient.isConnected())
                googleApiClient.connect();

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }
    }

}
