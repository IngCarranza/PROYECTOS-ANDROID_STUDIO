package com.ugb.controlesbasicos;

public class amigos {
    String _id;
    String _rev;
    String idPeliculas;
    String titulo;
    String sinopsis;
    String duracion;
    String actor;
    String foto;


    public amigos(String _id, String _rev, String idPeliculas, String titulo, String sinopsis, String duracion, String actor, String foto) {
        this._id = _id;
        this._rev = _rev;
        this.idPeliculas = idPeliculas;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.actor = actor;
        this.foto = foto;

    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String get_rev() {
        return _rev;
    }
    public void set_rev(String _rev) {
        this._rev = _rev;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdPeliculas() {
        return idPeliculas;
    }

    public void setIdPeliculas(String idPeliculas) {
        this.idPeliculas = idPeliculas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

}