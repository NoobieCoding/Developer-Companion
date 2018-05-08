package corp.tuter.developerscompanionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import corp.tuter.developerscompanionapp.CustomView.MyProjectUserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.Interactor.DBManager;

public class MyProjectMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DBManager mDBManager;
    private MyProjectUserBarView userBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }



    private void init() {
        if (mAuth.getCurrentUser() != null) {
            mDBManager = DBManager.getInstance();
            userBar = findViewById(R.id.user_bar_view);
            userBar.setEventListerner(new UserBarListener() {
                @Override
                public void onTriggered() {
                    Log.d("CC","AA");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        }
    }

    public void checkValid() {
        if (mAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_my_project_main);
        } else {
            setContentView(R.layout.my_project_not_logged_in);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkValid();
        init();
    }
}
