package com.example.shibushi.Outfits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shibushi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OutfitChildAdapter extends RecyclerView.Adapter<OutfitChildAdapter.OutfitChildViewHolder>{

    Context context;
    LayoutInflater inflater;
    OutfitChildModel.ChildDataSource datasource;

    public OutfitChildAdapter(Context context, OutfitChildModel.ChildDataSource datasource) {
        this.context = context;
        this.datasource = datasource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutfitChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = inflater.inflate(R.layout.viewoutfits_child_cardview, parent, false);
        return new OutfitChildViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitChildViewHolder holder, int position) {
        //todo: scale image to 150*150
        holder.outfitImageButton.setImageURI(this.datasource.get(position).image);
        holder.outfitName.setText(this.datasource.get(position).name);
        holder.outfitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"outfitImageButton",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.datasource.count();
    }

    public static class OutfitChildViewHolder extends RecyclerView.ViewHolder{
        ImageButton outfitImageButton;
        TextView outfitName;
        OutfitChildViewHolder(View view){
            super(view);
            outfitImageButton = view.findViewById(R.id.outfitImageButton);
            outfitName= view.findViewById(R.id.outfitName);

        }
    }
}
