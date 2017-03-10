package com.aja.proyectointegrado;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Javier on 28/02/2017.
 */

public class Usuario implements Parcelable{
    String nombre;
    String email;
    String foto;
    int numViaje;
    float valoracion;

    public Usuario(String email, float valoracion, int numViaje, String nombre, String foto) {
        this.email = email;
        this.valoracion = valoracion;
        this.numViaje = numViaje;
        this.nombre = nombre;
        this.foto = foto;
    }

    public Usuario() {
    }


    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumViaje() {
        return numViaje;
    }

    public void setNumViaje(int numViaje) {
        this.numViaje = numViaje;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Usuario (Parcel in){
        nombre = in.readString();
        email = in.readString();
        foto = in.readString();
        numViaje = in.readInt();
        valoracion = in.readFloat();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(foto);
        dest.writeInt(numViaje);
        dest.writeFloat(valoracion);
    }
}
