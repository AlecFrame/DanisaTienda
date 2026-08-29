package com.alecframe.danisatienda.request;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/")
    Call<ResponseBody> probarConexion();

    @POST("api/auth/login")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest loginRequest);
}