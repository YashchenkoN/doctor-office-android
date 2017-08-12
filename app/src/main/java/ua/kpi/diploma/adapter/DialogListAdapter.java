package ua.kpi.diploma.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ua.kpi.diploma.R;
import ua.kpi.diploma.dto.DialogItem;
import ua.kpi.diploma.dto.PatientItem;

/**
 * @author Mykola Yashchenko
 */
public class DialogListAdapter extends ArrayAdapter<DialogItem> {

    private Activity context;

    public DialogListAdapter(Activity context, int resource, DialogItem[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.dialog_list_row, parent, false);
        }

        DialogItem item = getItem(position);
        TextView patientName = (TextView) convertView.findViewById(R.id.lists_text_style);

        if (patientName != null) {
            patientName.setText(item.getName());
        }

        return convertView;
    }
}
