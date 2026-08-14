package com.alecframe.danisatienda.model;

import java.io.Serializable;
import java.util.List;

public class Carrito implements Serializable {
    private int idCarrito;
    private double montoTotal;
    private String estado;
    private List<CarritoDetalle> carritoDetalles;
    public Carrito() {
    }
    public Carrito(int idCarrito, double montoTotal, String estado) {
        this.idCarrito = idCarrito;
        this.montoTotal = montoTotal;
        this.estado = estado;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<CarritoDetalle> getCarritoDetalles() {
        return carritoDetalles;
    }

    public void setCarritoDetalles(List<CarritoDetalle> carritoDetalles) {
        this.carritoDetalles = carritoDetalles;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "idCarrito=" + idCarrito +
                ", montoTotal=" + montoTotal +
                ", estado='" + estado + '\'' +
                ", carritoDetalles=" + carritoDetalles +
                '}';
    }
}
