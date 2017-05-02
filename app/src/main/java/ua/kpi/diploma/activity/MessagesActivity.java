package ua.kpi.diploma.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import ua.kpi.diploma.R;

/**
 * Created by vsind on 30.04.2017.
 */
public class MessagesActivity extends BaseAuthorizedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "MessagesActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getContentId() {
        return R.layout.messages_content;
    }
}
