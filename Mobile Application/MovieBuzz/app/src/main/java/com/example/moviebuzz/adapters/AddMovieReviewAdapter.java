package com.example.moviebuzz.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.R;
import com.example.moviebuzz.databinding.AddlayoutBinding;

import java.util.List;

public class AddMovieReviewAdapter extends RecyclerView.Adapter<AddMovieReviewAdapter.AddMovieReviewAdapterHolder> {

    AddlayoutBinding addBinding;
    List<String> stringList;
    public AddMovieReviewAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public AddMovieReviewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        addBinding = DataBindingUtil.inflate(inflater, R.layout.addlayout, parent, false);
        return new AddMovieReviewAdapterHolder(addBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMovieReviewAdapterHolder holder, int position) {
        holder.bind(this.stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.stringList.size();
    }

    static class AddMovieReviewAdapterHolder extends RecyclerView.ViewHolder
    {
        AddlayoutBinding addBinding;
        public AddMovieReviewAdapterHolder(@NonNull AddlayoutBinding addBinding)
        {
            super(addBinding.getRoot());
            this.addBinding = addBinding;
        }
        public void bind(String s)
        {

        }
    }
}
