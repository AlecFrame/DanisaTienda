package com.alecframe.danisatienda.ui.categorias;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceCategorias;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Categoria>> mLista = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>(1);
    public CategoriasViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Categoria>> getLista() {
        return mLista;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public void cambiarEstado(int estado) {
        mEstado.setValue(estado);
    }
    public void cargarLista() {
        ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

        Call<List<Categoria>> call = servicio.filtrarPorEstado(mEstado.getValue());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar las categorías");
                    Log.d("CARGAR CATEGORÍAS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR CATEGORIA ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR CATEGORIA ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}