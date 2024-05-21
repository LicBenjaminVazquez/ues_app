package mx.ues.pasa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText usuario, password;
    private TextView tOlvide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        usuario = findViewById(R.id.etUsuario);
        password = findViewById(R.id.etPass);
        tOlvide = findViewById(R.id.tOlvide);
        tOlvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(home);
                Toast.makeText(getApplicationContext(),"Olvidaste tu contrasena",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void IngresarHome(View view) {
        String url = "https://server/api/login?expediente="+ usuario.getText() + "&password=" + password.getText();
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject user = new JSONObject(response);
                       if(user.getInt("respuesta") == 1){
                        Toast.makeText(getApplicationContext(),user.getString("mensaje"),Toast.LENGTH_LONG).show();
                        //SharedPreferences settings;
                        //SharedPreferences.Editor editor;
                        //settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        //editor = settings.edit();
                        //USER = response;
                        //editor.putString("USER", response);
                        //editor.commit();

                        // intent
                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(home);

                    }else{
                       Toast.makeText(getApplicationContext(), "¡¡¡" + user.getString("mensaje").toUpperCase() + "!!!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "¡¡¡ERROR DE COMUNICACION 1!!!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "¡¡¡ERROR DE COMUNICACION 2!!!"+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void Registro(View view){
        Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(registro);
    }
}