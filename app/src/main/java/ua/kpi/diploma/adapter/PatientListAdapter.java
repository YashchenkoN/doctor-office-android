package ua.kpi.diploma.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ua.kpi.diploma.R;
import ua.kpi.diploma.dto.PatientItem;

/**
 * Created by vsind on 01.05.2017.
 */
public class PatientListAdapter extends ArrayAdapter<PatientItem> {

    private Activity context;

    public PatientListAdapter(Activity context, int resource, PatientItem[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.patient_list_row, parent, false);
        }

        PatientItem item = getItem(position);
        TextView patientName = (TextView) convertView.findViewById(R.id.lists_text_style);

        if (patientName != null) {
            patientName.setText(item.getLastName() + " " + item.getFirstName());
        }

        return convertView;
    }
}
