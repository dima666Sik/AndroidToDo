package ua.gura.com.example.androidtodo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ua.gura.com.example.androidtodo.AddNewTask;
import ua.gura.com.example.androidtodo.BaseActivity;
import ua.gura.com.example.androidtodo.R;
import ua.gura.com.example.androidtodo.model.ToDoModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> toDoModelList;
    private BaseActivity activity;
    private FirebaseFirestore firestore;

    public Context getContext() {
        return activity;
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = toDoModelList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        ToDoModel toDoModel = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("task", toDoModel.getTask());
        bundle.putString("date",toDoModel.getDate());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(),addNewTask.getTag());
    }

    public ToDoAdapter(List<ToDoModel> toDoModelList, BaseActivity activity) {
        this.toDoModelList = toDoModelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.each_task, parent, false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = toDoModelList.get(position);
        holder.checkBox.setText(toDoModel.getTask());
        holder.textViewDate.setText("Date: " + toDoModel.getDate());
        holder.checkBox.setChecked(convertIntToBoolean(toDoModel.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task")
                            .document(toDoModel.TaskId)
                            .update("status",1);
                }else {
                    firestore.collection("task")
                            .document(toDoModel.TaskId)
                            .update("status",0);
                }
            }
        });
    }

    private boolean convertIntToBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return toDoModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.idDueDate);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
