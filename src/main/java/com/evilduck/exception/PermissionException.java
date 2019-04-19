package com.evilduck.exception;

import net.dv8tion.jda.core.Permission;

public class PermissionException extends RuntimeException {

    private final Permission[] permisions;

    public PermissionException(final Permission... permisions) {
        this.permisions = permisions;
    }

    public Permission[] getPermisions() {
        return permisions;
    }
}
