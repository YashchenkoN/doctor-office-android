package ua.kpi.diploma.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ua.kpi.diploma.R;
import ua.kpi.diploma.fragment.CalendarFragment;
import ua.kpi.diploma.fragment.MessagesFragment;
import ua.kpi.diploma.fragment.PatientListFragment;

/**
 * Created by vsind on 01.05.2017.
 */
public class BaseAuthorizedActivity extends AbstractAsyncActivity {

    private static final int REQUEST_CODE = 1;

    private boolean doubleBackToExitPressedOnce = false;
    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, new PatientListFragment(), PatientListFragment.TAG).commit();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawer.closeDrawer(GravityCompat.START);

                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content, new PatientListFragment(), PatientListFragment.TAG).commit();
                        break;
                    case R.id.nav_calendar:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content, new CalendarFragment()).commit();
                        break;
                    case R.id.nav_messages:
                        fragmentManager.beginTransaction()
                                .replace(R.id.content, new MessagesFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1) {
            PatientListFragment fragment = (PatientListFragment) getSupportFragmentManager()
                    .findFragmentByTag(PatientListFragment.TAG);
            fragment.loadPatients();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListView list = (ListView) findViewById(R.id.list);
                final ArrayAdapter adapter = (ArrayAdapter) list.getAdapter();
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }
}
