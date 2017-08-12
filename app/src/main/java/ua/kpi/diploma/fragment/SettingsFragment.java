package ua.kpi.diploma.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import ua.kpi.diploma.R;

/**
 * @author Mykola Yashchenko
 */
public class SettingsFragment extends AsyncFragment {

    // todo remove hardcode
    private static String[] LANGUAGES = {"English", "Русский", "Українська"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_content, container, false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, LANGUAGES);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) view.findViewById(R.id.language);
        materialDesignSpinner.setAdapter(arrayAdapter);

        return view;
    }
}
