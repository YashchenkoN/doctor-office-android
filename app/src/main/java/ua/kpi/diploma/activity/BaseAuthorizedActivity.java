package ua.kpi.diploma.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import ua.kpi.diploma.R;
import ua.kpi.diploma.fragment.MessagesFragment;

/**
 * Created by vsind on 01.05.2017.
 */
public abstract class BaseAuthorizedActivity extends AbstractAsyncActivity {

    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(getContentId(), null, false);
        drawer.addView(contentView, 0);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawer.closeDrawer(GravityCompat.START);

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_calendar:
                        break;
                    case R.id.nav_messages:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content, new MessagesFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    public abstract int getContentId();
}
