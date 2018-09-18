package bot.externalservice.apium;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class DataScraper {
    private File input = new File("E:\\java\\bot\\src\\main\\resources\\scraping\\ztmWebsite\\station_list.html");
    public DataScraper() throws Exception {
        Document doc = Jsoup.parse(this.input, "UTF-8", "http://example.com/");
        System.out.println(doc);
    }


}
