package com.devbaltasarq.androideseipracticas.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.devbaltasarq.androideseipracticas.R;

public class NuevaPractica extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_nueva_practica );

        final Button BT_INSERTA = this.findViewById( R.id.btInserta );
        final Button BT_CANCELA = this.findViewById( R.id.btCancela );

        BT_CANCELA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaPractica.this.finish();
            }
        });

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaPractica.this.guarda();
            }
        });
    }

    private void guarda()
    {
        final EditText ED_ASIGNATURA = this.findViewById( R.id.edAsignatura );
        final EditText ED_TRABAJO = this.findViewById( R.id.edTrabajo );
        final Intent DATOS = new Intent();

        DATOS.putExtra( "asignatura", ED_ASIGNATURA.getText().toString() );
        DATOS.putExtra( "trabajo", ED_TRABAJO.getText().toString() );

        this.setResult( Activity.RESULT_OK, DATOS );
        this.finish();
    }
}
