package mm.parking.client;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class ParkingClient {
    private static final String URL_PARKING_PRICES = "http://www.zagrebparking.hr/default.aspx?id=55";
    private static final String URL_PARKING_WORK_HOURS = "http://www.zagrebparking.hr/default.aspx?id=1461";
    // append zona_id value (1-10) in request
    private static final String URL_PARKING_LOCATIONS = "http://www.zagrebparking.hr/default.aspx?action=filterUlica&zona_id=";
    private static final String DEFAULT_DELIMITER = ";";

    private final Map<Integer, String> zoneMap = Map.of(
            1, "Zona 1",
            2, "Zona 1.1",
            3, "Zona 1.2",
            4, "Zona 2.1",
            5, "Zona 2.2",
            6, "Zona 2.3",
            7, "Zona 3",
            8, "Zona 3 Sesvete",
            9, "Zona 4.1",
            10, "Zona 4.2"
    );

    public String fetchParkingPrices() throws IOException {
        return fetch(URL_PARKING_PRICES);
    }

    public String fetchParkingWorkHours() throws IOException {
        return fetch(URL_PARKING_WORK_HOURS);
    }

    public String fetchParkingLocations() throws IOException {
        StringBuilder sb = new StringBuilder();

        // download all entries and build output string
        for (Map.Entry<Integer, String> entry : zoneMap.entrySet()) {
            String zone = entry.getValue();
            String fullUrl = URL_PARKING_LOCATIONS + entry.getKey();
            Connection connection = Jsoup.connect(fullUrl);
            Document document = connection.get();

            /*
            * The data is contained in a table for each document.
            * The table has multiple rows and each row has only one column which contains
            * the required information in the following format:
            *
            * <table>
            *     ...
            *     ...
            *     <tr><td> Data </td></tr>
            *     <tr><td> Data </td></tr>
            *     <tr><td> Data </td></tr>
            *     <tr><td> Data </td></tr>
            *     <tr><td> Data </td></tr>
            *     ...
            *     ...
            * </table>
            * */

            // get the data table
            Elements tables = document.getElementsByTag("table");
            Element targetTable = tables.get(0);

            Elements rows = targetTable.getElementsByTag("tr");
            rows.forEach(row -> {
                Elements columns = row.getElementsByTag("td");
                columns.forEach(column -> {
                    sb.append(zone)
                            .append(DEFAULT_DELIMITER)
                            .append(column.text());
                });
                sb.append(System.lineSeparator());
            });
        }

        String data = sb.toString();

        return data;
    }

    private String fetch(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();

        /*
         *  The data we need is contained inside a table which is part of pageContent div.
         *  The format is like following:
         *
         *  <div class="pageContent">
         *      ...
         *      ...
         *      <table>
         *          <tr>
         *              <td>Data ...</td>
         *              <td>Data ...</td>
         *          </tr>
         *          <tr>
         *              <td>Data ...</td>
         *              <td>Data ...</td>
         *          </tr>
         *          ...
         *          ...
         *      </table>
         *      ...
         *      <table></table>
         *  </div>
         *
         *  The pricing/work_hours information we need is contained inside the first table,
         *  so the code below first retrieves the wrapper div and then all the tables
         *  that it contains. The target table should be first in the list. After
         *  the table is obtained the data we are interested in is stored in one
         *  string variable and returned to the caller.
         * */

        Elements pageContentDiv = document.getElementsByClass("pageContent");
        Elements tables = pageContentDiv.get(0).getElementsByTag("table");
        Element targetTable = tables.get(0);

        Elements rows = targetTable.getElementsByTag("tr");
        StringBuilder sb = new StringBuilder();

        // extract data from each row/column with ; -> semicolon as default delimiter
        rows.forEach(row -> {
            Elements columns = row.getElementsByTag("td");
            columns.forEach(column -> {
                sb.append(column.text()).append(DEFAULT_DELIMITER);
            });
            sb.append(System.lineSeparator());
        });

        String data = sb.toString();

        return data;
    }
}
