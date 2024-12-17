package com.ugb.controlesbasicos;

public class amigos {
    String _id;
    String _rev;
    String idVehiculos;
    String marca;
    String motor;
    String chasis;
    String vin;
    String combustion;
    String foto;


    public amigos(String _id, String _rev, String idVehiculos, String marca, String motor, String chasis, String vin, String combustion, String foto) {
        this._id = _id;
        this._rev = _rev;
        this.idVehiculos = idVehiculos;
        this.marca = marca;
        this.motor = motor;
        this.chasis = chasis;
        this.vin = vin;
        this.combustion = combustion;
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

    public String getIdVehiculos() {
        return idVehiculos;
    }

    public void setIdVehiculos(String idVehiculos) {
        this.idVehiculos = idVehiculos;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCombustion() {
        return combustion;
    }

    public void setCombustion(String combustion) {this.combustion = combustion;
    }
}