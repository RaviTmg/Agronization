package com.sochfoundation.agronization;


import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFrag extends Fragment {
    RecyclerView recyclerView;
    List<EvenObject> evenObjectList;
    EventsRecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    RequestQueue requestQueue;
    CircularProgressView progressView;

    public EventsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        progressView = (CircularProgressView) view.findViewById(R.id.progress_view);
        evenObjectList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshlayout);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new EventsRecyclerAdapter(getActivity().getApplicationContext(), evenObjectList);
        recyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Cache.Entry entry = requestQueue.getCache().get(EventsConfig.DATA_URL);

        if (entry != null) {
            parseData(new String(entry.data));
        } else {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            getData();

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();
                getData();
                evenObjectList.clear();

            }
        });
        return view;
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        progressView.stopAnimation();
        swipeRefreshLayout.setRefreshing(false);

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseData(String response) {

        try {
            JSONObject jobj = new JSONObject(response);
            boolean status = jobj.getBoolean("status");
            if (status) {

                JSONArray array = jobj.getJSONArray(EventsConfig.MESSAGE);

                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        EvenObject evenObject = new EvenObject();
                        JSONObject jsonObject;
                        try {
                            jsonObject = array.getJSONObject(i);
                            evenObject.setName(jsonObject.getString(EventsConfig.NAME));
                            evenObject.setLocation(jsonObject.getString(EventsConfig.LOCATION));
                            evenObject.setType(jsonObject.getString(EventsConfig.TYPE));
                            evenObject.setId(jsonObject.getString(EventsConfig.ID));
                            evenObject.setImg(jsonObject.getString(EventsConfig.IMAGE));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        evenObjectList.add(evenObject);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();


    }

}
