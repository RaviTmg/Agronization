package com.sochfoundation.agronization;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OrgFrag extends Fragment {
    RecyclerView recyclerView;
    List<Organization> orgObjectList;
    OrgRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    RequestQueue requestQueue;
    CircularProgressView progressView;
    CoordinatorLayout coordinatorLayout;

    public OrgFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        orgObjectList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshlayout);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrgRecyclerAdapter(getActivity().getApplicationContext(), orgObjectList);
        recyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        getCache();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                orgObjectList.clear();

            }
        });

        return view;
    }

    private void getCache() {
        Cache.Entry entry = requestQueue.getCache().get(OrgConfig.DATA_URL);

        if (entry != null) {
            parseData(new String(entry.data));
        } else {

            getData();

        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(OrgConfig.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.stopAnimation();
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(coordinatorLayout, getString(R.string.check_internet), Snackbar.LENGTH_LONG).show();
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
                        Organization orgObject = new Organization();
                        JSONObject jsonObject;
                        try {
                            jsonObject = array.getJSONObject(i);

                            orgObject.setName(jsonObject.getString(OrgConfig.ORG_NAME));
                            orgObject.setLocation(jsonObject.getString(OrgConfig.ORG_LOCATION));
                            orgObject.setType(jsonObject.getString(OrgConfig.ORG_TYPE));
                            orgObject.setId(jsonObject.getString(OrgConfig.ID));
                            orgObject.setImg(jsonObject.getString(OrgConfig.IMAGE_URL));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        orgObjectList.add(orgObject);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressView.stopAnimation();
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();

    }

}
