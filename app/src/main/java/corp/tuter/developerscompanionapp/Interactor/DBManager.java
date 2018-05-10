package corp.tuter.developerscompanionapp.Interactor;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.CustomView.CreateNewProjectListener;
import corp.tuter.developerscompanionapp.Entity.AllComponentCallBack;
import corp.tuter.developerscompanionapp.Entity.FirebaseRealtimeDatabase;
import corp.tuter.developerscompanionapp.Entity.GetCallBack;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Entity.MyProjectCallback;
import corp.tuter.developerscompanionapp.Model.Category;
import corp.tuter.developerscompanionapp.Model.Framework;
import corp.tuter.developerscompanionapp.Model.Library;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.Model.Project;

public  class DBManager {

    static private List<Node> tempList = new ArrayList<>();

    private static FirebaseRealtimeDatabase db = new FirebaseRealtimeDatabase();
    public static DBManager instance = new DBManager();

    private DBManager(){}

    public static DBManager getInstance() {
        return instance;
    }



    public void getNode(final MyCallBack myCallBack) {
        db.readSimpleData("", new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                myCallBack.onCallBack(list);
            }
        });
    }

    public void getFrameWorks(String path, final MyCallBack myCallBack) {
        db.readDataIncludingChild(path, myCallBack, FirebaseRealtimeDatabase.FRAMEWORK);
    }

    public void getLibraries(String path, final MyCallBack myCallBack) {
        db.readDataIncludingChild(path, myCallBack, FirebaseRealtimeDatabase.LIBRARY);
    }

    public void getTools(String path, final MyCallBack myCallBack) {
        db.readDataIncludingChild(path, myCallBack, FirebaseRealtimeDatabase.TOOL);
    }

    public void addProjectToDatabase(Project project, String userEmail) {
        db.writeToDatabase("User/"+userEmail,"Project", project);
    }

    public void getProjects(String userID, MyProjectCallback callBack) {
       db.getUserProject(userID, callBack);
    }

    public void deleteProject(Project project, String userID) {
        db.deleteProject(project, userID);
    }

    public void getAllFrameworks(final GetCallBack myCallBack) {
        db.readAllChild(FirebaseRealtimeDatabase.FRAMEWORK, myCallBack);
    }

    public void getAllLibrary(final GetCallBack myCallBack) {
        db.readAllChild(FirebaseRealtimeDatabase.LIBRARY, myCallBack);
    }

    public void getAllTool(final GetCallBack myCallBack) {
        db.readAllChild(FirebaseRealtimeDatabase.TOOL, myCallBack);
    }

    public void updateComponent(Project project, String uid, Node target) {
        String identifier = "";
        if (target instanceof Framework) {
            identifier = "Framework";
        } else if (target instanceof Library) {
            identifier = "Library";
        } else {
            identifier = "Tool";
        }
        String ref = "User/" + uid + "/Project";
        db.WriteComponentToDatabase(ref, identifier, target, project);
    }

    public void getAllProjectComponent(Project project, String uid, AllComponentCallBack callBack) {
        db.readAllComponent(project, uid, callBack);
    }
}
