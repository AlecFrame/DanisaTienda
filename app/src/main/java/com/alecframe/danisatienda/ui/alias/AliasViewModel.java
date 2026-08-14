package com.alecframe.danisatienda.ui.alias;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAlias;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AliasViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Alias>> mLista = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>(1);
    public AliasViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Alias>> getLista() {
        return mLista;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public void cambiarEstado(int estado) {
        mEstado.setValue(estado);
    }
    public void cargarLista() {
        ApiServiceAlias servicio = ApiClient.getApiServiceAlias();

        Call<List<Alias>> call = servicio.obtenerTodosLosAlias(mEstado.getValue());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Alias>> call, Response<List<Alias>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar los alias");
                    Log.d("CARGAR ALIAS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Alias>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}