package com.firebase.postsactivity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PostsAdapter eventsAdapter;
    private RecyclerView eventReyclerView;
    private String TAG = "MainActivity.java";

    ExpensesDAO expensesDAO;

    LinearLayout nopostsavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventReyclerView    =   (RecyclerView)findViewById(R.id.recyclerview);
        expensesDAO = new ExpensesDAO(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView createpost= (ImageView) toolbar.findViewById(R.id.createpost);
        createpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreatePost.class);
                startActivity(intent);
            }
        });

        nopostsavailable   = (LinearLayout) findViewById(R.id.nopostsavailable);

        List<PostsModel> data = expensesDAO.getData();

        if(data!= null && data.size()>=0)
        {
            nopostsavailable.setVisibility(View.GONE);
            eventReyclerView.setVisibility(View.VISIBLE);
            Log.i(TAG,"data is"+data);
            try {
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
                eventReyclerView.setLayoutManager(layoutManager1);
                PostsAdapter eventsAdapter = new PostsAdapter(this,data);
                eventReyclerView.setAdapter(eventsAdapter);

            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            eventReyclerView.setVisibility(View.GONE);
            nopostsavailable.setVisibility(View.VISIBLE);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



}
