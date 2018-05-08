package corp.tuter.developerscompanionapp.Interactor;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.Entity.FirebaseRealtimeDatabase;
import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Model.Category;
import corp.tuter.developerscompanionapp.Model.Framework;
import corp.tuter.developerscompanionapp.Model.Node;

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
}
