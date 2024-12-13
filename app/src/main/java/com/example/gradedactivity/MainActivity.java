package com.example.gradedactivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView postsRecyclerView;
    EditText searchEditText;
    Button searchButton;
    List<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.et_search);
        searchButton = findViewById(R.id.btn_search);

        postsRecyclerView = findViewById(R.id.rv_posts);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppAPICalls appAPICalls = new AppAPICalls(this);

        appAPICalls.getPosts(new AppAPICalls.PostsResponse() {
            @Override
            public void onResponse(List<Post> posts) {
                if (!posts.isEmpty()) {
                    PostsAdapter postsAdapter = new PostsAdapter(posts);
                    MainActivity.this.posts = posts;
                    postsRecyclerView.setAdapter(postsAdapter);
                }
                else {
                    Toast.makeText(MainActivity.this, "No posts found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            List<Post> filteredPosts = new ArrayList<>();

            for (Post post : posts) {
                if (post.title.contains(searchQuery) || post.body.contains(searchQuery)) {
                    filteredPosts.add(post);
                }
            }

            if (!filteredPosts.isEmpty()) {
                PostsAdapter postsAdapter = new PostsAdapter(filteredPosts);
                postsRecyclerView.setAdapter(postsAdapter);
            }
            else {
                Toast.makeText(MainActivity.this, "No posts found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}