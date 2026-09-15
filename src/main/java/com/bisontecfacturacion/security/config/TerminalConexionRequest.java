package com.bisontecfacturacion.security.config;

public class TerminalConexionRequest {
    private int numeroTerminal;
    private String ip;
    private String nombreEquipo;

    public int getNumeroTerminal() {
        return numeroTerminal;
    }

    public void setNumeroTerminal(int numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }
}
