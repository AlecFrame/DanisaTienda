package com.alecframe.danisatienda.ui.gasto;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Gasto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceGastos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GastoDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Gasto> mGasto = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mProcesoTerminado = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mBack = new MutableLiveData<>();
    public GastoDetalleViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Gasto> getGasto() {
        return mGasto;
    }
    public LiveData<Boolean> getProcesoTerminado() {
        return mProcesoTerminado;
    }
    public LiveData<Boolean> getBack() {
        return mBack;
    }
    public void crearGasto(String descripcion, String categoria, String montoS) {
        try {
            if (descripcion.isBlank() | categoria.isBlank() | montoS.isBlank()) {
                mToastMessage.setValue("Faltan campos por rellenar");
                mProcesoTerminado.setValue(true);
            }else{
                Gasto nuevoGasto = new Gasto();
                double monto = Double.parseDouble(montoS);

                nuevoGasto.setDescripcion(descripcion);
                nuevoGasto.setCategoria(categoria);
                nuevoGasto.setMonto(monto);

                ApiServiceGastos servicio = ApiClient.getApiServiceGastos(getApplication());

                Call<Gasto> call = servicio.crearGasto(nuevoGasto);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                        if (response.isSuccessful()) {
                            mGasto.postValue(response.body());
                            mToastMessage.postValue("Gasto creado");
                            mBack.postValue(true);
                        } else {
                            mToastMessage.postValue("Error al crear el gasto");
                            mProcesoTerminado.postValue(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Gasto> call, Throwable t) {
                        mToastMessage.postValue("Error del servidor.");
                        mProcesoTerminado.postValue(true);
                        Log.d("CREAR GASTO ERROR", "DRAWABLE: call: "+call);
                        Log.d("CREAR GASTO ERROR", "DRAWABLE: throwable: "+t);
                    }
                });
            }
        }catch (NumberFormatException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            mProcesoTerminado.setValue(true);
            Log.d("CREAR GASTO ERROR", "ERROR: "+e);
        }
    }
}