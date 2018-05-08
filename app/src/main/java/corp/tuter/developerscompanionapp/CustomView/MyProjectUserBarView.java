package corp.tuter.developerscompanionapp.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import corp.tuter.developerscompanionapp.LoginActivity;
import corp.tuter.developerscompanionapp.MainActivity;
import corp.tuter.developerscompanionapp.MyProjectMainActivity;
import corp.tuter.developerscompanionapp.R;
import corp.tuter.developerscompanionapp.RegisterActivity;

public class MyProjectUserBarView extends FrameLayout implements View.OnClickListener {

    private TextView userEmail;
    private Button btn1, btn2;
    private FirebaseAuth mAuth;
    private Context thisContext;
    private LoginManager facebookLoginManager;
    private UserBarListener myListener;

    public MyProjectUserBarView(@NonNull Context context) {
        super(context);
        thisContext = context;
        setUp();

    }

    public MyProjectUserBarView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        thisContext = context;
        setUp();
    }

    public void setEventListerner(UserBarListener listerner) {
        myListener = listerner;
    }

    private void bindView() {
        userEmail = findViewById(R.id.user_email_at_bar);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1.setText(R.string.home);
    }

    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
        facebookLoginManager = LoginManager.getInstance();
        setLayout();
        bindView();
        setUserEmail();
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    private void setLayout() {
        inflate(getContext(), R.layout.widget_user_bar, this);
    }
    public void setUserEmail() {
        if (mAuth.getCurrentUser() != null)
            userEmail.setText(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void onClick(View view) {
        Log.d("BB","AAA");
        if (view == btn1) {
            onBtn1Click();
        } else if (view == btn2) {
            onBtn2Click();
        }
    }

    private void onBtn2Click() {
        Log.d("BB","clicked");
        FirebaseAuth.getInstance().signOut();
        facebookLoginManager.logOut();
        if (myListener != null)
            myListener.onTriggered();

    }

    private void onBtn1Click() {
        thisContext.startActivity(new Intent(thisContext, MainActivity.class));
    }

}
