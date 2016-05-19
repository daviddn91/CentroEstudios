package com.example.david.centroestudios.fragments;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.centroestudios.MainActivity;
import com.example.david.centroestudios.R;
import com.google.android.gms.maps.model.LatLng;

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
        db = getActivity().openOrCreateDatabase("BaseDeDatos",android.content.Context.MODE_PRIVATE ,null);
        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.buscar);
        // Inicializar Animes
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.buscar);
        // Inflate the layout for this fragment

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(1).setChecked(true);


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
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        System.out.println("Valor del location = " + location);
                        addressList = geocoder.getFromLocationName(location, 1);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    System.out.println(address.getLatitude() + "-" + address.getLongitude());

                    // HACER PETICION AL SERVIDOR CON LOS DATOS DE LA LONGITUD Y LATITUD CON LA API DE CERCANAS

                    // INSERTANDO CONTENIDO EN TARJETAS

                    List<CentrosEstudios> items = new ArrayList<>();

                    items.add(new CentrosEstudios("1","Centro 1","Calle la pantomima","Teléfono: "+"983281328","Canet de Mar","3","2"));
                    items.add(new CentrosEstudios("2", "Centro 2","Calle la pantomima","Teléfono: "+"983281328","Canet de Mar","3","2"));
                    items.add(new CentrosEstudios("3", "Centro 3","Calle la pantomima","Teléfono: "+"983281328","Canet de Mar","3","2"));
                    items.add(new CentrosEstudios("4", "Centro 4","Calle la pantomima","Teléfono: "+"983281328","Canet de Mar","3","2"));

                    // Obtener el Recycler
                    recycler.setHasFixedSize(true);

                    // Usar un administrador para LinearLayout
                    lManager = new LinearLayoutManager(getActivity());
                    recycler.setLayoutManager(lManager);

                    // Crear un nuevo adaptador
                    adapter = new CentrosEstudiosAdapter(items);
                    recycler.setAdapter(adapter);

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

    public String GetHTTPData(String urlString){
        String stream = null;
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
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
            }
            else
            {
                System.out.println("Else del GetHTTPData en FragmentBuscar");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Catch del GetHTTPData en FragmentBuscar");
        }finally {
            System.out.println("Finally del GetHTTPData en FragmentBuscar");
        }
        // Return the data from specified url
        return stream;
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.INTERNET)){

            Toast.makeText(getActivity().getApplicationContext(), "We need internet to obtain schools information", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.INTERNET},1);
        }
    }

    private boolean checkPermission(){
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
        private String longitud;
        private String latitud;

        public CentrosEstudios(String id, String nombre, String direccion, String telefono, String localidad, String longitud, String latitud) {
            this.id = id;
            this.nombre = nombre;
            this.direccion = direccion;
            this.telefono = telefono;
            this.localidad = localidad;
            this.longitud = longitud;
            this.latitud = latitud;
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

    }


    public class CentrosEstudiosAdapter extends RecyclerView.Adapter<CentrosEstudiosAdapter.CentrosEstudiosViewHolder> {
        private List<CentrosEstudios> items;

        public class CentrosEstudiosViewHolder extends RecyclerView.ViewHolder {
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
                    @Override
                    public void onClick(View v) {
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

                        db.execSQL("DELETE FROM centros WHERE id = '2331991D'");
                        String insert = "INSERT INTO centros (id, nombre, direccion, telefono, localidad, latitud, longitud) VALUES ('2331991D','"+selectedName+"','"+selectedDireccion+"','"+selectedTelefono+"','"+selectedLocalidad+"','"+selectedLatitude+"','"+selectedLongitude+"')";
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

}
