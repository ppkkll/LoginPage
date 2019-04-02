package com.fantasy_travel.loginpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SidebarActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bar);

        drawerLayout = findViewById(R.id.drawerLayout);

        drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                });
        NavigationView navigationView = findViewById(R.id.Sidebar_Nav_view);
        navigationView.setNavigationItemSelectedListener(


                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();

                        switch (id){
                            case R.id.nav_account:
                                Toast.makeText(getApplicationContext(),"Account",Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Daily_Commute:
                                Toast.makeText(getApplicationContext(),"DailyCommute",Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Find_Fellow_Traveller:
                                Toast.makeText(getApplicationContext(),"FindFellowTraveller",Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawers();
                                break;
                            case R.id.nav_Setting:
                                Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();

                        }


                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
