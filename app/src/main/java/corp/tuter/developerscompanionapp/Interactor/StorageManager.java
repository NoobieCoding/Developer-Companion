package corp.tuter.developerscompanionapp.Interactor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import corp.tuter.developerscompanionapp.Entity.GlideApp;

public class StorageManager {

    static StorageManager instance;
    StorageReference mStorageRef;

    private StorageManager() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static StorageManager getInstance() {
        if (instance == null)
            instance = new StorageManager();
        return instance;
    }

    public void assignPictureFromStorage(final Context mContext, final ImageView view, String imageUrl) {
        if (imageUrl == null || imageUrl.equals(""))
            return;
        final StorageReference mRef = mStorageRef.child(imageUrl);
        final long THREE_MEGABYTE = 1024 * 1024 * 3;
        if (!isValidContextForGlide(mContext))
            return;
        mRef.getBytes(THREE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                GlideApp.with(mContext)
                        .load(mRef)
                        .into(view);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

}
