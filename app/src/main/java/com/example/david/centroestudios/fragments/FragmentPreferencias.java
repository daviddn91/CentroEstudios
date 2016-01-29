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
    Integer idiomafrances;
    Integer idiomaaleman;

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
            idiomafrances = c.getInt(9);
            idiomaaleman = c.getInt(10);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preferencias, container, false);

        if (centropublico.equals(0)) {
            Switch sw3 = (Switch) view.findViewById(R.id.switch3);
            sw3.setChecked(false);
        }
        else if (centropublico.equals(1)) {
            Switch sw3 = (Switch) view.findViewById(R.id.switch3);
            sw3.setChecked(true);
        }
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
