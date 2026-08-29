package com.alecframe.danisatienda.request;

import com.alecframe.danisatienda.model.Usuario;

public class LoginResponse {
    private String mensaje;
    private String token;
    private Usuario usuario;

    public String getMensaje() {
        return mensaje;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}