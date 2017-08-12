package ua.kpi.diploma.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ua.kpi.diploma.R;
import ua.kpi.diploma.activity.DialogActivity;
import ua.kpi.diploma.adapter.DialogListAdapter;
import ua.kpi.diploma.dto.DialogItem;

/**
 * @author Mykola Yashchenko
 */
public class DialogListFragment extends AsyncFragment {

    public static String TAG = "DialogListFragment";

    private ArrayAdapter<DialogItem> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogs_list_content, container, false);

        loadDialogs();

        return view;
    }

    public void loadDialogs() {
        // tbd
    }

    private void displayResponse(final DialogItem[] response) {

        if (response.length == 0) {
            Toast.makeText(getActivity(), "Patients not found", Toast.LENGTH_LONG).show();
            return;
        }

        adapter = new DialogListAdapter(getActivity(), R.layout.dialog_list_row, response);
        ListView listView = (ListView) getActivity().findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogItem dialogItem = adapter.getItem(position);

                Intent intent = new Intent(getActivity(), DialogActivity.class);
                intent.putExtra("dialogId", dialogItem.getId());
                startActivity(intent);
            }
        });
    }
}
