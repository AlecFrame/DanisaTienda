package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Categoria;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

public interface ApiServiceCategorias {
    @GET("api/categorias") // GET /categorias
    Call<List<Categoria>> obtenerCategorias();
    @GET("api/categorias") // GET /categorias?nombre=fruta
    Call<List<Categoria>> filtrarPorNombre(
            @Query("nombre") String nombre
    );
    @GET("api/categorias") // GET /categorias?estado=1
    Call<List<Categoria>> filtrarPorEstado(
            @Query("estado") int estado
    );
    @GET("api/categorias") // GET /categorias?nombre=fruta&estado=1
    Call<List<Categoria>> filtrar(
            @Query("nombre") String nombre,
            @Query("estado") Integer estado
    );
    @GET("api/categorias/{id}") // GET /categorias/5
    Call<Categoria> obtenerCategoria(
            @Path("id") int id
    );
    @POST("api/categorias") // POST /categorias
    Call<Categoria> crearCategoria(
            @Body Categoria categoria
    );
    @Multipart
    @POST("api/categorias")
    Call<Categoria> crearCategoriaConFoto(
            @Part MultipartBody.Part foto,
            @Part("drawable") RequestBody drawable,
            @Part("color") RequestBody color,
            @Part("nombre") RequestBody nombre,
            @Part("ejemplos") RequestBody ejemplos,
            @Part("usuario") RequestBody usuario
    );
    @PUT("api/categorias/{id}") // PUT /categorias/5
    Call<Categoria> actualizarCategoria(
            @Path("id") int id,
            @Body Categoria categoria
    );
    @Multipart
    @PUT("api/categorias/{id}") // PUT /categorias/5
    Call<Categoria> actualizarCategoriaConFoto(
            @Path("id") int id,
            @Part MultipartBody.Part foto,
            @Part("drawable") RequestBody drawable,
            @Part("color") RequestBody color,
            @Part("nombre") RequestBody nombre,
            @Part("ejemplos") RequestBody ejemplos
    );
    @PATCH("api/categorias/{id}/desactivar") // PATCH /categorias/5/desactivar
    Call<Map<String, String>> desactivarCategoria(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
    @PATCH("api/categorias/{id}/activar") // PATCH /categorias/5/activar
    Call<Map<String, String>> activarCategoria(
            @Path("id") int id,
            @Body BodyUsuarioRequest bodyUsuarioRequest
    );
}
