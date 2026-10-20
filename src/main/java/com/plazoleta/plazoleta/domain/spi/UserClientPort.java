package com.plazoleta.plazoleta.domain.spi;

import java.util.Optional;

/** Puerto para consultar datos de usuarios-ms. */
public interface UserClientPort {
    /** Devuelve el rol del usuario (ADMIN, OWNER, ...) o vacío si el usuario no existe. */
    Optional<String> findRole(Long userId);
}
