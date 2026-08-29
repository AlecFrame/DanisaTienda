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

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GastosViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Gasto>> mLista = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>(1);
    private final MutableLiveData<String> mCategoria = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaDesde = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaHasta = new MutableLiveData<>();
    public GastosViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Gasto>> getLista() {
        return mLista;
    }
    public LiveData<String> getCategoria() {
        return mCategoria;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
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
    public void cambiarEstado(int estado) {
        mEstado.setValue(estado);
    }
    public void cambiarCategoria(String categoria) {
        mCategoria.setValue(categoria);
    }
    public void cargarLista(String descripcion) {
        ApiServiceGastos servicio = ApiClient.getApiServiceGastos(getApplication());
        Integer idCategoria;

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

        if (descripcion.isBlank()) { descripcion = null; }

        Call<List<Gasto>> call = servicio.listarGastos(
                descripcion,
                mCategoria.getValue(),
                null,
                mEstado.getValue(),
                fechaDesde,
                fechaHasta

        );
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Gasto>> call, Response<List<Gasto>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar los gastos");
                    Log.d("CARGAR GASTOS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Gasto>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR GASTOS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR GASTOS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}