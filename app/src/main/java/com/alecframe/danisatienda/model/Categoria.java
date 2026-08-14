package com.alecframe.danisatienda.model;

import com.alecframe.danisatienda.request.ApiClient;

import java.io.Serializable;

public class Categoria implements Serializable {
    private int idCategoria;
    private String foto;
    private String drawable;
    private int color;
    private String nombre;
    private String ejemplos;
    private int estado;
    private final String usuario = ApiClient.USUARIO;
    public Categoria() {
    }
    public Categoria(int idCategoria, String foto, String drawable, int color, String nombre, String ejemplos, int estado) {
        this.idCategoria = idCategoria;
        this.foto = foto;
        this.drawable = drawable;
        this.color = color;
        this.nombre = nombre;
        this.ejemplos = ejemplos;
        this.estado = estado;
    }
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEjemplos() {
        return ejemplos;
    }

    public void setEjemplos(String ejemplos) {
        this.ejemplos = ejemplos;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "idCategoria=" + idCategoria +
                ", foto='" + foto + '\'' +
                ", drawable='" + drawable + '\'' +
                ", color=" + color +
                ", nombre='" + nombre + '\'' +
                ", ejemplos='" + ejemplos + '\'' +
                ", estado=" + estado +
                '}';
    }
}
