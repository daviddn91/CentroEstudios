package com.example.david.centroestudios.fragments;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.centroestudios.Anime;
import com.example.david.centroestudios.AnimeAdapter;
import com.example.david.centroestudios.CentrosEstudios;
import com.example.david.centroestudios.CentrosEstudiosAdapter;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        requestPermission();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        List<CentrosEstudios> items = new ArrayList<>();

        items.add(new CentrosEstudios("Centro 1","Calle la pantomima","Telefono: "+"983281328","Canet de Mar","3","2"));
        items.add(new CentrosEstudios("Centro 2","Calle la pantomima","Telefono: "+"983281328","Canet de Mar","3","2"));
        items.add(new CentrosEstudios("Centro 3","Calle la pantomima","Telefono: "+"983281328","Canet de Mar","3","2"));
        items.add(new CentrosEstudios("Centro 4","Calle la pantomima","Telefono: "+"983281328","Canet de Mar","3","2"));

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new CentrosEstudiosAdapter(items);
        recycler.setAdapter(adapter);

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


}
