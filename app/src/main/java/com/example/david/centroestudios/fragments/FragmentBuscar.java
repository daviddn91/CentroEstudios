package com.example.david.centroestudios.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.centroestudios.MainActivity;
import com.example.david.centroestudios.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBuscar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBuscar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBuscar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText locationSearch;
    Button botonBuscar;

    /*
    Declarar instancias globales
     */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    SQLiteDatabase db;

    // Perfil del usuario

    Integer rentaminima;
    Integer discapacidad;
    Integer familianumerosa;
    Integer enfermedadcronica;

    String direccioncasa;
    String direcciontrabajo;

    String idescuela;
    String nombreescuela;
    String direccionescuela;

    String idescuelaold;
    String nombreescuelaold;
    String direccionescuelaold;


    // Preferencias al buscar

    Integer filtrosoloninas;
    Integer filtrosoloninos;
    Integer filtrocentropublico;
    Integer filtrocentroconcertado;
    Integer filtrocentroprivado;
    Integer filtroreligioso;
    Integer filtrolaico;
    Integer filtroidiomacastellano;
    Integer filtroidiomacatalan;
    Integer filtroidiomaingles;
    Integer filtroidiomafrances;
    Integer filtroidiomaaleman;
    Integer filtroeducacioninfantil1;
    Integer filtroeducacioninfantil2;
    Integer filtroeducacionprimaria;
    Integer filtroeducacionsecundaria;
    Integer filtrobachillerato;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBuscar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBuscar newInstance(String param1, String param2) {
        FragmentBuscar fragment = new FragmentBuscar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentBuscar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = getActivity().openOrCreateDatabase("BaseDeDatos", android.content.Context.MODE_PRIVATE, null);
        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.buscar);
        // Inicializar Animes
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled || !network_enabled) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.activaelgps, Toast.LENGTH_LONG).show();
        }

        getActivity().setTitle(R.string.buscar);
        // Inflate the layout for this fragment

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(2).setChecked(true);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        requestPermission();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        botonBuscar = (Button) view.findViewById(R.id.botonBuscar);
        locationSearch = (EditText) view.findViewById(R.id.editTextBuscar);

        recycler = (RecyclerView) view.findViewById(R.id.reciclador);

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = locationSearch.getText().toString();
                List<Address> addressList = null;
                // SI HAY INTERNET!!!

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        System.out.println("Valor del location = " + location);
                        addressList = geocoder.getFromLocationName(location, 1);


                    } catch (IOException e) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    if (addressList != null) {

                        // AQUI CONSULTAMOS LOS FILTROS DE BASE DE DATOS
                        Cursor c = db.rawQuery("SELECT * FROM filtros", null);
                        while (c.moveToNext()) {
                            filtrosoloninas = c.getInt(0);
                            filtrosoloninos = c.getInt(1);
                            filtrocentropublico = c.getInt(2);
                            filtrocentroconcertado = c.getInt(3);
                            filtrocentroprivado = c.getInt(4);
                            filtroreligioso = c.getInt(5);
                            filtrolaico = c.getInt(6);
                            filtroidiomacastellano = c.getInt(7);
                            filtroidiomacatalan = c.getInt(8);
                            filtroidiomaingles = c.getInt(9);
                            filtroidiomafrances = c.getInt(10);
                            filtroidiomaaleman = c.getInt(11);
                            filtroeducacioninfantil1 = c.getInt(12);
                            filtroeducacioninfantil2 = c.getInt(13);
                            filtroeducacionprimaria = c.getInt(14);
                            filtroeducacionsecundaria = c.getInt(15);
                            filtrobachillerato = c.getInt(16);
                        }

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        System.out.println(address.getLatitude() + "-" + address.getLongitude());

                        // HACER PETICION AL SERVIDOR CON LOS DATOS DE LA LONGITUD Y LATITUD CON LA API DE CERCANAS

                        // INSERTANDO CONTENIDO EN TARJETAS

                        List<CentrosEstudios> items = new ArrayList<>();

                        double latitud = address.getLatitude();
                        double longitud = address.getLongitude();
                        double longitudmin = longitud - 0.05;
                        String lonmin = String.valueOf(longitudmin);
                        lonmin = lonmin.replace(".", ",");
                        double longitudmax = longitud + 0.05;
                        String lonmax = String.valueOf(longitudmax);
                        lonmax = lonmax.replace(".", ",");
                        double latitudmin = latitud - 0.05;
                        String latmin = String.valueOf(latitudmin);
                        latmin = latmin.replace(".", ",");
                        double latitudmax = latitud + 0.05;
                        String latmax = String.valueOf(latitudmax);
                        latmax = latmax.replace(".", ",");

                        // Aqui consultamos el perfil del usuario para el recuento de puntos

                        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'rentaminima'", null);
                        while (c.moveToNext()) {
                            rentaminima = c.getInt(0);
                        }

                        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'discapacidad'", null);
                        while (c.moveToNext()) {
                            discapacidad = c.getInt(0);
                        }

                        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'familianumerosa'", null);
                        while (c.moveToNext()) {
                            familianumerosa = c.getInt(0);
                        }

                        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'enfermedadcronica'", null);
                        while (c.moveToNext()) {
                            enfermedadcronica = c.getInt(0);
                        }

                        c = db.rawQuery("SELECT spinner1, spinner2, spinner3 FROM perfil WHERE id = 'hermanosescolarizados'", null);
                        while (c.moveToNext()) {
                            idescuela = c.getString(0);
                            nombreescuela = c.getString(1);
                            direccionescuela = c.getString(2);
                        }

                        c = db.rawQuery("SELECT spinner1, spinner2, spinner3 FROM perfil WHERE id = 'escuelafamilia'", null);
                        while (c.moveToNext()) {
                            idescuelaold = c.getString(0);
                            nombreescuelaold = c.getString(1);
                            direccionescuelaold = c.getString(2);
                        }

                        c = db.rawQuery("SELECT textbox1 FROM perfil WHERE id = 'direccioncasa'", null);
                        while (c.moveToNext()) {
                            direccioncasa = c.getString(0);
                        }

                        c = db.rawQuery("SELECT textbox1 FROM perfil WHERE id = 'direcciontrabajo'", null);
                        while (c.moveToNext()) {
                            direcciontrabajo = c.getString(0);
                        }

                        // Comprobamos si la escuela está cerca del lugar de casa y del trabajo

                        double latitudcasa = 0.0;
                        double longitudcasa = 0.0;
                        double latitudtrabajo = 0.0;
                        double longitudtrabajo = 0.0;

                        // Llamadas a la API de Google para las direcciones

                        List<Address> addressListCasa = null;
                        List<Address> addressListTrabajo = null;

                        // Llamada API para direccion de casa

                        if (direccioncasa != null || !direccioncasa.equals("")) {
                            geocoder = new Geocoder(getActivity());
                            try {
                                System.out.println("Valor del location = " + direccioncasa);
                                addressListCasa = geocoder.getFromLocationName(direccioncasa, 1);


                            } catch (IOException e) {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            if (addressListCasa != null) {
                                address = addressListCasa.get(0);
                                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                System.out.println(address.getLatitude() + "-" + address.getLongitude());
                                latitudcasa = address.getLatitude();
                                longitudcasa = address.getLongitude();
                            }
                        }

                        if (direcciontrabajo != null || !direcciontrabajo.equals("")) {
                            geocoder = new Geocoder(getActivity());
                            try {
                                System.out.println("Valor del location = " + direccioncasa);
                                addressListTrabajo = geocoder.getFromLocationName(direccioncasa, 1);


                            } catch (IOException e) {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            if (addressListTrabajo != null) {
                                address = addressListTrabajo.get(0);
                                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                System.out.println(address.getLatitude() + "-" + address.getLongitude());
                                latitudtrabajo = address.getLatitude();
                                longitudtrabajo = address.getLongitude();
                            }
                        }


                        // BAJAMOS INFO NUEVA DE LOS CENTROS Y COLOCAMOS LOS MARCADORES

                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            //your codes here
                            String data = GetHTTPData("http://raspi.cat/api.php?cerca=1&longitudmin=" + lonmin + "&longitudmax=" + lonmax + "&latitudmin=" + latmin + "&latitudmax=" + latmax);
                            //String data2 = GetHTTPData("http://raspi.cat/api.php?id=8045641");

                            if (data == null) {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();
                            } else if (data != null && !data.isEmpty()) {

                                JSONObject datajson;

                                try {
                                    System.out.println("Data antes: " + data);
                                    data = data.replace("[", "");
                                    data = data.replace("]", "");
                                    String[] parts = data.split("fininfo");

                                    for (int i = 0; i < parts.length; i++) {
                                        if (!parts[i].equals("\"}")) {
                                            System.out.println("Parte cortada " + i + ": " + parts[i]);
                                            String parte = parts[i] + "fininfo\"}";
                                            parte = parte.replace("\"},", "");

                                            //System.out.println("Parte puesta:"+parte);
                                            //System.out.println("id:" + data2);
                                            datajson = new JSONObject(parte);
                                            String lat = datajson.getString("latitud");
                                            lat = lat.replace(",", ".");
                                            String lon = datajson.getString("longitud");
                                            lon = lon.replace(",", ".");
                                            System.out.println("PRINT JSON GENERADO: " + datajson.toString());

                                            Boolean inserta = true;

                                            System.out.println("Nombre: " + datajson.getString("nombre"));
                                            System.out.println("Centro publico: " + filtrocentropublico);
                                            System.out.println("Centro privado: " + filtrocentroprivado);
                                            System.out.println("Que nos llega: " + datajson.getString("publico"));
                                            System.out.println("Nino: " + filtrosoloninas);
                                            System.out.println("Nina: " + filtrosoloninos);

                                            if (filtrocentropublico.equals(0) && filtrocentroprivado.equals(0)) {
                                                inserta = false;
                                            } else if (filtrocentropublico.equals(0) && datajson.getString("publico").equals("N")) {
                                                inserta = false;
                                            } else if (filtrocentroprivado.equals(0) && !datajson.getString("publico").equals("N")) {
                                                inserta = false;
                                            } else if (filtroeducacioninfantil1.equals(0) && filtroeducacioninfantil2.equals(0) && filtroeducacionprimaria.equals(0) && filtroeducacionsecundaria.equals(0) && filtrobachillerato.equals(0)) {
                                                inserta = false;
                                            } else if ((filtrosoloninas.equals(0) && filtrosoloninos.equals(1)) || (filtrosoloninas.equals(1) && filtrosoloninos.equals(0)) || (filtrosoloninas.equals(0) && filtrosoloninos.equals(0))) {
                                                inserta = false;
                                            } else {
                                                inserta = false;
                                                if (filtroeducacioninfantil1.equals(1) && !datajson.getString("infantil1").equals("N")) {
                                                    inserta = true;
                                                } else if (filtroeducacioninfantil2.equals(1) && !datajson.getString("infantil2").equals("N")) {
                                                    inserta = true;
                                                } else if (filtroeducacionprimaria.equals(1) && !datajson.getString("primaria").equals("N")) {
                                                    inserta = true;
                                                } else if (filtroeducacionsecundaria.equals(1) && !datajson.getString("eso").equals("N")) {
                                                    inserta = true;
                                                } else if (filtrobachillerato.equals(1) && !datajson.getString("bachillerato").equals("N")) {
                                                    inserta = true;
                                                }
                                            }
                                            if (inserta) {

                                                // Aqui calculamos el total de puntos para ese marker y segun eso cambiamos el color o no

                                                int puntos = 0;

                                                if (rentaminima == 1) {
                                                    puntos = puntos + 10;
                                                }
                                                if (discapacidad == 1) {
                                                    puntos = puntos + 10;
                                                }
                                                if (familianumerosa == 1) {
                                                    puntos = puntos + 15;
                                                }
                                                if (enfermedadcronica == 1) {
                                                    puntos = puntos + 10;
                                                }

                                                // Comprobamos si la escuela es la misma que el hermano
                                                if (nombreescuela.equals(datajson.getString("nombre")+" ("+datajson.getString("localidad")+")")) {
                                                    puntos = puntos + 40;
                                                }

                                                // Comprobamos si la escuela es la antigua de otros familiares y si no tiene algun hermano ya en el centro
                                                if (nombreescuelaold.equals(datajson.getString("nombre")+" ("+datajson.getString("localidad")+")") && !nombreescuela.equals(nombreescuelaold)) {
                                                    puntos = puntos + 5;
                                                }


                                                // Comprobamos si la direccion de casa y la del trabajo nos dan puntos
                                                if (Math.abs(latitudcasa-Double.parseDouble(lat)) < 0.1 && Math.abs(longitudcasa-Double.parseDouble(lon)) < 0.1) {
                                                    puntos = puntos + 30;
                                                    // Centro en el area de influencia
                                                }
                                                else if (Math.abs(latitudtrabajo-Double.parseDouble(lat)) < 0.1 && Math.abs(longitudtrabajo-Double.parseDouble(lon)) < 0.1) {
                                                    puntos = puntos + 20;
                                                    // Centro cerca del area de influencia del trabajo, mirar con cariño
                                                }
                                                else if (Math.abs(latitudcasa-Double.parseDouble(lat)) < 0.5 && Math.abs(longitudcasa-Double.parseDouble(lon)) < 0.5) {
                                                    puntos = puntos + 10;
                                                    // Mirar con cuidado porque es si estan en el municipio pero no en el area de influencia
                                                }

                                                items.add(new CentrosEstudios(datajson.getString("id"), datajson.getString("nombre"), datajson.getString("direccion"), getResources().getString(R.string.telefono) + ": " + datajson.getString("telefono"), datajson.getString("localidad"), datajson.getString("publico"), datajson.getString("infantil1"), datajson.getString("infantil2"), datajson.getString("primaria"), datajson.getString("eso"), datajson.getString("bachillerato"), lon, lat, Integer.toString(puntos)));
                                            }
                                        }
                                    }
                                    if (items.size() < 1) {
                                        Toast.makeText(getActivity().getApplicationContext(), R.string.sinresultadoslocalizacion, Toast.LENGTH_SHORT).show();
                                    }
                                    System.out.println("Fin de la carga de cards en buscar");
                                } catch (JSONException e) {
                                    System.out.println("JSON Exception");
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.sinresultadoslocalizacion, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        // Obtener el Recycler
                        recycler.setHasFixedSize(true);

                        // Usar un administrador para LinearLayout
                        lManager = new LinearLayoutManager(getActivity());
                        recycler.setLayoutManager(lManager);

                        // Crear un nuevo adaptador
                        adapter = new CentrosEstudiosAdapter(items);
                        recycler.setAdapter(adapter);

                        if (v != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }

                    }
                } else if (location.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.introducelocalizacion, Toast.LENGTH_SHORT).show();
                } else if (location == null) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.errorlocalizacion, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void cambiaAMapa() {
        System.out.println("ESTOY EN EL FRAGMENT");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String GetHTTPData(String urlString) {
        String stream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Check the connection status
            if (urlConnection.getResponseCode() == 200) {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            } else {
                System.out.println("Else del GetHTTPData en FragmentBuscar");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Catch del GetHTTPData en FragmentBuscar");
            Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();

        } finally {
            System.out.println("Finally del GetHTTPData en FragmentBuscar");
        }
        // Return the data from specified url
        return stream;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.INTERNET)) {

            Toast.makeText(getActivity().getApplicationContext(), R.string.needinternet, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.INTERNET}, 1);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.INTERNET);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Clase para el contenido de las cardview en Buscar escuela
     */
    public class CentrosEstudios {
        private String id;
        private String nombre;
        private String direccion;
        private String telefono;
        private String localidad;
        private String publico;
        private String infantil1;
        private String infantil2;
        private String primaria;
        private String eso;
        private String bachillerato;
        private String longitud;
        private String latitud;
        private String puntos;

        public CentrosEstudios(String id, String nombre, String direccion, String telefono, String localidad, String publico, String infantil1, String infantil2, String primaria, String eso, String bachillerato, String longitud, String latitud, String puntos) {
            this.id = id;
            this.nombre = nombre;
            this.direccion = direccion;
            this.telefono = telefono;
            this.localidad = localidad;
            this.longitud = longitud;
            this.latitud = latitud;
            this.publico = publico;
            this.infantil1 = infantil1;
            this.infantil2 = infantil2;
            this.primaria = primaria;
            this.eso = eso;
            this.bachillerato = bachillerato;
            this.puntos = puntos;
        }

        public String getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDireccion() {
            return direccion;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getLocalidad() {
            return localidad;
        }

        public String getLongitud() {
            return longitud;
        }

        public String getLatitud() {
            return latitud;
        }

        public String getPublico() {
            return publico;
        }

        public String getInfantil1() {
            return infantil1;
        }

        public String getInfantil2() {
            return infantil2;
        }

        public String getPrimaria() {
            return primaria;
        }

        public String getEso() {
            return eso;
        }

        public String getBachillerato() {
            return bachillerato;
        }

        public String getPuntos() {
            return puntos;
        }

    }


    public class CentrosEstudiosAdapter extends RecyclerView.Adapter<CentrosEstudiosAdapter.CentrosEstudiosViewHolder> {
        private List<CentrosEstudios> items;

        public class CentrosEstudiosViewHolder extends RecyclerView.ViewHolder {
            // Campos respectivos de un item
            public TextView nombre;
            public TextView direccion;
            public TextView telefono;
            public TextView localidad;
            public TextView publico;
            public TextView nivel;
            public TextView puntos;
            public String selectedName;
            public String selectedLatitude;
            public String selectedLongitude;
            public String selectedDireccion;
            public String selectedTelefono;
            public String selectedLocalidad;
            public String selectedPublico;
            public String selectedInfantil1;
            public String selectedInfantil2;
            public String selectedPrimaria;
            public String selectedEso;
            public String selectedBachillerato;
            public String selectedPuntos;

            public View view;

            public CentrosEstudiosViewHolder(View v) {
                super(v);
                view = v;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Nombre + " + selectedName);
                        System.out.println("Latitud + " + selectedLatitude);
                        System.out.println("Longitud + " + selectedLongitude);
                        System.out.println("Direccion + " + selectedDireccion);
                        System.out.println("Telefono + " + selectedTelefono);
                        System.out.println("Localidad + " + selectedLocalidad);
                        System.out.println("Publico + " + selectedPublico);
                        System.out.println("Infantil1 + " + selectedInfantil1);
                        System.out.println("Infantil2 + " + selectedInfantil2);
                        System.out.println("Primaria + " + selectedPrimaria);
                        System.out.println("ESO + " + selectedEso);
                        System.out.println("Bachillerato + " + selectedBachillerato);
                        System.out.println("Puntos + " + puntos);

                        String lat = selectedLatitude;
                        lat = lat.replace(",", ".");
                        String lon = selectedLongitude;
                        lon = lon.replace(",", ".");

                        String nivelestudios = "";

                        if (!selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && !selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_JB);
                        } else if (!selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_JE);
                        } else if (!selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_JP);
                        } else if (!selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_JI);
                        } else if (!selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_J);
                        } else if (selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && !selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_IB);
                        } else if (selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_IE);
                        } else if (selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_IP);
                        } else if (selectedInfantil1.equals("N") && !selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_I);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && !selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_PB);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_PE);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && !selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_P);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && !selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_EB);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && !selectedBachillerato.equals("N") && selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_E);
                        } else if (selectedInfantil1.equals("N") && selectedInfantil2.equals("N") && selectedPrimaria.equals("N") && selectedBachillerato.equals("N") && !selectedBachillerato.equals("N")) {
                            nivelestudios = getResources().getString(R.string.nivel_B);
                        }

                        String esPublico = "";

                        if (selectedPublico.equals("S")) {
                            esPublico = getResources().getString(R.string.noespublico);
                        } else {
                            esPublico = getResources().getString(R.string.espublico);
                        }

                        db.execSQL("DELETE FROM centros WHERE id = '2331991D'");
                        String insert = "INSERT INTO centros (id, nombre, direccion, telefono, localidad, latitud, longitud, infantil1, actualizado) VALUES ('2331991D','" + selectedName + "','" + selectedDireccion + "','" + selectedTelefono + "','" + selectedLocalidad + "','" + selectedLatitude + "','" + selectedLongitude + "','" + nivelestudios + "','" + esPublico + "')";
                        System.out.println(insert);
                        db.execSQL(insert);


                        ((MainActivity) getActivity()).abrirMapaBuscar();

                        //MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title(datajson.getString("nombre"));
                        //googleMap.addMarker(marker.snippet(datajson.getString("direccion") + "\n" + datajson.getString("telefono") + "\n" + datajson.getString("localidad")));

                    }
                });
                nombre = (TextView) v.findViewById(R.id.nombre);
                direccion = (TextView) v.findViewById(R.id.direccion);
                telefono = (TextView) v.findViewById(R.id.telefono);
                localidad = (TextView) v.findViewById(R.id.localidad);
                publico = (TextView) v.findViewById(R.id.publico);
                nivel = (TextView) v.findViewById(R.id.nivel);
                puntos = (TextView) v.findViewById(R.id.puntos);

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

            if (items.get(i).getPublico().equals("S")) {
                viewHolder.publico.setText(getResources().getString(R.string.noespublico));
            } else {
                viewHolder.publico.setText(getResources().getString(R.string.espublico));
            }

            String nivel = "";

            if (!items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && !items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_JB);
            } else if (!items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_JE);
            } else if (!items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_JP);
            } else if (!items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_JI);
            } else if (!items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_J);
            } else if (items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && !items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_IB);
            } else if (items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_IE);
            } else if (items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_IP);
            } else if (items.get(i).getInfantil1().equals("N") && !items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_I);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && !items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_PB);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_PE);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && !items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_P);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && !items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_EB);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && !items.get(i).getBachillerato().equals("N") && items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_E);
            } else if (items.get(i).getInfantil1().equals("N") && items.get(i).getInfantil2().equals("N") && items.get(i).getPrimaria().equals("N") && items.get(i).getBachillerato().equals("N") && !items.get(i).getBachillerato().equals("N")) {
                nivel = getResources().getString(R.string.nivel_B);
            }

            viewHolder.nivel.setText(nivel);
            viewHolder.puntos.setText(items.get(i).getPuntos() + " " + getResources().getString(R.string.puntos));
            viewHolder.selectedName = items.get(i).getNombre();
            viewHolder.selectedLatitude = items.get(i).getLatitud();
            viewHolder.selectedLongitude = items.get(i).getLongitud();
            viewHolder.selectedDireccion = items.get(i).getDireccion();
            viewHolder.selectedTelefono = items.get(i).getTelefono();
            viewHolder.selectedLocalidad = items.get(i).getLocalidad();
            viewHolder.selectedInfantil1 = items.get(i).getInfantil1();
            viewHolder.selectedInfantil2 = items.get(i).getInfantil2();
            viewHolder.selectedPrimaria = items.get(i).getPrimaria();
            viewHolder.selectedEso = items.get(i).getEso();
            viewHolder.selectedBachillerato = items.get(i).getBachillerato();
            viewHolder.selectedPublico = items.get(i).getPublico();
            viewHolder.selectedPuntos = items.get(i).getPuntos();

        }
    }

}
