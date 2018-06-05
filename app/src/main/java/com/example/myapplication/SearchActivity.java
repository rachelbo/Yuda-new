package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.SessionManager;
import com.example.myapplication.fragments.BlankFragment;
import com.example.myapplication.fragments.ChatFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.MainFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    Fragment fragment = null;
    Class fragmentClass;
    CharSequence title=null;
    private SearchView searchView;
    private SessionManager session;
    private Button moveMe;
    private ImageView info;
    private AlertDialog.Builder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        session = new SessionManager(getApplicationContext());

        alertDialogBuilder = new AlertDialog.Builder(this);
        fragmentClass = MainFragment.class;
        title="Home";
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();
        setTitle(title);

        info = findViewById(R.id.information_button);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setTitle("Categories menu");
                alertDialogBuilder.setMessage(R.string.categories);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        moveMe = findViewById(R.id.moveMe);
        if(session.getMarket_Open().equals("all")){
            moveMe.setText("Open now");
        }
        else {
            moveMe.setText("Show all");
        }

        moveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.getMarket_Open().equals("all")){
                    session.setMarket_Open("open");
                }
                else {
                    session.setMarket_Open("all");
                }
                Intent i = new Intent(SearchActivity.this, SearchActivity.class);
                startActivity(i);
                finish();
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query",query);

                fragmentClass = SearchFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
                    setTitle(query);
                    fragment = null;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
