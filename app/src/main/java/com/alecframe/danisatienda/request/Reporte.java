package com.alecframe.danisatienda.request;

import java.io.Serializable;
import java.util.List;

public class Reporte implements Serializable {
    private Resumen resumen;
    private List<ReporteProducto> productosMasVendidos;

    public Reporte() {
    }

    public Reporte(Resumen resumen, List<ReporteProducto> productosMasVendidos) {
        this.resumen = resumen;
        this.productosMasVendidos = productosMasVendidos;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

    public List<ReporteProducto> getProductosMasVendidos() {
        return productosMasVendidos;
    }

    public void setProductosMasVendidos(List<ReporteProducto> productosMasVendidos) {
        this.productosMasVendidos = productosMasVendidos;
    }
}
