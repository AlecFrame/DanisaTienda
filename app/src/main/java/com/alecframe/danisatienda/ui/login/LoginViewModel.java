package com.alecframe.danisatienda.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.MainActivity;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiService;
import com.alecframe.danisatienda.request.LoginRequest;
import com.alecframe.danisatienda.request.LoginResponse;
import com.alecframe.danisatienda.utils.Configuracion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> mToastMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getToastMessage() {
        if (mToastMessage==null) {
            mToastMessage = new MutableLiveData<>();
        }
        return mToastMessage;
    }

    public void iniciarSesion(String email, String clave) {
        if (email.isBlank() || clave.isBlank()) {
            mToastMessage.postValue("Complete todos los campos");
            return;
        }
        ApiService servicio = ApiClient.getApiService(getApplication());
        LoginRequest request = new LoginRequest(email, clave);
        Call<LoginResponse> call = servicio.iniciarSesion(request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    ApiClient.USUARIO = response.body().getUsuario().getNombre();
                    ApiClient.guardarToken(getApplication(), token);

                    Intent i = new Intent(getApplication(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(i);
                } else {
                    Log.d("LOG_LOGIN_ERROR", "Código: " + response.code());
                    mToastMessage.postValue("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("LOG_LOGIN_FAILURE", t.getMessage());
                mToastMessage.postValue("Fallo del Callback en el LoginViewModel");
            }
        });
    }
}
