package ua.kpi.diploma.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpAuthentication;
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
import ua.kpi.diploma.dto.CurrentUser;

public class MainActivity extends AbstractAsyncActivity {

    protected static final String TAG = MainActivity.class.getSimpleName();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new FetchSecuredResourceTask().execute();
            }
        });

        final TextView registrationButton = (TextView) findViewById(R.id.register);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(intent);
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

    private void displayResponse(HttpStatus response) {
        if (response == HttpStatus.OK) {
            Intent intent = new Intent(MainActivity.this, BaseAuthorizedActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, response.getReasonPhrase(), Toast.LENGTH_LONG).show();
        }
    }

    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, HttpStatus> {

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
            final String url = getString(R.string.base_uri) + "users/me";

            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            try {
                Log.d(TAG, url);
                ResponseEntity<CurrentUser> response = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(requestHeaders), CurrentUser.class);

                AuthHolder.username = username;
                AuthHolder.password = password;

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