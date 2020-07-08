package com.example.instagrame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.instagrame.Fragments.HomeFragment;
import com.example.instagrame.Fragments.NotificationFragment;
import com.example.instagrame.Fragments.ProfileFragment;
import com.example.instagrame.Fragments.SearchFragment;

public class MainFrame extends AppCompatActivity {
private BottomNavigationView navigationView;
private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);
        navigationView=findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                       switch(menuItem.getItemId())
                       {
                           case R.id.nav_home:
                           {
                               fragment=new HomeFragment();
                               break;
                           }
                           case R.id.nav_search:
                           {
                               fragment=new SearchFragment();
                               break;
                           }
                           case R.id.nav_add:
                           {
                               fragment=null;
           startActivity(new Intent(MainFrame.this,PostActivity.class))
           ;
           finish();

                               break;
                           }
                           case R.id.nav_heart:
                           {
                               fragment=new NotificationFragment();
                               break;
                           }
                           case R.id.nav_person:
                           {
                               fragment=new ProfileFragment();
                               break;
                           }
                       }

if(fragment!=null){
    getSupportFragmentManager().beginTransaction().
            replace(R.id.fragment_container,fragment).commit();
}

                        return true;
                    }
                }
        );
        getSupportFragmentManager().beginTransaction().

                replace(R.id.fragment_container,new HomeFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
       menuInflater.inflate(R.menu.bottom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}