package com.example.gradedactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView commentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comments);

        commentsRecyclerView = findViewById(R.id.rv_comments);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppAPICalls appAPICalls = new AppAPICalls(this);

        int postId = getIntent().getIntExtra("postId", -1);

        if(postId == -1) {
            Toast.makeText(CommentsActivity.this, "No post id found", Toast.LENGTH_SHORT).show();
        }
        else {
            appAPICalls.getComments(postId, new AppAPICalls.CommentsResponse() {
                @Override
                public void onResponse(List<Comment> comments) {
                    if (!comments.isEmpty()) {
                        CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
                        commentsRecyclerView.setAdapter(commentsAdapter);
                    } else {
                        Toast.makeText(CommentsActivity.this, "No comments found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(CommentsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}