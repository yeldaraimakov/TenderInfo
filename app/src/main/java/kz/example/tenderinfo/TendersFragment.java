package kz.example.tenderinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TendersFragment extends Fragment {

    static FragmentManager fm;
    static boolean pressedSearch;
    static String label1, label2, label3, label4, label5, label6, label7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tenders, null);

        pressedSearch = false;

        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.framelayout, new TendersList());
        ft.commit();

        return view;
    }

    public static class Search extends Fragment {

        LinearLayout search;
        Spinner sp_sources, sp_ways, sp_regions;
        EditText et_key, et_from, et_to, et_number;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.search, null);

            search = (LinearLayout) view.findViewById(R.id.search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTenders();
                }
            });

            sp_sources = (Spinner) view.findViewById(R.id.sp_sources);
            sp_ways = (Spinner) view.findViewById(R.id.sp_ways);
            sp_regions = (Spinner) view.findViewById(R.id.sp_regions);
            et_key = (EditText) view.findViewById(R.id.et_key);
            et_from = (EditText) view.findViewById(R.id.et_from);
            et_to = (EditText) view.findViewById(R.id.et_to);
            et_number = (EditText) view.findViewById(R.id.et_number);

            sp_sources.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.sources)));
            sp_ways.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.ways)));
            sp_regions.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.regions)));

            return view;
        }

        private void showTenders() {
            label1 = et_key.getText().toString();
            label2 = sp_sources.getSelectedItem().toString();
            label3 = sp_ways.getSelectedItem().toString();
            label4 = sp_regions.getSelectedItem().toString();
            label5 = et_from.getText().toString();
            label6 = et_to.getText().toString();
            label7 = et_number.getText().toString();

            if (label2.equals("Все")) label2 = "";
            if (label3.equals("Все")) label3 = "";
            if (label4.equals("Все")) label4 = "";

            pressedSearch = true;
            if (label1.equals("") && label2.equals("") && label3.equals("") && label4.equals("") && label5.equals("") && label6.equals("") && label7.equals("")) pressedSearch = false;

            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.framelayout, new TendersList());
            ft.commit();
        }
    }

    public static class TendersList extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

        int preLast, page = 1;
        ListView listViewTenders;
        TenderAdapter tenderAdt;
        ArrayList<Tender> tenders = new ArrayList<Tender>();
        ProgressDialog progressDialog;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.tenderslist, null);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.framelayout, new Search());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            listViewTenders = (ListView) view.findViewById(R.id.listViewTenders);
            tenderAdt = new TenderAdapter(getActivity());
            listViewTenders.setAdapter(tenderAdt);
            listViewTenders.setOnItemClickListener(this);
            listViewTenders.setOnScrollListener(this);

            if (pressedSearch) {
                searchTenders();
            } else {
                loadTenders();
            }

            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), OneTender.class);
            intent.putExtra("name", tenders.get(position).getName());
            intent.putExtra("category", tenders.get(position).getCategory());
            intent.putExtra("status", tenders.get(position).getStatus());
            intent.putExtra("endDate", tenders.get(position).getEndDate());
            intent.putExtra("address", tenders.get(position).getAddress());
            intent.putExtra("amount", tenders.get(position).getAmount());
            intent.putExtra("customer", tenders.get(position).getCustomer());
            intent.putExtra("link", tenders.get(position).getLink());
            intent.putExtra("lots", tenders.get(position).getLots());
            intent.putExtra("source", tenders.get(position).getSource());
            intent.putExtra("id", tenders.get(position).getId());
            startActivity(intent);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        public class TenderAdapter extends BaseAdapter {

            private LayoutInflater tenderInf;

            public TenderAdapter(Context c) {
                tenderInf = LayoutInflater.from(c);
            }

            @Override
            public int getCount() {
                return tenders.size();
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
                    convertView = tenderInf.inflate(R.layout.item_tender, null);
                    holder = new ViewHolder();
                    holder.tenderName = (TextView) convertView.findViewById(R.id.tenderName);
                    holder.tenderCategory = (TextView) convertView.findViewById(R.id.tenderCategory);
                    holder.tenderStatus = (TextView) convertView.findViewById(R.id.tenderStatus);
                    holder.total = (TextView) convertView.findViewById(R.id.amount);
                    holder.endDate = (TextView) convertView.findViewById(R.id.endDate);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                String name = tenders.get(position).getName();
                holder.tenderName.setText(name.substring(0, Math.min(75, name.length())) + "...");
                holder.tenderCategory.setText(tenders.get(position).getCategory());
                holder.tenderStatus.setText(tenders.get(position).getStatus());
                holder.total.setText(tenders.get(position).getAmount());
                holder.endDate.setText(tenders.get(position).getEndDate());

                return convertView;
            }
        }

        static class ViewHolder {
            TextView tenderName, tenderCategory, tenderStatus, total, endDate;
        }

        private void loadTenders() {
            if (tenders.size() == 0) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(Constants.URLS.TENDERS_URL + page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        for (int i = 0; i < response.getJSONArray("tenders").length(); ++i) {
                            parseJSON(response.getJSONArray("tenders").getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tenderAdt.notifyDataSetChanged();
                    page++;
                }
            });
        }

        private void searchTenders() {
            if (tenders.size() == 0) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
            RequestParams params = new RequestParams();
            params.put("keywords", label1);
            params.put("source", label2);
            params.put("zakupka", label3);
            params.put("mesto", label4);
            params.put("min", label5);
            params.put("max", label6);
            params.put("nomer", label7);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constants.URLS.SEARCH_URL + page, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    try {
                        for (int i = 0; i < response.getJSONArray("ads2").length(); ++i) {
                            Log.d("response", i + " " + response.getJSONArray("ads2").getJSONObject(i));
                            parseJSON(response.getJSONArray("ads2").getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tenderAdt.notifyDataSetChanged();
                    page++;
                }
            });
        }

        private void parseJSON(JSONObject tend) {
            String s1="", s2="", s3="", s4="", s5="", s6="", s7="", s8="", s9="", s10="", s11="";
            try {
                s1 = tend.getString("name");
                s2 = tend.getString("category");
                s3 = tend.getString("method");
                s4 = tend.getString("amount");
                s5 = tend.getString("end_date");
                s6 = tend.getString("source");
                s7 = tend.getString("customer");
                s8 = tend.getString("location");
                s9 = tend.getString("link");
                s10 = tend.getString("lots");
                s11 = tend.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tenders.add(new Tender(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11));
        }

        @Override
        public void onScroll(AbsListView lw, final int firstVisibleItem,
                             final int visibleItemCount, final int totalItemCount) {

            switch(lw.getId()) {
                case R.id.listViewTenders:
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if(lastItem == totalItemCount) {
                        if(preLast!=lastItem){ //to avoid multiple calls for last item
                            preLast = lastItem;
                            if (pressedSearch) {
                                searchTenders();
                            } else {
                                loadTenders();
                            }
                        }
                    }
            }
        }
    }
}