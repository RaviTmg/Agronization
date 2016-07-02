package com.sochfoundation.agronization;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventInfoActivity extends AppCompatActivity {
    String id = "";
    TextView mdesc, mphone, mlocation, memail, mwebsite, mname, mtime, mpost;
    NetworkImageView mimg;
    RequestQueue requestQueue;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                id = bundle.getString("ID");

        }
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventInfoActivity.this, MapsActivity.class);
                    intent.putExtra("TAB_INDEX", 1);
                    intent.putExtra("_ID", id);
                    startActivity(intent);
                }
            });
        }
        mdesc = (TextView) findViewById(R.id.desc);
        mname = (TextView) findViewById(R.id.info_name);
        mphone = (TextView) findViewById(R.id.info_phone);
        memail = (TextView) findViewById(R.id.info_email);
        mwebsite = (TextView) findViewById(R.id.info_website);
        mlocation = (TextView) findViewById(R.id.info_location);
        mimg = (NetworkImageView) findViewById(R.id.img_info);
        mtime = (TextView) findViewById(R.id.info_time);
        mpost = (TextView) findViewById(R.id.info_post);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Cache.Entry entry = requestQueue.getCache().get(EventsConfig.DATA_URL);

        if (entry != null) {
            parseData(new String(entry.data));
        } else {

            getData();

        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(EventsConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.coordinator),
                        getString(R.string.check_internet), Snackbar.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseData(String response) {
        try {
            JSONObject jobj = new JSONObject(response);
            boolean status = jobj.getBoolean("status");
            if (status) {
                JSONArray array = jobj.getJSONArray("message");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject;
                        jsonObject = array.getJSONObject(i);
                        if (jsonObject.getString(EventsConfig.ID).equals(id)) {
                            setData(jsonObject);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setData(JSONObject jsonObject) throws JSONException {
        imageLoader = AppController.getInstance().getImageLoader();
        mdesc.setText(jsonObject.getString(EventsConfig.DESC));
        mname.setText(jsonObject.getString(EventsConfig.NAME));
        mpost.setText(jsonObject.getString(EventsConfig.POSTER));
        mtime.setText(jsonObject.getString(EventsConfig.TIME));
        mlocation.setText(jsonObject.getString(EventsConfig.LOCATION));
        mwebsite.setText(jsonObject.getString(EventsConfig.TYPE));
        mphone.setText(jsonObject.getString(EventsConfig.PHONE));
        memail.setText(jsonObject.getString(EventsConfig.EMAIL));
        mimg.setImageUrl(jsonObject.getString(EventsConfig.IMAGE), imageLoader);

    }


}
