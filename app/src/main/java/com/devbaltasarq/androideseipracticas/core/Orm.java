package com.devbaltasarq.androideseipracticas.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Orm extends SQLiteOpenHelper {
    private static int Version = 3;
    private static String Nombre = "practicas_esei";
    private static String LOG_TAG = "ORM";
    private static String TABLA_PRACTICAS = "practicas";
    public static String CAMPO_ID = "_id";
    public static String CAMPO_ASIGNATURA = "asignatura";
    public static String CAMPO_TRABAJO = "trabajo";

    public Orm(Context c)
    {
        super( c, Nombre, null, Version );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            db.beginTransaction();

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLA_PRACTICAS + "("
                    + CAMPO_ID + " integer PRIMARY KEY NOT NULL,"
                    + CAMPO_ASIGNATURA + " string(80) NOT NULL,"
                    + CAMPO_TRABAJO + " string(255) NOT NULL)"
            );

            db.setTransactionSuccessful();
        }
        catch(SQLException exc) {
            Log.e( LOG_TAG, "creando base de datos: " + exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try {
            db.beginTransaction();

            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_PRACTICAS );

            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e(LOG_TAG, "eliminando la base de datos: " + exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }

    public void guarda(Practica p)
    {
        final SQLiteDatabase DB = this.getWritableDatabase();
        final ContentValues VALUES = new ContentValues();

        // Asignar los valores de los datos
        VALUES.put( CAMPO_ASIGNATURA, p.getAsignatura() );
        VALUES.put( CAMPO_TRABAJO, p.getTrabajo() );

        try {
            DB.beginTransaction();
            DB.insert( TABLA_PRACTICAS, null, VALUES );
            DB.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( LOG_TAG, "guardando practica: " + exc.getMessage() );
        } finally {
            DB.endTransaction();
        }

        return;
    }

    public void borra(int id)
    {
        final SQLiteDatabase DB = this.getWritableDatabase();

        try {
            DB.beginTransaction();
            Log.i( LOG_TAG, "deleting row with id: " + id );

            DB.delete(
                TABLA_PRACTICAS,
                CAMPO_ID + "=?",
                new String[]{ Integer.toString( id ) } );

            DB.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( LOG_TAG, "borrando id: " + id + ": " + exc.getMessage() );
        } finally {
            DB.endTransaction();
        }

    }

    public Cursor getAllCursor()
    {
        return this.getReadableDatabase().query(
                                            TABLA_PRACTICAS,
                                            null, null, null, null, null, null
        );
    }

    /** @return un array con todos los objetos de la base de datos. NO RECOMENDADO. */
    public Practica[] recuperaTodo()
    {
        final List<Practica> TORET = new ArrayList<>();
        final SQLiteDatabase DB = this.getReadableDatabase();

        Cursor cursor = DB.query( TABLA_PRACTICAS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null );

        if ( cursor.moveToFirst() ) {
            do {
                String asignatura = cursor.getString(
                                        cursor.getColumnIndexOrThrow( CAMPO_ASIGNATURA ) );
                String trabajo = cursor.getString(
                                        cursor.getColumnIndexOrThrow( CAMPO_TRABAJO ) );

                TORET.add( new Practica( asignatura, trabajo ) );
            } while( cursor.moveToNext() );
        }

        cursor.close();
        return TORET.toArray( new Practica[ 0 ] );
    }
}
