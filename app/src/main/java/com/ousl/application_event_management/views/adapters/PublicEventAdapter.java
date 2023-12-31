package com.ousl.application_event_management.views.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.models.PublicEvent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PublicEventAdapter extends RecyclerView.Adapter<PublicEventAdapter.MyViewHolder>{

    private Context context;
    private List<PublicEvent> publicEventList;
    private OnItemClickListener onItemClickListener;


    public PublicEventAdapter(Context context){
        this.context = context;
        this.publicEventList = new ArrayList<>();
    }

    public void addEvent(PublicEvent publicEvent){
        publicEventList.add(publicEvent);
        notifyDataSetChanged();
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(PublicEvent event, String eventId, String userId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, date;
        private ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_view_title);
            date = itemView.findViewById(R.id.card_view_date);
            image = itemView.findViewById(R.id.card_view_image);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    PublicEvent clickedEvent = publicEventList.get(position);
                    onItemClickListener.onItemClick(clickedEvent, clickedEvent.getEventID(), clickedEvent.getUserId());
                }
            });
        }
    }

       public void setEvents(List<PublicEvent> events) {
        publicEventList = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PublicEvent publicEvent = publicEventList.get(position);
        holder.title.setText(publicEvent.getTitle());
        holder.date.setText(publicEvent.getDate());
        String path = "/"+publicEvent.getUserId()+ "/" + publicEvent.getEventID() +"/"+publicEvent.getImageName();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit()
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that may occur while getting the download URL
                Log.e("FirebaseStorage", "Error getting download URL: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return publicEventList.size();
    }

    public void searchedDataList(ArrayList<PublicEvent> searchList){
        publicEventList = searchList;
        notifyDataSetChanged();
        
    }

}
