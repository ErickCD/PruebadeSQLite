package com.clair.ecda.pruebadesqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtDni, txtNombre, txtCiudad, txtNumero;
    private Button btnSave, btnConsultDni, btnDelete, btnDataModification;
    AdminSQLiteOpenHelper admin;
    SQLiteDatabase db;
    String dni, name, city, number;
    Context context;

    //Pagina en que se baso
    //https://androidstudiofaqs.com/tutoriales/usar-sqlite-en-android-studio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        //Plain text
        txtDni = (EditText) findViewById(R.id.txt_dni);
        txtNombre = (EditText) findViewById(R.id.txt_name_surname);
        txtCiudad = (EditText) findViewById(R.id.txt_city);
        txtNumero = (EditText) findViewById(R.id.txt_number);
        //Buttons
        btnSave = (Button) findViewById(R.id.btn_save);
        btnConsultDni = (Button) findViewById(R.id.btn_consult_dni);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDataModification = (Button) findViewById(R.id.btn_data_modification);
        //Database
        admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = txtDni.getText().toString();
                name = txtNombre.getText().toString();
                city = txtCiudad.getText().toString();
                number = txtNumero.getText().toString();

                if (dni.length() == 0){
                    Toast.makeText(context, "Debe llenar el dni", Toast.LENGTH_SHORT).show();
                } else {
                    if (name.length() == 0){
                        Toast.makeText(context, "Debe llenar el nombre", Toast.LENGTH_SHORT).show();
                    } else {
                        if (city.length() == 0) {
                            Toast.makeText(context, "Debe llenar la ciudad", Toast.LENGTH_SHORT).show();
                        } else {
                            if (number.length() == 0){
                                Toast.makeText(context, "Debe llenar el numero", Toast.LENGTH_SHORT).show();
                            } else {
                                save();
                            }
                        }
                    }
                }
            }
        });

        btnConsultDni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = txtDni.getText().toString();

                if (dni.length() == 0){
                    Toast.makeText(context, "Debe llenar el dni", Toast.LENGTH_SHORT).show();
                } else {
                    search();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = txtDni.getText().toString();

                if (dni.length() == 0){
                    Toast.makeText(context, "Debe llenar el dni", Toast.LENGTH_SHORT).show();
                } else {
                    delete();
                }
            }
        });

        btnDataModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = txtDni.getText().toString();
                name = txtNombre.getText().toString();
                city = txtCiudad.getText().toString();
                number = txtNumero.getText().toString();

                if (dni.length() == 0){
                    Toast.makeText(context, "Debe llenar el dni", Toast.LENGTH_SHORT).show();
                } else {
                    if (name.length() == 0){
                        Toast.makeText(context, "Debe llenar el nombre", Toast.LENGTH_SHORT).show();
                    } else {
                        if (city.length() == 0) {
                            Toast.makeText(context, "Debe llenar la ciudad", Toast.LENGTH_SHORT).show();
                        } else {
                            if (number.length() == 0){
                                Toast.makeText(context, "Debe llenar el numero", Toast.LENGTH_SHORT).show();
                            } else {
                                modification();
                            }
                        }
                    }
                }
            }
        });
    }

    public void save() {
        db = admin.getWritableDatabase();

        dni = txtDni.getText().toString();
        name = txtNombre.getText().toString();
        city = txtCiudad.getText().toString();
        number = txtNumero.getText().toString();

        ContentValues registro = new ContentValues();

        registro.put("dni", dni);
        registro.put("nombre", name);
        registro.put("ciudad", city);
        registro.put("numero", number);

        //Se insertan los valores a la base de datos
        db.insert("usuario", null, registro);
        //Se cierra la base de datos
        db.close();
        //Se mandan valores vacios a los plain text
        txtDni.setText("");
        txtNombre.setText("");
        txtCiudad.setText("");
        txtNumero.setText("");
        //Mensaje al usuario para que vea que funciona bien
        Toast.makeText(context, "Los datos se han guardado de forma correcta", Toast.LENGTH_SHORT).show();
    }

    public void search() {
        db = admin.getWritableDatabase();

        dni = txtDni.getText().toString();

        Cursor row = db.rawQuery("select nombre, ciudad,  numero from usuario where dni = " + dni, null);
        if (row.moveToFirst()) {
            name = row.getString(0);
            city = row.getString(1);
            number = row.getString(2);

            txtNombre.setText(name);
            txtCiudad.setText(city);
            txtNumero.setText(number);

            //Toast.makeText(context, "Nombre = " + name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No existe ningun usuario con ese DNI", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void delete(){
        db = admin.getWritableDatabase();

        dni = txtDni.getText().toString();

        //Se borra de la base de datos;
        int cant = db.delete("usuario", "dni = " + dni, null);
        db.close();
        //Datos en blanco a los plan text
        txtDni.setText("");
        txtNombre.setText("");
        txtCiudad.setText("");
        txtNumero.setText("");

        if (cant == 1) {
            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No existe usuario", Toast.LENGTH_SHORT).show();
        }
    }

    public void modification(){
        db = admin.getWritableDatabase();

        dni = txtDni.getText().toString();
        name = txtNombre.getText().toString();
        city = txtCiudad.getText().toString();
        number = txtNumero.getText().toString();

        ContentValues registro  = new ContentValues();

        registro.put("nombre", name);
        registro.put("ciudad", city);
        registro.put("numero", number);

        int cant = db.update("usuario", registro, "dni = " + dni, null);
        db.close();

        if (cant == 1) {
            Toast.makeText(context, "Datos modificados con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No existe usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
