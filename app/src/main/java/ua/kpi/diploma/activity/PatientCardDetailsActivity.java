package ua.kpi.diploma.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import ua.kpi.diploma.dto.PatientCardItem;

/**
 * Created by vsind on 30.04.2017.
 */
public class PatientCardDetailsActivity extends AbstractAsyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_card_details_activity_layout);

        String patientCardId = getIntent().getStringExtra("patientCardId");

        new FetchPatientCardTask(patientCardId).execute();
    }

    private void displayResponse(PatientCardItem response) {
        TextView name = (TextView) findViewById(R.id.id);
        name.setText(response.getId());

        TextView dateOfBirth = (TextView) findViewById(R.id.date);
        dateOfBirth.setText(response.getDate());

        TextView gender = (TextView) findViewById(R.id.workplace);
        gender.setText(response.getWorkplace());

        TextView city = (TextView) findViewById(R.id.complaint);
        city.setText(response.getComplaint());

        TextView series = (TextView) findViewById(R.id.diagnosis);
        series.setText(response.getDiagnosis());

        TextView description = (TextView) findViewById(R.id.related_diseases);
        description.setText(response.getRelatedDiseases());

        TextView visualInspection = (TextView) findViewById(R.id.visual_inspection);
        visualInspection.setText(response.getRelatedDiseases());
    }

    private class FetchPatientCardTask extends AsyncTask<Object, Object, PatientCardItem> {

        private String patientCardId;

        public FetchPatientCardTask(String patientId) {
            this.patientCardId = patientId;
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected PatientCardItem doInBackground(Object... params) {
            final String url = getString(R.string.base_uri) + "cards/" + patientCardId + "/details";

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            requestHeaders.setAuthorization(new HttpBasicAuthentication(AuthHolder.username, AuthHolder.password));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<PatientCardItem> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), PatientCardItem.class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(PatientCardItem result) {
            dismissProgressDialog();
            displayResponse(result);
        }
    }
}
