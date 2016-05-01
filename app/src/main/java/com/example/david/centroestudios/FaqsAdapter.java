package com.example.david.centroestudios;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Creado por Hermosa Programación
 */
public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.FaqsViewHolder> {
    private List<Faqs> items;

    public static class FaqsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView pregunta;
        public TextView respuesta;

        public FaqsViewHolder(View v) {
            super(v);
            pregunta = (TextView) v.findViewById(R.id.pregunta);
            respuesta = (TextView) v.findViewById(R.id.respuesta);
        }
    }

    public FaqsAdapter(List<Faqs> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FaqsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.faqs, viewGroup, false);
        return new FaqsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FaqsViewHolder viewHolder, int i) {
        viewHolder.pregunta.setText(items.get(i).getPregunta());
        viewHolder.respuesta.setText(items.get(i).getRespuesta());
    }
}
