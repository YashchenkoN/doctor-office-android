package ua.kpi.diploma.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ua.kpi.diploma.R;
import ua.kpi.diploma.dto.PatientCardItem;

/**
 * Created by vsind on 01.05.2017.
 */
public class PatientCardListAdapter extends ArrayAdapter<PatientCardItem> {

    private Activity context;

    public PatientCardListAdapter(Activity context, int resource, PatientCardItem[] objects) {
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

        PatientCardItem item = getItem(position);
        TextView cardName = (TextView) convertView.findViewById(R.id.lists_text_style);

        if (cardName != null) {
            cardName.setText(item.getId() + " " + item.getDate());
        }

        return convertView;
    }
}
