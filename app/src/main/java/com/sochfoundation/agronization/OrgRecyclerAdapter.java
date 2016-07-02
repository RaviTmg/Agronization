package com.sochfoundation.agronization;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.sochfoundation.agronization.Widgets.FadeInNetworkImageView;

import java.util.List;

public class OrgRecyclerAdapter extends RecyclerView.Adapter<OrgRecyclerAdapter.Holder> {
    Context context;
    List<Organization> objectList;
    ImageLoader imageLoader;

    public OrgRecyclerAdapter(Context context, List<Organization> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_row, parent, false);
        // create ViewHolder instance
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Organization orgObject = objectList.get(position);

        imageLoader = AppController.getInstance().getImageLoader();
        holder.mimage.setImageUrl(orgObject.getImg(), imageLoader);
        holder.mname.setText(orgObject.getName());
        holder.mlocation.setText(orgObject.getLocation());
        holder.type.setText(orgObject.getType());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrgInfoActivity.class);
                intent.putExtra("ID", orgObject.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView mimage;
        TextView mname, type, mlocation;
        CardView cardView;

        public Holder(View itemView) {
            super(itemView);
            mimage = (FadeInNetworkImageView) itemView.findViewById(R.id.img);
            mname = (TextView) itemView.findViewById(R.id.txt_name);
            type = (TextView) itemView.findViewById(R.id.txt_name_tag);
            mlocation = (TextView) itemView.findViewById(R.id.txt_location);
            cardView = (CardView) itemView.findViewById(R.id.cv_row);
        }
    }
}
