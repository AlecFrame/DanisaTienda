package com.alecframe.danisatienda.ui.alias;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAlias;
import com.alecframe.danisatienda.request.ApiServiceCategorias;
import com.alecframe.danisatienda.request.BodyUsuarioRequest;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AliasDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<String> mViewMode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mBack = new MutableLiveData<>();
    private final MutableLiveData<Alias> mAlias = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mProcesoTerminado = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>();
    public AliasDetalleViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Alias> getAlias() {
        return mAlias;
    }
    public LiveData<String> getViewMode() {
        return mViewMode;
    }
    public LiveData<Boolean> getBack() {
        return mBack;
    }
    public LiveData<Boolean> getProcesoTerminado() {
        return mProcesoTerminado;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public void recuperarDatos(Bundle b) {
        String viewMode = b.getString("viewMode", "ver");
        Alias a = b.getSerializable("alias", Alias.class);
        if (a!=null) {
            mEstado.setValue(a.getEstado());
            mAlias.setValue(a);
        }
        mViewMode.setValue(viewMode);
    }
    public void setViewMode(String viewMode) {
        mViewMode.setValue(viewMode);
    }
    public void crearAlias(String valor, String banco, String propietario){
        try {
            if (valor.isBlank() | banco.isBlank() | propietario.isBlank()) {
                mToastMessage.setValue("Faltan campos por rellenar");
                mProcesoTerminado.setValue(true);
            }else{
                Alias nuevoAlias = new Alias();

                nuevoAlias.setValor(valor);
                nuevoAlias.setBanco(banco);
                nuevoAlias.setPropietario(propietario);

                ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

                Call<Alias> call = servicio.crearAlias(nuevoAlias);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Alias> call, Response<Alias> response) {
                        if (response.isSuccessful()) {
                            mAlias.postValue(response.body());
                            mToastMessage.postValue("Alias creado");
                            mBack.postValue(true);
                        } else {
                            mToastMessage.postValue("Error al crear el alias");
                            mProcesoTerminado.postValue(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Alias> call, Throwable t) {
                        mToastMessage.postValue("Error del servidor.");
                        mProcesoTerminado.postValue(true);
                        Log.d("CREAR ALIAS ERROR", "DRAWABLE: call: "+call);
                        Log.d("CREAR ALIAS ERROR", "DRAWABLE: throwable: "+t);
                    }
                });
            }
        }catch (NumberFormatException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            mProcesoTerminado.setValue(true);
            Log.d("CREAR ALIAS ERROR", "ERROR: "+e);
        }
    }
    public void actualizarAlias(String valor, String banco, String propietario){
        if (mAlias.getValue()==null) {
            mToastMessage.setValue("El alias no se cargo correctamente");
            return;
        }

        try {
            if (valor.isBlank() | banco.isBlank() | propietario.isBlank()) {
                mToastMessage.setValue("Faltan campos por rellenar");
            }else{
                Alias nuevoAlias = new Alias();

                nuevoAlias.setIdAlias(mAlias.getValue().getIdAlias());
                nuevoAlias.setValor(valor);
                nuevoAlias.setBanco(banco);
                nuevoAlias.setPropietario(propietario);
                nuevoAlias.setEstado(mAlias.getValue().getEstado());

                ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

                Call<Alias> call = servicio.actualizarAlias(mAlias.getValue().getIdAlias(), nuevoAlias);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Alias> call, Response<Alias> response) {
                        if (response.isSuccessful()) {
                            mAlias.postValue(response.body());
                            mToastMessage.postValue("Alias actualizado");
                            mBack.postValue(true);
                        } else {
                            mToastMessage.postValue("Error al actualizar el alias");
                            mProcesoTerminado.postValue(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Alias> call, Throwable t) {
                        mToastMessage.postValue("Error del servidor.");
                        mProcesoTerminado.postValue(true);
                        Log.d("ACTUALIZAR ALIAS ERROR", "DRAWABLE: call: "+call);
                        Log.d("ACTUALIZAR ALIAS ERROR", "DRAWABLE: throwable: "+t);
                    }
                });
            }
        }catch (NumberFormatException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            mProcesoTerminado.setValue(true);
            Log.d("ACTUALIZAR ALIAS ERROR", "ERROR: "+e);
        }
    }
    public void activarAlias() {
        Alias alias = mAlias.getValue();

        if (alias!=null) {
            ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

            Call<Map<String, String>> call = servicio.activarAlias(alias.getIdAlias(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(1);
                        mToastMessage.postValue("Alias: "+alias.getValor()+" ha sido activado");
                    } else {
                        mToastMessage.postValue("Error al activar el alias");
                        Log.d("ACTIVAR ALIAS", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    Log.d("ACTIVAR ALIAS ERROR", "DRAWABLE: call: " + call);
                    Log.d("ACTIVAR ALIAS ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo activar el alias");
        }
    }
    public void desactivarAlias() {
        Alias alias = mAlias.getValue();

        if (alias != null) {
            ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

            Call<Map<String, String>> call = servicio.desactivarAlias(alias.getIdAlias(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(0);
                        mToastMessage.postValue("Alias: " + alias.getValor() + " ha sido desactivado");
                    } else {
                        mToastMessage.postValue("Error al desactivar el alias");
                        Log.d("DESACTIVAR ALIAS", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    Log.d("DESACTIVAR ALIAS ERROR", "DRAWABLE: call: " + call);
                    Log.d("DESACTIVAR ALIAS ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        } else {
            mToastMessage.postValue("No se pudo desactivar el alias");
        }
    }
}