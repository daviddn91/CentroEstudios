package com.example.david.centroestudios.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPreferencias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPreferencias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPreferencias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteDatabase db;

    Integer soloninas;
    Integer soloninos;
    Integer centropublico;
    Integer centroconcertado;
    Integer centroprivado;
    Integer religioso;
    Integer laico;
    Integer idiomacastellano;
    Integer idiomacatalan;
    Integer idiomaingles;
    Integer idiomafrances;
    Integer idiomaaleman;
    Integer educacioninfantil1;
    Integer educacioninfantil2;
    Integer educacionprimaria;
    Integer educacionsecundaria;
    Integer bachillerato;

    // Ids de los radiogroup para guardar en BD porque no puedo acceder directamente
    Integer idrb1;
    Integer idrb2;
    Integer idrb3;
    Integer idrb4;
    Integer idrb5;
    Integer idrb6;

    // Valores para escuela de otros hijos
    String idescuela;
    String nombreescuela;
    String direccionescuela;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPreferencias.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPreferencias newInstance(String param1, String param2) {
        FragmentPreferencias fragment = new FragmentPreferencias();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPreferencias() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /* Accedemos a la BD y asignamos el valor de ella a las variables globales para luego en el OnCreateView mover switch y radiobuttons segun toque */

        db = getActivity().openOrCreateDatabase("BaseDeDatos",android.content.Context.MODE_PRIVATE ,null);
        Cursor c = db.rawQuery("SELECT * FROM filtros", null);
        while(c.moveToNext()) {
            soloninas = c.getInt(0);
            soloninos = c.getInt(1);
            centropublico = c.getInt(2);
            centroconcertado = c.getInt(3);
            centroprivado = c.getInt(4);
            religioso = c.getInt(5);
            laico = c.getInt(6);
            idiomacastellano = c.getInt(7);
            idiomacatalan = c.getInt(8);
            idiomaingles = c.getInt(9);
            idiomafrances = c.getInt(10);
            idiomaaleman = c.getInt(11);
            educacioninfantil1 = c.getInt(12);
            educacioninfantil2 = c.getInt(13);
            educacionprimaria = c.getInt(14);
            educacionsecundaria = c.getInt(15);
            bachillerato = c.getInt(16);
        }

        /*

        c = db.rawQuery("SELECT * FROM allcentros WHERE seleccionado = 1", null);
        if(c.moveToNext()) {
            idescuela = c.getString(0);
            nombreescuela = c.getString(1);
            direccionescuela = c.getString(2);
        }

        */

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.preferencias);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.preferencias);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferencias, container, false);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(3).setChecked(true);

        /* Aqui empieza lo de actualizar switch y radiobuttons segun la BD */

        // RadioGroup genero
        RadioGroup rg1 = (RadioGroup) view.findViewById(R.id.radioGroup);
        RadioButton rb1 = (RadioButton) view.findViewById(R.id.radioButton);
        RadioButton rb2 = (RadioButton) view.findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton) view.findViewById(R.id.radioButton3);
        idrb1 = rb1.getId();
        idrb2 = rb2.getId();
        idrb3 = rb3.getId();

        if (soloninas.equals(1) && soloninos.equals(1)) {
            rg1.check(rb3.getId());
        }
        else if (soloninas.equals(1) && soloninos.equals(0)) {
            rg1.check(rb1.getId());
        }
        else if (soloninas.equals(0) && soloninos.equals(1)) {
            rg1.check(rb2.getId());
        }

        // Switch publico
        Switch sw3 = (Switch) view.findViewById(R.id.switch3);
        if (centropublico.equals(0)) {
            sw3.setChecked(false);
        }
        else if (centropublico.equals(1)) {
            sw3.setChecked(true);
        }

        // Switch concertado
        Switch sw4 = (Switch) view.findViewById(R.id.switch4);
        if (centroconcertado.equals(0)) {
            sw4.setChecked(false);
        }
        else if (centroconcertado.equals(1)) {
            sw4.setChecked(true);
        }

        // Switch privado
        Switch sw5 = (Switch) view.findViewById(R.id.switch5);
        if (centroprivado.equals(0)) {
            sw5.setChecked(false);
        }
        else if (centroprivado.equals(1)) {
            sw5.setChecked(true);
        }

        // RadioGroup creencias religiosas
        RadioGroup rg2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        RadioButton rb4 = (RadioButton) view.findViewById(R.id.radioButton4);
        RadioButton rb5 = (RadioButton) view.findViewById(R.id.radioButton5);
        RadioButton rb6 = (RadioButton) view.findViewById(R.id.radioButton6);

        idrb4 = rb4.getId();
        idrb5 = rb5.getId();
        idrb6 = rb6.getId();

        if (religioso.equals(1) && laico.equals(1)) {
            rg2.check(rb6.getId());
        }
        else if (religioso.equals(1) && laico.equals(0)) {
            rg2.check(rb4.getId());
        }
        else if (religioso.equals(0) && laico.equals(1)) {
            rg2.check(rb5.getId());
        }

        // Switch idioma castellano
        Switch sw7 = (Switch) view.findViewById(R.id.switch7);
        if (idiomacastellano.equals(0)) {
            sw7.setChecked(false);
        }
        else if (idiomacastellano.equals(1)) {
            sw7.setChecked(true);
        }

        // Switch idioma catalan
        Switch sw9 = (Switch) view.findViewById(R.id.switch9);
        if (idiomacatalan.equals(0)) {
            sw9.setChecked(false);
        }
        else if (idiomacatalan.equals(1)) {
            sw9.setChecked(true);
        }

        // Switch idioma ingles
        Switch sw8 = (Switch) view.findViewById(R.id.switch8);
        if (idiomaingles.equals(0)) {
            sw8.setChecked(false);
        }
        else if (idiomaingles.equals(1)) {
            sw8.setChecked(true);
        }

        // Switch idioma aleman
        Switch sw11 = (Switch) view.findViewById(R.id.switch11);
        if (idiomaaleman.equals(0)) {
            sw11.setChecked(false);
        }
        else if (idiomaaleman.equals(1)) {
            sw11.setChecked(true);
        }

        // Switch idioma frances
        Switch sw10 = (Switch) view.findViewById(R.id.switch10);
        if (idiomafrances.equals(0)) {
            sw10.setChecked(false);
        }
        else if (idiomafrances.equals(1)) {
            sw10.setChecked(true);
        }

        // Switch infantil1
        Switch sw15 = (Switch) view.findViewById(R.id.switch15);
        if (educacioninfantil1.equals(0)) {
            sw15.setChecked(false);
        }
        else if (educacioninfantil1.equals(1)) {
            sw15.setChecked(true);
        }

        // Switch infantil2
        Switch sw19 = (Switch) view.findViewById(R.id.switch19);
        if (educacioninfantil2.equals(0)) {
            sw19.setChecked(false);
        }
        else if (educacioninfantil2.equals(1)) {
            sw19.setChecked(true);
        }


        // Switch primaria
        Switch sw16 = (Switch) view.findViewById(R.id.switch16);
        if (educacionprimaria.equals(0)) {
            sw16.setChecked(false);
        }
        else if (educacionprimaria.equals(1)) {
            sw16.setChecked(true);
        }

        // Switch eso
        Switch sw17 = (Switch) view.findViewById(R.id.switch17);
        if (educacionsecundaria.equals(0)) {
            sw17.setChecked(false);
        }
        else if (educacionsecundaria.equals(1)) {
            sw17.setChecked(true);
        }

        // Switch bachillerato
        Switch sw18 = (Switch) view.findViewById(R.id.switch18);
        if (bachillerato.equals(0)) {
            sw18.setChecked(false);
        }
        else if (bachillerato.equals(1)) {
            sw18.setChecked(true);
        }

        /* Hacemos a la inversa, ahora modificamos la BD si tocamos los switch y raddiobuttons */

        // RadioGroup genero
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == idrb1) {
                    db.execSQL("UPDATE filtros SET nina = 1;");
                    db.execSQL("UPDATE filtros SET nino = 0;");
                }
                else if (checkedId == idrb2) {
                    db.execSQL("UPDATE filtros SET nino = 1;");
                    db.execSQL("UPDATE filtros SET nina = 0;");
                }
                else if (checkedId == idrb3){
                    db.execSQL("UPDATE filtros SET nina = 1;");
                    db.execSQL("UPDATE filtros SET nino = 1;");
                }
            }
        });

        // Switch publico
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET publico = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET publico = 0;");
                }
            }
        });

        // Switch concertado
        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET concertado = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET concertado = 0;");
                }
            }
        });

        // Switch privado
        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET privado = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET privado = 0;");
                }
            }
        });

        // RadioGroup religion
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == idrb4) {
                    db.execSQL("UPDATE filtros SET religioso = 1;");
                    db.execSQL("UPDATE filtros SET laico = 0;");
                }
                else if (checkedId == idrb5) {
                    db.execSQL("UPDATE filtros SET laico = 1;");
                    db.execSQL("UPDATE filtros SET religioso = 0;");
                }
                else if (checkedId == idrb6){
                    db.execSQL("UPDATE filtros SET religioso = 1;");
                    db.execSQL("UPDATE filtros SET laico = 1;");
                }
            }
        });

        // Switch castellano
        sw7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET castellano = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET castellano = 0;");
                }
            }
        });

        // Switch catalan
        sw9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET catalan = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET catalan = 0;");
                }
            }
        });

        // Switch ingles
        sw8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET ingles = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET ingles = 0;");
                }
            }
        });

        // Switch frances
        sw11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET frances = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET frances = 0;");
                }
            }
        });

        // Switch aleman
        sw10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET aleman = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET aleman = 0;");
                }
            }
        });

        // Switch infantil1
        sw15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET infantil1 = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET infantil1 = 0;");
                }
            }
        });

        // Switch infantil2
        sw19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET infantil2 = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET infantil2 = 0;");
                }
            }
        });

        // Switch primaria
        sw16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET primaria = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET primaria = 0;");
                }
            }
        });

        // Switch eso
        sw17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET eso = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET eso = 0;");
                }
            }
        });

        // Switch bachillerato
        sw18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE filtros SET bachillerato = 1;");
                }
                else {
                    db.execSQL("UPDATE filtros SET bachillerato = 0;");
                }
            }
        });

        /*
        // Spinner con los centros

        Spinner spinner =  (Spinner) view.findViewById(R.id.spinner);

        // Descargamos los centros

        ArrayList<String> al = new ArrayList<>();

        al.add("-");

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            String data = GetHTTPData("http://raspi.cat/api.php?all=1");

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
                            //System.out.println("Parte cortada " + i + ": " + parts[i]);
                            String parte = parts[i] + "fininfo\"}";
                            parte = parte.replace("\"},", "");

                            datajson = new JSONObject(parte);
                            String id = datajson.getString("id");
                            String nombre = datajson.getString("nombre");
                            String localidad = datajson.getString("localidad");
                            if (!id.equals(null) && !id.isEmpty() && !nombre.equals(null) && !nombre.isEmpty() && !localidad.equals(null) && !localidad.isEmpty()) {
                                al.add(nombre + " (" + localidad + ")");
                            }

                        }
                    }
                    if (lista.size() == 0) {
                        //Toast.makeText(getActivity().getApplicationContext(), R.string.sinresultadoscercanas, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("Fin de la carga de todos los centros");
                }
                catch (JSONException e) {
                    System.out.println("JSON Exception");
                }
            }
        }


        // Seteamos los valores del spinner


        String[] arraySpinner = al.toArray(new String[al.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_selectable_list_item, arraySpinner);
        spinner.setAdapter(adapter);

        // Marcamos el seleccionado


        // Listener para saber el item seleccionado del spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                System.out.println("Spinner: " + parent.getItemAtPosition(position).toString());

                idescuela = Integer.toString(position);

                nombreescuela = parent.getItemAtPosition(position).toString();

                direccionescuela = parent.getItemAtPosition(position).toString();

                parent.setSelection(Integer.parseInt(idescuela));

                db.execSQL("DELETE FROM allcentros;");
                db.execSQL("INSERT INTO allcentros(id, nombre,localidad, seleccionado) values ('"+Integer.toString(parent.getSelectedItemPosition())+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"','1')");
                System.out.println("INSERT INTO allcentros(id, nombre,localidad, seleccionado) values ('"+parent.getSelectedItemPosition()+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"','1')");
                if (parent.getSelectedItemPosition() == 0) {
                    System.out.println("Es la posicion 0");
                }
                else {
                    System.out.println("No es el 0");
                    //db.execSQL("UPDATE allcentros SET seleccionado = 1 WHERE nombre = '"+ parent.getItemAtPosition(position).toString() + "';");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        */

        //return inflater.inflate(R.layout.fragment_preferencias, container, false);
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
                System.out.println("Else del GetHTTPData en FragmentPreferencias");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Catch del GetHTTPData en FragmentPreferencias");
            Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();

        }finally {
            System.out.println("Finally del GetHTTPData en FragmentPreferencias");
        }
        // Return the data from specified url
        return stream;
    }

}
