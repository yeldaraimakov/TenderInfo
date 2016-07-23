package kz.example.tenderinfo;

/**
 * Created by Эльдар on 20.06.2016.
 */
public class News {

    String title, content, brief, image;

    public News(String title, String image, String brief, String content) {
        this.title = title;
        this.brief = brief;
        this.image = image;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getBrief() {
        return brief;
    }
}
