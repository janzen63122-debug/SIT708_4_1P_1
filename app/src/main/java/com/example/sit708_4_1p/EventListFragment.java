package com.example.sit708_4_1p;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EventListFragment extends Fragment {

    private EventAdapter adapter;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);


        EventDatabase.getInstance(requireContext()).eventDao().getAllEvents()
                .observe(getViewLifecycleOwner(), events -> adapter.setEvents(events));


        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Event event) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", event.getId());
                bundle.putString("title", event.getTitle());
                bundle.putString("category", event.getCategory());
                bundle.putString("location", event.getLocation());
                bundle.putLong("dateTime", event.getDate());

                Navigation.findNavController(requireView()).navigate(R.id.addEditEventFragment2, bundle);
            }

            @Override
            public void onDeleteClick(Event event) {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    EventDatabase.getInstance(requireContext()).eventDao().delete(event);


                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });
    }
}