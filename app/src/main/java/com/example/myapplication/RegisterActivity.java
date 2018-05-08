package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.app.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView textView,forgot;
    private EditText name,username,email,password;
    private Button btnRegister;
    private String n,u,e,p;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textView = findViewById(R.id.toolbar_title);
        textView.setText("Register");
        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               n = name.getText().toString().trim();
               u = username.getText().toString().trim();
               p = password.getText().toString().trim();
               e = email.getText().toString().trim();

               if (n.isEmpty() || u.isEmpty() || p.isEmpty() || e.isEmpty()){
                   Toast.makeText(getApplicationContext(),"Some fields are empty",Toast.LENGTH_LONG).show();
               }
               else {
                   registerUser(n,u,e,p);
               }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void init(){
        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        forgot = findViewById(R.id.forgot);
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
    }

    private void registerUser(final String n,final String u, final String e, final String p) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        String id = jObj.getString("id");
                        String username = jObj.getString("username");
                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        session.setId(id);
                        session.setName(name);
                        session.setUsername(username);
                        session.setEmail(email);
                        session.setLogin(true);

                        String errorMsg = jObj.getString("error_msg");
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage(errorMsg);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialog.show();
                    }
                    else{
                        String errorMsg = jObj.getString("error_msg");
                        alertDialog.setTitle("An Error Occurred");
                        alertDialog.setMessage(errorMsg);
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "An Error occurred during registration", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("n", n);
                params.put("u", u);
                params.put("p", p);
                params.put("e", e);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
