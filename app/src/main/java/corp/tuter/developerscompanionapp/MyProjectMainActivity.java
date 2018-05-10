package corp.tuter.developerscompanionapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.CreateNewProjectListener;
import corp.tuter.developerscompanionapp.CustomView.CreateNewProjectView;
import corp.tuter.developerscompanionapp.CustomView.MyProjectUserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Entity.MyProjectCallback;
import corp.tuter.developerscompanionapp.Interactor.DBManager;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.RecyclerView.ProjectAdaptor;

public class MyProjectMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DBManager mDBManager;
    private MyProjectUserBarView userBar;
    private List<Node> nList;
    private List<String> options;
    private List<Project> projects;
    private RecyclerView projectsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }



    private void init() {
        if (mAuth.getCurrentUser() != null) {
            options = new ArrayList<>();
            nList = new ArrayList<>();
            projects = new ArrayList<>();
            options.add("Please Select Development Type");
            mDBManager = DBManager.getInstance();
            userBar = findViewById(R.id.user_bar_view);
            userBar.setEventListerner(new UserBarListener() {
                @Override
                public void onTriggered() {
                    Log.d("CC","AA");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
            getOptionList();
            mDBManager.getProjects(mAuth.getCurrentUser().getUid(), new MyProjectCallback() {
                @Override
                public void onCallBack(List<Project> rProjects) {
                    projects = rProjects;
                    setUpRecyclerView();
                }
            });

        }
    }

    private void setUpRecyclerView() {
        projectsView = findViewById(R.id.projects_view);
        projectsView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        projectsView.setAdapter(new ProjectAdaptor(projects));

    }

    private void getOptionList() {
        mDBManager.getNode(new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                nList = list;
                for(Node n: nList) {
                    options.add(n.getPath().replace("/",""));
                }
            }
        });
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

    public void addProject(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyProjectMainActivity.this);
        final AlertDialog dialog = builder.create();
        CreateNewProjectView createNewProjectView = new CreateNewProjectView(this,dialog);
        createNewProjectView.setEventListerner(new CreateNewProjectListener() {
            @Override
            public void onCreateProject(Project project) {
                projects.add(project);
                setUpRecyclerView();
                addToDatabase(project);
            }
        });
        dialog.setView(createNewProjectView);
        dialog.show();

    }

    private void addToDatabase(Project project) {
        mDBManager.addProjectToDatabase(project, mAuth.getCurrentUser().getUid());
    }

}
