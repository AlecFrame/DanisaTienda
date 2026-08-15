package com.alecframe.danisatienda.ui.categorias;

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

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceCategorias;
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

public class CategoriaDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<String> mViewMode = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mBack = new MutableLiveData<>();
    private final MutableLiveData<Integer> mColor = new MutableLiveData<>();
    private final MutableLiveData<Categoria> mCategoria = new MutableLiveData<>();
    private final MutableLiveData<Uri> mFotoUri = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mProcesoTerminado = new MutableLiveData<>();
    private final MutableLiveData<Integer> mEstado = new MutableLiveData<>();
    public CategoriaDetalleViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<String> getViewMode() {
        return mViewMode;
    }
    public LiveData<Boolean> getBack() {
        return mBack;
    }
    public LiveData<Integer> getColor() {
        return mColor;
    }
    public MutableLiveData<Categoria> getCategoria() {
        return mCategoria;
    }
    public MutableLiveData<Uri> getFotoUri() {
        return mFotoUri;
    }
    public LiveData<Boolean> getProcesoTerminado() {
        return mProcesoTerminado;
    }
    public LiveData<Integer> getEstado() {
        return mEstado;
    }
    public void recuperarDatos(Bundle b) {
        String viewMode = b.getString("viewMode", "ver");
        Categoria c = b.getSerializable("categoria", Categoria.class);
        if (c!=null) {
            mCategoria.setValue(c);
            mEstado.setValue(c.getEstado());
        }
        mViewMode.setValue(viewMode);
    }
    public void setViewMode(String viewMode) {
        mViewMode.setValue(viewMode);
    }
    public void setColor(int color) {
        mColor.setValue(color);
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
    public void crearCategoria(String drawable, int color, String nombre, String ejemplos){
        try {
            if (nombre.isBlank()) {
                mToastMessage.setValue("Debe nombrar la categoría");
                mProcesoTerminado.setValue(true);
            }else{
                Categoria nuevaCategoria = new Categoria();

                nuevaCategoria.setDrawable(drawable);
                nuevaCategoria.setColor(color);
                nuevaCategoria.setNombre(upperCaseFirstChar(nombre));
                if (ejemplos.isBlank()) {
                    nuevaCategoria.setEjemplos("...");
                }else
                    nuevaCategoria.setEjemplos(ejemplos);

                ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

                if (mFotoUri.getValue() != null) {
                    MultipartBody.Part foto = crearParteFoto(mFotoUri.getValue());
                    RequestBody drawableBody = RequestBody.create(drawable, MultipartBody.FORM);
                    RequestBody colorBody = RequestBody.create(String.valueOf(color), MultipartBody.FORM);
                    RequestBody nombreBody = RequestBody.create(nombre, MultipartBody.FORM);
                    RequestBody ejemplosBody = RequestBody.create(ejemplos, MultipartBody.FORM);
                    RequestBody usuario = RequestBody.create(ApiClient.USUARIO, MultipartBody.FORM);

                    Call<Categoria> call = servicio.crearCategoriaConFoto(
                            foto,
                            drawableBody,
                            colorBody,
                            nombreBody,
                            ejemplosBody,
                            usuario
                    );

                    Log.d("CREAR CATEGORIA", "URI NULL");

                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if (response.isSuccessful()) {
                                mCategoria.postValue(response.body());
                                mToastMessage.postValue("Categoría creada");
                                mBack.postValue(true);
                            } else {
                                mToastMessage.postValue("Error al crear la categoría");
                                mProcesoTerminado.postValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            mProcesoTerminado.postValue(true);
                            Log.d("CREAR CATEGORIA ERROR", "FOTO_URI: call: "+call);
                            Log.d("CREAR CATEGORIA ERROR", "FOTO_URI: throwable: "+t);
                        }
                    });
                } else {
                    Log.d("CREAR CATEGORIA", "DRAWABLESSS");

                    Call<Categoria> call = servicio.crearCategoria(nuevaCategoria);
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if (response.isSuccessful()) {
                                mCategoria.postValue(response.body());
                                mToastMessage.postValue("Categoría creada");
                                mBack.postValue(true);
                            } else {
                                mToastMessage.postValue("Error al crear la categoría");
                                mProcesoTerminado.postValue(true);
                            }
                        }
                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            mProcesoTerminado.postValue(true);
                            Log.d("CREAR CATEGORIA ERROR", "DRAWABLE: call: "+call);
                            Log.d("CREAR CATEGORIA ERROR", "DRAWABLE: throwable: "+t);
                        }
                    });
                }
            }
        }catch (NumberFormatException | IOException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            mProcesoTerminado.setValue(true);
            Log.d("CREAR CATEGORIA ERROR", "ERROR: "+e);
        }
    }
    public void actualizarCategoria(String drawable, int color, String nombre, String ejemplos){
        if (mCategoria.getValue()==null) {
            mToastMessage.setValue("La categoría no se cargo correctamente");
            return;
        }

        try {
            if (nombre.isBlank()) {
                mToastMessage.setValue("Debe tener nombre la categoría");
            }else{
                Categoria nuevaCategoria = new Categoria();

                nuevaCategoria.setIdCategoria(mCategoria.getValue().getIdCategoria());
                nuevaCategoria.setDrawable(drawable);
                nuevaCategoria.setColor(color);
                nuevaCategoria.setNombre(upperCaseFirstChar(nombre));
                nuevaCategoria.setEstado(mCategoria.getValue().getEstado());
                if (ejemplos.isBlank()) {
                    nuevaCategoria.setEjemplos("...");
                }else
                    nuevaCategoria.setEjemplos(ejemplos);

                Log.d("ACTUALIZAR CATEGORIA", "FotoUri: "+mFotoUri.getValue());
                Log.d("ACTUALIZAR CATEGORIA", "Drawable: "+drawable);
                Log.d("ACTUALIZAR CATEGORIA", "Color: "+color);
                Log.d("ACTUALIZAR CATEGORIA", "Nombre: "+upperCaseFirstChar(nombre));
                Log.d("ACTUALIZAR CATEGORIA", "Ejemplos: "+ejemplos);

                ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

                if (mFotoUri.getValue() != null) {
                    MultipartBody.Part foto = crearParteFoto(mFotoUri.getValue());
                    RequestBody drawableBody = RequestBody.create(drawable, MultipartBody.FORM);
                    RequestBody colorBody = RequestBody.create(String.valueOf(color), MultipartBody.FORM);
                    RequestBody nombreBody = RequestBody.create(nombre, MultipartBody.FORM);
                    RequestBody ejemplosBody = RequestBody.create(ejemplos, MultipartBody.FORM);

                    Call<Categoria> call = servicio.actualizarCategoriaConFoto(
                            nuevaCategoria.getIdCategoria(),
                            foto,
                            drawableBody,
                            colorBody,
                            nombreBody,
                            ejemplosBody
                    );

                    Log.d("ACTUALIZAR CATEGORIA", "URI NULL");

                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if (response.isSuccessful()) {
                                mCategoria.postValue(response.body());
                                mToastMessage.postValue("Categoría actualizada");
                                mViewMode.postValue("ver");
                            } else {
                                mToastMessage.postValue("Error al actualizar la categoría");
                            }
                        }
                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            Log.d("ACTUALIZAR CATEGORIA ERROR", "FOTO_URI: call: "+call);
                            Log.d("ACTUALIZAR CATEGORIA ERROR", "FOTO_URI: throwable: "+t);
                        }
                    });
                } else {
                    Log.d("ACTUALIZAR CATEGORIA", "DRAWABLESSS");

                    Call<Categoria> call = servicio.actualizarCategoria(nuevaCategoria.getIdCategoria(), nuevaCategoria);
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<Categoria> call, Response<Categoria> response) {
                            if (response.isSuccessful()) {
                                mCategoria.postValue(response.body());
                                mToastMessage.postValue("Categoría actualizada");
                                mViewMode.postValue("ver");
                            } else {
                                mToastMessage.postValue("Error al actualizar la categoría");
                            }
                        }
                        @Override
                        public void onFailure(Call<Categoria> call, Throwable t) {
                            mToastMessage.postValue("Error del servidor.");
                            Log.d("ACTUALIZAR CATEGORIA ERROR", "DRAWABLE: call: "+call);
                            Log.d("ACTUALIZAR CATEGORIA ERROR", "DRAWABLE: throwable: "+t);
                        }
                    });
                }
            }
        }catch (NumberFormatException | IOException e){
            mToastMessage.setValue("Algunos datos son invalidos");
            Log.d("ACTUALIZAR CATEGORIA ERROR", "ERROR: "+e);
        }
    }
    public void activarCategoria() {
        Categoria categoria = mCategoria.getValue();
        if (categoria!=null) {
            ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

            Call<Map<String, String>> call = servicio.activarCategoria(categoria.getIdCategoria(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(1);
                        mToastMessage.postValue("Categoría: "+categoria.getNombre()+" ha sido activada");
                    } else {
                        mToastMessage.postValue("Error al activar la categoría");
                        mProcesoTerminado.postValue(true);
                        Log.d("ACTIVAR CATEGORÍAS", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.postValue(true);
                    Log.d("ACTIVAR CATEGORIA ERROR", "DRAWABLE: call: " + call);
                    Log.d("ACTIVAR CATEGORIA ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo activar la categoría");
            mProcesoTerminado.postValue(true);
        }
    }
    public void desactivarCategoria() {
        Categoria categoria = mCategoria.getValue();
        if (categoria!=null) {
            ApiServiceCategorias servicio = ApiClient.getApiServiceCategorias();

            Call<Map<String, String>> call = servicio.desactivarCategoria(categoria.getIdCategoria(), new BodyUsuarioRequest());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful()) {
                        mEstado.postValue(0);
                        mToastMessage.postValue("Categoría: "+categoria.getNombre()+" ha sido desactivada");
                    } else {
                        mToastMessage.postValue("Error al desactivadar la categoría");
                        mProcesoTerminado.postValue(true);
                        Log.d("DESACTIVAR CATEGORÍAS", "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    mToastMessage.postValue("Error del servidor.");
                    mProcesoTerminado.postValue(true);
                    Log.d("DESACTIVAR CATEGORIA ERROR", "DRAWABLE: call: " + call);
                    Log.d("DESACTIVAR CATEGORIA ERROR", "DRAWABLE: throwable: " + t);
                }
            });
        }else {
            mToastMessage.postValue("No se pudo desactivadar la categoría");
            mProcesoTerminado.postValue(true);
        }
    }
    public void limpiarFotoUri() {
        mFotoUri.setValue(null);
    }
    public String upperCaseFirstChar(String string) {
        char primerLetter = string.toUpperCase().charAt(0);
        char[] result = string.toLowerCase().toCharArray();
        result[0] = primerLetter;

        return  String.valueOf(result);
    }
}