package com.example.luiseduardo.infrafacil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.auth.User;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private User currentUser;
    private SharedPreferences sharedPreferences;
    private EditText editTextName;
    private EditText editTextPassword;
    private CheckBox checkBoxRememberPassword;

    private  String Name_Logged, id_login,username,Password_Logged,status;

    JSONParser jsonParser = new JSONParser();
    JSONObject object =null;

    public static ArrayList itenslogin = null;

    private static String urlAll = "http://futsexta.16mb.com/Proatec/Login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRememberPassword = findViewById(R.id.checkBoxRememberPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();
                boolean rememberPassword = checkBoxRememberPassword.isChecked();



                // PEGAR DADOS DO LOGIN
                //DadosLogin myDadosReturne = new DadosLogin();
                //String savedUsername = myDados.getUsername();
                //String savedPassword = myDados.getPassword();
                //String message = "Name: " + savedUsername + ", Password: " + savedPassword + ", Remember password: " + rememberPassword;
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                if (checkBoxRememberPassword.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", editTextName.getText().toString());
                    editor.putString("password", editTextPassword.getText().toString());
                    editor.putBoolean("rememberPassword", checkBoxRememberPassword.isChecked());
                    editor.apply();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.putBoolean("rememberPassword", checkBoxRememberPassword.isChecked());
                    editor.apply();
                }

                new Login.GetLogin().execute();
            }
        });

        checkBoxRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // O usuário selecionou "lembrar senha"
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", editTextName.getText().toString());
                    editor.putString("password", editTextPassword.getText().toString());
                    editor.putBoolean("rememberPassword", true);
                    editor.apply();

                } else {
                    // O usuário não selecionou "lembrar senha"
                    //editor.remove("rememberPassword");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.putBoolean("rememberPassword", false);
                    editor.apply();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean rememberPassword = sharedPreferences.getBoolean("rememberPassword", false);

        //String savedId = sharedPreferences.getString("id", "");

        editTextName.setText(savedUsername);
        editTextPassword.setText(savedPassword);
        if (rememberPassword) {
            checkBoxRememberPassword.setChecked(true);
        } else {
            checkBoxRememberPassword.setChecked(false);
        }
    }

    class GetLogin extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String name = editTextName.getText().toString();
            String password = editTextPassword.getText().toString();

            List params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));


            JSONObject json = jsonParser.makeHttpRequest(urlAll, "POST",
                    params);

            //Log.i("Profile JSON: ", json.toString());

            if (json != null) {
                try {
                    JSONObject parent = new JSONObject(String.valueOf(json));
                    JSONArray eventDetails = parent.getJSONArray("clientes");

                    for (int i = 0; i < eventDetails.length(); i++) {
                        object = eventDetails.getJSONObject(i);
                        String id = object.getString("Id");
                        String nome = object.getString("Nome");
                        String Password = object.getString("Password");
                        String Username = object.getString("Username");
                        String statuss = object.getString("Status");

                        id_login = id;
                        Name_Logged = nome;
                        Password_Logged = Password;
                        username = Username;
                        status = statuss;

                    }

                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                            "Usuario ou senha errado Verifique",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                        .show();
            }

            if (id_login == null || id_login.equals("") || id_login.isEmpty() ) {
                return false;
            } else {
                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result){
                Toast.makeText(getApplicationContext(), "LOGOU", Toast.LENGTH_SHORT).show();
                //SETEI VARIAVEIS GLOBAIS
                DadosLogin myDados = new DadosLogin();
                myDados.setId(id_login);
                myDados.setUsername(username);
                myDados.setPassword(Password_Logged);
                myDados.setName(Name_Logged);

                // Atualiza as informações de login na instância de DadosLoginSingleton
                DadosLogin dadosLogin = DadosLoginSingleton.getInstance().getDadosLogin();
                dadosLogin.setId(id_login);
                dadosLogin.setUsername(username);
                dadosLogin.setPassword(Password_Logged);
                dadosLogin.setName(Name_Logged);

                id_login = "";

                Intent it3 = new Intent(Login.this, MainActivity.class);
                startActivity(it3);
            }else {
                Toast.makeText(getApplicationContext(), "Erro de Login Usuario ou Senha Incorretos!", Toast.LENGTH_SHORT).show();

            }



        }
    }
}