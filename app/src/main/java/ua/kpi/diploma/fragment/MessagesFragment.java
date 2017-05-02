package ua.kpi.diploma.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.kpi.diploma.R;

/**
 * Created by vsind on 01.05.2017.
 */
public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_content, container, false);
    }
}
