package corp.tuter.developerscompanionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.AddComponentListener;
import corp.tuter.developerscompanionapp.CustomView.AddComponentView;
import corp.tuter.developerscompanionapp.CustomView.MyProjectUserBarView;
import corp.tuter.developerscompanionapp.CustomView.UserBarListener;
import corp.tuter.developerscompanionapp.Entity.AllComponentCallBack;
import corp.tuter.developerscompanionapp.Entity.GetCallBack;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Interactor.DBManager;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.RecyclerView.ImageCustomAdaptor;

public class ProjectActivity extends AppCompatActivity {

    private Project project;
    private FirebaseAuth mAuth;
    private Button suggestionBtn;
    private TextView projectNameView;
    private DBManager dbManager;
    private MyProjectUserBarView userBarView;
    private RecyclerView frameworkView,  libraryView, toolView;
    private List<Node> nodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        checkValid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkValid();
        project = (Project)getIntent().getSerializableExtra("project");
        init();
    }

    private void init() {
        if (mAuth.getCurrentUser() != null) {
            dbManager = DBManager.getInstance();
            userBarView = findViewById(R.id.user_bar_view);
            userBarView.setEventListerner(new UserBarListener() {
                @Override
                public void onTriggered() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });

            frameworkView = findViewById(R.id.project_framework);
            frameworkView.setLayoutManager(new GridLayoutManager(this, 2));
            libraryView = findViewById(R.id.project_library);
            libraryView.setLayoutManager(new GridLayoutManager(this, 2));
            toolView = findViewById(R.id.project_tool);
            toolView.setLayoutManager(new GridLayoutManager(this, 2));

            dbManager.getAllProjectComponent(project, mAuth.getCurrentUser().getUid(), new AllComponentCallBack() {
                @Override
                public void onGetComponents(final List<Node> list1, final List<Node> list2, final List<Node> list3) {

                    Log.d("Zeta", list1.size() + "/" + list2.size() + "/" + list3.size());

                   dbManager.getAllFrameworks(new GetCallBack() {
                       @Override
                       public void onTriggered(List<Node> list) {

                           for(Node n: list) {
                               Log.d("Zeta",n.getName() + "/" + n.getDescription());
                           }

                           for (Node an: list) {
                               for (Node n: list1) {
                                   if (n != null && an != null) {
                                       if (n.getName().equals(an.getName()))
                                           project.frameworks.add(an);
                                   }
                               }
                           }
                           setUpRecyclerView(frameworkView, project.frameworks);
                       }
                   });

                    dbManager.getAllLibrary(new GetCallBack() {
                        @Override
                        public void onTriggered(List<Node> list) {
                            for (Node an: list) {
                                for (Node n: list2) {
                                    if (n != null && an != null) {
                                        if (n.getName().equals(an.getName()))
                                            project.libraries.add(an);
                                    }
                                }
                            }
                            setUpRecyclerView(libraryView, project.libraries);
                        }
                    });

                    dbManager.getAllTool(new GetCallBack() {
                        @Override
                        public void onTriggered(List<Node> list) {
                            for (Node an: list) {
                                for (Node n: list3) {
                                    if (n != null && an != null) {
                                        if (n.getName().equals(an.getName()))
                                            project.tools.add(an);
                                    }
                                }
                            }
                            setUpRecyclerView(toolView, project.tools);
                        }

                    });
                }

            });


            Typeface type = Typeface.createFromAsset(getAssets(),"Lato-Black.ttf");
            projectNameView = findViewById(R.id.project_name_header);
            projectNameView.setText(project.getName());
            projectNameView.setTypeface(type);
        }
    }

    public void checkValid() {
        if (mAuth.getCurrentUser() != null) {
            setContentView(R.layout.activity_project);
        } else {
            setContentView(R.layout.my_project_not_logged_in);
        }
    }

    public void goToSuggestion(View view) {

    }

    public void deleteProject(View view) {
        //alert confirmation first
        new AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Do you really want to delete the project?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        applyDeletion();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void applyDeletion() {
        //delete project form database
        dbManager.deleteProject(project, mAuth.getCurrentUser().getUid());
        Intent intent = new Intent(getApplicationContext(), MyProjectMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void addFramework(View view) {
            final List<String> options = new ArrayList<>();
            dbManager.getAllFrameworks(new GetCallBack() {
                @Override
                public void onTriggered(List<Node> list) {
                        options.clear();
                        nodeList = list;
                        for (Node n : list) {
                            options.add(n.getName());
                        }

                        // pop up alert dialog
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this);
                        final AlertDialog dialog = builder.create();
                        AddComponentView addComponentView = new AddComponentView(getApplicationContext(), dialog);
                        addComponentView.setOptions(options);
                        addComponentView.setEventListerner(new AddComponentListener() {
                            @Override
                            public void onAdded(String str) {
                                updateFramework(str);
                            }
                        });
                        dialog.setView(addComponentView);
                        dialog.show();
                    }
            });
    }

    private void updateFramework(String str) {
        Node target = null;
        for (Node n: nodeList) {
            if (n.getName().equals(str)) {
                target = n;
                break;
            }
        }
        if (target != null) {
            project.frameworks.add(target);
            dbManager.updateComponent(project, mAuth.getCurrentUser().getUid(), target);
            setUpRecyclerView(frameworkView, project.frameworks);
            setUpRecyclerView(libraryView, project.libraries);
            setUpRecyclerView(toolView, project.tools);
        }
    }

    public void setUpRecyclerView(RecyclerView recyclerView, List<Node> list) {
        recyclerView.setAdapter(new ImageCustomAdaptor(list, this));
    }

    public void addLibrary(View view) {
        final List<String> options = new ArrayList<>();
        dbManager.getAllLibrary(new GetCallBack() {
            @Override
            public void onTriggered(List<Node> list) {
                options.clear();
                nodeList = list;
                for (Node n : list) {
                    options.add(n.getName());
                }

                // pop up alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this);
                final AlertDialog dialog = builder.create();
                AddComponentView addComponentView = new AddComponentView(getApplicationContext(), dialog);
                addComponentView.setOptions(options);
                addComponentView.setEventListerner(new AddComponentListener() {
                    @Override
                    public void onAdded(String str) {
                        updateLibrary(str);
                    }
                });
                dialog.setView(addComponentView);
                dialog.show();
            }
        });
    }

    private void updateLibrary(String str) {
        Node target = null;
        for (Node n : nodeList) {
            if (n.getName().equals(str)) {
                target = n;
                break;
            }
        }
        if (target != null) {
            project.libraries.add(target);
            dbManager.updateComponent(project, mAuth.getCurrentUser().getUid(), target);
            setUpRecyclerView(libraryView, project.libraries);
        }
    }

    public void addTool(View view){
        final List<String> options = new ArrayList<>();
        dbManager.getAllTool(new GetCallBack() {
            @Override
            public void onTriggered(List<Node> list) {
                options.clear();
                nodeList = list;
                for (Node n : list) {
                    options.add(n.getName());
                }

                // pop up alert dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProjectActivity.this);
                final AlertDialog dialog = builder.create();
                AddComponentView addComponentView = new AddComponentView(getApplicationContext(), dialog);
                addComponentView.setOptions(options);
                addComponentView.setEventListerner(new AddComponentListener() {
                    @Override
                    public void onAdded(String str) {
                        updateTool(str);
                    }
                });
                dialog.setView(addComponentView);
                dialog.show();
            }
        });
    }

    private void updateTool(String str) {
        Node target = null;
        for (Node n : nodeList) {
            if (n.getName().equals(str)) {
                target = n;
                break;
            }
        }
        if (target != null) {
            project.tools.add(target);
            dbManager.updateComponent(project, mAuth.getCurrentUser().getUid(), target);
            setUpRecyclerView(toolView, project.tools);
        }
    }
}
