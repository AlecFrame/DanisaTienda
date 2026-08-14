package com.alecframe.danisatienda.model;

import com.alecframe.danisatienda.request.ApiClient;

import java.io.Serializable;
import java.time.Instant;

public class Venta implements Serializable {
    private int idVenta;
    private int idCarrito;
    private String tipoPago;
    private int idAlias;
    private double ganancia;
    private Instant fecha;
    private Carrito carrito;
    private Alias alias;
    private int estado;
    private final String usuario = ApiClient.USUARIO;
    public Venta() {
    }
    public Venta(int idVenta, int idCarrito, String tipoPago, int idAlias, double ganancia, Instant fecha, Carrito carrito, Alias alias, int estado) {
        this.idVenta = idVenta;
        this.idCarrito = idCarrito;
        this.tipoPago = tipoPago;
        this.idAlias = idAlias;
        this.ganancia = ganancia;
        this.fecha = fecha;
        this.carrito = carrito;
        this.alias = alias;
        this.estado = estado;
    }
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public int getIdAlias() {
        return idAlias;
    }

    public void setIdAlias(int idAlias) {
        this.idAlias = idAlias;
    }

    public double getGanancia() {
        return ganancia;
    }

    public void setGanancia(double ganancia) {
        this.ganancia = ganancia;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }
}
