package com.zhy.fabridge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhy.fabridge.annotation.FCallbackId;

public class MainActivity extends AppCompatActivity
{

    private TextView mTextView;
    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.id_content_tv);
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.id_list_fragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_menu_item)
        {
            showNewMenuItemDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNewMenuItemDialog()
    {
        NewMenuItemDialog newMenuItemDialog = new NewMenuItemDialog();
        newMenuItemDialog.show(getSupportFragmentManager(), "newItem");
    }

    @FCallbackId(id = NewMenuItemDialog.EVENT_CREATE_NEW_ITEM)
    public void addNewItem(String title)
    {
        listFragment.addTitle(title);
    }


    @FCallbackId(id = ListFragment.EVENT_LIST_ITEM_CLICK)
    public void menuItemClick(View view, int pos, String title)
    {
        mTextView.setText(title);
    }


}
