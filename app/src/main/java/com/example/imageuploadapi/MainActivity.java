package com.example.imageuploadapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText img_title;
    private Button btnChoose, btnUpload;
    private ImageView img;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_title = (EditText) findViewById(R.id.img_title);
        btnChoose = (Button) findViewById(R.id.chooseBtn);
        btnUpload = (Button) findViewById(R.id.uploadBtn);
        img = (ImageView) findViewById(R.id.imageView);

        btnChoose.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.chooseBtn :
                selectImage();
                break;

            case R.id.uploadBtn :
                uploadImage();
                break;
        }


    }

    private void uploadImage()
    {
        String Image = imageToString();
        String Title = img_title.getText().toString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass> call = apiInterface.uploadImage(Title,Image);

        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
                ImageClass imageClass = response.body();
                Toast.makeText(MainActivity.this, "Server Response" +imageClass.getResponse(), Toast.LENGTH_SHORT).show();
                img.setVisibility(View.GONE);
                img_title.setVisibility(View.GONE);
                btnChoose.setEnabled(true);
                btnUpload.setEnabled(false);
                img_title.setText("");
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {

            }
        });
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                img_title.setVisibility(View.VISIBLE);
                btnChoose.setEnabled(false);
                btnUpload.setEnabled(true);
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    //method ini untuk di saring dari base64 ke String (disini memakai base64 karena kalau 2gb> pasti ngelag uploadnya
    private String imageToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //gambar di kompres menjadi JPEG dan kualitasnya 100
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }


}
