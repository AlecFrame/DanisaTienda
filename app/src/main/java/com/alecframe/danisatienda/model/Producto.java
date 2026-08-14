package com.alecframe.danisatienda.model;

import com.alecframe.danisatienda.request.ApiClient;

import java.io.Serializable;
import java.util.Objects;

public class Producto implements Serializable {
    private int idProducto;
    private String foto;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private double precio;
    private double costoCompra;
    private String unidad;
    private int stock;
    private int stockBajo;
    private int estado;
    private Categoria categoria;
    private final String usuario = ApiClient.USUARIO;
    public Producto() {
    }

    public Producto(int idProducto, String foto, String nombre, String descripcion, int idCategoria, double precio, double costoCompra, String unidad, int stock, int stockBajo, int estado, Categoria categoria) {
        this.idProducto = idProducto;
        this.foto = foto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.precio = precio;
        this.costoCompra = costoCompra;
        this.unidad = unidad;
        this.stock = stock;
        this.stockBajo = stockBajo;
        this.estado = estado;
        this.categoria = categoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockBajo() {
        return stockBajo;
    }

    public void setStockBajo(int stockBajo) {
        this.stockBajo = stockBajo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getUsuario() {
        return usuario;
    }

    public double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(double costoCompra) {
        this.costoCompra = costoCompra;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", foto='" + foto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idCategoria=" + idCategoria +
                ", precio=" + precio +
                ", unidad='" + unidad + '\'' +
                ", stock=" + stock +
                ", stockBajo=" + stockBajo +
                ", estado=" + estado +
                ", categoria=" + categoria +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idProducto);
    }
}
