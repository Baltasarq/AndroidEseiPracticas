package com.devbaltasarq.androideseipracticas.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.devbaltasarq.androideseipracticas.R;
import com.devbaltasarq.androideseipracticas.core.Orm;
import com.devbaltasarq.androideseipracticas.core.Practica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static int RC_NUEVA_PRACTICA = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa
        //this.practicas = new ArrayList<>();

        // Listeners
        final Button BT_INSERTA = this.findViewById( R.id.btInserta );
        final ListView LV_PRACTICAS = this.findViewById( R.id.lvPracticas );

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.inserta();
            }
        });

        this.adaptador = new SimpleCursorAdapter(
                this,
                R.layout.entrada_practica,
                null,
                new String[]{ Orm.CAMPO_ASIGNATURA, Orm.CAMPO_TRABAJO },
                new int[]{ R.id.lblAsignatura, R.id.lblTrabajo }
        );

        LV_PRACTICAS.setAdapter( this.adaptador );

        /*
        // Usado cuando se borraba un elemento mediante el pulsado largo.

        LV_PRACTICAS.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.borrarNum( i );
                return true;
            }
        });
        */

        this.registerForContextMenu( LV_PRACTICAS );

        // Actualiza la vista
        this.muestraEstado();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        /*
        // Ya no se usan las preferencias
        final SharedPreferences PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE );
        final Set<String> PRACTICAS = PREFS.getStringSet( "practicas", new HashSet<String>() );

        this.practicas.clear();
        for(String strPractica: PRACTICAS) {
            String[] partesPractica = strPractica.split( ":" );

            if ( partesPractica.length > 1 ) {
                this.practicas.add(
                        new Practica(
                                partesPractica[ 0 ],
                                partesPractica[ 1 ] )
                );
            } else {
                Log.e( LOG_TAG, "practica incorrecta: '" + strPractica + "'" );
            }
        }

        this.adaptador.notifyDataSetChanged();
        */



        this.orm = new Orm( this.getApplicationContext() );

        // Esta es la forma incorrecta de hacerlo, aún manteniendo buena parte de la estructura
        /*
        this.practicas.clear();
        this.practicas.addAll( Arrays.asList( this.orm.recuperaTodo() ) );
        this.adaptador.notifyDataSetChanged();
         */

        this.adaptador.changeCursor( this.orm.getAllCursor() );

        this.muestraEstado();
    }

    /*
    // No es necesario, pues en cuanto se añade se guarda
    @Override
    public void onPause()
    {
        super.onPause();

        final SharedPreferences.Editor PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE ).edit();

        // Crear conjunto de trabajos
        Set<String> practicas = new HashSet<>();

        for(Practica p: this.practicas) {
            practicas.add( p.toString() );
        }

        PREFS.putStringSet( "practicas", practicas );
        PREFS.apply();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.getMenuInflater().inflate( R.menu.menu_ppal, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.opInserta:
                this.inserta();
                break;
        }

        return toret;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if ( v.getId() == R.id.lvPracticas ) {
            this.getMenuInflater().inflate( R.menu.menu_ctx, menu );
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        boolean toret = false;

        switch( item.getItemId() ) {
            case R.id.opBorra:
                int pos = ( (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo() ).position;
                this.borraNum( pos );
                break;
        }

        return toret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if ( requestCode == RC_NUEVA_PRACTICA
          && resultCode == Activity.RESULT_OK )
        {
            final Practica PRACTICA_NUEVA =
                    new Practica(
                            data.getExtras().getString( "asignatura" ),
                            data.getExtras().getString( "trabajo" ) );

            /*
            // Ya no es necesario guardar en preferencias
            final SharedPreferences PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE );
            final SharedPreferences.Editor EDIT_PREFS = this.getSharedPreferences( "prefs", MODE_PRIVATE ).edit();
            final Set<String> PRACTICAS = PREFS.getStringSet( "practicas", new HashSet<String>() );

            PRACTICAS.add( PRACTICA_NUEVA.toString() );
            EDIT_PREFS.putStringSet( "practicas", PRACTICAS );
            EDIT_PREFS.apply();
            */

            this.orm.guarda( PRACTICA_NUEVA );
        }

        return;
    }

    /*
    // Esta fn. era inserta() cuando para insertar un nuevo trabajo se utilizaba un dlg.
    private void insertaConDialogo()
    {
        AlertDialog.Builder dlg = new AlertDialog.Builder( this );
        final EditText ED_PRACTICA = new EditText( this );

        dlg.setTitle( "Práctica (asignatura: práctica)" );
        dlg.setView( ED_PRACTICA );
        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] partes = ED_PRACTICA.getText().toString().split( ":" );

                if (partes.length > 1 ) {
                    MainActivity.this.adaptador.add(
                            new Practica( partes[ 0 ], partes[ 1 ] )
                    );
                } else {
                    Toast.makeText( MainActivity.this,
                            "Error: siga el formato:\n <asignatura>: <trabajo>",
                                 Toast.LENGTH_LONG ).show();
                }

                MainActivity.this.muestraEstado();
            }
        });

        dlg.show();
    }*/

    private void inserta()
    {
        this.startActivityForResult( new Intent( this, NuevaPractica.class ), RC_NUEVA_PRACTICA );
    }

    private void borraNum(int pos)
    {
        // this.practicas.remove( pos );
        // this.adaptador.notifyDataSetChanged();
        final Cursor CURSOR = this.adaptador.getCursor();

        CURSOR.moveToPosition( pos );
        int id = CURSOR.getInt( CURSOR.getColumnIndexOrThrow( Orm.CAMPO_ID ) );
        this.orm.borra( id );
        this.adaptador.changeCursor( this.orm.getAllCursor() );

        this.muestraEstado();
    }

    private void muestraEstado()
    {
        final TextView LBL_PRACTICAS = this.findViewById( R.id.lblPracticas );

        LBL_PRACTICAS.setText(
                Integer.toString( this.adaptador.getCount() )
                + " tarea(s)."
        );
    }

    private SimpleCursorAdapter adaptador;
    //private ArrayList<Practica> practicas;
    private Orm orm;
}
