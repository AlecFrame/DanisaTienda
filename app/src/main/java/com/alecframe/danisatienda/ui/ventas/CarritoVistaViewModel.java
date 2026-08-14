package com.alecframe.danisatienda.ui.ventas;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Venta;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceVentas;
import com.alecframe.danisatienda.request.BodyUsuarioRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoVistaViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Venta> mVenta = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mProcesoTerminado = new MutableLiveData<>();
    private final MutableLiveData<List<CarritoDetalle>> mListaDetalle = new MutableLiveData<>();
    public CarritoVistaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Venta> getVenta() {
        return mVenta;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public LiveData<Boolean> getProcesoTerminado() {
        return mProcesoTerminado;
    }
    public LiveData<List<CarritoDetalle>> getListaDetalle() {
        return mListaDetalle;
    }
    public void cargarVenta(Bundle bundle) {
        Venta venta = bundle.getSerializable("venta", Venta.class);
        if (venta!=null) { mVenta.setValue(venta); }
    }

    public void cargarDetalles() {
        Venta venta = mVenta.getValue();
        if (venta!=null) {
            ApiServiceVentas servicio = ApiClient.getApiServiceVentas();

            Call<List<CarritoDetalle>> call = servicio.obtenerDetallesDeCarrito(venta.getIdCarrito());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<CarritoDetalle>> call, Response<List<CarritoDetalle>> response) {
                    if (response.isSuccessful()) {
                        mListaDetalle.postValue(response.body());
                    } else {
                        mToastMessage.postValue("Error al cargar los detalles de la venta");
                        mProcesoTerminado.setValue(true);
                        Log.d("CARGAR DETALLES DE VENTA", "Error: " + response.message());
                    }
                }
                @Override
                public void onFailure(Call<List<CarritoDetalle>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.setValue(true);
                    Log.d("CARGAR DETALLES DE VENTA ERROR", "DRAWABLE: call: " + call);
                    Log.d("CARGAR DETALLES DE VENTA ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se encontró la venta para cargar los detalles");
            mProcesoTerminado.setValue(true);
        }
    }
    public void activarVenta() {
        Venta venta = mVenta.getValue();
        if (venta!=null) {
            ApiServiceVentas servicio = ApiClient.getApiServiceVentas();

            Call<Map<String, String>> call = servicio.activarVenta(venta.getIdVenta(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(1);
                        mToastMessage.postValue("La venta ha sido activada");
                    } else {
                        mToastMessage.postValue("Error al activar la venta");
                        mProcesoTerminado.setValue(true);
                        Log.d("ACTIVAR VENTA", "Error: " + response.message());
                    }
                }
                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.setValue(true);
                    Log.d("ACTIVAR VENTA ERROR", "DRAWABLE: call: " + call);
                    Log.d("ACTIVAR VENTA ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo activar la venta");
            mProcesoTerminado.setValue(true);
        }
    }
    public void desactivarVenta() {
        Venta venta = mVenta.getValue();
        if (venta!=null) {
            ApiServiceVentas servicio = ApiClient.getApiServiceVentas();

            Call<Map<String, String>> call = servicio.desactivarVenta(venta.getIdVenta(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(0);
                        mToastMessage.postValue("La venta ha sido desctivada");
                    } else {
                        mToastMessage.postValue("Error al desctivar la venta");
                        mProcesoTerminado.setValue(true);
                        Log.d("DESACTIVAR VENTA", "Error: " + response.message());
                    }
                }
                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.setValue(true);
                    Log.d("DESACTIVAR VENTA ERROR", "DRAWABLE: call: " + call);
                    Log.d("DESACTIVAR VENTA ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo desactivar la venta");
            mProcesoTerminado.setValue(true);
        }
    }
}