package com.alecframe.danisatienda.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.alecframe.danisatienda.utils.Configuracion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.Strictness;

import java.time.Instant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "http://192.168.56.1:3000";
    public static String USUARIO = "No Registrado";
    // public static String BASE_URL = "http://10.0.2.2:3000/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        generarRetrofit();
        return retrofit.create(ApiService.class);
    }
    public static ApiServiceCategorias getApiServiceCategorias() {
        generarRetrofit();
        return retrofit.create(ApiServiceCategorias.class);
    }
    public static ApiServiceProductos getApiServiceProductos() {
        generarRetrofit();
        return retrofit.create(ApiServiceProductos.class);
    }
    public static ApiServiceAlias getApiServiceAlias() {
        generarRetrofit();
        return retrofit.create(ApiServiceAlias.class);
    }
    public static ApiServiceVentas getApiServiceVentas() {
        generarRetrofit();
        return retrofit.create(ApiServiceVentas.class);
    }
    public static ApiServiceAuditorias getApiServiceAuditorias() {
        generarRetrofit();
        return retrofit.create(ApiServiceAuditorias.class);
    }
    public static ApiServiceGastos getApiServiceGastos() {
        generarRetrofit();
        return retrofit.create(ApiServiceGastos.class);
    }

    public static void generarRetrofit() {
        if(retrofit == null){
            reiniciarRetrofit();
        }
    }

    public static void reiniciarRetrofit() {
        Gson gson = new GsonBuilder()
                .setStrictness(Strictness.LENIENT)
                .registerTypeAdapter(
                        Instant.class,
                        (JsonDeserializer<Instant>) (json, type, context) ->
                                Instant.parse(json.getAsString())
                )
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(gson))
                .build();
    }

    public static void guardarConfiguracion(Context context, Configuracion configuracion) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("urlServer", configuracion.getUrlServer());
        editor.putString("usuario", configuracion.getUsuario());
        editor.apply();

        aplicarConfiguracion(context);
    }
    public static Configuracion obtenerConfiguracion(Context context) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);

        return new Configuracion(
                sp.getString("urlServer", null),
                sp.getString("usuario", null)
        );
    }
    public static void aplicarConfiguracion(Context context) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);

        String urlServer = sp.getString("urlServer", null);
        String usuario = sp.getString("usuario", null);

        if (urlServer!=null | usuario!=null) {
            if (urlServer != null) {
                BASE_URL = urlServer;
            }
            if (usuario != null) {
                USUARIO = usuario;
            }

            reiniciarRetrofit();
        }
    }
    public static void eliminarConfiguraciones(Context context) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //Agregue el Baerer para no tener que configuralo cada vez que lo llamamos
        editor.putString("token", "Bearer "+token);
        editor.apply();
    }
    public static String obtenerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
    public static void eliminarCredenciales(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
