package com.alecframe.danisatienda.ui.producto;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceCategorias;
import com.alecframe.danisatienda.request.ApiServiceProductos;
import com.alecframe.danisatienda.request.BodyUsuarioRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Producto> mProducto = new MutableLiveData<>();
    private final MutableLiveData<Categoria> mCategoria = new MutableLiveData<>();
    private final MutableLiveData<List<Categoria>> mListaCategorias = new MutableLiveData<>();
    private final MutableLiveData<Uri> mFotoUri = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mProcesoTerminado = new MutableLiveData<>();
    private final MutableLiveData<String> mViewMode = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>();

    public ProductoDetalleViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Producto> getProducto() {
        return mProducto;
    }
    public LiveData<Categoria> getCategoria() {
        return mCategoria;
    }
    public LiveData<List<Categoria>> getListaCategorias() {
        return mListaCategorias;
    }
    public MutableLiveData<Uri> getFotoUri() {
        return mFotoUri;
    }
    public LiveData<Boolean> getProcesoTerminado() {
        return mProcesoTerminado;
    }
    public void setCategoria(Categoria categoria) {
        mCategoria.setValue(categoria);
    }
    public void cargarSpinnerCategorias() {
        ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

        Call<List<Categoria>> call = servicio.filtrarPorEstado(1);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    mListaCategorias.postValue(response.body());
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
    public void recibirFoto(ActivityResult resultado) {
        if (resultado.getResultCode() == Activity.RESULT_OK) {
            Intent data = resultado.getData();
            Uri uri = data.getData();
            Log.d("galeria","uri: "+uri.toString());
            mFotoUri.setValue(uri);
        }
    }
    public MultipartBody.Part crearParteFoto(Uri uri) throws IOException {
        InputStream inputStream = getApplication()
                .getContentResolver()
                .openInputStream(uri);

        File file = new File( getApplication().getCacheDir(), "foto.jpg" );

        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));

        return MultipartBody.Part.createFormData("foto", file.getName(), requestFile);
    }
    public void limpiarFotoUri() {
        mFotoUri.setValue(null);
    }
    public LiveData<String> getViewMode() {
        return mViewMode;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public void recuperarDatos(Bundle b) {
        String viewMode = b.getString("viewMode", "ver");
        Producto p = b.getSerializable("producto", Producto.class);
        if (p!=null) {
            mProducto.setValue(p);
            mEstado.setValue(p.getEstado());
        }
        mViewMode.setValue(viewMode);
    }
    public void setViewMode(String viewMode) {
        mViewMode.setValue(viewMode);
    }
    public void cargarCategoria(int idCategoria) {
        ApiServiceCategorias api = ApiClient.getApiServiceCategorias();

        Call<Categoria> calls = api.obtenerCategoria(idCategoria);

        calls.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                if (response.isSuccessful()) {
                    mCategoria.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar la categoría");
                    mProcesoTerminado.postValue(true);
                }
            }
            @Override
            public void onFailure(Call<Categoria> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                mProcesoTerminado.postValue(true);
                Log.d("CARGAR CATEGORIA ERROR", "FOTO_URI: call: " + call);
                Log.d("CARGAR CATEGORIA ERROR", "FOTO_URI: throwable: " + t);
            }
        });
    }
    public void actualizarProducto(String nombre, String descripcion, int idCategoria, String unidad, String precioS, String costoCompraS, String stockS, String stockBajoS){
        if (mProducto.getValue()==null) {
            mToastMessage.setValue("El Producto no se cargo correctamente");
            return;
        }

        try {
            if (nombre.isBlank() | idCategoria<1 | precioS.isBlank() | costoCompraS.isBlank() | stockS.isBlank() | stockBajoS.isBlank()) {
                mToastMessage.setValue("Faltan campos por rellenar");
                mProcesoTerminado.setValue(true);
            }else{
                double precio;
                double costoCompra;
                int stock;
                int stockBajo;

                try {
                    precio = Double.parseDouble(precioS);
                    costoCompra = Double.parseDouble(costoCompraS);
                    stock = Integer.parseInt(stockS);
                    stockBajo = Integer.parseInt(stockBajoS);
                }catch (Exception e) {
                    mToastMessage.setValue("Datos numerales invalidos");
                    mProcesoTerminado.setValue(true);
                    return;
                }

                Producto nuevoProducto = new Producto();

                nuevoProducto.setIdProducto(mProducto.getValue().getIdProducto());
                nuevoProducto.setNombre(nombre);
                nuevoProducto.setIdCategoria(idCategoria);
                nuevoProducto.setUnidad(unidad);
                nuevoProducto.setPrecio(precio);
                nuevoProducto.setCostoCompra(costoCompra);
                nuevoProducto.setStock(stock);
                nuevoProducto.setStockBajo(stockBajo);
                nuevoProducto.setEstado(mProducto.getValue().getEstado());
                if (descripcion.isBlank()) {
                    nuevoProducto.setDescripcion("...");
                }else
                    nuevoProducto.setDescripcion(descripcion);

                ApiServiceProductos servicio = ApiClient.getApiServiceProductos();

                if (mFotoUri.getValue() != null) {
                    MultipartBody.Part foto = crearParteFoto(mFotoUri.getValue());
                    RequestBody nombreBody = RequestBody.create(nombre, MultipartBody.FORM);
                    RequestBody descripcionBody = RequestBody.create(descripcion, MultipartBody.FORM);
                    RequestBody idCategoriaBody = RequestBody.create(String.valueOf(idCategoria), MultipartBody.FORM);
                    RequestBody unidadBody = RequestBody.create(unidad, MultipartBody.FORM);
                    RequestBody precioBody = RequestBody.create(precioS, MultipartBody.FORM);
                    RequestBody costoCompraBody = RequestBody.create(costoCompraS, MultipartBody.FORM);
                    RequestBody stockBody = RequestBody.create(stockS, MultipartBody.FORM);
                    RequestBody stockBajoBody = RequestBody.create(stockBajoS, MultipartBody.FORM);

                    Call<Producto> call = servicio.actualizarProductoConFoto(
                            nuevoProducto.getIdProducto(),
                            foto,
                            nombreBody,
                            descripcionBody,
                            idCategoriaBody,
                            unidadBody,
                            precioBody,
                            costoCompraBody,
                            stockBody,
                            stockBajoBody
                    );

                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            if (response.isSuccessful()) {
                                mProducto.postValue(response.body());
                                mToastMessage.postValue("Producto actualizado");
                                mViewMode.postValue("ver");
                            } else {
                                mToastMessage.postValue("Error al actualizar el producto");
                                mProcesoTerminado.postValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            mProcesoTerminado.postValue(true);
                            Log.d("ACTUALIZAR PRODUCTO ERROR", "FOTO_URI: call: "+call);
                            Log.d("ACTUALIZAR PRODUCTO ERROR", "FOTO_URI: throwable: "+t);
                        }
                    });
                } else {
                    Log.d("ACTUALIZAR PRODUCTO", "DRAWABLESSS");

                    Call<Producto> call = servicio.actualizarProducto(nuevoProducto.getIdProducto(), nuevoProducto);
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> response) {
                            if (response.isSuccessful()) {
                                mProducto.postValue(response.body());
                                mToastMessage.postValue("Producto actualizado");
                                mViewMode.postValue("ver");
                            } else {
                                mToastMessage.postValue("Error al actualizar el producto");
                                mProcesoTerminado.postValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            mProcesoTerminado.postValue(true);
                            Log.d("ACTUALIZAR PRODUCTO ERROR", "DRAWABLE: call: "+call);
                            Log.d("ACTUALIZAR PRODUCTO ERROR", "DRAWABLE: throwable: "+t);
                        }
                    });
                }
            }
        }catch (NumberFormatException | IOException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            mProcesoTerminado.setValue(true);
            Log.d("ACTUALIZAR PRODUCTO ERROR", "ERROR: "+e);
        }
    }
    public void activarCategoria() {
        Producto producto = mProducto.getValue();
        if (producto!=null) {
            ApiServiceProductos servicio = ApiClient.getApiServiceProductos();

            Call<Map<String, String>> call = servicio.activarProducto(producto.getIdProducto(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(1);
                        mToastMessage.postValue("Producto: "+producto.getNombre()+" ha sido activado");
                    } else {
                        mToastMessage.postValue("Error al activar el producto");
                        mProcesoTerminado.setValue(true);
                        Log.d("ACTIVAR PRODUCTO", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.setValue(true);
                    Log.d("ACTIVAR PRODUCTO ERROR", "DRAWABLE: call: " + call);
                    Log.d("ACTIVAR PRODUCTO ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo activar el producto");
            mProcesoTerminado.setValue(true);
        }
    }
    public void desactivarCategoria() {
        Producto producto = mProducto.getValue();
        if (producto!=null) {
            ApiServiceProductos servicio = ApiClient.getApiServiceProductos();

            Call<Map<String, String>> call = servicio.desactivarProducto(producto.getIdProducto(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(0);
                        mToastMessage.postValue("Producto: "+producto.getNombre()+" ha sido desctivado");
                    } else {
                        mToastMessage.postValue("Error al desctivar el producto");
                        mProcesoTerminado.setValue(true);
                        Log.d("DESACTIVAR PRODUCTO", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.setValue(true);
                    Log.d("DESACTIVAR PRODUCTO ERROR", "DRAWABLE: call: " + call);
                    Log.d("DESACTIVAR PRODUCTO ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo desactivar el producto");
            mProcesoTerminado.setValue(true);
        }
    }
}