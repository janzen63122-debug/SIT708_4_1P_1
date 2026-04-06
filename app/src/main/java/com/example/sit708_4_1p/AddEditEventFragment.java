package com.example.sit708_4_1p;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditEventFragment extends Fragment {

    private EditText titleEditText;
    private EditText dateEditText;
    private Button saveButton;
    private Calendar calendar = Calendar.getInstance();
    private int currentEventId = -1;

    public AddEditEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleEditText = view.findViewById(R.id.editTextTitle);
        dateEditText = view.findViewById(R.id.editTextDate);

        Spinner categorySpinner = view.findViewById(R.id.spinnerCategory);
        EditText locationEditText = view.findViewById(R.id.editTextLocation);
        EditText timeEditText = view.findViewById(R.id.editTextTime);

        saveButton = view.findViewById(R.id.buttonSave);
        if (getArguments() != null && getArguments().containsKey("id")) {
            currentEventId = getArguments().getInt("id");


            titleEditText.setText(getArguments().getString("title"));
            locationEditText.setText(getArguments().getString("location"));


            long savedTime = getArguments().getLong("dateTime");
            calendar.setTimeInMillis(savedTime);

            SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateEditText.setText(dateFmt.format(calendar.getTime()));

            SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeEditText.setText(timeFmt.format(calendar.getTime()));


            saveButton.setText("Update Event");
        }

        dateEditText.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(requireContext(), (view1, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dateEditText.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });


        timeEditText.setOnClickListener(v -> {
            android.app.TimePickerDialog timeDialog = new android.app.TimePickerDialog(requireContext(), (view12, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                timeEditText.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timeDialog.show();
        });


        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String dateText = dateEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            if (title.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(requireContext(), "Title and Date cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            long selectedDateTimeInMillis = calendar.getTimeInMillis();


            Event eventToSave = new Event(title, category, location, selectedDateTimeInMillis);

            if (currentEventId != -1) {
                eventToSave.setId(currentEventId);
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                if (currentEventId == -1) {

                    EventDatabase.getInstance(requireContext()).eventDao().insert(eventToSave);
                } else {

                    EventDatabase.getInstance(requireContext()).eventDao().update(eventToSave);
                }

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), currentEventId == -1 ? "Event Saved!" : "Event Updated!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).popBackStack();
                });
            });
        });
    }
}