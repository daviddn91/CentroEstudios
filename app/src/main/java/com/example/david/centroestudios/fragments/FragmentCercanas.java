package com.example.david.centroestudios.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.centroestudios.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCercanas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCercanas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCercanas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteDatabase db;
    boolean zoominicial = true;

    double latitud = 1;
    double longitud = 1;

    double latituddatos = -10;
    double longituddatos = -10;

    boolean actualiza = true;

    MapView mMapView;
    private GoogleMap googleMap;
    private OnFragmentInteractionListener mListener;
    Location location;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFaqs.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCercanas newInstance(String param1, String param2) {
        FragmentCercanas fragment = new FragmentCercanas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentCercanas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Cambia el texto del titulo al nombre de la seccion
        db = getActivity().openOrCreateDatabase("BaseDeDatos",android.content.Context.MODE_PRIVATE ,null);
        getActivity().setTitle(R.string.cercanas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (!gps_enabled || !network_enabled) {
            Toast.makeText(getActivity().getApplicationContext(),R.string.activaelgps, Toast.LENGTH_LONG).show();
        }

        actualiza = true;
        getActivity().setTitle(R.string.cercanas);
        zoominicial = true;
        View v = inflater.inflate(R.layout.fragment_cercanas, container,
                false);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(0).setChecked(true);

        requestPermission();
        requestPermission2();
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        if (checkPermission()) {
            googleMap.setMyLocationEnabled(true);
            if (googleMap != null) {

                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        //googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                        if (zoominicial) {
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(arg0.getLatitude(), arg0.getLongitude())).zoom(16).build();
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                            zoominicial = false;
                        }

                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(getActivity());
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getActivity());
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getActivity());
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

                        if (actualiza) {

                            actualiza = false;
                            latitud = arg0.getLatitude();
                            latituddatos = arg0.getLatitude();
                            longitud = arg0.getLongitude();
                            longituddatos = arg0.getLongitude();
                            double longitudmin = longitud-0.05;
                            String lonmin = String.valueOf(longitudmin);
                            lonmin = lonmin.replace(".",",");
                            double longitudmax = longitud+0.05;
                            String lonmax = String.valueOf(longitudmax);
                            lonmax = lonmax.replace(".",",");
                            double latitudmin = latitud-0.05;
                            String latmin = String.valueOf(latitudmin);
                            latmin = latmin.replace(".",",");
                            double latitudmax = latitud+0.05;
                            String latmax = String.valueOf(latitudmax);
                            latmax = latmax.replace(".",",");


                            // BAJAMOS INFO NUEVA DE LOS CENTROS Y COLOCAMOS LOS MARCADORES

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                //your codes here
                                String data = GetHTTPData("http://raspi.cat/api.php?cerca=1&longitudmin="+lonmin+"&longitudmax="+lonmax+"&latitudmin="+latmin+"&latitudmax="+latmax);
                                //String data2 = GetHTTPData("http://raspi.cat/api.php?id=8045641");

                                if (data != null && !data.isEmpty()) {
                                    // AQUI IR DIVIDIENDO EL STRING Y HACER UN BUCLE PARA PASAR A JSON


                                    //AuthMsg msg = new Gson().fromJson(data, AuthMsg.class);


                                    JSONObject datajson;
                                    try {
                                        System.out.println("Data antes: "+ data);
                                        data = data.replace("[","");
                                        data = data.replace("]","");
                                        String[] parts = data.split("fininfo");
                                        ArrayList<String> lista = new ArrayList();

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
                                                lista.add(datajson.getString("id"));
                                                String publico = datajson.getString("publico");
                                                String infantil1 = datajson.getString("infantil1");
                                                String infantil2 = datajson.getString("infantil2");
                                                String primaria = datajson.getString("primaria");
                                                String eso = datajson.getString("eso");
                                                String bachillerato = datajson.getString("bachillerato");
                                                String nivel = "";

                                                if (publico.equals("S")) {
                                                    publico = getResources().getString(R.string.noespublico);
                                                } else {
                                                    publico = getResources().getString(R.string.espublico);
                                                }

                                                if (!infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && !bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_JB);
                                                }
                                                else if (!infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_JE);
                                                }
                                                else if (!infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_JP);
                                                }
                                                else if (!infantil1.equals("N") && !infantil2.equals("N") && primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_JI);
                                                }
                                                else if (!infantil1.equals("N") && infantil2.equals("N") && primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_J);
                                                }
                                                else if (infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && !bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_IB);
                                                }
                                                else if (infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_IE);
                                                }
                                                else if (infantil1.equals("N") && !infantil2.equals("N") && !primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_IP);
                                                }
                                                else if (infantil1.equals("N") && !infantil2.equals("N") && primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_I);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && !bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_PB);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && !primaria.equals("N") && !eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_PE);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && !primaria.equals("N") && eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_P);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && primaria.equals("N") && !eso.equals("N") && !bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_EB);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && primaria.equals("N") && !eso.equals("N") && bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_E);
                                                }
                                                else if (infantil1.equals("N") && infantil2.equals("N") && primaria.equals("N") && eso.equals("N") && !bachillerato.equals("N")) {
                                                    nivel = getResources().getString(R.string.nivel_B);
                                                }

                                                //System.out.println("PRINT JSON GENERADO: " +datajson.toString());
                                                MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title(datajson.getString("nombre"));
                                                googleMap.addMarker(marker.snippet(datajson.getString("direccion") + "\n" + datajson.getString("telefono") + "\n" + datajson.getString("localidad") + "\n" + publico + "\n" + nivel));

                                            /*
                                            System.out.println(datajson.getString("id"));
                                            System.out.println(datajson.getString("nombre"));
                                            System.out.println(datajson.getString("latitud"));
                                            System.out.println(datajson.getString("longitud"));
                                            */
                                            }
                                        }
                                        if (lista.size() == 0) {
                                            Toast.makeText(getActivity().getApplicationContext(), R.string.sinresultadoscercanas, Toast.LENGTH_SHORT).show();
                                        }
                                        System.out.println("Fin de la carga de markers");
                                    }
                                    catch (JSONException e) {
                                        System.out.println("JSON Exception");
                                    }

                                }

                                            /*
                                            // Changing marker icon
                                            //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                            //marker.icon(BitmapDescriptorFactory.defaultMarker());
                                            */
                            }



                        }
                    }
                });
            }
        }
        else {
            googleMap.setMyLocationEnabled(false);
        }

        // Perform any camera updates here
        return v;
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(getActivity().getApplicationContext(),R.string.gpspermiso, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission2(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.INTERNET)){

            Toast.makeText(getActivity().getApplicationContext(),R.string.gpspermiso, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.INTERNET},1);
        }
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
            Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();

        }finally {
            System.out.println("Finally del GetHTTPData en FragmentBuscar");
        }
        // Return the data from specified url
        return stream;
    }

}
