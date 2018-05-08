package corp.tuter.developerscompanionapp.Entity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.Model.Framework;
import corp.tuter.developerscompanionapp.Model.Library;
import corp.tuter.developerscompanionapp.Model.Node;
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


}
