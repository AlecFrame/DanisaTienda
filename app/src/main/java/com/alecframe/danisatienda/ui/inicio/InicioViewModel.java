package com.alecframe.danisatienda.ui.inicio;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Auditoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAuditorias;
import com.alecframe.danisatienda.request.ApiServiceProductos;
import com.alecframe.danisatienda.request.ApiServiceVentas;
import com.alecframe.danisatienda.request.ResumenVentas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Producto>> mListaProductos = new MutableLiveData<>();
    private final MutableLiveData<List<Auditoria>> mListaAcciones = new MutableLiveData<>();
    private final MutableLiveData<ResumenVentas> mResumen = new MutableLiveData<>();
    public InicioViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Producto>> getListaProductos() {
        return mListaProductos;
    }
    public LiveData<List<Auditoria>> getListaAcciones() {
        return mListaAcciones;
    }
    public LiveData<ResumenVentas> getResumen() {
        return mResumen;
    }
    public void cargarResumen() {
        ApiServiceVentas servicio = ApiClient.getApiServiceVentas();

        Call<ResumenVentas> call = servicio.obtenerResumen();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResumenVentas> call, Response<ResumenVentas> response) {
                if (response.isSuccessful()) {
                    mResumen.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar el resumen");
                    Log.d("CARGAR RESUMEN", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<ResumenVentas> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR RESUMEN ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR RESUMEN ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
    public void cargarListaProductos() {
        ApiServiceProductos servicio = ApiClient.getApiServiceProductos();

        Call<List<Producto>> call = servicio.obtenerProductosStockBajo();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    mListaProductos.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar el stock bajos");
                    Log.d("CARGAR STOCK BAJO", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR STOCK BAJO ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR STOCK BAJO ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
    public void cargarListaAcciones() {
        ApiServiceAuditorias servicio = ApiClient.getApiServiceAuditorias();

        Call<List<Auditoria>> call = servicio.obtenerAuditorias(
                null,
                null,
                null,
                null,
                null
        );

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Auditoria>> call, Response<List<Auditoria>> response) {
                if (response.isSuccessful()) {
                    mListaAcciones.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar el historial");
                    Log.d("CARGAR HISTORIAL", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Auditoria>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR HISTORIAL ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR HISTORIAL ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}