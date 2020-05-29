package com.dpaksoni.assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dpaksoni.assignment.models.CountryCount;
import com.dpaksoni.assignment.models.Resource;
import com.dpaksoni.assignment.tableview.CountryTableAdapter;
import com.dpaksoni.assignment.tableview.MyTableViewListener;
import com.dpaksoni.assignment.viewmodel.CountViewModel;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.sort.SortState;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {
    private String TAG = FirstFragment.class.getSimpleName();

    private static final int MSG_FETCH_DATA = 101;
    private static final long DELAY_FETCH_DATA = 60 * 1000;
    private static final int REQ_CODE_LOCATION_PERMISSION = 102;

    private TableView mCountryCountTableView;
    private CountViewModel mCountViewModel;
    private TextView tvTotalConfirmedCases;
    private TextView tvTotalDeaths;
    private TextView tvTotalRecovered;
    private CountryTableAdapter mCountryAdapter;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        mCountryCountTableView = view.findViewById(R.id.countryTableView);
        tvTotalConfirmedCases = view.findViewById(R.id.tvTotalCases);
        tvTotalDeaths = view.findViewById(R.id.tvTotalDeath);
        tvTotalRecovered = view.findViewById(R.id.tvTotalRecovered);

        this.mContext = getContext();
        initView();

        mCountViewModel = ViewModelProviders.of(this).get(CountViewModel.class);

        mCountViewModel.getTotalConfirmedCases().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalConfirmedCases.setText(String.valueOf(integer));
            }
        });

        mCountViewModel.getTotalDeath().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalDeaths.setText(String.valueOf(integer));
            }
        });

        mCountViewModel.getTotalRecovered().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tvTotalRecovered.setText(String.valueOf(integer));
            }
        });

        return view;
    }

    private void initView() {
        mCountryAdapter = new CountryTableAdapter(getContext());
        mCountryCountTableView.setAdapter(mCountryAdapter);
        mCountryCountTableView.setTableViewListener(new MyTableViewListener(mCountryCountTableView));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mHandler.hasMessages(MSG_FETCH_DATA)) {
            mHandler.removeMessages(MSG_FETCH_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkUserCountry();

        if(!mHandler.hasMessages(MSG_FETCH_DATA)) {
            mHandler.sendEmptyMessage(MSG_FETCH_DATA);
        }
    }

    public void getCountryCount() {
        Log.d(TAG, "In getCountryCount");

        mCountViewModel.getCount().observe(getViewLifecycleOwner(), new Observer<Resource<List<CountryCount>>>() {
            @Override
            public void onChanged(Resource<List<CountryCount>> listResource) {
                switch (listResource.getStatus()) {
                    case SUCCESS:
                    {
                        List<CountryCount> listCountries = listResource.getData();
                        mCountViewModel.createRowHeaderList(listCountries.size());

                        mCountryAdapter.setCountryList(mCountViewModel.getListColumnHeaderModel(),
                                mCountViewModel.getListRowHeaderModels(), mCountViewModel.createCellModelList(listCountries));

                        SortState sortState = mCountryCountTableView.getSortingStatus(1);

                        if(sortState == SortState.UNSORTED) {
                            mCountryCountTableView.hideRow(0);
                            mCountryCountTableView.sortColumn(1, SortState.DESCENDING);
                            mCountryCountTableView.showRow(0);
                        }
                    }
                    break;
                }
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == MSG_FETCH_DATA) {
                Log.d(TAG, "msg.what == MSG_FETCH_DATA");

                getCountryCount();
                sendEmptyMessageDelayed(MSG_FETCH_DATA, DELAY_FETCH_DATA);
            }

            super.handleMessage(msg);
        }
    };

    private void checkUserCountry() {
        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(getContext(), Constants.KEY_COUNTRY);
        String country = prefHelper.getString(Constants.KEY_COUNTRY, null);

        if(TextUtils.isEmpty(country)) {
            fetchUserCountry();
        }
    }

    private void fetchUserCountry() {
        boolean isLocationPermissionGranted = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(isLocationPermissionGranted) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);

                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
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
                    fetchUserCountry();
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