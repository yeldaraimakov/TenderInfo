package kz.example.tenderinfo;

/**
 * Created by Эльдар on 21.06.2016.
 */
public class Tender {

    String name, category, source, endDate, status, customer, address, link, lots, amount, id;

    public Tender(String name, String category, String status, String amount, String endDate, String source, String customer, String address, String link, String lots, String id) {
        this.name = name;
        this.category = category;
        this.source = source;
        this.amount = amount;
        this.endDate = endDate;
        this.status = status;
        this.customer = customer;
        this.address = address;
        this.link = link;
        this.lots = lots;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getCustomer() {
        return customer;
    }

    public String getLink() {
        return link;
    }

    public String getLots() {
        return lots;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

}
