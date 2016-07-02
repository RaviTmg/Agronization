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

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    Context context;
    List<EvenObject> objectList;
    ImageLoader imageLoader;

    public EventsRecyclerAdapter(Context context, List<EvenObject> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_row, parent, false);
        // create ViewHolder instance
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EvenObject evenObject = objectList.get(position);
        //   imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader = AppController.getInstance().getImageLoader();
        // imageLoader.get(orgObject.getImg(), ImageLoader.getImageListener(holder.mimage,
        //         R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.mimage.setImageUrl(evenObject.getImg(), imageLoader);
        holder.mname.setText(evenObject.getName());
        holder.mlocation.setText(evenObject.getLocation());
        holder.type.setText(evenObject.getType());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrgInfoActivity.class);
                intent.putExtra("ID", evenObject.getId());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView mimage;
        TextView mname, type, mlocation;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mimage = (FadeInNetworkImageView) itemView.findViewById(R.id.img);
            mname = (TextView) itemView.findViewById(R.id.txt_name);
            type = (TextView) itemView.findViewById(R.id.txt_name_tag);
            mlocation = (TextView) itemView.findViewById(R.id.txt_location);
            cardView = (CardView) itemView.findViewById(R.id.cv_row);
        }
    }
}
