package ua.kpi.diploma.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * Created by vsind on 02.05.2017.
 */
public class AsyncFragment extends Fragment {
    private ProgressDialog progressDialog;
    private boolean destroyed = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    public void showLoadingProgressDialog() {
        this.showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
