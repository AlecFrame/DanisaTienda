package com.alecframe.danisatienda.utils;

public class SpinnerNDC {
    private String nombre;
    private int drawable;
    private int color;
    public SpinnerNDC(String nombre, int drawable, int color) {
        this.nombre = nombre;
        this.drawable = drawable;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
