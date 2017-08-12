package ua.kpi.diploma.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import ua.kpi.diploma.adapter.PatientCardListAdapter;
import ua.kpi.diploma.adapter.PatientListAdapter;
import ua.kpi.diploma.dto.PatientCardItem;
import ua.kpi.diploma.dto.PatientItem;

/**
 * @author Mykola Yashchenko
 */
public class PatientCardListActivity extends AbstractAsyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_card_list_activity_layout);

        findViewById(R.id.drawer_layout).setBackground(getApplicationContext().getDrawable(R.drawable.pic_2));
        findViewById(R.id.drawer_layout).getBackground().setAlpha(40);

        String patientCardId = getIntent().getStringExtra("patientId");

        new FetchPatientCardListTask(patientCardId).execute();

        FloatingActionButton createPatientCardButton = (FloatingActionButton) findViewById(R.id.create_patient_card_button);
        createPatientCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientCardListActivity.this, CreatePatientCardActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void displayResponse(PatientCardItem[] response) {
        if (response.length == 0) {
            Toast.makeText(this, "Patient cards not found", Toast.LENGTH_LONG).show();
            return;
        }

        final PatientCardListAdapter adapter = new PatientCardListAdapter(this, R.layout.patient_list_row, response);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientCardItem patientCardItem = adapter.getItem(position);

                Intent intent = new Intent(PatientCardListActivity.this, PatientCardDetailsActivity.class);
                intent.putExtra("patientCardId", patientCardItem.getId());
                startActivity(intent);
            }
        });
    }

    private class FetchPatientCardListTask extends AsyncTask<Object, Object, PatientCardItem[]> {

        private String patientId;

        public FetchPatientCardListTask(String patientId) {
            this.patientId = patientId;
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected PatientCardItem[] doInBackground(Object... params) {
            final String url = getString(R.string.base_uri) + "cards/" + patientId;

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            requestHeaders.setAuthorization(new HttpBasicAuthentication(AuthHolder.username, AuthHolder.password));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<PatientCardItem[]> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), PatientCardItem[].class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new PatientCardItem[0];
            }
        }

        @Override
        protected void onPostExecute(PatientCardItem[] result) {
            dismissProgressDialog();
            displayResponse(result);
        }
    }
}
