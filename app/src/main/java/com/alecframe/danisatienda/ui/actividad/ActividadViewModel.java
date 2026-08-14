package com.alecframe.danisatienda.ui.actividad;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.model.Auditoria;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAlias;
import com.alecframe.danisatienda.request.ApiServiceAuditorias;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Auditoria>> mLista = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaDesde = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> mFechaHasta = new MutableLiveData<>();
    private final MutableLiveData<Integer> mFechaMode = new MutableLiveData<>();
    private final MutableLiveData<String> mEntidad = new MutableLiveData<>();
    private final MutableLiveData<String> mAccion = new MutableLiveData<>();
    public ActividadViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Auditoria>> getLista() {
        return mLista;
    }
    public LiveData<LocalDate> getFechaDesde() {
        return mFechaDesde;
    }
    public LiveData<LocalDate> getFechaHasta() {
        return mFechaHasta;
    }
    public LiveData<Integer> getFechaMode() {
        return mFechaMode;
    }
    public LiveData<String> getEntidad() {
        return mEntidad;
    }
    public LiveData<String> getAccion() {
        return mAccion;
    }
    public void setEntidad(String entidad) {
        mEntidad.setValue(entidad);
    }
    public void setAccion(String accion) {
        mAccion.setValue(accion);
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
    public void cargarLista(String usuario) {
        ApiServiceAuditorias servicio = ApiClient.getApiServiceAuditorias();

        if (usuario.isBlank()) {
            usuario = null;
        }

        String entidad = mEntidad.getValue();
        String accion = mAccion.getValue();

        if (Objects.equals(mEntidad.getValue(), "Todos")) {
            entidad = null;
        }
        if (Objects.equals(mAccion.getValue(), "Todas")) {
            accion = null;
        }

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

        Call<List<Auditoria>> call = servicio.obtenerAuditorias(
                entidad,
                accion,
                fechaDesde,
                fechaHasta,
                usuario
        );

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Auditoria>> call, Response<List<Auditoria>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
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