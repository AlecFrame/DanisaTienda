package com.alecframe.danisatienda.model;

import java.io.Serializable;
import java.time.Instant;

public class Gasto implements Serializable {
    private int idGasto;
    private String descripcion;
    private double monto;
    private Instant fecha;
    private String categoria;
    private String observacion;
    private String pagado;
    private int estado;
    public Gasto() {
    }
    public Gasto(int idGasto, String descripcion, double monto, Instant fecha, String categoria, String observacion, String pagado, int estado) {
        this.idGasto = idGasto;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.categoria = categoria;
        this.observacion = observacion;
        this.pagado = pagado;
        this.estado = estado;
    }

    public int getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
