package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceProductos {
    @GET("api/productos") // GET /productos?nombre=fruta&idCategoria=1&estado=1&ordenStock=desc
    Call<List<Producto>> obtenerProductos(
            @Query("nombre") String nombre,
            @Query("idCategoria") Integer idCategoria,
            @Query("estado") Integer estado,
            @Query("ordenStock") String ordenStock
    );
    @GET("api/productos/stock_bajos") // GET /productos/stockBajos
    Call<List<Producto>> obtenerProductosStockBajo();
    @GET("api/productos/{id}") // GET /productos/5
    Call<Producto> obtenerProducto(
            @Path("id") int id
    );
    @POST("api/productos") // POST /productos
    Call<Producto> crearProducto(
            @Body Producto producto
    );
    @Multipart
    @POST("api/productos")
    Call<Producto> crearProductoConFoto(
            @Part MultipartBody.Part foto,
            @Part("nombre") RequestBody nombre,
            @Part("descripcion") RequestBody descripcion,
            @Part("idCategoria") RequestBody idCategoria,
            @Part("unidad") RequestBody unidad,
            @Part("precio") RequestBody precio,
            @Part("costoCompra") RequestBody costoCompra,
            @Part("stock") RequestBody stock,
            @Part("stockBajo") RequestBody stockBajo,
            @Part("usuario") RequestBody usuario
    );
    @PUT("api/productos/{id}") // PUT /productos/5
    Call<Producto> actualizarProducto(
            @Path("id") int id,
            @Body Producto producto
    );
    @Multipart
    @PUT("api/productos/{id}")
    Call<Producto> actualizarProductoConFoto(
            @Path("id") int id,
            @Part MultipartBody.Part foto,
            @Part("nombre") RequestBody nombre,
            @Part("descripcion") RequestBody descripcion,
            @Part("idCategoria") RequestBody idCategoria,
            @Part("unidad") RequestBody unidad,
            @Part("precio") RequestBody precio,
            @Part("costoCompra") RequestBody costoCompra,
            @Part("stock") RequestBody stock,
            @Part("stockBajo") RequestBody stockBajo
    );
    @PATCH("api/productos/{id}/desactivar") // PATCH /productos/5/desactivar
    Call<Map<String, String>> desactivarProducto(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @PATCH("api/productos/{id}/activar") // PATCH /productos/5/activar
    Call<Map<String, String>> activarProducto(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
}