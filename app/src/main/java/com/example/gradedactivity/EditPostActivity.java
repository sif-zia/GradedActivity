package com.example.gradedactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditPostActivity extends AppCompatActivity {

    EditText bodyEditText;
    EditText titleEditText;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_post);

        titleEditText = findViewById(R.id.et_eTitle);
        bodyEditText = findViewById(R.id.et_eBody);
        updateButton = findViewById(R.id.btn_update);

        int postId = getIntent().getIntExtra("postId", -1);
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        int userId = getIntent().getIntExtra("userId", -1);

        if(postId == -1) {
            finish();
        }

        titleEditText.setText(title);
        bodyEditText.setText(body);

        updateButton.setOnClickListener(view -> {
            String updatedTitle = titleEditText.getText().toString().trim();
            String updatedBody = bodyEditText.getText().toString().trim();

            if (updatedTitle.isEmpty() || updatedBody.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            AppAPICalls appAPICalls = new AppAPICalls(view.getContext());
            appAPICalls.updatePost(postId, updatedTitle, updatedTitle, userId);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("postId", postId); // Original post ID
            resultIntent.putExtra("updatedTitle", updatedTitle);
            resultIntent.putExtra("updatedBody", updatedBody);
            resultIntent.putExtra("userId", userId);

            setResult(RESULT_OK, resultIntent); // Set result as OK
            finish();
        });
    }
}