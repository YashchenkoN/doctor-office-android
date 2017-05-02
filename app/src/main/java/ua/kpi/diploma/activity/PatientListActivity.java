package ua.kpi.diploma.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import ua.kpi.diploma.AuthHolder;
import ua.kpi.diploma.R;
import ua.kpi.diploma.adapter.PatientListAdapter;
import ua.kpi.diploma.dto.PatientItem;

/**
 * Created by vsind on 30.04.2017.
 */
public class PatientListActivity extends BaseAuthorizedActivity {

    private static final int REQUEST_CODE = 1;

    private boolean doubleBackToExitPressedOnce = false;
    private ArrayAdapter<PatientItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FetchPatientsTask().execute();

        FloatingActionButton createPatientButton = (FloatingActionButton) findViewById(R.id.create_patient_button);
        createPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientListActivity.this, CreatePatientActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public int getContentId() {
        return R.layout.patient_list_content;
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
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) {
                new FetchPatientsTask().execute();
                return;
            }
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
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    private void displayResponse(final PatientItem[] response) {

        if (response.length == 0) {
            Toast.makeText(this, "Patients not found", Toast.LENGTH_LONG).show();
            return;
        }

        adapter = new PatientListAdapter(this, R.layout.patient_list_row, response);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientItem patientItem = adapter.getItem(position);

                Intent intent = new Intent(PatientListActivity.this, PatientDetailsActivity.class);
                intent.putExtra("patientId", patientItem.getId());
                PatientListActivity.this.startActivity(intent);
            }
        });
    }

    private class FetchPatientsTask extends AsyncTask<Object, Object, PatientItem[]> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected PatientItem[] doInBackground(Object... params) {
            final String url = getString(R.string.base_uri) + "patients";

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            requestHeaders.setAuthorization(new HttpBasicAuthentication(AuthHolder.username, AuthHolder.password));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<PatientItem[]> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), PatientItem[].class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new PatientItem[0];
            }
        }

        @Override
        protected void onPostExecute(PatientItem[] result) {
            dismissProgressDialog();
            displayResponse(result);
        }
    }
}
