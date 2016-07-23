package kz.example.tenderinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    int preLast, page = 1;
    ListView listViewNews;
    NewsAdapter newsAdt;
    ArrayList<News> news = new ArrayList<News>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, null);

        listViewNews = (ListView) view.findViewById(R.id.listViewNews);
        newsAdt = new NewsAdapter(getActivity());
        listViewNews.setAdapter(newsAdt);
        listViewNews.setOnItemClickListener(this);
        listViewNews.setOnScrollListener(this);

        loadNews();

        return view;
    }

    private void loadNews() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URLS.NEWS_URL + page + "/ru", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    for (int i = 0; i < response.getJSONArray("posts").length(); ++i) {
                        Log.d("response", i + " " + response.getJSONArray("posts").getJSONObject(i));
                        parseJSON(response.getJSONArray("posts").getJSONObject(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                newsAdt.notifyDataSetChanged();
                page++;
            }
        });
    }

    private void parseJSON(JSONObject post) {
        String s1="", s2="", s3="", s4="";
        try {
            s1 = post.getString("title");
            s2 = post.getString("media1");
            s3 = post.getString("brief");
            s4 = post.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        news.add(new News(s1, s2, s3, s4));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), OneNews.class);
        intent.putExtra("title", news.get(position).getTitle());
        intent.putExtra("content", news.get(position).getContent());
        intent.putExtra("image", news.get(position).getImage());
        startActivity(intent);
    }

    public class NewsAdapter extends BaseAdapter {

        private LayoutInflater newsInf;

        public NewsAdapter(Context c) {
            newsInf = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = newsInf.inflate(R.layout.item_news, null);
                holder = new ViewHolder();
                holder.newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
                holder.newsBrief = (TextView) convertView.findViewById(R.id.newsBrief);
                holder.newsImage = (ImageView) convertView.findViewById(R.id.newsImage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.newsTitle.setText(news.get(position).getTitle());
            holder.newsBrief.setText(news.get(position).getBrief());

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(null)
                    .showImageOnFail(null)
                    .showImageOnLoading(null).build();
            imageLoader.displayImage(Constants.URLS.SITE_URL + news.get(position).getImage(), holder.newsImage, options);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView newsTitle, newsBrief;
        ImageView newsImage;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {

        switch(lw.getId()) {
            case R.id.listViewNews:
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(preLast!=lastItem){ //to avoid multiple calls for last item
                        Log.d("eldar", "last");
                        preLast = lastItem;
                        loadNews();
                    }
                }
        }
    }
}