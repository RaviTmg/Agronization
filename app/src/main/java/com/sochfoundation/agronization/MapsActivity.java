package com.sochfoundation.agronization;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    public static final String PAR_KEY = "DAATA";
    GoogleMap mMap;
    int tabIndex;
    String id;
    Organization org;
    Marker marker;
    Button button;
    double lat, lon;
    RequestQueue requestQueue;
    List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        button = (Button) findViewById(R.id.button_next);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                tabIndex = bundle.getInt("TAB_INDEX");
                id = bundle.getString("_ID");
            }
        }


    }

    private void getOrgWithId(final GoogleMap mMap) {

        StringRequest stringRequest = new StringRequest(OrgConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        JSONArray array = jobj.getJSONArray("message");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject;
                                jsonObject = array.getJSONObject(i);
                                if (jsonObject.getString(OrgConfig.ID).equals(id)) {

                                    String lat = jsonObject.getString(OrgConfig.ORG_LOCATION_lat);
                                    String lon = jsonObject.getString(OrgConfig.ORG_LOCATION_lan);
                                    LatLng latLng = new LatLng(Double.parseDouble(lat),
                                            Double.parseDouble(lon));
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(jsonObject.getString(OrgConfig.ORG_NAME)));
                                }
                            }
                        }
                    } else
                        Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void getEventWithId(final GoogleMap mMap) {

        StringRequest stringRequest = new StringRequest(EventsConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                                    String lat = jsonObject.getString(EventsConfig.LOCATION_LAT);
                                    String lon = jsonObject.getString(EventsConfig.LOCATION_LAN);
                                    LatLng latLng = new LatLng(Double.parseDouble(lat),
                                            Double.parseDouble(lon));
                                    mMap.addMarker(new MarkerOptions().position(latLng).title(jsonObject.getString(OrgConfig.ORG_NAME)));
                                }
                            }
                        }
                    } else
                        Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void getEvent(final GoogleMap mMap) {

        StringRequest stringRequest = new StringRequest(EventsConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        JSONArray array = jobj.getJSONArray("message");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                final JSONObject jsonObject;
                                try {
                                    jsonObject = array.getJSONObject(i);
                                    String lat = jsonObject.getString(EventsConfig.LOCATION_LAT);
                                    String lon = jsonObject.getString(EventsConfig.LOCATION_LAN);
                                    LatLng latLng = new LatLng(Double.parseDouble(lat),
                                            Double.parseDouble(lon));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                            .title(jsonObject.getString(OrgConfig.ORG_NAME)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else
                        Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void getOrg(final GoogleMap mMap) {
        Cache.Entry entry = requestQueue.getCache().get(OrgConfig.DATA_URL);
        StringRequest stringRequest = new StringRequest(OrgConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        JSONArray array = jobj.getJSONArray("message");
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                final JSONObject jsonObject;
                                try {
                                    jsonObject = array.getJSONObject(i);
                                    String lat = jsonObject.getString(OrgConfig.ORG_LOCATION_lat);
                                    String lon = jsonObject.getString(OrgConfig.ORG_LOCATION_lan);
                                    LatLng latLng = new LatLng(Double.parseDouble(lat),
                                            Double.parseDouble(lon));
                                    mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .title(jsonObject.getString(OrgConfig.ORG_NAME)));
                                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                        @Override
                                        public void onMapLongClick(LatLng latLng) {
                                            Intent intent = new Intent(MapsActivity.this, OrgInfoActivity.class);
                                            try {
                                                intent.putExtra("ID", jsonObject.getString(OrgConfig.ID));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(intent);

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else
                        Toast.makeText(MapsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(MapsActivity.this, "permisssion not granted", Toast.LENGTH_LONG).show();
                return;
            }
            mMap.setMyLocationEnabled(true);
        LatLng kathmandu = new LatLng(27.7172f, 85.3240f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kathmandu, 6));
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);
        // Add a marker
        switch (tabIndex) {
            case 0:
                getOrg(mMap);
                getEvent(mMap);
                break;
            case 1:
                getEventWithId(mMap);
                break;
            case 2:
                getOrgWithId(mMap);
                break;
            case 3:
                submitOrg(mMap);
        }
    }

    private void submitOrg(final GoogleMap mMap) {
        button.setVisibility(View.VISIBLE);
        org = getIntent().getParcelableExtra(OrgSubmit2Activity.PAR_KEY);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                Toast.makeText(getApplicationContext(), latLng.toString(), Toast.LENGTH_LONG).show();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                Toast.makeText(getApplicationContext(), "new Marker at " + latLng.toString(), Toast.LENGTH_LONG).show();
                lat = latLng.latitude;
                lon = latLng.longitude;


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org.setLat(String.valueOf(lat));
                org.setLon(String.valueOf(lon));
                Intent intent = new Intent(MapsActivity.this, OrgSubmit3Activity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, org);
                intent.putExtras(mBundle);
                startActivity(intent);
                String location = getLocalityName(getApplicationContext(), lat, lon);
                org.setLocation(location);
            }
        });
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(27.97320, 84.8003)).zoom(7).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    public static String getLocalityName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return null;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).

        return false;

    }
}
