package com.sochfoundation.agronization;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {
    EditText musername, memail, mpassword;
    Button msignup;
    ProgressDialog pDialog;
    TextView mlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        musername = (EditText) findViewById(R.id.username);
        memail = (EditText) findViewById(R.id.email);
        mpassword = (EditText) findViewById(R.id.password);
        msignup = (Button) findViewById(R.id.sign_up_button);
        mlogin = (TextView) findViewById(R.id.go_signup);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(SignupActivity.this, "Signup cancelled", Toast.LENGTH_LONG).show();
            }
        });
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = musername.getText().toString().trim();
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString();
                if (name.isEmpty()) {
                    musername.setError(getString(R.string.please_fill));
                }
                if (email.isEmpty()) {
                    memail.setError(getString(R.string.please_fill));
                }
                if (password.isEmpty()) {
                    mpassword.setError(getString(R.string.please_fill));
                } else {
                    registerUser(name, email, password);
                }
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser(final String name, final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        Toast.makeText(getApplicationContext(),
                                "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                //  params.put("name", name);
                params.put("email", email);
                params.put("name", name);
                params.put("password", password);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


}
