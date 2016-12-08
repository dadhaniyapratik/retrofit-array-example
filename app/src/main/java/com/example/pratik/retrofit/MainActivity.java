package com.example.pratik.retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratik.retrofit.Pojo.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String url = "https://jsonplaceholder.typicode.com";
    CustomBaseAdapter customBaseAdapter;
    DatabaseHandler databaseHandler;
    ListView listView;
    ProgressDialog mProgressDialog;

    //    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        databaseHandler = new DatabaseHandler(MainActivity.this);


        if(isNetworkAvailable()) {


            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Android JSON Parse Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

            Call<List<UserDetail>> call = service.getStudentDetails();

            call.enqueue(new Callback<List<UserDetail>>() {
                @Override
                public void onResponse(Call<List<UserDetail>> call, Response<List<UserDetail>> response) {
                    mProgressDialog.dismiss();
                    List<UserDetail> userDetails = response.body();
                    databaseHandler.addContact(userDetails);
                    databaseHandler.getAllContacts();
                    customBaseAdapter = new CustomBaseAdapter(MainActivity.this, userDetails);
                    listView.setAdapter(customBaseAdapter);
                    customBaseAdapter.notifyDataSetChanged();


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UserDetail userDetail = (UserDetail) customBaseAdapter.getItem(position);
                            CustomBaseAdapter customBaseAdapter = (CustomBaseAdapter) parent.getAdapter();
                            customBaseAdapter.getItem(position);
                            String s1 = userDetail.getAddress().getGeo().getLat();
                            String s2 = userDetail.getAddress().getGeo().getLng();
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=-" + s1 + "," + s2 + " (" + userDetail.getName() + ")"));
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "lat" + s1 + "lng" + s2, Toast.LENGTH_SHORT).show();
                        }
                    });
                    customBaseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<UserDetail>> call, Throwable t) {

                }
            });
        }
        else {
            List<UserDetail> userDetails = databaseHandler.getAllContacts();
            customBaseAdapter = new CustomBaseAdapter(MainActivity.this, userDetails);
            listView.setAdapter(customBaseAdapter);
            customBaseAdapter.notifyDataSetChanged();


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserDetail userDetail = (UserDetail) customBaseAdapter.getItem(position);
                    CustomBaseAdapter customBaseAdapter = (CustomBaseAdapter) parent.getAdapter();
                    customBaseAdapter.getItem(position);
                    String s1 = userDetail.getAddress().getGeo().getLat();
                    String s2 = userDetail.getAddress().getGeo().getLng();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=-" + s1 + "," + s2 + " (" + userDetail.getName() + ")"));
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "lat" + s1 + "lng" + s2, Toast.LENGTH_SHORT).show();
                }
            });
            customBaseAdapter.notifyDataSetChanged();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
