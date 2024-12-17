package com.ugb.controlesbasicos;

// Persona.java
public class Persona {
    private String nombre;
    private String edad;
    private String correo;
    private String profesion;
    private String trabajoProfesional;
    private int foto;

    public Persona(String nombre, String edad, String correo, String profesion, String trabajoProfesional,int foto) {
        this.nombre = nombre;
        this.edad = edad;
        this.correo = correo;
        this.profesion = profesion;
        this.trabajoProfesional = trabajoProfesional;
        this.foto = foto;
    }

    public String getNombre() { return nombre; }
    public String getEdad() { return edad; }
    public String getCorreo() { return correo; }
    public String getProfesion() { return profesion; }
    public String getTrabajoProfesional() { return trabajoProfesional; }
    public int getFoto() { return foto; }
}
