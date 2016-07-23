package kz.example.tenderinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Эльдар on 20.06.2016.
 */
public class OneNews extends AppCompatActivity {

    TextView newsTitle, newsContent;
    ImageView newsImage;
    String s1, s2, s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        s1 = getIntent().getStringExtra("title");
        s2 = getIntent().getStringExtra("content");
        s3 = getIntent().getStringExtra("image");

        newsTitle = (TextView) findViewById(R.id.newsTitle);
        newsContent = (TextView) findViewById(R.id.newsContent);
        newsImage = (ImageView) findViewById(R.id.newsImage);

        int pos = s2.indexOf("// <![CDATA");
        Log.d("eldar", pos + "");
        if (pos == -1) pos = s2.length();
        s2 = s2.substring(0, pos);

        setTitle(s1);
        newsTitle.setText(s1);
        newsContent.setText(Html.fromHtml(s2));

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnLoading(null).build();
        imageLoader.displayImage(Constants.URLS.SITE_URL + s3, newsImage, options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
