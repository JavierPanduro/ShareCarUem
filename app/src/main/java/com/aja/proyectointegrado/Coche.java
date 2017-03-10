package com.aja.proyectointegrado;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Javier on 17/02/2017.
 */

public class Coche implements Parcelable{
    private String Marca;
    private int anoCoche;
    private int timeCarnet;

    public Coche(int timeCarnet, String marca, int anoCoche) {
        this.timeCarnet = timeCarnet;
        Marca = marca;
        this.anoCoche = anoCoche;
    }

    public Coche() {
    }

    protected Coche(Parcel in) {
        Marca = in.readString();
        anoCoche = in.readInt();
        timeCarnet = in.readInt();
    }

    public static final Creator<Coche> CREATOR = new Creator<Coche>() {
        @Override
        public Coche createFromParcel(Parcel in) {
            return new Coche(in);
        }

        @Override
        public Coche[] newArray(int size) {
            return new Coche[size];
        }
    };

    public int getAnoCoche() {
        return anoCoche;
    }

    public void setAnoCoche(int anoCoche) {
        this.anoCoche = anoCoche;
    }

    public int getTimeCarnet() {
        return timeCarnet;
    }

    public void setTimeCarnet(int timeCarnet) {
        this.timeCarnet = timeCarnet;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Marca);
        dest.writeInt(anoCoche);
        dest.writeInt(timeCarnet);
    }
}
