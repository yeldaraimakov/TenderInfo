package kz.example.tenderinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Эльдар on 20.06.2016.
 */
public class OneTender extends AppCompatActivity {

    TextView name, category, source, endDate, status, customer, address, link, lots, amount, docs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_tender);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Тендер");

        name = (TextView) findViewById(R.id.name);
        category = (TextView) findViewById(R.id.category);
        source = (TextView) findViewById(R.id.source);
        endDate = (TextView) findViewById(R.id.endDate);
        status = (TextView) findViewById(R.id.status);
        customer = (TextView) findViewById(R.id.customer);
        address = (TextView) findViewById(R.id.address);
        link = (TextView) findViewById(R.id.link);
        lots = (TextView) findViewById(R.id.lots);
        amount = (TextView) findViewById(R.id.amount);
        docs = (TextView) findViewById(R.id.docs);

        name.setText(getIntent().getStringExtra("name"));
        category.setText(getIntent().getStringExtra("category"));
        source.setText(getIntent().getStringExtra("source"));
        endDate.setText(getIntent().getStringExtra("endDate"));
        status.setText(getIntent().getStringExtra("status"));
        customer.setText(getIntent().getStringExtra("customer"));
        address.setText(getIntent().getStringExtra("address"));
        link.setText(getIntent().getStringExtra("link"));
        lots.setText(getIntent().getStringExtra("lots"));
        amount.setText(getIntent().getStringExtra("amount"));
        docs.setText(Constants.URLS.DOC_URL + getIntent().getStringExtra("id"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
