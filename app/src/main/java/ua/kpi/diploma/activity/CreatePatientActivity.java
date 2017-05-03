package ua.kpi.diploma.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import ua.kpi.diploma.AuthHolder;
import ua.kpi.diploma.R;
import ua.kpi.diploma.dto.CreatePatientRequest;
import ua.kpi.diploma.dto.PatientItem;

/**
 * Created by vsind on 30.04.2017.
 */
public class CreatePatientActivity extends AbstractAsyncActivity {

    private static final String[] GENDER = {"Чоловік", "Жінка"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_patient_activity_layout);

        Button createButton = (Button) findViewById(R.id.submit);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreatePatientTask().execute();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, GENDER);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.gender);
        materialDesignSpinner.setAdapter(arrayAdapter);
    }

    private void displayResponse(HttpStatus response) {
        switch (response) {
            case CREATED:
                Toast.makeText(this, "Patient created", Toast.LENGTH_LONG).show();
                setResult(1);
                finish();
                break;
            default:
                Toast.makeText(this, "Error: " + response.getReasonPhrase(), Toast.LENGTH_LONG).show();
        }
    }

    private class CreatePatientTask extends AsyncTask<Object, Object, HttpStatus> {

        private String firstName;
        private String lastName;
        private String dateOfBirth;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            EditText editText = (EditText) findViewById(R.id.first_name);
            this.firstName = editText.getText().toString();

            editText = (EditText) findViewById(R.id.last_name);
            this.lastName = editText.getText().toString();

            editText = (EditText) findViewById(R.id.date_of_birth);
            this.dateOfBirth = editText.getText().toString();
        }

        @Override
        protected HttpStatus doInBackground(Object... params) {
            final String url = getString(R.string.base_uri) + "patients/";

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            requestHeaders.setAuthorization(new HttpBasicAuthentication(AuthHolder.username, AuthHolder.password));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<PatientItem> response = restTemplate.exchange(url, HttpMethod.POST,
                        new HttpEntity<>(new CreatePatientRequest(firstName, lastName, dateOfBirth),
                                requestHeaders), PatientItem.class);
                return response.getStatusCode();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return HttpStatus.BAD_REQUEST;
            }
        }

        @Override
        protected void onPostExecute(HttpStatus result) {
            dismissProgressDialog();
            displayResponse(result);
        }
    }
}
