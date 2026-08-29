package com.alecframe.danisatienda;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiService;
import com.alecframe.danisatienda.request.ApiServiceAlias;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Alias> mAlias = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Alias> getAlias() {
        return mAlias;
    }
    public void cargarAlias() {
        ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

        Call<Alias> call = servicio.obtenerAlias(1);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Alias> call, Response<Alias> response) {
                if (response.isSuccessful()) {
                    mAlias.postValue(response.body());
                } else {
                    mAlias.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<Alias> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}
