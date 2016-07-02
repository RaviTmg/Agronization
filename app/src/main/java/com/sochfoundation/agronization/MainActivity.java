package com.sochfoundation.agronization;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String uery = "";
    List<Organization> orgObjectList;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    FloatingActionButton fab;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    SeissonManager seissonManager;
    TextView noMatch;
    MaterialSearchView searchView;
    CoordinatorLayout coordinatorLayout;
    CircularProgressView progressView;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // startActivity(new Intent(this, ProfileActivity.class));
        setUpViews();
        seissonManager = new SeissonManager(this);
        noMatch.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                pos = tab.getPosition();
            }

        });
        setUpTabIcons();
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("TAB_INDEX", 0);
                    startActivity(intent);

                }
            });
        }
        orgObjectList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                uery = query;
                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {

                if (uery.isEmpty()) {
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);

        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        assert navigationView != null;
        final Menu menu = navigationView.getMenu();
        menu.add(Menu.NONE, 1, Menu.NONE, "log In");
        menu.add(Menu.NONE, 2, Menu.NONE, "Submit Listing");
        menu.add(Menu.NONE, 3, Menu.NONE, "Crowd Mapping");
        menu.add(Menu.NONE, 4, Menu.NONE, "About");
        menu.add(Menu.NONE, 5, Menu.NONE, "Contact");
        menu.add(Menu.NONE, 6, Menu.NONE, "share");
        menu.add(Menu.NONE, 7, Menu.NONE, "Give Feedback");
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.sign_in));
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.upload));
        menu.getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.map));
        menu.getItem(3).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_about));
        menu.getItem(4).setIcon(ContextCompat.getDrawable(this, R.drawable.phone));
        menu.getItem(5).setIcon(ContextCompat.getDrawable(this, R.drawable.share_alt));
        menu.getItem(6).setIcon(ContextCompat.getDrawable(this, R.drawable.feedback));

    }

    private void setUpViews() {
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        noMatch = (TextView) findViewById(R.id.txt_no_match);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate);
        progressView = (CircularProgressView) findViewById(R.id.progress_view_search);
    }


    private void performSearch(String currentQuery) {
        String url = OrgConfig.SEARCH_URL + currentQuery;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(getString(R.string.search_result));
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.stopAnimation();
                progressView.setVisibility(View.GONE);
                Snackbar.make(coordinatorLayout, getString(R.string.check_internet), Snackbar.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void parseData(String response) {
        progressView.stopAnimation();
        progressView.setVisibility(View.GONE);
        try {
            JSONObject jobj = new JSONObject(response);
            boolean status = jobj.getBoolean("status");
            if (status) {
                JSONArray array = jobj.getJSONArray("message");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length() - 1; i++) {
                        Organization orgObject = new Organization();
                        JSONObject jsonObject;
                        try {
                            jsonObject = array.getJSONObject(i);
                            orgObject.setImg(jsonObject.getString(OrgConfig.IMAGE_URL));
                            orgObject.setName(jsonObject.getString(OrgConfig.ORG_NAME));
                            orgObject.setLocation(jsonObject.getString(OrgConfig.ORG_LOCATION));
                            orgObject.setType(jsonObject.getString(OrgConfig.ORG_TYPE));
                            orgObject.setId(jsonObject.getString(OrgConfig.ID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        orgObjectList.add(orgObject);
                    }
                }
            } else {
                String nomatchfound = getString(R.string.no_match) + "  " + uery;
                noMatch.setVisibility(View.VISIBLE);
                noMatch.setText(nomatchfound);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        recyclerView.setAdapter(new SearchRecyclerAdapter(MainActivity.this, orgObjectList));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrgFrag(), getString(R.string.organization));
        adapter.addFragment(new EventsFrag(), getString(R.string.events));
        viewPager.setAdapter(adapter);
    }

    private void setUpTabIcons() {
    //    tabLayout.getTabAt(0).setIcon(R.drawable.feedback);

        /*
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        */
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (progressView.getVisibility() == View.VISIBLE)
            progressView.setVisibility(View.GONE);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        if (noMatch.getVisibility() == View.VISIBLE) {
            noMatch.setVisibility(View.GONE);
        }
        if (recyclerView.getVisibility() == View.VISIBLE)
            recyclerView.setVisibility(View.GONE);
        if (viewPager.getVisibility() == View.GONE || tabLayout.getVisibility() == View.GONE) {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case 1:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case 2:
/*
                if (seissonManager.getUserToken().isEmpty())
                    Snackbar.make(coordinatorLayout, "You must Login First", Snackbar.LENGTH_INDEFINITE).
                            setAction("login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                else
                */
                startActivity(new Intent(MainActivity.this, OrgSubmitActivity.class));

                break;
            case 3:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("TAB_INDEX", 0);
                startActivity(intent);
                break;
            case 4:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
