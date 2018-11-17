package mm.parking.client;

import org.jsoup.Jsoup;

import java.io.IOException;

public class ParkingClient {
    private static final String URL_PARKING_PRICES = "http://www.zagrebparking.hr/default.aspx?id=55";
    private static final String URL_PARKING_WORK_HOURS = "http://www.zagrebparking.hr/default.aspx?id=1461";
    // append zona_id value (1-10) in request
    private static final String URL_PARKING_LOCATIONS = "http://www.zagrebparking.hr/default.aspx?action=filterUlica&zona_id=";
    private static final String DEFAULT_DELIMITER = ";";

    public String fetchParkingPrices() throws IOException {
        var connection = Jsoup.connect(URL_PARKING_PRICES);
        var document = connection.get();

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
        *  The pricing information we need is contained inside the first table,
        *  so the code below first retrieves the wrapper div and then all the tables
        *  that it contains. The target table should be first in the list. After
        *  the table is obtained the data we are interested in is stored in one
        *  string variable and returned to the caller.
        * */

        var pageContentDiv = document.getElementsByClass("pageContent");
        var tables = pageContentDiv.get(0).getElementsByTag("table");
        var pricingTable = tables.get(0);

        var rows = pricingTable.getElementsByTag("tr");
        var sb = new StringBuilder();

        // extract data from each row/column with ; -> semicolon as default delimiter
        rows.forEach(row -> {
            var columns = row.getElementsByTag("td");
            columns.forEach(column -> {
                sb.append(column.text());
                sb.append(DEFAULT_DELIMITER);
            });
            sb.append(System.lineSeparator());
        });

        var payload = sb.toString();

        return payload;
    }

    public void fetchParkingWorkHours() {
        // HTML
        // Work hours URL ->  http://www.zagrebparking.hr/default.aspx?id=1461
    }

    public void fetchParkingLocations() {
        // Plain text
        // Locations URL -> http://www.zagrebparking.hr/default.aspx?action=filterUlica&zona_id=10   1 <= id <= 10
    }
}
