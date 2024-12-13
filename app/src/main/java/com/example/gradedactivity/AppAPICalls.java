package com.example.gradedactivity;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppAPICalls {
    private Context context;
    private String url = "https://jsonplaceholder.typicode.com/";

    AppAPICalls(Context context) {
        this.context = context;
    }

    public interface PostsResponse {
        void onResponse(List<Post> posts);
        void onError(String error);
    }

    public void getPosts(PostsResponse postsResponse) {
        List<Post> posts = new ArrayList<>();

        JsonArrayRequest getPostsRequest = new JsonArrayRequest(Request.Method.GET, url + "posts/", null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject postJSON = (JSONObject) response.get(i);
                            Post post = new Post(
                                    postJSON.getInt("userId"),
                                    postJSON.getInt("id"),
                                    postJSON.getString("title"),
                                    postJSON.getString("body")
                            );

                            posts.add(post);
                        }
                        catch (JSONException e) {
                            postsResponse.onError(e.getMessage());
                        }
                    }

                    postsResponse.onResponse(posts);
                },
                error -> {
                    postsResponse.onError(error.getMessage());
                }
        );

        RequestQueueProvider.getRequestQueue(context).add(getPostsRequest);
    }

    public interface CommentsResponse {
        void onResponse(List<Comment> comments);
        void onError(String error);
    }

    public void getComments(int postId, CommentsResponse commentsResponse) {
        JsonArrayRequest getCommentsRequest = new JsonArrayRequest(Request.Method.GET, url + "posts/" + postId + "/comments", null,
                response -> {
                    List<Comment> comments = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject commentJSON = (JSONObject) response.get(i);
                            Comment comment = new Comment(
                                    commentJSON.getInt("postId"),
                                    commentJSON.getInt("id"),
                                    commentJSON.getString("name"),
                                    commentJSON.getString("email"),
                                    commentJSON.getString("body")
                            );

                            comments.add(comment);
                        }
                        catch (JSONException e) {
                            commentsResponse.onError(e.getMessage());
                        }
                    }

                    commentsResponse.onResponse(comments);
                },
                error -> {
                    commentsResponse.onError(error.getMessage());
                }
        );

        RequestQueueProvider.getRequestQueue(context).add(getCommentsRequest);
    }

    public void deletePost(int postId) {
        StringRequest deletePostRequest = new StringRequest(Request.Method.DELETE, url + "posts/" + postId, response -> {
            Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueueProvider.getRequestQueue(context).add(deletePostRequest);
    }

    public void updatePost(int postId, String updatedTitle, String updatedBody, int userId) {
        StringRequest updatePostRequest = new StringRequest(Request.Method.PUT, url + "posts/" + postId, response -> {
            Toast.makeText(context, "Post updated", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject postJSON = new JSONObject();
                try {
                    postJSON.put("id", postId);
                    postJSON.put("userId", userId);
                    postJSON.put("title", updatedTitle);
                    postJSON.put("body", updatedBody);
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return postJSON.toString().getBytes();
            }
        };

        RequestQueueProvider.getRequestQueue(context).add(updatePostRequest);
    }
}
