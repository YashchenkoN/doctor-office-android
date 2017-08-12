package ua.kpi.diploma.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
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
import ua.kpi.diploma.dto.AddressItem;
import ua.kpi.diploma.dto.PassportItem;
import ua.kpi.diploma.dto.PatientItem;

/**
 * @author Mykola Yashchenko
 */
public class PatientDetailsActivity extends AbstractAsyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details_activity_layout);

        findViewById(R.id.main_content).setBackground(getApplicationContext().getDrawable(R.drawable.pic_2));
        findViewById(R.id.main_content).getBackground().setAlpha(40);

        final String patientId = getIntent().getStringExtra("patientId");

        new FetchPatientTask(patientId).execute();

        FloatingActionButton cards = (FloatingActionButton) findViewById(R.id.cards);
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDetailsActivity.this, PatientCardListActivity.class);
                intent.putExtra("patientId", patientId);
                startActivityForResult(intent, 1);
            }
        });

        /*FloatingActionButton message = (FloatingActionButton) findViewById(R.id.message);
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + phoneNumber));
            }
        });*/
    }

    private void displayResponse(PatientItem response) {
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(response.getLastName() + " " + response.getFirstName());

        TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);
        dateOfBirth.setText(response.getDateOfBirth());

        TextView gender = (TextView) findViewById(R.id.gender);
        gender.setText(response.getGender());

        TextView city = (TextView) findViewById(R.id.address);
        AddressItem address = response.getAddress();
        city.setText(address.getCity() + ", " + address.getStreet() + ", " + address.getHomeNumber());

        TextView series = (TextView) findViewById(R.id.series);
        series.setText(response.getPassport().getSeries() + " " + response.getPassport().getNumber());

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(response.getPassport().getDate() + ", " + response.getPassport().getWhere());
    }

    private class FetchPatientTask extends AsyncTask<Object, Object, PatientItem> {

        private String patientId;

        public FetchPatientTask(String patientId) {
            this.patientId = patientId;
        }

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();
        }

        @Override
        protected PatientItem doInBackground(Object... params) {
            final String url = getString(R.string.base_uri) + "patients/" + patientId;

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            requestHeaders.setAuthorization(new HttpBasicAuthentication(AuthHolder.username, AuthHolder.password));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<PatientItem> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), PatientItem.class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(PatientItem result) {
            dismissProgressDialog();
            displayResponse(result);
        }
    }
}
