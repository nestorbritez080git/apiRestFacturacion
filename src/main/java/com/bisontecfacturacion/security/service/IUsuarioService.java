package com.bisontecfacturacion.security.service;

import com.bisontecfacturacion.security.model.Usuario;

public interface IUsuarioService {

    public Usuario findByUsername(String username);
}