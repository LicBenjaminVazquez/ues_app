package mx.ues.pasa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity {
    private ArrayList<String> carreras;
    private Spinner spCarreras;
    private EditText tExpediente,tPassword,tNombre,tTelefono,tCorreo;
    private Button bRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spCarreras = findViewById(R.id.spCarreras);
        tExpediente = findViewById(R.id.tExpediente);
        tPassword = findViewById(R.id.tPassword);
        tNombre = findViewById(R.id.tNombre);
        tTelefono = findViewById(R.id.tTelefono);
        tCorreo = findViewById(R.id.tCorreo);

        carreras = new ArrayList<String>();
            String url = "https://api-pasa.dsoft-sonora.com/api/getMateria";
            Log.i("TEST", url);
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json_response = new JSONObject(response);
                        Log.i("TEST", response);
                        if( !json_response.getString("carreras").isEmpty() ){
                            String array_carreras[] = json_response.getString("carreras").split(",");
                            for(int i=0; i<array_carreras.length; i++){
                                carreras.add(array_carreras[i]);
                            }
                            ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter(getApplicationContext(), R.layout.carreras_item_layout, carreras);
                            spCarreras.setAdapter(adapter1);
                        }else{
                            Toast.makeText(getApplicationContext(), "¡¡¡" + json_response.getString("mensaje").toUpperCase() + "!!!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "NO HAY MATERIAS", Toast.LENGTH_LONG).show();
                        finish();
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "¡¡¡ERROR DE COMUNICACION!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "¡¡¡ERROR DE COMUNICACION!!!", Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

            bRegistrarse = (Button)findViewById(R.id.bRegistrarse);
            bRegistrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registrarse();
                }
            });

    }
    public void registrarse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Registro");
        builder.setMessage("¿Estás seguro de que quieres guardar la información?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Aquí colocas el código para guardar la información
                // Por ejemplo, puedes llamar a un método para guardar los datos en la base de datos
                guardarRegistro();
                //Toast.makeText(getApplicationContext(),"Entro",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada o mostrar un mensaje de cancelación
                dialog.dismiss(); // Cierra el cuadro de diálogo
            }
        });
        builder.show();
    }
    public void guardarRegistro(){
        String datos = "carrera=" +  Uri.encode(spCarreras.getSelectedItem().toString()) +
                "&expediente="+ Uri.encode(tExpediente.getText().toString()) +
                "&password=" +Uri.encode(tPassword.getText().toString()) +
                "&nombre=" +Uri.encode(tNombre.getText().toString()) +
                "&telefono=" +Uri.encode(tTelefono.getText().toString())+
                "&correo=" +Uri.encode(tCorreo.getText().toString());
        String url = "https://api-pasa.dsoft-sonora.com/api/register?"+datos;

        Log.i("TEST", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Entro",Toast.LENGTH_LONG).show();

                try {
                    JSONObject json_response = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), "¡¡¡" + json_response.getString("mensaje").toUpperCase() + "!!!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "¡¡¡" + "Error "+ e.toString() + "!!!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "¡¡¡" + "Error 2"+ error.toString() + "!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}