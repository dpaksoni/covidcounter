package com.dpaksoni.assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQ_CODE_LOCATION_PERMISSION = 102;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        checkUserCountry();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    private void checkUserCountry() {

        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(mContext, Constants.KEY_COUNTRY);
        String country = prefHelper.getString(Constants.KEY_COUNTRY, null);
        Log.d(TAG, "checkUserCountry: country = "+country);
        if(TextUtils.isEmpty(country)) {
            fetchUserCountry();
        }
    }

    private void fetchUserCountry() {
        boolean isLocationPermissionGranted = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(isLocationPermissionGranted) {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);

                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
                mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest, mLocationCallback,
                        Looper.myLooper()
                );
            }
            else {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if(mLastLocation != null) {
                double lat = mLastLocation.getLatitude();
                double lng = mLastLocation.getLongitude();

                new FindCountryAsync(mContext, lat, lng).execute();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_CODE_LOCATION_PERMISSION) {
            if(grantResults != null && grantResults.length > 0) {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Location permission granted");
                    fetchUserCountry();
                }
                else {
                    Log.d(TAG, "Location permission denied");
                }
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class FindCountryAsync extends AsyncTask<Void, Void, String> {
        private double lat, lng;
        private Context mContext;
        public FindCountryAsync(Context context, double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            this.mContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Geocoder geocoder = new Geocoder(this.mContext, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(this.lat, this.lng, 1);
                return addresses.get(0).getCountryName();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(mContext, Constants.PREF);
            prefHelper.setString(Constants.KEY_COUNTRY, s);
        }
    }
}
