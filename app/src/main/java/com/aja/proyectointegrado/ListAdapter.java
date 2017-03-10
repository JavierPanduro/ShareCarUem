package com.aja.proyectointegrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Javier on 23/01/2017.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Viaje> viaje;

    public ListAdapter(Context context, ArrayList<Viaje> viaje) {
        this.context = context;
        this.viaje = viaje;
    }

    @Override
    public int getCount() {
        return viaje.size();
    }

    @Override
    public Object getItem(int position) {
        return viaje.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_viaje, null);

            holder = new ViewHolder();

            holder.txtUsuario = (TextView) convertView.findViewById(R.id.txtUsuario);
            holder.etOrigen = (TextView) convertView.findViewById(R.id.etOrigen);
            holder.etDestino = (TextView) convertView.findViewById(R.id.etDestino);
            holder.etFecha = (TextView) convertView.findViewById(R.id.etFecha);
            holder.etHora = (TextView) convertView.findViewById(R.id.etHora);
            holder.etPrecio = (TextView) convertView.findViewById(R.id.etPrecio);
            holder.etDescripcion = (TextView) convertView.findViewById(R.id.etDescripcion);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtUsuario.setText("Viaje publicado por: "+viaje.get(position).usuario.getNombre());
        holder.etOrigen.setText("Origen: "+viaje.get(position).getOrigen());
        holder.etDestino.setText("Destino: "+viaje.get(position).getDestino());
        holder.etFecha.setText("Fecha: "+viaje.get(position).getFecha());
        holder.etHora.setText("Hora: "+viaje.get(position).getHora());
        holder.etPrecio.setText("Precio: "+viaje.get(position).getPrecio()+"€");
        holder.etDescripcion.setText("Nº Plazas: "+viaje.get(position).getNumeroplazas());

        return convertView;
    }

    private static class ViewHolder{
        TextView txtUsuario, etOrigen, etDestino, etFecha, etHora, etPrecio, etDescripcion;
    }
}
