package com.alecframe.danisatienda.ui.ventas;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.model.Venta;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAlias;
import com.alecframe.danisatienda.request.ApiServiceVentas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentasViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Venta>> mLista = new MutableLiveData<>();
    private final MutableLiveData<String> mTipoPago = new MutableLiveData<>();
    private final MutableLiveData<Integer> mIdAlias = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado  = new MutableLiveData<>(1);
    private final MutableLiveData<LocalDate> mFechaDesde = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaHasta = new MutableLiveData<>();
    private final MutableLiveData<List<Alias>> mListaAlias = new MutableLiveData<>();
    private final MutableLiveData<Integer> mFechaMode = new MutableLiveData<>();
    public VentasViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Venta>> getLista() {
        return mLista;
    }
    public LiveData<String> getTipoPago() {
        return mTipoPago;
    }
    public LiveData<Integer> getIdAlias() {
        return mIdAlias;
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
    public LiveData<List<Alias>> getListaAlias() {
        return mListaAlias;
    }
    public LiveData<Integer> getFechaMode() {
        return mFechaMode;
    }
    public void cambiarTipoPago(String tipoPago) {
        mTipoPago.setValue(tipoPago);
    }
    public void cambiarIdAlias(Integer idAlias) {
        mIdAlias.setValue(idAlias);
    }
    public void cambiarEstado(int estado) {
        mEstado.setValue(estado);
    }
    public void cambiarFechaDesde(LocalDate fechaDesde) {
        mFechaDesde.setValue(fechaDesde);
    }
    public void cambiarFechaHasta(LocalDate fechaHasta) {
        mFechaHasta.setValue(fechaHasta);
    }
    public void cambiarFechaMode(int fechaMode) {
        mFechaMode.setValue(fechaMode);
    }
    public void cargarLista() {
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

        Call<List<Venta>> call = servicio.obtenerVentas(
                mTipoPago.getValue(),
                mIdAlias.getValue(),
                mEstado.getValue(),
                fechaDesde,
                fechaHasta
        );

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Venta>> call, Response<List<Venta>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar las ventas");
                    Log.d("CARGAR VENTAS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Venta>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR VENTAS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR VENTAS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
    public void cargarSpinnerAlias() {
        ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

        Call<List<Alias>> call = servicio.obtenerTodosLosAlias(1);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Alias>> call, Response<List<Alias>> response) {
                if (response.isSuccessful()) {
                    Alias primerAlias = new Alias(0, "Cualquier alias", "", "", 1);
                    List<Alias> copia = new ArrayList<>(response.body());

                    copia.add(0, primerAlias);

                    mListaAlias.postValue(copia);
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