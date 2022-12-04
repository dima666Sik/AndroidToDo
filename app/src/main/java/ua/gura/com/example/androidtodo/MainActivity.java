package ua.gura.com.example.androidtodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.imageView).setOnClickListener((view) -> {
            Intent intent = new Intent(this,
                    BaseActivity.class);
            startActivity(intent);
        });
    }
}