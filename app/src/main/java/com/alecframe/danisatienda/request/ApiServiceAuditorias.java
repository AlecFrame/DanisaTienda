package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Auditoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceAuditorias {
    @GET("api/auditorias") // GET /auditorias?
    Call<List<Auditoria>> obtenerAuditorias(
            @Query("entidad") String entidad,
            @Query("accion") String accion,
            @Query("fechaDesde") String fechaDesde,
            @Query("fechaHasta") String fechaHasta,
            @Query("usuario") String usuario
    );
    @GET("api/auditorias/recientes") // GET /auditorias?
    Call<List<Auditoria>> listarAuditoriasRecientes();
    @GET("api/auditorias/{id}") // GET /auditorias/5
    Call<Auditoria> obtenerAuditoria(
            @Path("id") int id
    );
    @POST("api/auditorias") // POST /auditorias
    Call<Auditoria> crearAuditoria(
            @Body Auditoria auditoria
    );
    @PUT("api/auditorias/{id}") // PUT /auditorias/5
    Call<Auditoria> actualizarAuditoria(
            @Path("id") int id,
            @Body Auditoria auditoria
    );
}