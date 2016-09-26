package com.example.oseung.myapplication;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Baskitball extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView list,list1;
    private ImageView teammember,teamsearch;
    private CalendarView calendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baskitball);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        list =(ListView) findViewById(R.id.listView);

        teammember=(ImageView)findViewById(R.id.teammember);
        teamsearch=(ImageView)findViewById(R.id.teamsearch);
        list1=(ListView)findViewById(R.id.listView1);
        calendar=(CalendarView)findViewById(R.id.calendar);



        teamsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BaskitballTeamFlagment();
                fragment.getActivity();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Baskitball.this, Baskitball.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_gallery) {

        }
        // Handle the camera action


        else if (id == R.id.nav_share) {

            fragment = new BaskitballTeamFlagment();
            list.setVisibility(View.VISIBLE);
            calendar.setVisibility(View.INVISIBLE);
            teammember.setVisibility(View.INVISIBLE);
            teamsearch.setVisibility(View.INVISIBLE);
            list1.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_send) {

            fragment = new BaskitballTeamSelectFlagment();
            list1.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
            calendar.setVisibility(View.INVISIBLE);
            teammember.setVisibility(View.INVISIBLE);
            teamsearch.setVisibility(View.INVISIBLE);




        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_camera11) {

         Intent intent = new Intent(Baskitball.this,MapsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_camera111) {

        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class BaskitballTeamFlagment extends Fragment {
        public BaskitballTeamFlagment() {

        }

        String myJSON;

        private static final String TAG_RESULTS = "result";
        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "name";
        private static final String TAG_ADD = "address";

        JSONArray peoples = null;

        ArrayList<HashMap<String, String>> personList;

        ListView list;


        @Override
        public View onCreateView(LayoutInflater Inflater, ViewGroup container, Bundle savedInstanceState) {
            list = (ListView) findViewById(R.id.listView);


            personList = new ArrayList<HashMap<String, String>>();
            getData("http://matching.dothome.co.kr/member/tstr.php");
            return Inflater.inflate(R.layout.fragment_baskitballteam, container, false);

        }


        protected void showList() {
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String address = c.getString(TAG_ADD);

                    HashMap<String, String> persons = new HashMap<String, String>();

                    persons.put(TAG_ID, id);
                    persons.put(TAG_NAME, name);
                    persons.put(TAG_ADD, address);

                    personList.add(persons);
                }

                final ListAdapter adapter = new SimpleAdapter(
                        Baskitball.this, personList, R.layout.fragment_baskitballteam,
                        new String[]{TAG_ID, TAG_NAME, TAG_ADD},
                        new int[]{R.id.id, R.id.name, R.id.address}
                );

                list.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        public void getData(String url) {
            class GetDataJSON extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... params) {

                    String uri = params[0];

                    BufferedReader bufferedReader = null;
                    try {
                        URL url = new URL(uri);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        StringBuilder sb = new StringBuilder();

                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String json;
                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json + "\n");
                        }

                        return sb.toString().trim();

                    } catch (Exception e) {
                        return null;
                    }


                }

                @Override
                protected void onPostExecute(String result) {
                    myJSON = result;
                    showList();
                }
            }
            GetDataJSON g = new GetDataJSON();
            g.execute(url);
        }


    }

    public class  BaskitballTeamSelectFlagment extends Fragment {
        public  BaskitballTeamSelectFlagment() {

        }
        String myJSON;

        private static final String TAG_RESULTS="result";
        private static final String TAG_ID = "tpname";
        private static final String TAG_NAME = "tclass";


        JSONArray peoples = null;

        ArrayList<HashMap<String, String>> personList;

        ListView list;


        @Override
        public View onCreateView(LayoutInflater Inflater, ViewGroup container, Bundle savedInstanceState) {
            list = (ListView) findViewById(R.id.listView1);
            personList = new ArrayList<HashMap<String,String>>();

            getData("http://matching.dothome.co.kr/member/team_info.php");
            return Inflater.inflate(R.layout.fragment_baskitballteamselect, container, false);

        }
        protected void showList(){
            try {
                personList.clear();
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);
                for(int i=0;i<peoples.length();i++){
                    JSONObject c = peoples.getJSONObject(i);
                    String tpname = c.getString(TAG_ID);
                    String tclass = c.getString(TAG_NAME);


                    HashMap<String,String> persons = new HashMap<String,String>();
                    persons.put(TAG_ID,tpname);
                    persons.put(TAG_NAME,tclass);


                    personList.add(persons);
                }

                ListAdapter adapter = new SimpleAdapter(
                        Baskitball.this, personList, R.layout.fragment_baskitballteamselect,
                        new String[]{TAG_ID,TAG_NAME},
                        new int[]{R.id.tpname, R.id.tclass}
                );

                list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void getData(String url){
            class GetDataJSON extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... params) {

                    String uri = params[0];

                    BufferedReader bufferedReader = null;
                    try {
                        URL url = new URL(uri);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        StringBuilder sb = new StringBuilder();

                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String json;
                        while((json = bufferedReader.readLine())!= null){
                            sb.append(json+"\n");
                        }

                        return sb.toString().trim();

                    }catch(Exception e){
                        return null;
                    }



                }

                @Override
                protected void onPostExecute(String result){
                    myJSON=result;
                    showList();
                }
            }
            GetDataJSON g = new GetDataJSON();
            g.execute(url);
        }


    }





}