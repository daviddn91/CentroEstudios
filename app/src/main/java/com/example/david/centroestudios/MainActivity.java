package com.example.david.centroestudios;

import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.david.centroestudios.fragments.FragmentBuscar;
import com.example.david.centroestudios.fragments.FragmentCercanas;
import com.example.david.centroestudios.fragments.FragmentContacta;
import com.example.david.centroestudios.fragments.FragmentFaqs;
import com.example.david.centroestudios.fragments.FragmentPreferencias;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FragmentCercanas fcercanas;
    FragmentBuscar fbuscar;
    FragmentPreferencias fpreferencias;
    FragmentFaqs ffaqs;
    FragmentContacta fcontacta;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fcercanas = new FragmentCercanas();
        fbuscar = new FragmentBuscar();
        fpreferencias = new FragmentPreferencias();
        ffaqs = new FragmentFaqs();
        fcontacta = new FragmentContacta();

        /* Cambio el fragment por defecto al abrir la aplicación */

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fpreferencias);
        ftrans.commit();

        /* Base de datos */
        db = openOrCreateDatabase("BaseDeDatos", Context.MODE_PRIVATE, null);

        // Creamos la tabla filtros con las preferencias
        db.execSQL("CREATE TABLE IF NOT EXISTS filtros (nina INTEGER, nino INTEGER, publico INTEGER, concertado INTEGER, privado INTEGER, religioso INTEGER, laico INTEGER, castellano INTEGER, catalan INTEGER, ingles INTEGER, frances INTEGER, aleman INTEGER);");

        // Ahora miramos si no existe nada en la tabla e insertamos los valores por defecto
        Cursor c = db.rawQuery("SELECT * FROM filtros", null);
        if(!c.moveToFirst())
        {
            db.execSQL("INSERT INTO filtros (nina, nino, publico, concertado, privado, religioso, laico, castellano, catalan, ingles, frances, aleman) VALUES (1,1,1,1,1,1,1,1,1,1,0,0);");
        }

        /* Asignamos valores a los switch y radiobutton segun la BD */
        c = db.rawQuery("SELECT * FROM filtros", null);
        while(c.moveToNext()) {
            Integer soloninas = c.getInt(0);
            Integer soloninos = c.getInt(1);
            Integer centropublico = c.getInt(2);
            Integer centroconcertado = c.getInt(3);
            Integer centroprivado = c.getInt(4);
            Integer religioso = c.getInt(5);
            Integer laico = c.getInt(6);
            Integer idiomacastellano = c.getInt(7);
            Integer idiomacatalan = c.getInt(8);
            Integer idiomaingles = c.getInt(9);
            Integer idiomafrances = c.getInt(10);
            Integer idiomaaleman = c.getInt(11);

            // TextView en content main para probar que la BD funciona
            TextView textview10 =(TextView)findViewById(R.id.textView10);
            textview10.setText(soloninos.toString());

            if (soloninas.equals(1) && soloninos.equals(1)) {
                textview10.setText("2");
            }
            else if (soloninas.equals(1) && soloninos.equals(0)){
                //rbsoloninas.setChecked(true);
            }
            else {
                //rbsoloninos.setSelected(true);
                //rggenero.check(R.id.radioButton2);
            }
            //Switch swpublico = (Switch)findViewById(R.id.switch3);
            //swpublico.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();

        if (id == R.id.nav_cercanas) {
            ftrans.replace(R.id.container, fcercanas);
        } else if (id == R.id.nav_buscar) {
            ftrans.replace(R.id.container, fbuscar);
        } else if (id == R.id.nav_manage) {
            ftrans.replace(R.id.container, fpreferencias);
        } else if (id == R.id.nav_share) {
            ftrans.replace(R.id.container, ffaqs);
        } else if (id == R.id.nav_send) {
            ftrans.replace(R.id.container, fcontacta);
        } ftrans.commit();

        /* Creo que esto cierra el menu lateral */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
