package com.example.sakib23arko.shareandlearn;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;






public class PostDetails extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private TextView PostTitle, PostTime, Postdetails,postDetailsUserName,editPost;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = PostDetails.class.getSimpleName();
    private String TitleName,TimeName,DescriptionName,TagName,downloadFileUrl,ext,userUid,userName,vis,postID;
    private Button downloadButton;
    private ImageView postUserPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        TitleName = getIntent().getStringExtra("Title");
        TimeName = getIntent().getStringExtra("Timee");
        DescriptionName = getIntent().getStringExtra("Description");
        TagName=getIntent().getStringExtra("Tag");
        downloadFileUrl= getIntent().getStringExtra("Chosen");
        ext= getIntent().getStringExtra("Ext");
        userUid = getIntent().getStringExtra("UID");
        userName = getIntent().getStringExtra("Name");
        postID= getIntent().getStringExtra("PostID");
        vis=getIntent().getStringExtra("vis");

        PostTitle = findViewById(R.id.ProjectTitleID);
        PostTime = findViewById(R.id.TimeID);
        Postdetails = findViewById(R.id.InformationID);
        downloadButton=findViewById(R.id.downloadButtonId);
        postUserPic = findViewById(R.id.postDetailsProPicID);
        postDetailsUserName= findViewById(R.id.postDetaisTextViewUserID);
        editPost = findViewById(R.id.postDetailsEditPostID);

        postDetailsUserName.setText(userName);
        PostTitle.setText(TitleName);
        PostTime.setText(TimeName);
        Postdetails.setText(DescriptionName);
        if(downloadFileUrl.isEmpty())downloadButton.setText("No File ");
        else downloadButton.setText("Download File");

        if(vis.equals("edit"))
            editPost.setVisibility(View.VISIBLE);

        StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference("profilePictures/" + userUid + ".jpg");


        GlideApp.with( getApplicationContext()/* context */)
                .load(userProfileImageRef)
                .into(postUserPic);

        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNewPost.class);

                intent.putExtra("Title", TitleName);
                intent.putExtra("Timee", TimeName);
                intent.putExtra("Description", DescriptionName);
                intent.putExtra("Tag",TagName);
                intent.putExtra("Chosen",downloadFileUrl);
                intent.putExtra("Ext",ext);
                intent.putExtra("UID",userUid);
                intent.putExtra("Name",userName);
                intent.putExtra("PostID",postID);
                intent.putExtra("vis","edit");
                Log.d("postt", "PostDetails "+postID + "  " + TitleName + " " + DescriptionName);
                startActivity(intent);
            }
        });


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if SD card is present or not
                if (CheckForSDCard.isSDCardPresent()) {

                    //check if app has permission to write to the external storage.
                    if (EasyPermissions.hasPermissions(PostDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Get the URL entered
                        new DownloadFile().execute(downloadFileUrl);

                    } else {
                        //If permission is not present request for the same.
                        EasyPermissions.requestPermissions(PostDetails.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }


                } else {
                    Toast.makeText(getApplicationContext(),"SD Card not found", Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, PostDetails.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        new DownloadFile().execute(downloadFileUrl);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(PostDetails.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName+"."+ext;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "shareAndLearn/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }


}
