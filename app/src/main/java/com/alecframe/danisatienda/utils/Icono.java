package com.alecframe.danisatienda.utils;

public class Icono {
    private final String nombre;
    private final int drawable;
    private final String drawableNombre;
    public Icono(String nombre, int drawable, String drawableNombre) {
        this.nombre = nombre;
        this.drawable = drawable;
        this.drawableNombre = drawableNombre;
    }
    public String getNombre() {
        return nombre;
    }
    public int getDrawable() {
        return drawable;
    }
    @Override
    public String toString() {
        return nombre;
    }
    public String getDrawableNombre() {
        return drawableNombre;
    }
}
