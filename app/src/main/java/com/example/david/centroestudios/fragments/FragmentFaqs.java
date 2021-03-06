package com.example.david.centroestudios.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.david.centroestudios.Faqs;
import com.example.david.centroestudios.FaqsAdapter;
import com.example.david.centroestudios.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFaqs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFaqs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFaqs extends Fragment {
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

    private OnFragmentInteractionListener mListener;

    // Variable para determinar los dpi y ajustar el padding izquierdo segun eso
    private float scale;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFaqs.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFaqs newInstance(String param1, String param2) {
        FragmentFaqs fragment = new FragmentFaqs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFaqs() {
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
        getActivity().setTitle(R.string.faqs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.faqs);
        // Inflate the layout for this fragment

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(4).setChecked(true);

        View view = inflater.inflate(R.layout.fragment_faqs, null);

        List<Faqs> items = new ArrayList<>();

        items.add(new Faqs(getResources().getString(R.string.faqs_pregunta5),getResources().getString(R.string.faqs_respuesta5)));
        items.add(new Faqs(getResources().getString(R.string.faq_pregunta1),getResources().getString(R.string.faq_respuesta1)));
        items.add(new Faqs(getResources().getString(R.string.faq_pregunta2),getResources().getString(R.string.faq_respuesta2)));
        items.add(new Faqs(getResources().getString(R.string.faq_pregunta3),getResources().getString(R.string.faq_respuesta3)));
        items.add(new Faqs(getResources().getString(R.string.faq_pregunta4),getResources().getString(R.string.faq_respuesta4)));
        items.add(new Faqs(getResources().getString(R.string.faqs_pregunta6),getResources().getString(R.string.faqs_respuesta6)));


        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.recicladorFaqs);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new FaqsAdapter(items);
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

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private String[] preguntas = { getResources().getString(R.string.faq_pregunta1), getResources().getString(R.string.faq_pregunta2), getResources().getString(R.string.faq_pregunta3)};

        private String[][] respuestas = {
                { getResources().getString(R.string.faq_respuesta1) },
                { getResources().getString(R.string.faq_respuesta2) },
                { getResources().getString(R.string.faq_respuesta3) }
        };

        @Override
        public int getGroupCount() {
            return preguntas.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return respuestas[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return preguntas[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return respuestas[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(FragmentFaqs.this.getActivity());
            textView.setText(getGroup(i).toString());
            textView.setTextSize(18);
            int padding = (int) (40 * scale + 0.5f);
            textView.setPadding(padding, 25, 0, 25);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(FragmentFaqs.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(16);
            int padding = (int) (50 * scale + 0.5f);
            textView.setPadding(padding, 0, 0, 25);
            textView.setTextColor(Color.BLACK);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }


    }

}
