package kz.example.tenderinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    private static ActionBar bar;
    private static String[] title = {"Тендеры", "Полезно знать", "О нас"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bar = getSupportActionBar();
        setMyTitle(0);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);

        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.custom_tab_title, R.id.tab_layout_icon);
        slidingTabLayout.setViewPager(viewPager);

        setUpUIL();
    }

    private void setUpUIL() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);

    }

    public static void setMyTitle(int x) {
        bar.setTitle(title[x]);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private static int[] icons = {R.drawable.tab1_selector, R.drawable.tab2_selector, R.drawable.tab3_selector};

        public MyAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0: {
                    return new TendersFragment();
                }
                case 1: {
                    return new NewsFragment();
                }
                case 2: {
                    return new InfoFragment();
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public int getDrawable(int position) {
            return icons[position];
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0 && TendersFragment.fm.getBackStackEntryCount() > 0){
            TendersFragment.fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}