package com.alecframe.danisatienda.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class Auditoria implements Serializable {
    private int idAuditoria;
    private int idEntidad;
    private String entidad;
    private String accion;
    private String descripcion;
    private Instant fecha;
    private String datoExtra;
    private int idUsuario;
    private Usuario usuario;
    public Auditoria() {
    }
    public Auditoria(int idAuditoria, int idEntidad, String entidad, String accion, String descripcion, Instant fecha, String datoExtra, int idUsuario, Usuario usuario) {
        this.idAuditoria = idAuditoria;
        this.idEntidad = idEntidad;
        this.entidad = entidad;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.datoExtra = datoExtra;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDatoExtra() {
        return datoExtra;
    }

    public void setDatoExtra(String datoExtra) {
        this.datoExtra = datoExtra;
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "idAuditoria=" + idAuditoria +
                ", idEntidad=" + idEntidad +
                ", entidad='" + entidad + '\'' +
                ", accion='" + accion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", usuario='" + usuario + '\'' +
                ", datoExtra='" + datoExtra + '\'' +
                '}';
    }
}
