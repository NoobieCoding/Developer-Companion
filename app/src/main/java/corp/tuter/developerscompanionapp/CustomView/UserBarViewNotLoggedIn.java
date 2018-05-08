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
import corp.tuter.developerscompanionapp.MyProjectMainActivity;
import corp.tuter.developerscompanionapp.R;
import corp.tuter.developerscompanionapp.RegisterActivity;

public class UserBarViewNotLoggedIn extends FrameLayout implements View.OnClickListener{
    private Button btn1, btn2;
    private Context thisContext;

    public UserBarViewNotLoggedIn(@NonNull Context context) {
        super(context);
        thisContext = context;
        setUp();

    }

    public UserBarViewNotLoggedIn(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        thisContext = context;
        setUp();
    }

    private void bindView() {
        btn1 = findViewById(R.id.btn3);
        btn2 = findViewById(R.id.btn4);
    }

    public void setUp() {
        setLayout();
        bindView();
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    private void setLayout() {
        inflate(getContext(), R.layout.widget_user_bar_not_logged_in, this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn1) {
            onBtn1Click();
        } else if (view == btn2) {
            onBtn2Click();
        }
    }

    private void onBtn2Click() {
        thisContext.startActivity(new Intent(thisContext, RegisterActivity.class));

    }

    private void onBtn1Click() {
        Intent i = new Intent(thisContext, LoginActivity.class);
        thisContext.startActivity(i);
    }

}
