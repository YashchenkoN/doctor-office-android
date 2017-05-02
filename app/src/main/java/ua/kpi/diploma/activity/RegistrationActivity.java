package ua.kpi.diploma.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import ua.kpi.diploma.R;
import ua.kpi.diploma.dto.CreateUserRequest;
import ua.kpi.diploma.dto.CurrentUser;

/**
 * Created by vsind on 30.04.2017.
 */
public class RegistrationActivity extends AbstractAsyncActivity {
    protected static final String TAG = RegistrationActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity_layout);

        final Button registrationButton = (Button) findViewById(R.id.register);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterUserTask().execute();
            }
        });
    }

    private void displayResponse(HttpStatus response) {
        switch (response) {
            case CREATED:
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                Toast.makeText(this, response.getReasonPhrase(), Toast.LENGTH_LONG).show();
        }
    }

    private class RegisterUserTask extends AsyncTask<Void, Void, HttpStatus> {

        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            EditText editText = (EditText) findViewById(R.id.username);
            this.username = editText.getText().toString();

            editText = (EditText) findViewById(R.id.password);
            this.password = editText.getText().toString();
        }

        @Override
        protected HttpStatus doInBackground(Void... params) {
            final String url = getString(R.string.base_uri) + "users";

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<CurrentUser> response = restTemplate.exchange(url, HttpMethod.POST,
                        new HttpEntity<>(new CreateUserRequest(username, password), requestHeaders), CurrentUser.class);
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
