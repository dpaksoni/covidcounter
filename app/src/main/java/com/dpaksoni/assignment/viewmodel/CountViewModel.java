package com.dpaksoni.assignment.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dpaksoni.assignment.Constants;
import com.dpaksoni.assignment.R;
import com.dpaksoni.assignment.SharedPreferencesHelper;
import com.dpaksoni.assignment.models.CountryCount;
import com.dpaksoni.assignment.models.Resource;
import com.dpaksoni.assignment.repo.CountryCountRepository;
import com.dpaksoni.assignment.tableview.model.CellModel;
import com.dpaksoni.assignment.tableview.model.ColumnHeaderModel;
import com.dpaksoni.assignment.tableview.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class CountViewModel extends AndroidViewModel {
    private Application application;
    private CountryCountRepository countRepo;

    private MutableLiveData<Integer> mTotalConfirmedCases = new MutableLiveData<>();
    private MutableLiveData<Integer> mTotalDeaths = new MutableLiveData<>();
    private MutableLiveData<Integer> mTotalRecovered = new MutableLiveData<>();

    private List<RowHeaderModel> listRowHeaderModels;
    private List<ColumnHeaderModel> listColumnHeaderModel;

    public LiveData<Integer> getTotalConfirmedCases() {
        return mTotalConfirmedCases;
    }

    public LiveData<Integer> getTotalDeath() {
        return mTotalDeaths;
    }

    public LiveData<Integer> getTotalRecovered() {
        return mTotalRecovered;
    }

    public CountViewModel(Application application) {
        super(application);
        this.application = application;
        this.countRepo = new CountryCountRepository(this.application);
        createColumnHeaderModelList();
    }

    public LiveData<Resource<List<CountryCount>>> getCount() {
        return countRepo.getCountryCount();
    }

    public List<RowHeaderModel> createRowHeaderList(int size) {
        listRowHeaderModels = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            listRowHeaderModels.add(new RowHeaderModel(""));
        }
        return listRowHeaderModels;
    }

    public List<ColumnHeaderModel> createColumnHeaderModelList() {
        listColumnHeaderModel = new ArrayList<>();

        // Create Column Headers
        listColumnHeaderModel.add(new ColumnHeaderModel(application.getString(R.string.labelCountry)));
        listColumnHeaderModel.add(new ColumnHeaderModel(application.getString(R.string.labelTotalCases)));
        listColumnHeaderModel.add(new ColumnHeaderModel(application.getString(R.string.labelTotalDeath)));
        listColumnHeaderModel.add(new ColumnHeaderModel(application.getString(R.string.labelTotalRecovered)));
        return listColumnHeaderModel;
    }

    public List<List<CellModel>> createCellModelList(List<CountryCount> countryList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service

        int totalCases = 0, totalDeaths = 0, totalRecovered = 0;

        SharedPreferencesHelper prefHelper = new SharedPreferencesHelper(application, Constants.PREF);
        String countryName = prefHelper.getString(Constants.KEY_COUNTRY, null);

        for (int i = 0; i < countryList.size(); i++) {
            CountryCount country = countryList.get(i);

            if(country.getTotalConfirmed() > 0) {
                List<CellModel> list = new ArrayList<>();
                // The order should be same with column header list;
                list.add(new CellModel("1-" + i, country.getCountry()));
                list.add(new CellModel("2-" + i, country.getTotalConfirmed()));
                list.add(new CellModel("3-" + i, country.getTotalDeaths()));
                list.add(new CellModel("4-" + i, country.getTotalRecovered()));

                if(!TextUtils.isEmpty(countryName) && countryName.equalsIgnoreCase(country.getCountry())) {
                    lists.add(0, list);
                }
                else {
                    lists.add(list);
                }

                totalCases += country.getTotalConfirmed();
                totalDeaths += country.getTotalDeaths();
                totalRecovered += country.getTotalRecovered();
            }

            mTotalConfirmedCases.postValue(totalCases);
            mTotalDeaths.postValue(totalDeaths);
            mTotalRecovered.postValue(totalRecovered);
        }

        return lists;
    }

    public List<RowHeaderModel> getListRowHeaderModels() {
        return listRowHeaderModels;
    }

    public List<ColumnHeaderModel> getListColumnHeaderModel() {
        return listColumnHeaderModel;
    }
}