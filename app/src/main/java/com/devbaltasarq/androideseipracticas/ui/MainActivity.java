package com.devbaltasarq.androideseipracticas.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.devbaltasarq.androideseipracticas.R;
import com.devbaltasarq.androideseipracticas.core.Practica;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa
        this.practicas = new ArrayList<>();

        // Listeners
        final Button BT_INSERTA = this.findViewById( R.id.btInserta );
        final ListView LV_PRACTICAS = this.findViewById( R.id.lvPracticas );

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.inserta();
            }
        });

        this.adaptador = new PracticaArrayAdapter(
                this, this.practicas
        );

        LV_PRACTICAS.setAdapter( this.adaptador );

        LV_PRACTICAS.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.this.borrarNum( i );
                return true;
            }
        });

        // Actualiza la vista
        this.muestraEstado();
    }

    private void inserta()
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
                }

                MainActivity.this.muestraEstado();
            }
        });

        dlg.show();
    }

    private void borrarNum(int pos)
    {
        this.practicas.remove( pos );
        this.adaptador.notifyDataSetChanged();
        this.muestraEstado();
    }

    private void muestraEstado()
    {
        final TextView LBL_PRACTICAS = this.findViewById( R.id.lblPracticas );

        LBL_PRACTICAS.setText(
                Integer.toString( this.practicas.size() )
                + " tarea(s)."
        );
    }

    private PracticaArrayAdapter adaptador;
    private ArrayList<Practica> practicas;
}