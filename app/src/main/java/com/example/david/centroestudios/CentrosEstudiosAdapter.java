package com.example.david.centroestudios;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        public String selectedName;
        public String selectedLatitude;
        public String selectedLongitude;
        public String selectedDireccion;
        public String selectedTelefono;
        public String selectedLocalidad;

        public View view;

        public CentrosEstudiosViewHolder(View v) {
            super(v);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    System.out.println("Nombre + " + selectedName);
                    System.out.println("Latitud + " + selectedLatitude);
                    System.out.println("Longitud + " + selectedLongitude);
                    System.out.println("Direccion + " + selectedDireccion);
                    System.out.println("Telefono + " + selectedTelefono);
                    System.out.println("Localidad + " + selectedLocalidad);

                    // AQUI TOCARIA ABRIR UN NUEVO FRAGMENT CON EL MAPA CON ESA LATITUD
                    // Y LONGITUD PONIENDO UN MARKER CON LOS MISMOS DATOS QUE EN CERCANAS

                    String lat = selectedLatitude;
                    lat = lat.replace(",", ".");
                    String lon = selectedLongitude;
                    lon = lon.replace(",", ".");
                    //MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title(datajson.getString("nombre"));
                    //googleMap.addMarker(marker.snippet(datajson.getString("direccion") + "\n" + datajson.getString("telefono") + "\n" + datajson.getString("localidad")));

                }
            });
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
        viewHolder.selectedName = items.get(i).getNombre();
        viewHolder.selectedLatitude = items.get(i).getLatitud();
        viewHolder.selectedLongitude = items.get(i).getLongitud();
        viewHolder.selectedDireccion = items.get(i).getDireccion();
        viewHolder.selectedTelefono = items.get(i).getTelefono();
        viewHolder.selectedLocalidad = items.get(i).getLocalidad();
    }
}
