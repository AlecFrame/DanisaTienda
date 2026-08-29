package com.alecframe.danisatienda.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alecframe.danisatienda.utils.Configuracion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.Strictness;

import java.time.Instant;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL = "http://192.168.56.1:3000";
    public static String USUARIO = "No Registrado";
    // public static String BASE_URL = "http://10.0.2.2:3000/";

    private static Retrofit retrofit;

    public static ApiService getApiService(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiService.class);
    }
    public static ApiServiceCategorias getApiServiceCategorias(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceCategorias.class);
    }
    public static ApiServiceProductos getApiServiceProductos(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceProductos.class);
    }
    public static ApiServiceAlias getApiServiceAlias(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceAlias.class);
    }
    public static ApiServiceVentas getApiServiceVentas(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceVentas.class);
    }
    public static ApiServiceAuditorias getApiServiceAuditorias(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceAuditorias.class);
    }
    public static ApiServiceGastos getApiServiceGastos(Context context) {
        generarRetrofit(context);
        return retrofit.create(ApiServiceGastos.class);
    }

    public static void generarRetrofit(Context context) {
        if(retrofit == null){
            reiniciarRetrofit(context);
        }
    }

    public static void reiniciarRetrofit(Context context) {
        Gson gson = new GsonBuilder()
                .setStrictness(Strictness.LENIENT)
                .registerTypeAdapter(
                        Instant.class,
                        (JsonDeserializer<Instant>) (json, type, jsonContext) ->
                                Instant.parse(json.getAsString())
                )
                .create();

        Interceptor authInterceptor = chain -> {
            Request request = chain.request();

            if (!esRutaPublica(request)) {
                String token = obtenerToken(context);

                if (token != null && !token.isEmpty()) {
                    request = request.newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                }
            }

            return chain.proceed(request);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(gson)
                )
                .build();
    }

    private static boolean esRutaPublica(Request request) {
        String path = request.url().encodedPath();

        return path.startsWith("/api/public")
                || path.startsWith("/api/auth")
                || path.equals("/");
    }

    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //Agregue el Baerer para no tener que configuralo cada vez que lo llamamos
        editor.putString("token", token);
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

    public static void guardarConfiguracion(Context context, Configuracion configuracion) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("urlServer", configuracion.getUrlServer());
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
        if (urlServer != null) {
            BASE_URL = urlServer;
        }
        reiniciarRetrofit(context);
    }
    public static void eliminarConfiguraciones(Context context) {
        SharedPreferences sp = context.getSharedPreferences("configuracion.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
