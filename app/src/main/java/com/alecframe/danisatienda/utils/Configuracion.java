package com.alecframe.danisatienda.utils;

import java.io.Serializable;

public class Configuracion implements Serializable {
    private String urlServer;
    private String usuario;

    public Configuracion(String urlServer, String usuario) {
        this.urlServer = urlServer;
        this.usuario = usuario;
    }

    public String getUrlServer() {
        return urlServer;
    }

    public void setUrlServer(String urlServer) {
        this.urlServer = urlServer;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "urlServer='" + urlServer + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
