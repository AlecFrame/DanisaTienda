package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Alias;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceAlias {
    @GET("api/alias") // GET /alias?estado=1
    Call<List<Alias>> obtenerTodosLosAlias(
            @Query("estado") Integer estado
    );
    @GET("api/alias/{id}") // GET /alias/5
    Call<Alias> obtenerAlias(
            @Path("id") int id
    );
    @POST("api/alias") // POST /alias
    Call<Alias> crearAlias(
            @Body Alias alias
    );
    @PUT("api/alias/{id}") // PUT /alias/5
    Call<Alias> actualizarAlias(
            @Path("id") int id,
            @Body Alias alias
    );
    @PATCH("api/alias/{id}/desactivar") // PATCH /alias/5/desactivar
    Call<Map<String, String>> desactivarAlias(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @PATCH("api/alias/{id}/activar") // PATCH /alias/5/activar
    Call<Map<String, String>> activarAlias(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
}