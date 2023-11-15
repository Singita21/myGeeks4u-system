package com.example.mygeeks4u.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygeeks4u.R;
import com.example.mygeeks4u.activities.ChatActivity;
import com.example.mygeeks4u.databinding.ItemContainerUserBinding;
import com.example.mygeeks4u.listeners.UserListener;
import com.example.mygeeks4u.models.User;
import com.example.mygeeks4u.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends BaseAdapter {

    private final List<User> usersLists;
    private final Context context;
    private final UserListener userListener;

    public UsersAdapter(Context context, ArrayList<User> usersLists, UserListener userListener) {
        this.context = context;
        this.usersLists = usersLists;
        this.userListener = userListener;
    }

    public int getUsers()
    {
        if(usersLists.isEmpty())
        {
            return 1;
        }
        else
        {
            return 10;
        }

    }

    @Override
    public int getCount() {
        return usersLists.size();
    }

    @Override
    public Object getItem(int position) {
        return usersLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_container_user, parent, false);
            holder = new ViewHolder();
            User user = usersLists.get(position);

            convertView.setOnClickListener(v ->{
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Constants.KEY_USER,user);
                context.startActivity(intent);
            });
            holder.nameTextView = convertView.findViewById(R.id.textName);
            holder.emailTextView = convertView.findViewById(R.id.textEmail);

            holder.nameTextView.setText(user.name);
            holder.emailTextView.setText(user.email);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }




        return convertView;
    }

    public void updateData(List<User> newUsers) {
        usersLists.addAll(newUsers);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
    }
}
