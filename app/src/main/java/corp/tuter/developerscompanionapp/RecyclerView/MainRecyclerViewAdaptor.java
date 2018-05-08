package corp.tuter.developerscompanionapp.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.Entity.MyCallBack;
import corp.tuter.developerscompanionapp.Interactor.DBManager;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.R;

public class MainRecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Node parent;
    private DBManager dbManager;

    public MainRecyclerViewAdaptor(Node parent) {
        this.parent = parent;
        dbManager = DBManager.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recycler_view_1, parent, false);
        MyViewHolder2 vh = new MyViewHolder2(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder2 viewHolder = (MyViewHolder2)holder;
        final RecyclerView subView1 = viewHolder.subView1;
        RecyclerView subView2 = viewHolder.subView2;
        RecyclerView subView3 = viewHolder.subView3;
        subView1.setLayoutManager(new GridLayoutManager(subView1.getContext(), 2));
        subView2.setLayoutManager(new GridLayoutManager(subView1.getContext(), 2));
        subView3.setLayoutManager(new GridLayoutManager(subView1.getContext(), 2));

        dbManager.getFrameWorks(parent.getPath(), new MyCallBack() {
            @Override
            public void onCallBack(List<Node> list) {
                subView1.setAdapter(new ImageCustomAdaptor(list));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {

        public RecyclerView subView1, subView2, subView3;
        public MyViewHolder2(View v) {
            super(v);
            this.subView1 = v.findViewById(R.id.sub_recycler_view_1);
            this.subView2 = v.findViewById(R.id.sub_recycler_view_2);
            this.subView3 = v.findViewById(R.id.sub_recycler_view_3);
        }
    }
}
