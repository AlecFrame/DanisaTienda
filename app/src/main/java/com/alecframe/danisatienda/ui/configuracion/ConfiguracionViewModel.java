package com.alecframe.danisatienda.ui.configuracion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracionViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> mConexionServidor = new MutableLiveData<>();

    public ConfiguracionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Integer> getConexionServidor() {
        return mConexionServidor;
    }
    public void probarServidor() {
        ApiService api = ApiClient.getApiService();

        mConexionServidor.setValue(2);

        api.probarConexion().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("API", "Código: " + response.code());

                if(response.isSuccessful()){
                    try {
                        mToastMessage.postValue("Servidor conectado");
                        mConexionServidor.setValue(1);
                        Log.d("API", response.body().string());
                    } catch (Exception e){
                        mToastMessage.postValue("Error fallo del servidor");
                        mConexionServidor.setValue(3);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mToastMessage.postValue("Error servidor no conectado");
                mConexionServidor.setValue(0);
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }
}