package com.firebase.postsactivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePost extends AppCompatActivity {


    EditText titleEdit,descriptionEdit;
    ImageView imageView;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String imageUrl;
    private String TAG = "CreatePost.java";
    Button createNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        titleEdit           = (EditText) findViewById(R.id.title);
        descriptionEdit     =  (EditText) findViewById(R.id.description);
        imageView           =  (ImageView)findViewById(R.id.createpostimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                Log.i(TAG,"image url"+imageUrl);
            }
        });
        Log.i(TAG,"image url1"+imageUrl);


        createNewPost   = (Button) findViewById(R.id.createpostbutton);
        createNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(titleEdit.getText().toString().length()>0 && descriptionEdit.getText().toString().length() >0 && imageUrl.length()>0)
              {
                ExpensesDAO expensesDAO = new ExpensesDAO(CreatePost.this);
                PostsModel  postsModel = new PostsModel();
                postsModel.setTitle(titleEdit.getText().toString());
                postsModel.setDescription(descriptionEdit.getText().toString());
                postsModel.setImgUrl(imageUrl);

               if(expensesDAO.insert(postsModel)!= -1)
               {
                   Intent intent = new Intent(CreatePost.this,MainActivity.class);
                   startActivity(intent);
               }
                else
               {
                   Toast.makeText(CreatePost.this,"something wrong",Toast.LENGTH_LONG).show();
               }
              }
                else
              {
                  Toast.makeText(CreatePost.this,"Enter all details",Toast.LENGTH_LONG).show();

              }
            }
        });




    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Remove Image" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        int permissionContacts = ContextCompat.checkSelfPermission(CreatePost.this,
                                Manifest.permission.CAMERA);
                        if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(CreatePost.this,"need permissions",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }
                    }

                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                }
                else if (items[item].equals("Choose from Library"))
                {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        int permissionContacts = ContextCompat.checkSelfPermission(CreatePost.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permissionContacts != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(CreatePost.this,"need permissions",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    SELECT_FILE);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    }
                } else if (items[item].equals("Remove Image")) {
                    imageView.setImageResource(R.drawable.two);
                    imageUrl=null;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                BitmapFactory.Options imageOpts = new BitmapFactory.Options ();
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                //thumbnail = Bitmap.createScaledBitmap (thumbnail, 600, 800, false);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 imageView.setImageBitmap(thumbnail);
                imageUrl = destination.toString();

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageView.setImageBitmap(bm);

               imageUrl = selectedImagePath;

            }
        }
    }

    public String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream;
        byte[] byteArray = new byte[0];
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String b64) {

        byte[] imageAsBytes = new byte[0];
        try {
            imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);

        }catch (Exception e)
        {
            e.printStackTrace();
        } return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}
