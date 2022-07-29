package com.example.juegohormigas;

import java.io.Serializable;

public class Jugador implements Serializable, Comparable<Jugador> {

    private String uId;
    private  String email;
    private  String nombres;
    private  String password;
    private int puntaje;
    private String edad;
    private String fecha;
    private String pais;
    private  String imagen;

    public Jugador(String uId, String email, String nombres, String password, int puntaje) {
        this.uId = uId;
        this.email = email;
        this.nombres = nombres;
        this.password = password;
        this.puntaje = puntaje;
    }

    public Jugador(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "uId='" + uId + '\'' +
                ", email='" + email + '\'' +
                ", nombres='" + nombres + '\'' +
                ", password='" + password + '\'' +
                ", puntaje=" + puntaje +
                '}';
    }

    @Override
    public int compareTo(Jugador o) {


        Jugador jugador =  o;
        return Integer.compare( this.getPuntaje(), ((Jugador) o).getPuntaje());
    }
}
