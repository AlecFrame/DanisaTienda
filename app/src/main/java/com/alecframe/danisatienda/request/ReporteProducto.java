package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Producto;

import java.io.Serializable;

public class ReporteProducto implements Serializable {
    private int idProducto;
    private double cantidadVendida;
    private Producto producto;

    public ReporteProducto() {
    }

    public ReporteProducto(int idProducto, double cantidadVendida, Producto producto) {
        this.idProducto = idProducto;
        this.cantidadVendida = cantidadVendida;
        this.producto = producto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(double cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
