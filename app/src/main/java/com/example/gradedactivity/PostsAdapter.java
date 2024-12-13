package com.example.gradedactivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {
    List<Post> posts;

    PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, null, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.body.setText(posts.get(position).body);
        holder.title.setText(posts.get(position).title);

        holder.commentsButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CommentsActivity.class);
            intent.putExtra("postId", posts.get(position).id);

            view.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(view -> {
            AppAPICalls appAPICalls = new AppAPICalls(view.getContext());
            appAPICalls.deletePost(posts.get(position).id);

            posts.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, posts.size());
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

class PostViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView body;
    Button editButton;
    Button commentsButton;
    Button deleteButton;

    public PostViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.tv_title);
        body = itemView.findViewById(R.id.tv_body);
        editButton = itemView.findViewById(R.id.btn_edit);
        commentsButton = itemView.findViewById(R.id.btn_comments);
        deleteButton = itemView.findViewById(R.id.btn_delete);
    }
}