package ua.kpi.diploma.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ua.kpi.diploma.activity.CreatePatientActivity;
import ua.kpi.diploma.activity.PatientDetailsActivity;
import ua.kpi.diploma.adapter.PatientListAdapter;
import ua.kpi.diploma.dto.PatientItem;

/**
 * Created by vsind on 01.05.2017.
 */
public class PatientListFragment extends AsyncFragment {

    public static String TAG = "PatientListFragment";

    private static final int REQUEST_CODE = 1;

    private ArrayAdapter<PatientItem> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_list_content, container, false);

        loadPatients();

        FloatingActionButton createPatientButton = (FloatingActionButton) view.findViewById(R.id.create_patient_button);
        createPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreatePatientActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return view;
    }

    public void loadPatients() {
        new FetchPatientsTask().execute();
    }

    private void displayResponse(final PatientItem[] response) {

        if (response.length == 0) {
            Toast.makeText(getActivity(), "Patients not found", Toast.LENGTH_LONG).show();
            return;
        }

        adapter = new PatientListAdapter(getActivity(), R.layout.patient_list_row, response);
        ListView listView = (ListView) getActivity().findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientItem patientItem = adapter.getItem(position);

                Intent intent = new Intent(getActivity(), PatientDetailsActivity.class);
                intent.putExtra("patientId", patientItem.getId());
                startActivity(intent);
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
                Log.d(getTag(), url);
                ResponseEntity<PatientItem[]> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), PatientItem[].class);
                return response.getBody();
            } catch (Exception e) {
                Log.e(getTag(), e.getLocalizedMessage(), e);
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
