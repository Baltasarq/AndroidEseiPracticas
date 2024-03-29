package com.devbaltasarq.androideseipracticas.core;

public class Practica {
    public Practica(String asignatura, String trabajo) {
        this.asignatura = asignatura.trim().toUpperCase();
        this.trabajo = trabajo.trim().replace( ':', ';' );
    }

    public String getAsignatura() {
        return asignatura;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public String toString()
    {
        return this.getAsignatura() + ": " + this.getTrabajo();
    }

    private String asignatura;
    private String trabajo;
}
