package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Gasto;

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

public interface ApiServiceGastos {
    @GET("api/gastos") // GET /gastos?estado=1
    Call<List<Gasto>> listarGastos(
            @Query("descripcion") String descripcion,
            @Query("categoria") String categoria,
            @Query("pagado") String pagado,
            @Query("estado") Integer estado,
            @Query("fechaDesde") String fechaDesde,
            @Query("fechaHasta") String fechaHasta
    );
    @GET("api/gastos/{id}") // GET /gastos/5
    Call<Gasto> obtenerGasto(
            @Path("id") int id
    );
    @POST("api/gastos") // POST /gastos
    Call<Gasto> crearGasto(
            @Body Gasto gasto
    );
    @PUT("api/gastos/{id}") // PUT /gastos/5
    Call<Gasto> actualizarGasto(
            @Path("id") int id,
            @Body Gasto gasto
    );
    @PATCH("api/gastos/{id}/desactivar") // PATCH /gastos/5/desactivar
    Call<Map<String, String>> desactivarGasto(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @PATCH("api/gastos/{id}/activar") // PATCH /gastos/5/activar
    Call<Map<String, String>> activarGasto(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
}