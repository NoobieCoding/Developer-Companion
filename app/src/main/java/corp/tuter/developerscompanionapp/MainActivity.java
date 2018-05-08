package corp.tuter.developerscompanionapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.CustomView.UserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarViewNotLoggedIn;
import corp.tuter.developerscompanionapp.Entity.FirebaseRealtimeDatabase;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Interactor.DBManager;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.RecyclerView.ImageCustomAdaptor;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userName;
    private UserBarView userBarView;
    private UserBarViewNotLoggedIn userBarViewNotLoggedIn;
    private RecyclerView recyclerView;
    private TextView header;
    private List<Node> list;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference();
    ImageCustomAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //debug
        //mDb.readData("");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            userBarViewNotLoggedIn.setVisibility(View.VISIBLE);
            userBarView.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)header.getLayoutParams();
            header.setLayoutParams(params);
        } else {
            userBarViewNotLoggedIn.setVisibility(View.GONE);
            userBarView.setVisibility(View.VISIBLE);
            userBarView.setUserEmail();
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)header.getLayoutParams();
            header.setLayoutParams(params);
            userName = currentUser.getEmail();
        }
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();
        userBarView = findViewById(R.id.user_bar_view);
        userBarViewNotLoggedIn = findViewById(R.id.user_bar_view_2);
        header = findViewById(R.id.header_1);
        Typeface type = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");
        header.setTypeface(type);
        userBarView.setVisibility(View.GONE);
        userBarView.setEventListerner(new UserBarListener() {
            @Override
            public void onTriggered() {
                updateUI(mAuth.getCurrentUser());
            }
        });
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_1);
        DBManager.getInstance().getNode(new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                adaptor = new ImageCustomAdaptor(list, getApplicationContext());
                recyclerView.setAdapter(adaptor);

            }
        });
        //setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("AAA", "reach");
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String key = ds.getKey();
                    String name = ds.child("name").getValue(String.class);
                    String desc = ds.child("description").getValue(String.class);
                    Log.d("DB", key + "/" + name + "/" + desc);
                    String imageName = key + "_icon.png";
                    String newPath = key;
                    Node node = new Node(name, imageName, desc, newPath);
                    list.add(node);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                adaptor = new ImageCustomAdaptor(list, getApplicationContext());
                recyclerView.setAdapter(adaptor);

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(mAuth.getCurrentUser());
    }

}
