package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Venta;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceVentas {
    @GET("api/ventas") // GET /ventas?estado=1
    Call<List<Venta>> obtenerVentas(
            @Query("tipoPago") String tipoPago,
            @Query("idAlias") Integer idAlias,
            @Query("estado") Integer estado,
            @Query("fechaDesde") String fechaDesde,
            @Query("fechaHasta") String fechaHasta
    );
    @GET("api/ventas/{id}") // GET /ventas/5
    Call<Venta> obtenerVenta(
            @Path("id") int id
    );
    @POST("api/ventas") // POST /ventas
    Call<Venta> crearVenta(
            @Body CrearVentaRequest ventaRequest
    );
    @PATCH("api/ventas/{id}/desactivar") // PATCH /ventas/5/desactivar
    Call<Map<String, String>> desactivarVenta(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @PATCH("api/ventas/{id}/activar") // PATCH /ventas/5/activar
    Call<Map<String, String>> activarVenta(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @GET("api/ventas/reporte") // POST /ventas/reporte
    Call<Reporte> obtenerReporte(
            @Query("fechaDesde") String fechaDesde,
            @Query("fechaHasta") String fechaHasta
    );
    @GET("api/ventas/resumen") // GET /ventas/resumen
    Call<ResumenVentas> obtenerResumen();
    @GET("api/ventas/detalles/{idCarrito}")
    Call<List<CarritoDetalle>> obtenerDetallesDeCarrito(
            @Path("idCarrito") int idCarrito
    );
}