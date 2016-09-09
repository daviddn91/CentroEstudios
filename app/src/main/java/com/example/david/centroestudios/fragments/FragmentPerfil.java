package com.example.david.centroestudios.fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.centroestudios.R;

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
 * {@link FragmentPerfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteDatabase db;

    Integer rentaminima;
    Integer discapacidad;
    Integer familianumerosa;
    Integer cronica;

    // Valores para escuela de otros hijos
    String idescuela;
    String nombreescuela;
    String direccionescuela;

    // Valores para escuela de familiares antigua
    String idescuelaold;
    String nombreescuelaold;
    String direccionescuelaold;

    // Direcciones
    String direccioncasa;
    String direcciontrabajo;

    // Autocompletar
    private AutoCompleteTextView mACTVAddress;
    private AutoCompleteTextView mACTVAddress2;



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
    public static FragmentPerfil newInstance(String param1, String param2) {
        FragmentPerfil fragment = new FragmentPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPerfil() {
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

        db = getActivity().openOrCreateDatabase("BaseDeDatos", Context.MODE_PRIVATE ,null);
        Cursor c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'rentaminima'", null);
        while(c.moveToNext()) {
            rentaminima = c.getInt(0);
        }

        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'discapacidad'", null);
        while(c.moveToNext()) {
            discapacidad = c.getInt(0);
        }

        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'familianumerosa'", null);
        while(c.moveToNext()) {
            familianumerosa = c.getInt(0);
        }

        c = db.rawQuery("SELECT switch1 FROM perfil WHERE id = 'enfermedadcronica'", null);
        while(c.moveToNext()) {
            cronica = c.getInt(0);
        }

        c = db.rawQuery("SELECT spinner1, spinner2, spinner3 FROM perfil WHERE id = 'hermanosescolarizados'", null);
        while(c.moveToNext()) {
            idescuela = c.getString(0);
            nombreescuela = c.getString(1);
            direccionescuela = c.getString(2);
        }

        c = db.rawQuery("SELECT spinner1, spinner2, spinner3 FROM perfil WHERE id = 'escuelafamilia'", null);
        while(c.moveToNext()) {
            idescuelaold = c.getString(0);
            nombreescuelaold = c.getString(1);
            direccionescuelaold = c.getString(2);
        }

        c = db.rawQuery("SELECT textbox1 FROM perfil WHERE id = 'direccioncasa'", null);
        while(c.moveToNext()) {
            direccioncasa = c.getString(0);
        }

        c = db.rawQuery("SELECT textbox1 FROM perfil WHERE id = 'direcciontrabajo'", null);
        while(c.moveToNext()) {
            direcciontrabajo = c.getString(0);
        }

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.perfil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.perfil);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(0).setChecked(true);

        mACTVAddress = (AutoCompleteTextView) view.findViewById(R.id.editTextBuscar);

        // Address AutoCompleteTextView
        mACTVAddress.setAdapter(new AutoCompleteAdapter(getActivity()));

        mACTVAddress2 = (AutoCompleteTextView) view.findViewById(R.id.editTextBuscar2);

        // Address AutoCompleteTextView
        mACTVAddress2.setAdapter(new AutoCompleteAdapter(getActivity()));

        /* Aqui empieza lo de actualizar switch y radiobuttons segun la BD */

        // Switch renta minima
        Switch sw1 = (Switch) view.findViewById(R.id.switch1);
        if (rentaminima.equals(0)) {
            sw1.setChecked(false);
        }
        else if (rentaminima.equals(1)) {
            sw1.setChecked(true);
        }

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE perfil SET switch1 = 1 WHERE id = 'rentaminima';");
                }
                else {
                    db.execSQL("UPDATE perfil SET switch1 = 0 WHERE id = 'rentaminima';");
                }
            }
        });

        // Switch discapacidad
        Switch sw2 = (Switch) view.findViewById(R.id.switch2);
        if (discapacidad.equals(0)) {
            sw2.setChecked(false);
        }
        else if (discapacidad.equals(1)) {
            sw2.setChecked(true);
        }

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE perfil SET switch1 = 1 WHERE id = 'discapacidad';");
                }
                else {
                    db.execSQL("UPDATE perfil SET switch1 = 0 WHERE id = 'discapacidad';");
                }
            }
        });


        // Switch familianumerosa
        Switch sw3 = (Switch) view.findViewById(R.id.switch3);
        if (familianumerosa.equals(0)) {
            sw3.setChecked(false);
        }
        else if (familianumerosa.equals(1)) {
            sw3.setChecked(true);
        }

        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE perfil SET switch1 = 1 WHERE id = 'familianumerosa';");
                }
                else {
                    db.execSQL("UPDATE perfil SET switch1 = 0 WHERE id = 'familianumerosa';");
                }
            }
        });


        // Switch enfermedad cronica
        Switch sw4 = (Switch) view.findViewById(R.id.switch4);
        if (cronica.equals(0)) {
            sw4.setChecked(false);
        }
        else if (cronica.equals(1)) {
            sw4.setChecked(true);
        }

        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.execSQL("UPDATE perfil SET switch1 = 1 WHERE id = 'enfermedadcronica';");
                }
                else {
                    db.execSQL("UPDATE perfil SET switch1 = 0 WHERE id = 'enfermedadcronica';");
                }
            }
        });

        // Edit text con direcciones

        AutoCompleteTextView casa = (AutoCompleteTextView) view.findViewById(R.id.editTextBuscar);
        AutoCompleteTextView trabajo = (AutoCompleteTextView) view.findViewById(R.id.editTextBuscar2);

        if (!direccioncasa.equals("")) {
            casa.setText(direccioncasa);
        }
        if (!direcciontrabajo.equals("")) {
            trabajo.setText(direcciontrabajo);
        }

        casa.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                db.execSQL("DELETE FROM perfil WHERE id = 'direccioncasa';");
                db.execSQL("INSERT INTO perfil(id, textbox1) values ('direccioncasa','"+s.toString()+"')");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        trabajo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                db.execSQL("DELETE FROM perfil WHERE id = 'direcciontrabajo';");
                db.execSQL("INSERT INTO perfil(id, textbox1) values ('direcciontrabajo','"+s.toString()+"')");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Spinner con los centros

        Spinner spinner =  (Spinner) view.findViewById(R.id.spinner);

        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);

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
        spinner2.setAdapter(adapter);

        // Marcamos el seleccionado

        spinner.setSelection(Integer.parseInt(idescuela));
        spinner2.setSelection(Integer.parseInt(idescuelaold));

        // Listener para saber el item seleccionado del spinner de hermanos escolarizados
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                System.out.println("Spinner: " + parent.getItemAtPosition(position).toString());

                idescuela = Integer.toString(position);

                nombreescuela = parent.getItemAtPosition(position).toString();

                direccionescuela = parent.getItemAtPosition(position).toString();

                parent.setSelection(Integer.parseInt(idescuela));

                db.execSQL("DELETE FROM perfil WHERE id = 'hermanosescolarizados';");
                db.execSQL("INSERT INTO perfil(id, spinner1,spinner2, spinner3) values ('hermanosescolarizados','"+Integer.toString(parent.getSelectedItemPosition())+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"')");
                System.out.println("INSERT INTO perfil(id, spinner1, spinner2, spinner3) values ('hermanosescolarizados','"+parent.getSelectedItemPosition()+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"')");
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

        // Listener para saber el item seleccionado del spinner de familiares escolarizados
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                System.out.println("Spinner: " + parent.getItemAtPosition(position).toString());

                idescuelaold = Integer.toString(position);

                nombreescuelaold = parent.getItemAtPosition(position).toString();

                direccionescuelaold = parent.getItemAtPosition(position).toString();

                parent.setSelection(Integer.parseInt(idescuelaold));

                db.execSQL("DELETE FROM perfil WHERE id = 'escuelafamilia';");
                db.execSQL("INSERT INTO perfil(id, spinner1,spinner2, spinner3) values ('escuelafamilia','"+Integer.toString(parent.getSelectedItemPosition())+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"')");
                System.out.println("INSERT INTO perfil(id, spinner1, spinner2, spinner3) values ('escuelafamilia',"+parent.getSelectedItemPosition()+"','"+parent.getItemAtPosition(position).toString()+"','"+parent.getItemAtPosition(position).toString()+"')");
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
                System.out.println("Else del GetHTTPData en FragmentPerfil");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Catch del GetHTTPData en FragmentPerfil");
            Toast.makeText(getActivity().getApplicationContext(), R.string.errorconexion, Toast.LENGTH_SHORT).show();

        }finally {
            System.out.println("Finally del GetHTTPData en FragmentPerfil");
        }
        // Return the data from specified url
        return stream;
    }

    private class AutoCompleteAdapter extends ArrayAdapter<Address> implements Filterable {

        private LayoutInflater mInflater;
        private Geocoder mGeocoder;
        private StringBuilder mSb = new StringBuilder();

        public AutoCompleteAdapter(final Context context) {
            super(context, -1);
            mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            mGeocoder = new Geocoder(context);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = (TextView) mInflater.inflate(android.R.layout.simple_selectable_list_item, parent, false);
            }

            tv.setText(createFormattedAddressFromAddress(getItem(position)));
            return tv;
        }

        private String createFormattedAddressFromAddress(final Address address) {
            mSb.setLength(0);
            final int addressLineSize = address.getMaxAddressLineIndex();
            for (int i = 0; i < addressLineSize; i++) {
                mSb.append(address.getAddressLine(i));
                if (i != addressLineSize - 1) {
                    mSb.append(", ");
                }
            }
            return mSb.toString();
        }

        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(final CharSequence constraint) {
                    List<Address> addressList = null;
                    if (constraint != null) {
                        try {
                            addressList = mGeocoder.getFromLocationName((String) constraint, 5);
                        } catch (IOException e) {
                        }
                    }
                    if (addressList == null) {
                        addressList = new ArrayList<Address>();
                    }

                    final FilterResults filterResults = new FilterResults();
                    filterResults.values = addressList;
                    filterResults.count = addressList.size();

                    return filterResults;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(final CharSequence contraint, final FilterResults results) {
                    clear();
                    for (Address address : (List<Address>) results.values) {
                        add(address);
                    }
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }

                @Override
                public CharSequence convertResultToString(final Object resultValue) {
                    return resultValue == null ? "" : createFormattedAddressFromAddress((Address) resultValue);
                }
            };
            return myFilter;
        }
    }

}
