package com.dpaksoni.assignment.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dpaksoni.assignment.R;
import com.dpaksoni.assignment.models.CountryCount;
import com.dpaksoni.assignment.models.Resource;
import com.dpaksoni.assignment.network.NetworkUtil;
import com.dpaksoni.assignment.network.RestApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryCountRepository {
    private String TAG = CountryCountRepository.class.getSimpleName();

    private Context mContext;

    public CountryCountRepository(Context ctx) {
        this.mContext = ctx;
    }

    public LiveData<Resource<List<CountryCount>>> getCountryCount() {
        final MutableLiveData<Resource<List<CountryCount>>> result = new MutableLiveData<>();

        Resource<List<CountryCount>> loadingResource = new Resource<>();
        loadingResource.loading();

        result.setValue(loadingResource);

        RestApi api = NetworkUtil.getRetrofit().create(RestApi.class);
        api.getCounts().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponse: isSuccessful: true");
                    try {
                        JSONObject responseObj = new JSONObject(response.body().string());
                        Gson gson = new Gson();
                        List<CountryCount> listCountry
                                = gson.fromJson(responseObj.getJSONArray("Countries").toString(), new TypeToken< List<CountryCount> >(){}.getType());

                        Resource<List<CountryCount>> successResource = new Resource<>();
                        successResource.success(listCountry);
                        result.setValue(successResource);
                    }
                    catch (Exception e) {
                        Resource<List<CountryCount>> errorResource = new Resource<>();
                        errorResource.error(mContext.getString(R.string.some_error_occured));

                        result.setValue(errorResource);
                    }
                }
                else {
                    Log.d(TAG, "onResponse: isSuccessful: false");
                    try {
                        JSONObject fromCache = getJSONFromRaw();
                        if(fromCache != null) {
                            Gson gson = new Gson();
                            List<CountryCount> listCountry
                                    = gson.fromJson(fromCache.getJSONArray("Countries").toString(), new TypeToken< List<CountryCount> >(){}.getType());

                            Resource<List<CountryCount>> successResource = new Resource<>();
                            successResource.success(listCountry);
                            result.setValue(successResource);
                        }
                    }
                    catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Resource<List<CountryCount>> errorResource = new Resource<>();
                errorResource.error(mContext.getString(R.string.some_error_occured));

                result.setValue(errorResource);
            }
        });

        return result;
    }

    private JSONObject getJSONFromRaw() {
        try {
            InputStream is = mContext.getResources().openRawResource(R.raw.covidsampledata);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            finally {
                is.close();
            }

            String jsonString = writer.toString();
            return new JSONObject(jsonString);
        }
        catch (Exception e ) {
            return null;
        }
    }
}
