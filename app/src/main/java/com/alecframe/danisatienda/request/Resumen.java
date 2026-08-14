package com.alecframe.danisatienda.request;

import java.io.Serializable;

public class Resumen implements Serializable {
    double totalVentas;
    double totalGastos;
    double ganancia;
    int cantidadVentas;
    public Resumen() {
    }
    public Resumen(double totalVentas, double totalGastos, double ganancia, int cantidadVentas) {
        this.totalVentas = totalVentas;
        this.totalGastos = totalGastos;
        this.ganancia = ganancia;
        this.cantidadVentas = cantidadVentas;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getGanancia() {
        return ganancia;
    }

    public void setGanancia(double ganancia) {
        this.ganancia = ganancia;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }
}
