package com.devbaltasarq.androideseipracticas.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devbaltasarq.androideseipracticas.R;
import com.devbaltasarq.androideseipracticas.core.Practica;

import java.util.ArrayList;

public class PracticaArrayAdapter extends ArrayAdapter<Practica> {
    public PracticaArrayAdapter(Context context, ArrayList<Practica> practicas)
    {
        super( context, 0, practicas );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        final LayoutInflater INFLATER = LayoutInflater.from( this.getContext() );
        final Practica PRACTICA = this.getItem( position );

        if ( convertView == null ) {
            convertView = INFLATER.inflate( R.layout.entrada_practica, null );
        }

        final TextView LBL_ASIGNATURA = convertView.findViewById( R.id.lblAsignatura );
        final TextView LBL_TRABAJO = convertView.findViewById( R.id.lblTrabajo );

        LBL_ASIGNATURA.setText( PRACTICA.getAsignatura() );
        LBL_TRABAJO.setText( PRACTICA.getTrabajo() );

        return convertView;
    }
}
