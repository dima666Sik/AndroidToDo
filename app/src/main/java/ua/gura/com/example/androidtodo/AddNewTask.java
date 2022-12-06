package ua.gura.com.example.androidtodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    private TextView textViewDate;
    private EditText editTextTask;
    private Button buttonSave;
    private FirebaseFirestore firestore;
    private Context context;
    private String date = new String();
    private String id = new String();
    private String dateUpdate = new String();

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewDate = view.findViewById(R.id.setDate);
        editTextTask = view.findViewById(R.id.taskEditText);
        buttonSave = view.findViewById(R.id.buttonSave);

        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dateUpdate = bundle.getString("date");
            editTextTask.setText(task);
            textViewDate.setText(dateUpdate);
            if (task.length() > 0) {
                buttonSave.setEnabled(false);
                buttonSave.setBackgroundColor(Color.GRAY);
            }
        }
        editTextTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence
                        .toString()
                        .equals("")) {
                    buttonSave.setEnabled(false);
                    buttonSave.setBackgroundColor(Color.GRAY);
                } else {
                    buttonSave.setEnabled(true);
                    buttonSave.setBackgroundColor(Color.YELLOW);

                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textViewDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int MONTH = calendar.get(Calendar.MONTH);
            int YEAR = calendar.get(Calendar.YEAR);
            int DAY = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMonth) -> {
                month = month + 1;
                date = dayOfMonth + "-" + month + "-" + year;
                textViewDate.setText(date);
            }, YEAR, MONTH, DAY);
            datePickerDialog.show();
        });

        boolean finalIsUpdate = isUpdate;
        buttonSave.setOnClickListener(v -> {
            String task = editTextTask.getText().toString();

            if (finalIsUpdate) {
                firestore.collection("task")
                        .document(id)
                        .update("task",task
                                ,"date",date);
                Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show();
            } else {
                if (task.isEmpty()) {
                    Toast.makeText(context, "Empty task not allowed!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("task", task);
                    taskMap.put("date", date);
                    taskMap.put("status", 0);
                    taskMap.put("time", FieldValue.serverTimestamp());

                    firestore.collection("task")
                            .add(taskMap)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(context, "Task saved!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
            dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
