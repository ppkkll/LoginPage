package com.fantasy_travel.loginpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_listview);

            populateListView();

        }

        private void populateListView(){
            String[] Test = {"One","Two","Three","Four","Five"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview,Test);

            //ListView list = (ListView) findViewById(R.id.listView);
            //list.setAdapter(adapter);
        }
}
