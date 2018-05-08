package corp.tuter.developerscompanionapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.CustomView.UserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarViewNotLoggedIn;
import corp.tuter.developerscompanionapp.Interactor.StorageManager;
import corp.tuter.developerscompanionapp.Model.Node;

public class DescriptionActivity extends AppCompatActivity {

    Node node;
    private TextView topicName, description;
    private ImageView pic;
    private Button backBtn;
    private FirebaseAuth mAuth;
    private StorageManager mStorageManager;
    private UserBarView userBarView;
    private UserBarViewNotLoggedIn userBarViewNotLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        mAuth = FirebaseAuth.getInstance();
        mStorageManager = StorageManager.getInstance();
        node = (Node)getIntent().getSerializableExtra("parent");
        if (savedInstanceState != null) {
            Log.d("SAVE","REACH");
            node = (Node) savedInstanceState.getSerializable("parent");
        }
        init();
        updateUI(mAuth.getCurrentUser());
    }

    private void init() {
        Typeface type = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");
        topicName = findViewById(R.id.description_header);
        description = findViewById(R.id.description_body);
        pic = findViewById(R.id.description_picture);
        backBtn = findViewById(R.id.back_button);
        topicName.setTypeface(type);
        loadPicture();

        userBarView = findViewById(R.id.user_bar_view);
        userBarViewNotLoggedIn = findViewById(R.id.user_bar_view_2);
        userBarView.setVisibility(View.GONE);
        userBarView.setEventListerner(new UserBarListener() {
            @Override
            public void onTriggered() {
                updateUI(mAuth.getCurrentUser());
            }
        });

        topicName.setText(node.getName());
        description.setText(node.getDescription());

    }

    private void loadPicture() {
        mStorageManager.assignPictureFromStorage(getApplicationContext(), pic, node.getImageName());
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            userBarViewNotLoggedIn.setVisibility(View.VISIBLE);
            userBarView.setVisibility(View.GONE);
        } else {
            userBarViewNotLoggedIn.setVisibility(View.GONE);
            userBarView.setVisibility(View.VISIBLE);
            userBarView.setUserEmail();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(mAuth.getCurrentUser());
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("parent", node);
        Log.d("SAVE",outState.toString());
    }
}
