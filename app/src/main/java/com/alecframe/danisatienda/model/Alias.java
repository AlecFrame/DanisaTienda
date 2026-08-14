package com.alecframe.danisatienda.model;

import com.alecframe.danisatienda.request.ApiClient;

import java.io.Serializable;

public class Alias implements Serializable {
    private int idAlias;
    private String valor;
    private String banco;
    private String propietario;
    private int estado;
    private final String usuario = ApiClient.USUARIO;

    public Alias() {
    }
    public Alias(int idAlias, String valor, String banco, String propietario, int estado) {
        this.idAlias = idAlias;
        this.valor = valor;
        this.banco = banco;
        this.propietario = propietario;
        this.estado = estado;
    }

    public int getIdAlias() {
        return idAlias;
    }

    public void setIdAlias(int idAlias) {
        this.idAlias = idAlias;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
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

    @Override
    public String toString() {
        return "Alias{" +
                "idAlias=" + idAlias +
                ", valor='" + valor + '\'' +
                ", banco='" + banco + '\'' +
                ", propietario='" + propietario + '\'' +
                ", estado=" + estado +
                '}';
    }
}
