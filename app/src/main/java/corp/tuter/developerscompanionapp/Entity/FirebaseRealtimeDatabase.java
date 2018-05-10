package corp.tuter.developerscompanionapp.Entity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.CreateNewProjectListener;
import corp.tuter.developerscompanionapp.Model.Framework;
import corp.tuter.developerscompanionapp.Model.Library;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.Model.Tool;

public class FirebaseRealtimeDatabase {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public static final int FRAMEWORK = 1, LIBRARY = 2, TOOL = 3;

    public void readSimpleData(String path, final MyCallBack callBack) {
        DatabaseReference mRef;
        if (path.equals(""))
            mRef = mDatabase.getReference();
        else
            mRef = mDatabase.getReference(path);

        final String oldPath = path;
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Node> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    if(ds.getKey().equals("User"))
                        continue;
                    String name = ds.child("name").getValue(String.class);
                    String desc = ds.child("description").getValue(String.class);
                    String imageName = ds.child("image_url").getValue(String.class);
                    Log.d("DB", key + "/" + name + "/" + desc);
                    String newPath = oldPath +"/" + key;
                    Node node = new Node(name, imageName, desc, newPath);
                    list.add(node);

                }
                Log.d("Call", list.size() +"!!");
                callBack.onCallBack(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }

    public void readDataIncludingChild(String path, final MyCallBack callBack, final int type) {
        DatabaseReference mRef;
        String queryPath = path;
        //update path depending on type
        switch (type) {
            case FRAMEWORK:
                queryPath += "/Framework";
                break;
            case LIBRARY:
                queryPath += "/Library";
                break;
            case TOOL:
                queryPath += "/Tool";
                break;
            default:
                break;
        }

        mRef = mDatabase.getReference(queryPath);
        final String oldPath = queryPath;
        Log.d("Type", queryPath);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Node> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Node node;
                    String key = ds.getKey();
                    String name = ds.child("name").getValue(String.class);
                    String desc = ds.child("description").getValue(String.class);
                    String imageName = ds.child("image_url").getValue(String.class);
                    Log.d("DB", key + "/" + name + "/" + desc);
                    String newPath = oldPath +"/" + key;
                    if (type == FRAMEWORK)
                        node = new Framework(name, imageName, desc, newPath);
                    else if (type == LIBRARY)
                        node = new Library(name, imageName, desc, newPath);
                    else if (type == TOOL)
                        node = new Tool(name, imageName, desc, newPath);
                    else
                        node= new Node(name, imageName, desc, newPath);
                    list.add(node);

                }
                Log.d("Call", list.size() +"!!");
                callBack.onCallBack(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }

    public void writeToDatabase(String ref, String key, Project project) {
        mDatabase.getReference().child(ref).child(key).push().setValue(project);
    }

    public void getUserProject(String userID, final MyProjectCallback callBack) {
        DatabaseReference mRef;
        String path = "User/" + userID + "/Project";
        mRef = mDatabase.getReference(path);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Project project;
                    String name = ds.child("name").getValue(String.class);
                    String desc = ds.child("description").getValue(String.class);

                    List<Node> frameworks = new ArrayList<>();
                    List<Node> libraries = new ArrayList<>();
                    List<Node> tools = new ArrayList<>();

                    project = new Project(name, desc);
                    project.frameworks = frameworks;
                    project.libraries = libraries;
                    project.tools = tools;
                    list.add(project);
                }
                callBack.onCallBack(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }

    public void deleteProject(final Project project, String userID) {
        final DatabaseReference mRef;
        final String path = "User/"+ userID + "/Project";
        mRef = mDatabase.getReference(path);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("name").getValue(String.class).equals(project.getName())
                            &&ds.child("description").getValue(String.class).equals(project.getDescription())) {
                        DatabaseReference reference = mDatabase.getReference(path + "/" + ds.getKey());
                        reference.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to find value", error.toException());
            }
        });
    }

    public void readAllChild(final int type, final GetCallBack myCallBack) {
        DatabaseReference mRef = mDatabase.getReference();
        String identifier = "";
        if (type == FRAMEWORK)
            identifier = "Framework";
        else if (type == LIBRARY)
            identifier = "Library";
        else
            identifier = "Tool";

        final String finalIdentifier = identifier;

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Node> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(""))
                        continue;
                    for (DataSnapshot ds2: ds.child(finalIdentifier).getChildren()) {
                        Node node;
                        String newPath = ds.getKey() + "/"+finalIdentifier+"/" + ds2.getKey();
                        String name = ds2.child("name").getValue(String.class);
                        String desc = ds2.child("description").getValue(String.class);
                        String imageName = ds2.child("image_url").getValue(String.class);
                        if (type == FRAMEWORK)
                            node = new Framework(name, imageName, desc, newPath);
                        else if (type == LIBRARY)
                            node = new Library(name, imageName, desc, newPath);
                        else if (type == TOOL)
                            node = new Tool(name, imageName, desc, newPath);
                        else
                            node = new Node(name, imageName, desc, newPath);
                        list.add(node);
                    }
                }

                myCallBack.onTriggered(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }

    public void WriteComponentToDatabase(String ref, final String identifier, final Node node, final Project project) {
        final DatabaseReference mRef = mDatabase.getReference(ref);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("name").getValue(String.class).equals(project.getName())
                            &&ds.child("description").getValue(String.class).equals(project.getDescription())) {
                        mRef.child(ds.getKey()).child(identifier).push().setValue(node);
                        return;
                    } else {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("DB", "Failed to write value.", error.toException());
            }
        });
    }

    public void readAllComponent(final Project project, String uid, final AllComponentCallBack callBack) {
        final DatabaseReference mRef = mDatabase.getReference("User/"+uid+"/Project");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("name").getValue(String.class).equals(project.getName())
                            &&ds.child("description").getValue(String.class).equals(project.getDescription())) {

                        List<Node> frameworks = new ArrayList<>();
                        for (DataSnapshot dsf : ds.child("Framework").getChildren()) {
                            String fName = dsf.child("name").getValue(String.class);
                            String fDesc = dsf.child("description").getValue(String.class);
                            String fImageName = dsf.child("image_url").getValue(String.class);
                            Node framework = new Framework(fName, fDesc, fImageName, "");
                            frameworks.add(framework);
                        }

                        List<Node> libraries = new ArrayList<>();
                        for (DataSnapshot dsl : ds.child("Library").getChildren()) {
                            String fName = dsl.child("name").getValue(String.class);
                            String fDesc = dsl.child("description").getValue(String.class);
                            String fImageName = dsl.child("image_url").getValue(String.class);
                            Node library = new Library(fName, fDesc, fImageName, "");
                            libraries.add(library);
                        }

                        List<Node> tools = new ArrayList<>();
                        for (DataSnapshot dst : ds.child("Tool").getChildren()) {
                            String fName = dst.child("name").getValue(String.class);
                            String fDesc = dst.child("description").getValue(String.class);
                            String fImageName = dst.child("image_url").getValue(String.class);
                            Node tool = new Tool(fName, fDesc, fImageName, "");
                            tools.add(tool);
                        }
                        callBack.onGetComponents(frameworks, libraries, tools);
                    } else {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DB", "Cancelled");
            }
        });
    }
}
