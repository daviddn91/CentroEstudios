package com.example.david.centroestudios;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Creado por Hermosa Programación
 */
public class CentrosEstudiosAdapter extends RecyclerView.Adapter<CentrosEstudiosAdapter.CentrosEstudiosViewHolder> {
    private List<CentrosEstudios> items;

    public static class CentrosEstudiosViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView direccion;
        public TextView telefono;
        public TextView localidad;


        public CentrosEstudiosViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre);
            direccion = (TextView) v.findViewById(R.id.direccion);
            telefono = (TextView) v.findViewById(R.id.telefono);
            localidad = (TextView) v.findViewById(R.id.localidad);
        }
    }

    public CentrosEstudiosAdapter(List<CentrosEstudios> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CentrosEstudiosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.centros_estudios, viewGroup, false);
        return new CentrosEstudiosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CentrosEstudiosViewHolder viewHolder, int i) {
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.direccion.setText(items.get(i).getDireccion());
        viewHolder.telefono.setText(items.get(i).getTelefono());
        viewHolder.localidad.setText(items.get(i).getLocalidad());
    }
}
