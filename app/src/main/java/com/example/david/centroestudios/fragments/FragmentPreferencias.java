package com.example.david.centroestudios.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.david.centroestudios.R;

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

    // Ids de los radiogroup para guardar en BD porque no puedo acceder directamente
    Integer idrb1;
    Integer idrb2;
    Integer idrb3;
    Integer idrb4;
    Integer idrb5;
    Integer idrb6;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferencias, container, false);

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



}
