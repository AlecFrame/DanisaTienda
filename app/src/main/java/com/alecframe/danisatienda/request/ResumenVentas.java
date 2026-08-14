package com.alecframe.danisatienda.request;

import java.io.Serializable;

public class ResumenVentas implements Serializable {
    private double totalVentasHoy;
    private double totalVentasSemana;
    public ResumenVentas() {}
    public ResumenVentas(double totalVentasHoy, double totalVentasSemana) {
        this.totalVentasHoy = totalVentasHoy;
        this.totalVentasSemana = totalVentasSemana;
    }

    public double getTotalVentasHoy() {
        return totalVentasHoy;
    }

    public void setTotalVentasHoy(double totalVentasHoy) {
        this.totalVentasHoy = totalVentasHoy;
    }

    public double getTotalVentasSemana() {
        return totalVentasSemana;
    }

    public void setTotalVentasSemana(double totalVentasSemana) {
        this.totalVentasSemana = totalVentasSemana;
    }
}
