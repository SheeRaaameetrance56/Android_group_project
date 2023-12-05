package com.ousl.application_event_management.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ousl.application_event_management.R;
import com.ousl.application_event_management.models.PrivateEvents;

import java.util.ArrayList;
import java.util.List;

public class MyInvitationsAdapter extends RecyclerView.Adapter<MyInvitationsAdapter.MyViewHolder> {
    private Context context;
    private List<PrivateEvents> invitationsList;

    private OnItemClickListener onItemClickListener;

    public MyInvitationsAdapter(Context context) {
        this.context = context;
        invitationsList = new ArrayList<>();
    }

    public void addInvitation(PrivateEvents invitation) {
        invitationsList.add(invitation);
        notifyDataSetChanged();
    }

    // Add a new method to clear the existing invitations
    public void clearInvitations() {
        invitationsList.clear();
        notifyDataSetChanged();
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemClick(PrivateEvents invitation, String eventId, String userId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.private_card_view_title);
            date = itemView.findViewById(R.id.private_card_view_date);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    PrivateEvents clickedInvitation = invitationsList.get(position);
                    onItemClickListener.onItemClick(clickedInvitation, clickedInvitation.getEventId(), clickedInvitation.getUserId());
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PrivateEvents invitation = invitationsList.get(position);
        holder.title.setText(invitation.getTitle());
        holder.date.setText(invitation.getDate());
    }

    @Override
    public int getItemCount() {
        return invitationsList.size();
    }
}
