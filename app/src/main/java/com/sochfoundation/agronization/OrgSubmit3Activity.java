package com.sochfoundation.agronization;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrgSubmit3Activity extends AppCompatActivity {
    private static final int SELECT_FILE = 1;
    Button msel1, msel2, msubmit;
    ImageView mimg1, mimg2;
    SeissonManager seissonManager;
    Organization org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_submit3);
        msel1 = (Button) findViewById(R.id.btnSelectPhoto);
        msel2 = (Button) findViewById(R.id.btnSelectPhoto1);
        msubmit = (Button) findViewById(R.id.button_submit);
        mimg1 = (ImageView) findViewById(R.id.ivImage);
        mimg2 = (ImageView) findViewById(R.id.ivImage1);
        seissonManager = new SeissonManager(this);
        final String token = seissonManager.getUserToken();

        msel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.checkPermission(OrgSubmit3Activity.this);
                galleryIntent();

            }
        });
        msel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org = getIntent().getParcelableExtra(MapsActivity.PAR_KEY);

                Toast.makeText(OrgSubmit3Activity.this, org.getName() + org.getEmail() + org.getMainWork(), Toast.LENGTH_LONG).show();

                submit(token, org.getName(), org.getType(), org.getPhone(),
                        org.getEmail(), org.getWebsite(), org.getMainWork(), org.getLat(), org.getLon(), org.getLocation());
            }
        });
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    //TODO: deny code
                    //code for deny
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            //TODO:cancelled
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mimg1.setImageBitmap(bm);
    }

    private void submit(final String token, final String name, final String type,
                        final String phone, final String email, final String website, final String desc,
                        final String lat, final String lon, final String location) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, OrgConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(OrgSubmit3Activity.this, "response", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrgSubmit3Activity.this, "error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                //  params.put("name", name);
                params.put("token", token);
                params.put(OrgConfig.ORG_LOCATION, location);
                params.put(OrgConfig.ORG_LOCATION_lat, lat);
                params.put(OrgConfig.ORG_LOCATION_lan, lon);
                params.put(OrgConfig.ORG_NAME, name);
                params.put(OrgConfig.ORG_TYPE, type);
                params.put(OrgConfig.ORG_PH_NO, phone);
                params.put(OrgConfig.ORG_EMAIL, email);
                params.put(OrgConfig.ORG_WEBSITE, website);
                params.put(OrgConfig.ORG_DESC, desc);


                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}
