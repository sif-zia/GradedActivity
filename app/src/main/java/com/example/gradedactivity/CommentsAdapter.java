package com.example.gradedactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    List<Comment> commentList;

    CommentsAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null, false);

        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.name.setText(commentList.get(position).name);
        holder.email.setText(commentList.get(position).email);
        holder.body.setText(commentList.get(position).body);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

class CommentsViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView email;
    TextView body;

    public CommentsViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_cname);
        email = itemView.findViewById(R.id.tv_cemail);
        body = itemView.findViewById(R.id.tv_cbody);
    }
}