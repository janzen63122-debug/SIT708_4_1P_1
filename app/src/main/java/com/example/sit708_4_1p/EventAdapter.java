package com.example.sit708_4_1p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Event event);
        void onDeleteClick(Event event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = events.get(position);

        holder.titleTextView.setText(currentEvent.getTitle());
        holder.categoryTextView.setText(currentEvent.getCategory());
        holder.locationTextView.setText("Location: " + currentEvent.getLocation());


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a", Locale.getDefault());
        String readableDateTime = sdf.format(new Date(currentEvent.getDate())); // Ensure Event.java uses getDate() or getDateTime()
        holder.dateTimeTextView.setText(readableDateTime);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, categoryTextView, locationTextView, dateTimeTextView;
        private Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textEventTitle);
            categoryTextView = itemView.findViewById(R.id.textEventCategory);
            locationTextView = itemView.findViewById(R.id.textEventLocation);
            dateTimeTextView = itemView.findViewById(R.id.textEventDateTime);
            deleteButton = itemView.findViewById(R.id.buttonDelete);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(events.get(position));
                }
            });


            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(events.get(position));
                }
            });
        }
    }
}