package corp.tuter.developerscompanionapp.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import corp.tuter.developerscompanionapp.ProjectActivity;
import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.R;

public class ProjectAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Project> items;
    private Context context;

    public ProjectAdaptor(List<Project> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public ProjectAdaptor(List<Project> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_with_pic_item, parent, false);
        MyViewHolder vh1 = new MyViewHolder(v);
        return vh1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewholder = (MyViewHolder) holder;
        TextView name = viewholder.name;
        name.setText(items.get(position).getName());

        ImageButton pic =  viewholder.pic;
        pic.setImageResource(R.mipmap.project_ic);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProjectActivity.class);
                intent.putExtra("project", items.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageButton pic;
        public MyViewHolder(View v) {
            super(v);
            this.pic = v.findViewById(R.id.logo_1);
            this.name = v.findViewById(R.id.name_1);
        }
    }
}
