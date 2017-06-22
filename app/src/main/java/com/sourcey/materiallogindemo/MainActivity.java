package com.sourcey.materiallogindemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final class AdapterModel {
        private final int layoutId;
        private final String user;

        private AdapterModel(int layoutId, String user) {
            this.layoutId = layoutId;
            this.user = user != null ? user : "";
        }
    }

    private static final class Adapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private final List<AdapterModel> items;

        private Adapter(@NonNull Context context, List<AdapterModel> items) {
            layoutInflater = LayoutInflater.from(context);
            this.items = items != null ? items : Collections.<AdapterModel>emptyList();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            // first you need to inflate the page (the view) and then to return it
            View view = layoutInflater.inflate(items.get(position).layoutId, container, false);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            if (object instanceof View) {
                container.removeView((View) object);
            }
        }

        @Override
        public int getItemPosition(final Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            return items.get(position).user;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);



        final String user = ((Application) this.getApplication()).getUser();

        // create a dummy list of view layout ids to be loaded in our view pager
        List<AdapterModel> adapterModels = new ArrayList<AdapterModel>() {{



            add(new AdapterModel(R.layout.monday, user));
            add(new AdapterModel(R.layout.tuesday, user));
            add(new AdapterModel(R.layout.wednesday, user));
            add(new AdapterModel(R.layout.thursday, user));
            add(new AdapterModel(R.layout.friday, user));
            add(new AdapterModel(R.layout.saturday, user));
            add(new AdapterModel(R.layout.sunday, user));


        }};

        // create a new instance of our adapter
        Adapter adapter = new Adapter(this, adapterModels);

        // get a reference to our view pager and set the adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);



        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }*/
}
