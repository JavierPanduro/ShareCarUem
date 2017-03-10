package com.aja.proyectointegrado;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Javier on 12/01/2017.
 */

public class Viaje implements Parcelable {

    public String origen;
    public String destino;
    public String descripcion;
    public String hora;
    public String fecha;
    public String precio;
    public Usuario usuario;
    public int numeroplazas;
    public int plazasreservadas;
    public Coche coche;
    public String keyViaje;
    public String keyReserva;
    public String uidConductor;
    public String uidReserva;
    public float valoracion;

    public Viaje(Coche coche, String fecha, String descripcion, String destino, String hora, String keyReserva, String keyViaje, int numeroplazas, String origen, int plazasreservadas, String precio, String uidConductor, String uidReserva, Usuario usuario, float valoracion) {
        this.coche = coche;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.destino = destino;
        this.hora = hora;
        this.keyReserva = keyReserva;
        this.keyViaje = keyViaje;
        this.numeroplazas = numeroplazas;
        this.origen = origen;
        this.plazasreservadas = plazasreservadas;
        this.precio = precio;
        this.uidConductor = uidConductor;
        this.uidReserva = uidReserva;
        this.usuario = usuario;
        this.valoracion = valoracion;
    }

    public Viaje() {
    }

    protected Viaje(Parcel in) {
        origen = in.readString();
        destino = in.readString();
        descripcion = in.readString();
        hora = in.readString();
        fecha = in.readString();
        precio = in.readString();
        usuario = in.readParcelable(getClass().getClassLoader());
        numeroplazas = in.readInt();
        plazasreservadas = in.readInt();
        coche = in.readParcelable(getClass().getClassLoader());
        keyViaje = in.readString();
        keyReserva = in.readString();
        uidConductor = in.readString();
        uidReserva = in.readString();
        valoracion = in.readFloat();
    }

    public static final Creator<Viaje> CREATOR = new Creator<Viaje>() {
        @Override
        public Viaje createFromParcel(Parcel in) {
            return new Viaje(in);
        }

        @Override
        public Viaje[] newArray(int size) {
            return new Viaje[size];
        }
    };

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUidReserva() {
        return uidReserva;
    }

    public void setUidReserva(String uidReserva) {
        this.uidReserva = uidReserva;
    }

    public String getUidConductor() {
        return uidConductor;
    }

    public void setUidConductor(String uidConductor) {
        this.uidConductor = uidConductor;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getPlazasreservadas() {
        return plazasreservadas;
    }

    public void setPlazasreservadas(int plazasreservadas) {
        this.plazasreservadas = plazasreservadas;
    }

    public int getNumeroplazas() {
        return numeroplazas;
    }

    public void setNumeroplazas(int numeroplazas) {
        this.numeroplazas = numeroplazas;
    }

    public String getKeyViaje() {
        return keyViaje;
    }

    public void setKeyViaje(String keyViaje) {
        this.keyViaje = keyViaje;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getKeyReserva() {
        return keyReserva;
    }

    public void setKeyReserva(String keyReserva) {
        this.keyReserva = keyReserva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(descripcion);
        dest.writeString(hora);
        dest.writeString(fecha);
        dest.writeString(precio);
        dest.writeParcelable(usuario, flags);
        dest.writeInt(numeroplazas);
        dest.writeInt(plazasreservadas);
        dest.writeParcelable(coche, flags);
        dest.writeString(keyViaje);
        dest.writeString(keyReserva);
        dest.writeString(uidConductor);
        dest.writeString(uidReserva);
        dest.writeFloat(valoracion);
    }
}
