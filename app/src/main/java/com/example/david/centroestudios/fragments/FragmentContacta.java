package com.example.david.centroestudios.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.centroestudios.MainActivity;
import com.example.david.centroestudios.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentContacta.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentContacta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContacta extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText editNombre;
    EditText editMail;
    EditText editComentario;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentContacta.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentContacta newInstance(String param1, String param2) {
        FragmentContacta fragment = new FragmentContacta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentContacta() {
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
        getActivity().setTitle(R.string.contactar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Cambia el texto del titulo al nombre de la seccion
        getActivity().setTitle(R.string.contactar);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contacta, container, false);

        requestPermission();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Button button = (Button) view.findViewById(R.id.button);
            editNombre = (EditText) view.findViewById(R.id.editText);
            editMail = (EditText) view.findViewById(R.id.editText2);
            editComentario = (EditText) view.findViewById(R.id.editText3);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombre = editNombre.getText().toString();
                    String mail = editMail.getText().toString();
                    String comentario = editComentario.getText().toString();

                    String data = "Data: Valor";

                    if (nombre.length() < 1 || mail.length() < 1 || comentario.length() < 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Por favor rellena todos los campos para enviar el mensaje.", Toast.LENGTH_SHORT).show();
                    } else if (!mail.contains("@") || !mail.contains(".")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Por favor introduce un mail correcto.", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("Nombre: " + nombre);
                        System.out.println("Mail: " + mail);
                        System.out.println("Comentario: " + comentario);

                        try {
                            data = GetText(nombre, mail, comentario);
                            System.out.println("Data:" + data);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (!data.equals("0")) {
                            Toast.makeText(getActivity().getApplicationContext(), "Mensaje enviado correctamente.", Toast.LENGTH_SHORT).show();
                            editNombre.setText("");
                            editMail.setText("");
                            editComentario.setText("");
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "No se ha podido enviar el mensaje. Por favor revisa la conexión a Internet y si el problema persiste por favor contacta en david.delgado@est.fib.upc.edu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
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


    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.INTERNET)){

            Toast.makeText(getActivity().getApplicationContext(), "We need internet to obtain schools information", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.INTERNET},1);
        }
    }


    public  String  GetText(String nombre, String mail, String comentario)  throws  UnsupportedEncodingException
    {
        // Create data variable for sent values to server

        String data = URLEncoder.encode("nombre", "UTF-8")
                + "=" + URLEncoder.encode(nombre, "UTF-8");

        data += "&" + URLEncoder.encode("mail", "UTF-8") + "="
                + URLEncoder.encode(mail, "UTF-8");

        data += "&" + URLEncoder.encode("comentario", "UTF-8")
                + "=" + URLEncoder.encode(comentario, "UTF-8");

        BufferedReader reader=null;

        String result = "0";

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://raspi.cat/api.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            result = sb.toString();

        }
        catch(Exception ex)
        {
            System.out.println("Pete enviar comentario en FragmentContacta");
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                System.out.println("Pete enviar comentario en FragmentContacta");
            }
        }

        return result;

    }

}

