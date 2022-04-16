package com.example.shibushi.Outfits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.R;

import java.util.List;

public class OutfitParentAdapter extends RecyclerView.Adapter<OutfitParentAdapter.OutfitParentViewHolder>{

    Context context;
    LayoutInflater inflater;
    List<OutfitChildModel.ChildDataSource> datas;
    String category;

    public OutfitParentAdapter(Context context, OutfitParentModel.ParentDataSource parentDataSource) {
        this.context = context;
        this.datas = parentDataSource.data;
        this.category = parentDataSource.category;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutfitParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View outfitParentView = inflater.inflate(R.layout.snippet_viewoutfits_parent_main, parent, false);
        return new OutfitParentViewHolder(outfitParentView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitParentViewHolder holder, int position) {
        OutfitChildAdapter outfitChildAdapter = new OutfitChildAdapter(context, datas.get(position));
        LinearLayoutManager parentlinearLayoutManager = new LinearLayoutManager(context);
        parentlinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.outfitParentRV.setLayoutManager(parentlinearLayoutManager);
        holder.outfitParentRV.setAdapter(outfitChildAdapter);
        holder.outfitParentRV.setVisibility(View.VISIBLE);
        holder.categoryTextView.setText(datas.get(position).category);
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    public static class OutfitParentViewHolder extends RecyclerView.ViewHolder{
        RecyclerView outfitParentRV;
        TextView categoryTextView;
        OutfitParentViewHolder(View view){
            super(view);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            outfitParentRV = view.findViewById(R.id.outfitParentRecyclerView);
        }
    }
}