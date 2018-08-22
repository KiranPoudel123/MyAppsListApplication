package myapp.training.newitventure.com.myinstalledappsapplication;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private ProgressDialog progressDialog;
    private PackageManager packageManager;
    private ArrayList<ApplicationInfo> applicationInfos;
    private ApplicationAdapter adapter;
    private RecyclerView recyclerView;
    private  SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageManager = getPackageManager();
        applicationInfos = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

         progressDialog = ProgressDialog.show(this, "Loading Installed Apps", "Loading application info...");
         progressDialog.setCanceledOnTouchOutside(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),  4,
                GridLayoutManager.VERTICAL, false));


        adapter = new ApplicationAdapter(this, applicationInfos);
        recyclerView.setAdapter(adapter);
        recyclerView.requestFocus();

        getSearchView();

    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
        }
        return  true;
    }
    */
/*
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/
    private void getSearchView() {

        searchView = (SearchView) findViewById(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(onQueryTextListener()); // text changed listener
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //filter adapter and update ListView
                adapter.getFilter().filter(s);
                return false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //invoke asynctask
        new GetAllAppsTask(this, applicationInfos, packageManager).execute();
    }

    public void callBackDataFromAsynctask(List<ApplicationInfo> list) {
        applicationInfos.clear();

        for (int i = 0; i < list.size(); i++) {
            applicationInfos.add(list.get(i));
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
