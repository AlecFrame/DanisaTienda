package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.CarritoDetalle;

import java.io.Serializable;
import java.util.List;

public class CrearVentaRequest implements Serializable {
    private final String tipoPago;
    private final Integer idAlias;
    private final Double montoTotal;
    private final List<CarritoDetalle> detalles;

    public CrearVentaRequest(String tipoPago, Integer idAlias, Double montoTotal,
                             List<CarritoDetalle> detalles) {
        this.tipoPago = tipoPago;
        this.idAlias = idAlias;
        this.montoTotal = montoTotal;
        this.detalles = detalles;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public Integer getIdAlias() {
        return idAlias;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public List<CarritoDetalle> getDetalles() {
        return detalles;
    }

    @Override
    public String toString() {
        return "CrearVentaRequest{" +
                "tipoPago='" + tipoPago + '\'' +
                ", idAlias=" + idAlias +
                ", montoTotal=" + montoTotal +
                ", detalles=" + detalles +
                '}';
    }
}
