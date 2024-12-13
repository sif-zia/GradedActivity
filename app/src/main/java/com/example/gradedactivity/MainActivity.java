package com.example.gradedactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                    PostsAdapter postsAdapter = new PostsAdapter(posts, MainActivity.this);
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
                PostsAdapter postsAdapter = (PostsAdapter) postsRecyclerView.getAdapter();
                if(postsAdapter != null) {
                    postsAdapter.setPosts(filteredPosts);
                }
            }
            else {
                Toast.makeText(MainActivity.this, "No posts found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int EDIT_POST_REQUEST_CODE = 1;
        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the updated data
            int postId = data.getIntExtra("postId", -1);
            String updatedTitle = data.getStringExtra("updatedTitle");
            String updatedBody = data.getStringExtra("updatedBody");

            // Find the post in the list and update it
            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).id == postId) {
                    posts.get(i).title = updatedTitle;
                    posts.get(i).body = updatedBody;

                    // Notify the adapter to refresh the item
                    PostsAdapter postsAdapter = (PostsAdapter) postsRecyclerView.getAdapter();
                    if(postsAdapter != null) {
                        postsAdapter.notifyItemChanged(i);
                    }
                    break;
                }
            }
        }
    }
}