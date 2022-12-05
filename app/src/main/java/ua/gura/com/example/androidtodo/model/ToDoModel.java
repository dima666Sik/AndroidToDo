package ua.gura.com.example.androidtodo.model;

public class ToDoModel extends TaskId{
    private String task;
    private String date;
    private int status;

    public String getTask() {
        return task;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }
}
