package com.alecframe.danisatienda.ui.inventario;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceCategorias;
import com.alecframe.danisatienda.request.ApiServiceProductos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Producto>> mLista = new MutableLiveData<>();
    private final MutableLiveData<List<Categoria>> mListaCategorias = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>(1);
    private final MutableLiveData<String> mStockOrden = new MutableLiveData<>();
    private final MutableLiveData<Integer> mIdCategoria = new MutableLiveData<>();
    private final Application application;
    public InventarioViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<List<Producto>> getLista() {
        return mLista;
    }
    public LiveData<List<Categoria>> getListaCategorias() {
        return mListaCategorias;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public LiveData<String> getStockOrden() {
        return mStockOrden;
    }
    public LiveData<Integer> getIdCategoria() {
        return mIdCategoria;
    }
    public void cambiarCategoriaId(Integer id) {
        mIdCategoria.setValue(id);
    }
    public void cambiarEstado(int estado) {
        mEstado.setValue(estado);
    }
    public void cambiarStockOrden(String stockOrden) {
        mStockOrden.setValue(stockOrden);
    }
    public void cargarSpinnerCategorias() {
        ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

        Call<List<Categoria>> call = servicio.filtrarPorEstado(1);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    Categoria primeraCategoria = new Categoria(0, null, "remove_24px",
                            application.getResources().getColor(R.color.gray_light1),
                            "Todas las categorias", "...", 1);

                    List<Categoria> copia = new ArrayList<>(response.body());

                    copia.add(0, primeraCategoria);

                    mListaCategorias.postValue(copia);
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
    public void cargarLista(String nombre) {
        ApiServiceProductos servicio = ApiClient.getApiServiceProductos();
        Integer idCategoria;

        if (nombre.isBlank()) { nombre = null; }

        if (mIdCategoria.getValue() != null && mIdCategoria.getValue()==0) { idCategoria = null; } else {
            idCategoria = mIdCategoria.getValue();
        }

        Call<List<Producto>> call = servicio.obtenerProductos(nombre, idCategoria, mEstado.getValue(), mStockOrden.getValue());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful()) {
                    mLista.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar los productos");
                    Log.d("CARGAR PRODUCTOS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR PRODUCTOS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR PRODUCTOS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }
}