package com.example.youngjungoo.warningapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViolenceLogActivity extends AppCompatActivity {

    private String TAG = "ViolenceLogActivity";

    private ListViewAdapter urlList = new ListViewAdapter();
    private CognitoCachingCredentialsProvider credentialsProvider;
    private AmazonS3Client s3;

    TransferUtility transferUtility;

    //track Choosing Image Intent
    private static final int CHOOSING_IMAGE_REQUEST = 1234;

    private int cnt = 1;
    private ListViewAdapter adapter;
    private ListView listView;
    private ImageView imageView;

    private Uri fileUri;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violence_log);

        listView = findViewById(R.id.violenceList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                Intent intent = new Intent(getApplicationContext(), DetailInfoActivity.class);

                intent.putExtra("title", item.getTitle());
                intent.putExtra("img", item.getIcon().toString());
                intent.putExtra("dsc", item.getDesc());



                startActivity(intent);

            }
        });

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:8021df9a-d853-4f12-9c80-579de9952b82", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");


        downloadFile();

    }

    private void downloadFile() {

        transferUtility = TransferUtility.builder()
                .context(getApplicationContext())
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(s3)
                .build();

        Thread thread = new Thread(new Runnable(){
            @Override
            public synchronized void run() {
                try {
                    synchronized(this){
                        List<String> listing = getObjectNamesForBucket("detected-image", s3);
                        Log.i(TAG, "listing "+ listing);
                        Log.i(TAG,"listSize"+listing.size());
                        for(String key:listing){
                            urlList.addItem(s3.getUrl("detected-image", key),"폭력이미지" + cnt++,"발생시간:" +key);
                            Log.i(TAG, "key "+ key);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Exception found while listing "+ e);
                }
            }
        });

        thread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpListView();
                    }
                });
            }
        }).start();



       /*
        new Thread() {
            @Override
            public void run() {
               // super.run();
                ObjectListing objects = s3.listObjects("detected-image");

                List<S3ObjectSummary> summaries = objects.getObjectSummaries();
                while (objects.isTruncated()) {
                    objects = s3.listNextBatchOfObjects (objects);
                    summaries.addAll (objects.getObjectSummaries());
                }
                for(int i = 0 ; i < summaries.size(); i++) {
                    urlList.add(summaries.get(i).getKey().toString());
                }

            }
        }.start();
        */

        //URL url = s3.getUrl("detected-image", "mycam2_2.png");

        //   Glide.with(getBaseContext())
        //                .load(url.toString()).into(imageView);


    }

    private synchronized List<String> getObjectNamesForBucket(String bucket, AmazonS3 s3Client) {
        ObjectListing objects=s3Client.listObjects(bucket);
        List<String> objectNames=new ArrayList<String>(objects.getObjectSummaries().size());
        Iterator<S3ObjectSummary> oIter=objects.getObjectSummaries().iterator();
        while (oIter.hasNext()) {
            objectNames.add(oIter.next().getKey());
        }
        while (objects.isTruncated()) {
            objects=s3Client.listNextBatchOfObjects(objects);
            oIter=objects.getObjectSummaries().iterator();
            while (oIter.hasNext()) {
                objectNames.add(oIter.next().getKey());
                // urlList.add(oIter.next().getKey());
            }
        }
        return objectNames;
    }

    public synchronized void setUpListView() {
        adapter = urlList;
        adapter.refreshAdapter(urlList);
        Log.i(TAG,"adapt" + adapter);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (bitmap != null) {
            bitmap.recycle();
        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}