package com.example.mygeeks4u.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygeeks4u.R;
import com.example.mygeeks4u.listeners.UserListener;
import com.example.mygeeks4u.models.User;
import com.example.mygeeks4u.adapters.UsersAdapter;
import com.example.mygeeks4u.databinding.ActivityUsersBinding;
import com.example.mygeeks4u.utilities.Constants;
import com.example.mygeeks4u.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {
    ProgressBar progressBar;
    TextView text;
    private int count=0;
    private ListView userListView;
    private PreferenceManager preferenceManager;
    private UsersAdapter usersAdapter;
    private UsersAdapter usersListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_users);

        // Initialize views
        userListView = findViewById(R.id.userListView);
        progressBar = findViewById(R.id.progressBar); // Assuming you have a ProgressBar with this ID


        preferenceManager = new PreferenceManager(getApplicationContext());
        usersListAdapter = new UsersAdapter(this, new ArrayList<>(),this);
        userListView.setAdapter(usersListAdapter);
        getUser();
    }
    public void getUser ()
    {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task ->
        {
            loading(false);
            String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (task.isSuccessful() && task.getResult() != null)
            {
                List<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                {
                    if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    User user = new User();
                    user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                    user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                    user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                    user.id = queryDocumentSnapshot.getId();
                    users.add(user);

                }

                if(!users.isEmpty())
                {
                    usersListAdapter.updateData(users);
                    userListView.setVisibility(View.VISIBLE);

                } else {
                    // Handle any errors that occurred during the data retrieval

                    showErrorMessage();

                }
            }else{
                showErrorMessage();
            }
        });
    }
    public void showErrorMessage() {
        // Handle and display an error message
        text.setText(String.format("%s", "No users found"));
        text.setVisibility(View.VISIBLE);
    }

    public void loading(Boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User users) {

    }


}