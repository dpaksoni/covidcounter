package com.dpaksoni.assignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.List;

public class FirstFragment extends Fragment {
    private String TAG = FirstFragment.class.getSimpleName();

    private static final int MSG_FETCH_DATA = 101;
    private static final long DELAY_FETCH_DATA = 10 * 1000;


    private TableView mCountryCountTableView;
    private CountViewModel mCountViewModel;
    private TextView tvTotalConfirmedCases;
    private TextView tvTotalDeaths;
    private TextView tvTotalRecovered;
    private CountryTableAdapter mCountryAdapter;
    private Context mContext;

    private ProgressDialog mLoadingProgress;

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
                    case LOADING:
                    {
                        dismissLoadingProgress();

                        mLoadingProgress = ProgressDialog.show(mContext, null, getString(R.string.loading_data), true, false);
                    }
                    break;

                    case ERROR:
                    {
                        dismissLoadingProgress();

                        String errormsg = listResource.getError();
                        showErrorMessage(errormsg);
                    }

                    case SUCCESS:
                    {
                        dismissLoadingProgress();

                        List<CountryCount> listCountries = listResource.getData();
                        mCountViewModel.createRowHeaderList(listCountries.size());

                        mCountryAdapter.setCountryList(mCountViewModel.getListColumnHeaderModel(),
                                mCountViewModel.getListRowHeaderModels(), mCountViewModel.createCellModelList(listCountries));

                        SortState sortState = mCountryCountTableView.getSortingStatus(1);

                        if(sortState == SortState.UNSORTED) {
                            mCountryCountTableView.hideRow(0);
                            mCountryCountTableView.sortColumn(1, SortState.DESCENDING);
                            mCountryCountTableView.showRow(0);

                            mCountryCountTableView.scrollToRowPosition(0);
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

    private void showErrorMessage(String errormsg) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setMessage(errormsg)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void dismissLoadingProgress() {
        if(mLoadingProgress != null && mLoadingProgress.isShowing()) {
            mLoadingProgress.dismiss();
            mLoadingProgress = null;
        }
    }
}