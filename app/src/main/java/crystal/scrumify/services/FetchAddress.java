package crystal.scrumify.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crystal.scrumify.contracts.OnTaskCompleted;

public class FetchAddress extends AsyncTask<Location, Void, String> {

    private Context context;
    private OnTaskCompleted onTaskCompleted;


    public FetchAddress(Context context, OnTaskCompleted onTaskCompleted) {
        this.context = context;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Location location = locations[0];

        List<Address> addresses = null;
        String message = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            message = "Not available";
            e.printStackTrace();
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "No address found", Toast.LENGTH_SHORT).show();
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressParts = new ArrayList<>();

            for (int i=0; i<=address.getMaxAddressLineIndex(); i++) {
                addressParts.add(address.getAddressLine(i));
            }
            message = TextUtils.join("\n", addressParts);
        }

        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onTaskCompleted.onCompleted(s);
    }
}
