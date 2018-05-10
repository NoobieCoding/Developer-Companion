package corp.tuter.developerscompanionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import corp.tuter.developerscompanionapp.CustomView.MyProjectUserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.Model.Project;

public class SuggestionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Project project;
    private MyProjectUserBarView userBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        checkValid();
    }

    private void checkValid() {
        if (mAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_suggestion);
        } else {
            setContentView(R.layout.my_project_not_logged_in);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkValid();
        project = (Project) getIntent().getSerializableExtra("project");
        init();
    }

    private void init() {
        if (mAuth.getCurrentUser() != null) {
            userBarView = findViewById(R.id.user_bar_view);
            userBarView.setEventListerner(new UserBarListener() {
                @Override
                public void onTriggered() {
                    Log.d("CC","AA");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        }
    }
}
