package corp.tuter.developerscompanionapp.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.DescriptionActivity;
import corp.tuter.developerscompanionapp.Interactor.StorageManager;
import corp.tuter.developerscompanionapp.Level1Activity;
import corp.tuter.developerscompanionapp.Model.Library;
import corp.tuter.developerscompanionapp.Model.Node;
import corp.tuter.developerscompanionapp.Model.Tool;
import corp.tuter.developerscompanionapp.R;

public class ImageCustomAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Node> items;
    private StorageManager mStorageManager;
    private Context context;

    public ImageCustomAdaptor(List<Node> items, Context context) {
        this.items = items;
        mStorageManager = StorageManager.getInstance();
        this.context = context;
    }

    public ImageCustomAdaptor(List<Node> items) {
        this.items = items;
        mStorageManager = StorageManager.getInstance();
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
        Log.d("Name", items.get(position).getName() + "AAAa");
        name.setText(items.get(position).getName());

        ImageButton pic =  viewholder.pic;
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (items.get(position) instanceof Library || items.get(position) instanceof Tool) {
                    intent = new Intent(v.getContext(), DescriptionActivity.class);
                } else {
                    intent = new Intent(v.getContext(), Level1Activity.class);
                }
                intent.putExtra("parent", items.get(position));
                v.getContext().startActivity(intent);
            }
        });
        mStorageManager.assignPictureFromStorage(context,pic, items.get(position).getImageName());
        //set Image
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
