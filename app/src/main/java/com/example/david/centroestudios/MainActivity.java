package com.example.david.centroestudios;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.david.centroestudios.fragments.FragmentBuscar;
import com.example.david.centroestudios.fragments.FragmentCercanas;
import com.example.david.centroestudios.fragments.FragmentContacta;
import com.example.david.centroestudios.fragments.FragmentFaqs;
import com.example.david.centroestudios.fragments.FragmentPerfil;
import com.example.david.centroestudios.fragments.FragmentPreferencias;
import com.example.david.centroestudios.fragments.FragmentMapaBuscar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FragmentCercanas fcercanas;
    FragmentBuscar fbuscar;
    FragmentPreferencias fpreferencias;
    FragmentFaqs ffaqs;
    FragmentContacta fcontacta;
    FragmentMapaBuscar fmapabuscar;
    FragmentPerfil fperfil;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        requestPermission2();
        while (!checkPermission()) {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        fmapabuscar = new FragmentMapaBuscar();
        fperfil = new FragmentPerfil();

        /* Cambio el fragment por defecto al abrir la aplicación */

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fcercanas);
        ftrans.commit();

        // Esta linea es importante y hace que quede marcada la primera opcion del menu cuando abramos la app
        navigationView.getMenu().getItem(1).setChecked(true);

        /* Base de datos */
        db = openOrCreateDatabase("BaseDeDatos", Context.MODE_PRIVATE, null);

        Cursor c = db.rawQuery("SELECT * FROM centros WHERE id = '12345678910'", null);

        if(!c.moveToFirst())
        {
            db.execSQL("DROP TABLE perfil");
            db.execSQL("DROP TABLE filtros");
            db.execSQL("DROP TABLE centros");
        }

        // Creamos la tabla filtros con las preferencias
        db.execSQL("CREATE TABLE IF NOT EXISTS filtros (nina INTEGER, nino INTEGER, publico INTEGER, concertado INTEGER, privado INTEGER, religioso INTEGER, laico INTEGER, castellano INTEGER, catalan INTEGER, ingles INTEGER, frances INTEGER, aleman INTEGER, infantil1 INTEGER, infantil2 INTEGER, primaria INTEGER, eso INTEGER, bachillerato INTEGER);");

        // Ahora miramos si no existe nada en la tabla e insertamos los valores por defecto
        c = db.rawQuery("SELECT * FROM filtros", null);
        if(!c.moveToFirst())
        {
            db.execSQL("INSERT INTO filtros (nina, nino, publico, concertado, privado, religioso, laico, castellano, catalan, ingles, frances, aleman, infantil1, infantil2, primaria, eso, bachillerato) VALUES (1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1);");
        }

        // Creamos la tabla con la info de los colegios y borramos los que sean muy antiguos
        db.execSQL("CREATE TABLE IF NOT EXISTS centros (id VARCHAR(100), nombre VARCHAR(100), direccion VARCHAR(1000), codigopostal VARCHAR(10), telefono VARCHAR(15), localidad VARCHAR(100), infantil1 VARCHAR(1), infantil2 VARCHAR(1), primaria VARCHAR(1), eso VARCHAR(1), bachillerato VARCHAR(1), actualizado VARCHAR(100), latitud VARCHAR(1000), longitud VARCHAR(1000));");

        db.execSQL("INSERT INTO centros (id) VALUES ('12345678910');");

        //db.execSQL("DELETE FROM centros WHERE actualizado < sysdate()-30;");

        db.execSQL("CREATE TABLE IF NOT EXISTS perfil (id VARCHAR(100), spinner1 VARCHAR(100), spinner2 VARCHAR(100), spinner3 VARCHAR(100), textbox1 VARCHAR(100), switch1 INTEGER(1));");

        // Ahora miramos si no existe nada en la tabla e insertamos los valores por defecto
        c = db.rawQuery("SELECT * FROM perfil", null);
        if(!c.moveToFirst())
        {
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('hermanosescolarizados','0','-','-','-','1');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('escuelafamilia','0','-','-','-','1');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('direccioncasa','-','-','-','','1');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('direcciontrabajo','-','-','-','','1');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('rentaminima','-','-','-','-','0');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('discapacidad','-','-','-','-','0');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('familianumerosa','-','-','-','-','0');");
            db.execSQL("INSERT INTO perfil (id, spinner1, spinner2, spinner3, textbox1, switch1) VALUES ('enfermedadcronica','-','-','-','-','0');");
        }

    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public void onBackPressed() {

        FragmentManager manager = getFragmentManager();
        int count = manager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            manager.popBackStack();
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
            ftrans.replace(R.id.container, fcercanas).addToBackStack(null);
        } else if (id == R.id.nav_buscar) {
            ftrans.replace(R.id.container, fbuscar).addToBackStack(null);
        } else if (id == R.id.nav_perfil) {
            ftrans.replace(R.id.container, fperfil).addToBackStack(null);
        } else if (id == R.id.nav_manage) {
            ftrans.replace(R.id.container, fpreferencias).addToBackStack(null);
        } else if (id == R.id.nav_share) {
            ftrans.replace(R.id.container, ffaqs).addToBackStack(null);
        } else if (id == R.id.nav_send) {
            ftrans.replace(R.id.container, fcontacta).addToBackStack(null);
        } ftrans.commit();

        /* Creo que esto cierra el menu lateral */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirMapaBuscar() {

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.container, fmapabuscar).addToBackStack(null);
        ftrans.commit();
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(this.getApplicationContext(),R.string.gpspermiso, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        return;
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission2(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.INTERNET)){

            Toast.makeText(this.getApplicationContext(),R.string.gpspermiso, Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.INTERNET},1);
        }

        return;
    }
}
