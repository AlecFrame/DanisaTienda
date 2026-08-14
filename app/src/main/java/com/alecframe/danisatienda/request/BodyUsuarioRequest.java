package com.alecframe.danisatienda.request;

import java.io.Serializable;

public class BodyUsuarioRequest implements Serializable {
    private final String usuario = ApiClient.USUARIO;
    public BodyUsuarioRequest() {}

    public String getUsuario() {
        return usuario;
    }
}
