package ua.kpi.diploma.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import ua.kpi.diploma.R;

/**
 * Created by vsind on 30.04.2017.
 */
public abstract class AbstractAsyncActivity extends AppCompatActivity {

    protected static final String TAG = AbstractAsyncActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private boolean destroyed = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    public void showLoadingProgressDialog() {
        this.showProgressDialog(getString(R.string.loading));
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && !destroyed) {
            progressDialog.dismiss();
        }
    }

}
