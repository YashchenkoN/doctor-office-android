package ua.kpi.diploma.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import ua.kpi.diploma.R;
import ua.kpi.diploma.fragment.MessagesFragment;

/**
 * @author Mykola Yashchenko
 */
public class DialogActivity extends AbstractAsyncActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, new MessagesFragment()).commit();
    }
}
