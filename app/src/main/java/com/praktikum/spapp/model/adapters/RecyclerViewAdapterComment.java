package com.praktikum.spapp.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praktikum.spapp.R;



import com.praktikum.spapp.common.DateStringSplitter;
import com.praktikum.spapp.model.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterComment extends RecyclerView.Adapter<RecyclerViewAdapterComment.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Comment> comments;
    private ArrayList<Comment> commentsAll;


    private Context aContext;

    public RecyclerViewAdapterComment(ArrayList<Comment> comments, Context aContext) {
        this.comments = comments;
        this.commentsAll = new ArrayList<>(comments);
        this.aContext = aContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_comment, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView commentContent;
        TextView commentAuthor;
        TextView CommentDate;
        boolean commentIsRestricted;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentContent = itemView.findViewById(R.id.comment_adapter_content);
            commentAuthor = itemView.findViewById(R.id.comment_adapter_author);
            CommentDate = itemView.findViewById(R.id.comment_adapter_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder:  called.");

        viewHolder.commentContent.setText(comments.get(i).getContent());
        viewHolder.CommentDate.setText("Posted at: " + DateStringSplitter.datePrettyPrint(comments.get(i).getCreationTime()) + " " + DateStringSplitter.timePrettyPrint(comments.get(i).getCreationTime()));
        if (comments.get(i).getAuthor() != null) {
            viewHolder.commentAuthor.setText(comments.get(i).getAuthor().getUsername());
        }
        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(aContext, com.praktikum.spapp.activity.comment.CommentDetailsActivity.class);
            intent.putExtra("comment", comments.get(i));
            aContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    /**
     * @input: filter query.
     */

    public Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Comment> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(commentsAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Comment comment : commentsAll) {
                    //filter based on restricted
//                    if (comment.getContent().toLowerCase().contains(filterPattern)) {
                    if (comment.isRestricted() && filterPattern.equals("restricted")) {
                        filteredList.add(comment);
                    } else if (!comment.isRestricted() && filterPattern.equals("not restricted")) {
                        filteredList.add(comment);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            comments.clear();
            comments.addAll((List) results.values);
            Collections.sort(comments);
            notifyDataSetChanged();
        }
    };
}
