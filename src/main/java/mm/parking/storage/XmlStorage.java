package mm.parking.storage;

import mm.parking.ParkingLocation;
import mm.parking.ParkingPrice;
import mm.parking.ParkingTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlStorage extends FileStorage {

    public XmlStorage(String dirPath) {
        super(dirPath);
    }

    @Override
    public void storePrices(List<ParkingPrice> prices) throws IOException {
        try {
            Document document = newDocument();
            Element root = document.createElement("parkingprices");
            document.appendChild(root);
            prices.forEach(price -> {
                Element priceNode = document.createElement("price");

                Element zone = document.createElement("zone");
                zone.appendChild(document.createTextNode(price.getZone()));
                priceNode.appendChild(zone);

                Element phone = document.createElement("phone");
                phone.appendChild(document.createTextNode(price.getPhoneNumber()));
                priceNode.appendChild(phone);

                Element hourly = document.createElement("hourlyprice");
                hourly.appendChild(document.createTextNode(String.valueOf(price.getHourlyPrice())));
                priceNode.appendChild(hourly);

                Element daily = document.createElement("dailyprice");
                daily.appendChild(document.createTextNode(String.valueOf(price.getDailyPrice())));
                priceNode.appendChild(daily);

                Element maxHours = document.createElement("maxhours");
                maxHours.appendChild(document.createTextNode(String.valueOf(price.getMaxHours())));
                priceNode.appendChild(maxHours);

                root.appendChild(priceNode);
            });
            write(document, "prices.xml");
        } catch (ParserConfigurationException | TransformerException e ) {
            throw new IOException("XML configuration error: " + e.getMessage());
        }
    }

    @Override
    public void storeWorkHours(List<ParkingTime> workTimeList) throws IOException {
        try {
            Document document = newDocument();
            Element root = document.createElement("workhours");
            document.appendChild(root);
            workTimeList.forEach(workTime -> {
                Element workHourNode = document.createElement("time");

                Element zone = document.createElement("zone");
                zone.appendChild(document.createTextNode(workTime.getZone()));
                workHourNode.appendChild(zone);

                Element workday = document.createElement("workday");
                workday.appendChild(document.createTextNode(workTime.getWorkDayHours()));
                workHourNode.appendChild(workday);

                Element saturday = document.createElement("saturday");
                saturday.appendChild(document.createTextNode(workTime.getSaturdayHours()));
                workHourNode.appendChild(saturday);

                Element sunday = document.createElement("sunday");
                sunday.appendChild(document.createTextNode(workTime.getSundayHolidayHours()));
                workHourNode.appendChild(sunday);

                Element holiday = document.createElement("holiday");
                holiday.appendChild(document.createTextNode(workTime.getSundayHolidayHours()));
                workHourNode.appendChild(holiday);

                root.appendChild(workHourNode);
            });
            write(document, "work_hours.xml");
        } catch (ParserConfigurationException | TransformerException e ) {
            throw new IOException("XML configuration error: " + e.getMessage());
        }
    }

    @Override
    public void storeLocations(List<ParkingLocation> locations) throws IOException {
        try {
            Document document = newDocument();
            Element root = document.createElement("parkinglocations");
            document.appendChild(root);
            locations.forEach(location -> {
                Element locationNode = document.createElement("location");

                Element zone = document.createElement("zone");
                zone.appendChild(document.createTextNode(location.getZone()));
                locationNode.appendChild(zone);

                Element city = document.createElement("city");
                city.appendChild(document.createTextNode(location.getCity()));
                locationNode.appendChild(city);

                Element address = document.createElement("address");
                address.appendChild(document.createTextNode(location.getAddress()));
                locationNode.appendChild(address);

                root.appendChild(locationNode);
            });
            write(document, "locations.xml");
        } catch (ParserConfigurationException | TransformerException e ) {
            throw new IOException("XML configuration error: " + e.getMessage());
        }
    }

    private Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    private Transformer newTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }

    private void write(Document document, String filename) throws TransformerException {
        Transformer transformer = newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult sr = new StreamResult(new File(getDirPath() + filename));
        transformer.transform(source, sr);
    }
}
