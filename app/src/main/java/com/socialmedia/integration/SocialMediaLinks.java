package com.socialmedia.integration;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class SocialMediaLinks extends ActionBarActivity {

    String[] mediaList ={"LinkedIn","Twitter","Google","Facebook","Yahoo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_links);
        ListView listView=(ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mediaList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){

                    case 0: startActivity(new Intent(SocialMediaLinks.this,LinkedInLoginActivity.class));break;
                    case 1: //startActivity(new Intent(SocialMediaLinks.this,TwitterActivity.class));
                        Toast.makeText(getBaseContext(),"Work in Progress!! Please try Later",Toast.LENGTH_SHORT).show(); break;
                    case 2: Toast.makeText(getBaseContext(),"Work in Progress!! Please try Later",Toast.LENGTH_SHORT).show(); break;
                    case 3: Toast.makeText(getBaseContext(),"Work in Progress!! Please try Later",Toast.LENGTH_SHORT).show(); break;
                    case 4: Toast.makeText(getBaseContext(),"Work in Progress!! Please try Later",Toast.LENGTH_SHORT).show(); break;

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_social_media_links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
