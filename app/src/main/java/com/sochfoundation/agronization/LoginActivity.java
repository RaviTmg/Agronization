package com.sochfoundation.agronization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    AutoCompleteTextView memail;
    EditText mpassword;
    Button mlogin;
    TextView textView;
    ProgressDialog progressDialog;
    SeissonManager seissonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textView = (TextView) findViewById(R.id.go_register);
        memail = (AutoCompleteTextView) findViewById(R.id.email);
        mpassword = (EditText) findViewById(R.id.password);
        mlogin = (Button) findViewById(R.id.log_in_button);
        seissonManager = new SeissonManager(LoginActivity.this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString();
                if (email.isEmpty()) {
                    memail.setError(getString(R.string.please_fill));
                }if (password.isEmpty()) {
                    mpassword.setError(getString(R.string.please_fill));
                } else {
                    loginUser(email, password);
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void loginUser(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Logging in ...");
        progressDialog.setCanceledOnTouchOutside(true);

        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String message  = jsonObject.getString("message");
                    if (status) {
                        String token = jsonObject.getString("token");
                        seissonManager.setUserToken(token);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    } if (!status){
                        // Error occurred in registration. Get the error
                        // message
                        Toast.makeText(getApplicationContext(),
                                "something is wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Registration Error: " + error.getMessage());
                String errorMsg = error.getMessage();
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                //  params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.hide();

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }


}
