package com.alecframe.danisatienda.ui.registroVentas;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceVentas;
import com.alecframe.danisatienda.request.Reporte;
import com.alecframe.danisatienda.request.ReporteProducto;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroVentasViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Reporte> mReporte = new MutableLiveData<>();
    private final MutableLiveData<List<ReporteProducto>> mLista = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaDesde = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaHasta = new MutableLiveData<>();
    public RegistroVentasViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Reporte> getReporte() {
        return mReporte;
    }
    public LiveData<List<ReporteProducto>> getLista() {
        return mLista;
    }
    public LiveData<LocalDate> getFechaDesde() {
        return mFechaDesde;
    }
    public LiveData<LocalDate> getFechaHasta() {
        return mFechaHasta;
    }
    public void cambiarFechaDesde(LocalDate fechaDesde) {
        mFechaDesde.setValue(fechaDesde);
    }
    public void cambiarFechaHasta(LocalDate fechaHasta) {
        mFechaHasta.setValue(fechaHasta);
    }
    public void cargarReporte() {
        ApiServiceVentas servicio = ApiClient.getApiServiceVentas(getApplication());

        String fechaDesde = null;
        String fechaHasta = null;

        if (mFechaDesde.getValue()!=null) {
            fechaDesde = mFechaDesde.getValue().toString();
            if (mFechaHasta.getValue()==null) {
                fechaHasta = LocalDate.now().toString();
            }else {
                fechaHasta = mFechaHasta.getValue().toString();
            }
        }else if (mFechaHasta.getValue()!=null) {
            fechaHasta = mFechaHasta.getValue().toString();
            if (mFechaDesde.getValue()==null) {
                fechaDesde = LocalDate.now().toString();
            }else {
                fechaDesde = mFechaDesde.getValue().toString();
            }
        }

        Call<Reporte> call = servicio.obtenerReporte(
                fechaDesde,
                fechaHasta
        );

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                if (response.isSuccessful()) {
                    mReporte.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar el reporte");
                    Log.d("CARGAR REPORTE", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<Reporte> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR REPORTE ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR REPORTE ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }

    public void cargarLista(List<ReporteProducto> reporteProductos) {
        mLista.setValue(reporteProductos);
    }
}