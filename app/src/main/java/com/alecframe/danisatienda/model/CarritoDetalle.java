package com.alecframe.danisatienda.model;

import java.io.Serializable;
import java.util.Objects;

public class CarritoDetalle implements Serializable {
    private int idCarritoDetalle;
    private int idCarrito;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private double costoUnitario;
    private double subtotal;
    private Carrito carrito;
    private Producto producto;
    public CarritoDetalle() {
    }

    public CarritoDetalle(int idCarritoDetalle, int idCarrito, int idProducto, int cantidad, double precioUnitario, double costoUnitario, double subtotal, Carrito carrito, Producto producto) {
        this.idCarritoDetalle = idCarritoDetalle;
        this.idCarrito = idCarrito;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
        this.carrito = carrito;
        this.producto = producto;
    }

    public int getIdCarritoDetalle() {
        return idCarritoDetalle;
    }

    public void setIdCarritoDetalle(int idCarritoDetalle) {
        this.idCarritoDetalle = idCarritoDetalle;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "CarritoDetalle{" +
                "idCarritoDetalle=" + idCarritoDetalle +
                ", idCarrito=" + idCarrito +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                ", producto=" + producto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarritoDetalle that = (CarritoDetalle) o;
        return idCarrito == that.idCarrito && idProducto == that.idProducto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrito, idProducto);
    }
}
