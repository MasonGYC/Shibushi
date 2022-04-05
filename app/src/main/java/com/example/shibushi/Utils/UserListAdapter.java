package com.example.shibushi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shibushi.Models.User;
import com.example.shibushi.Models.UserAccountSettings;
import com.example.shibushi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is used for the displaying of users in searchActivity
 */
public class UserListAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UserListAdapter";

    private LayoutInflater mInflator;
    private List<User> mUsers = null;
    private int layoutResource;
    private Context mContext;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = resource;
        this.mUsers = objects;

    }

    private static class ViewHolder {
        TextView username;
        CircleImageView profile_photo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflator.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.username = convertView.findViewById(R.id.username);
            viewHolder.profile_photo = convertView.findViewById(R.id.profile_photo);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // For username
        viewHolder.username.setText(getItem(position).getUsername());

        // Query for profile photo
        DatabaseReference mRef = FirebaseDatabase.getInstance("https://shibushi-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        Query query = mRef.child("user_account_settings")
                .orderByChild("user_id")
                .equalTo(getItem(position).getUserID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot: snapshot.getChildren()) {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(singleSnapshot.getValue(
                            UserAccountSettings.class).getProfile_photo(),
                            viewHolder.profile_photo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return convertView;
    }

}
