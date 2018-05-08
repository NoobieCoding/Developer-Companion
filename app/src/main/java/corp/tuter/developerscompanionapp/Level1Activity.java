package corp.tuter.developerscompanionapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.CustomView.UserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarViewNotLoggedIn;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Interactor.DBManager;
import corp.tuter.developerscompanionapp.Interactor.StorageManager;
import corp.tuter.developerscompanionapp.Model.Framework;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.RecyclerView.ImageCustomAdaptor;
import corp.tuter.developerscompanionapp.RecyclerView.MainRecyclerViewAdaptor;

public class Level1Activity extends AppCompatActivity {

    private Node parent;
    private ImageView parentImage;
    private TextView parentName, frameWorkHeader, libraryHeader, toolHeader;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private StorageManager mStorageManager;
    private DBManager mDBManager;
    private UserBarView userBarView;
    private UserBarViewNotLoggedIn userBarViewNotLoggedIn;
    private FirebaseAuth mAuth;
    private boolean isFramework;
    private Button descriptionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (Node)getIntent().getSerializableExtra("parent");
        if (savedInstanceState != null) {
            Log.d("SAVE","REACH");
            parent = (Node) savedInstanceState.getSerializable("parent");
        }
        determineType();
        if(!isFramework)
            setContentView(R.layout.activity_level1);
        else
            setContentView(R.layout.activity_level1_type_2);


        mStorageManager = StorageManager.getInstance();
        mDBManager = DBManager.getInstance();
        mAuth = FirebaseAuth.getInstance();
        init();
        updateUI(mAuth.getCurrentUser());
    }

    private void determineType() {
        if (parent instanceof Framework)
            isFramework = true;
        else
            isFramework = false;

    }

    private void init() {
        parentImage = findViewById(R.id.level1_main_image);
        parentName = findViewById(R.id.header_2);
        Typeface type = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");
        parentName.setTypeface(type);

        if (!isFramework)
            initType1Component();
        else
            initType2Component();

        libraryHeader = findViewById(R.id.sub_header_2_2);
        libraryHeader.setTypeface(type);
        toolHeader = findViewById(R.id.sub_header_2_3);
        toolHeader.setTypeface(type);

        userBarView = findViewById(R.id.user_bar_view);
        userBarViewNotLoggedIn = findViewById(R.id.user_bar_view_2);
        userBarView.setVisibility(View.GONE);
        userBarView.setEventListerner(new UserBarListener() {
            @Override
            public void onTriggered() {
                updateUI(mAuth.getCurrentUser());
            }
        });
        loadData();

        recyclerView2 = findViewById(R.id.recycler_2_2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));
        mDBManager.getLibraries(parent.getPath(), new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                recyclerView2.setAdapter(new ImageCustomAdaptor(list, getApplicationContext()));
            }
        });

        recyclerView3 = findViewById(R.id.recycler_2_3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 2));
        mDBManager.getTools(parent.getPath(), new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                recyclerView3.setAdapter(new ImageCustomAdaptor(list, getApplicationContext()));
            }
        });

    }

    private void loadData() {
        mStorageManager.assignPictureFromStorage(getApplicationContext(), parentImage, parent.getImageName());
        parentName.setText(parent.getName());
//        mDBManager.getFrameWorks(parent.getPath(), new MyCallBack() {
//            @Override
//            public void onCallBack(List<Node> list) {
//                //do something
//            }
//        });
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("parent", parent);
        Log.d("SAVE",outState.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(mAuth.getCurrentUser());
    }

    void initType1Component() {
        Typeface type = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");
        frameWorkHeader = findViewById(R.id.sub_header_2_1);
        frameWorkHeader.setTypeface(type);
        recyclerView1 = findViewById(R.id.recycler_2_1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2));
        mDBManager.getFrameWorks(parent.getPath(), new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                recyclerView1.setAdapter(new ImageCustomAdaptor(list, getApplicationContext()));
            }
        });

        recyclerView1 = findViewById(R.id.recycler_2_1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2));
        mDBManager.getFrameWorks(parent.getPath(), new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                recyclerView1.setAdapter(new ImageCustomAdaptor(list, getApplicationContext()));
            }
        });
    }

    void initType2Component() {
        descriptionBtn = findViewById(R.id.description_btn);
    }

    public void goToFrameworkDescription(View view) {
        Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
        intent.putExtra("parent", parent);
        startActivity(intent);
    }
}
