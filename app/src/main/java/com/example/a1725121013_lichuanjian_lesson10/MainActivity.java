package com.example.a1725121013_lichuanjian_lesson10;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;
import java.text.FieldPosition;

public class MainActivity extends AppCompatActivity {
    String filename, image;
    File file;
    Button saveButton, displayButton;
    ImageView imageView;
    RadioButton camilaRadioButton, taylorRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveButton = findViewById(R.id.bt_save);
        displayButton = findViewById(R.id.bt_dis);
        imageView = findViewById(R.id.imageView);

        camilaRadioButton = findViewById(R.id.rb_camila);
        taylorRadioButton = findViewById(R.id.rb_taylor);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            camilaRadioButton.setEnabled(false);
            taylorRadioButton.setEnabled(false);
            Toast.makeText(MainActivity.this, "外部存储不可用", Toast.LENGTH_SHORT).show();
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filename != null) {
                    downloadImage();
                }
            }
        });
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filename != null) {
                    readImageFile();
                }
            }
        });


    }

    private void readImageFile() {
        try {
            File readFile = new File(file, filename);
            if (readFile.exists()) {
                FileInputStream inputStream = new FileInputStream(readFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                imageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(MainActivity.this, "图片不存在!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File write_file = new File(file, filename);
                    if (!write_file.exists()) {
                        URL url = new URL(image);
                        URLConnection connection = (URLConnection) url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        FileOutputStream outputStream = new FileOutputStream(write_file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_gakki:
                if (checked) {
                    image = getString(R.string.url_pic_gakki);
                    filename = "My_persistent_file_internal";
                    file = getFilesDir();
                }break;
            case R.id.rb_satomi:
                if (checked) {
                    image = getString(R.string.url_pic_satomi);
                    filename = "My_cache_file_internal";
                    file = getCacheDir();
                }break;

            case R.id.rb_taylor:
                if (checked) {
                    image = getString(R.string.url_pic_taylor);
                    filename = "My_persistent_file_external";
                    file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                }break;
            case R.id.rb_camila:
                if (checked) {
                    image = getString(R.string.url_pic_camila);
                    filename = "My_cache_file_external";
                    file = getExternalCacheDir();
                }
                break;
        }
        System.out.println(image);
    }
}
