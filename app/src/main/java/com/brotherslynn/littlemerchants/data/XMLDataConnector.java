package com.brotherslynn.littlemerchants.data;

import android.content.Context;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLDataConnector implements IDataConnector {

    private Context context;
    public XMLDataConnector(Context thisContext)
    {
        this.context = thisContext;
    }

    //region file Read and Write


    private boolean writeFile(Document doc, String fileName)
    {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // send DOM to file
            File file = new File(context.getFilesDir() + fileName);
            Boolean result = file.createNewFile();
            tr.transform(new DOMSource(doc),
                    new StreamResult(new FileOutputStream(file)));

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return true;
    }

    private String readFileContents(String fileName)
    {
        try {

            String filePath = context.getFilesDir() + fileName;

            File checkFile = new File(filePath);
            boolean exists = checkFile.exists();

            if (!exists)
            {
                if (!checkFile.createNewFile())
                    return "[ERROR Reading File] File Missing. Could Not Create File.";
            }

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
            } finally {
                reader.close();
            }
        } catch (Exception exc)
        {
            return "[ERROR Reading File] " + exc.getMessage();
        }
    }

    //endregion

    //region XML Conversion

    private Document xmlDocumentFromString(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            return builder.parse( new InputSource( new StringReader( xmlString ) ) );
        } catch (Exception e) {
            throw new InvalidParameterException("String is not properly formatted XML.");
        }
    }

    private Document LocationsToXML(List<Location> locations)
    {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();

            Element rootEle = doc.createElement("locations");

            for (Location loc : locations) {

                Element locElem = doc.createElement("location");

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(loc.getName()));
                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(loc.getId().toString()));
                Element x = doc.createElement("x");
                x.appendChild(doc.createTextNode(Integer.toString(loc.getLocationX())));
                Element y = doc.createElement("y");
                y.appendChild(doc.createTextNode(Integer.toString(loc.getLocationY())));
                Element connections = doc.createElement("connections");
                for (UUID cid : loc.getConnections())
                {
                    Element connection = doc.createElement("connection");
                    connection.appendChild(doc.createTextNode(cid.toString()));
                    connections.appendChild(connection);
                }

                locElem.appendChild(id);
                locElem.appendChild(name);
                locElem.appendChild(x);
                locElem.appendChild(y);
                locElem.appendChild(connections);
                rootEle.appendChild(locElem);
            }


            doc.appendChild(rootEle);
        }
        catch (Exception ex)
        {
            throw new InvalidParameterException("Error creating XML Document");
        }

        return doc;
    }

    private List<Location> XMLToLocations(Document doc)
    {
        ArrayList<Location> locations = new ArrayList<Location>();
        NodeList lNodes = doc.getElementsByTagName("location");
        for (int i=0; i< lNodes.getLength(); i++)
        {
            NodeList cNodes = lNodes.item(i).getChildNodes();
            Location loc = new Location();
            int x = -1;
            int y = -1;
            for (int n = 0; n < cNodes.getLength(); n++) {
                switch (cNodes.item(n).getNodeName())
                {
                    case "id":
                        loc.setId(UUID.fromString(cNodes.item(n).getTextContent()));
                        break;
                    case "name":
                        loc.setName(cNodes.item(n).getTextContent());
                        break;
                    case "x":
                        x = Integer.parseInt(cNodes.item(n).getTextContent());
                        break;
                    case "y":
                        y = Integer.parseInt(cNodes.item(n).getTextContent());
                        break;
                    case "connections":
                        if (cNodes.item(n).hasChildNodes()) {
                            NodeList connectionNodes = cNodes.item(n).getChildNodes();
                            ArrayList<UUID> connections = new ArrayList<UUID>();
                            for (int k = 0; k < connectionNodes.getLength(); k++) {
                                if (connectionNodes.item(k).getNodeName().equals("connection"))
                                {
                                    connections.add(UUID.fromString(connectionNodes.item(k).getTextContent()));
                                }
                            }
                            loc.setConnections(connections);
                        }
                        else
                        {
                            loc.setConnections(new ArrayList<UUID>());
                        }
                        break;
                }
            }
            if (x > -1 && y > -1)
            {
                loc.setLocationCoordinates(x,y);
            }
            locations.add(loc);
        }
        return locations;
    }

    private Document PlayerToXML(Player player)
    {
        Document doc = null;
        Element name, mtype,csteps,cloc, ctrip = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();

            // create the root element
            Element rootEle = doc.createElement("player");

            name = doc.createElement("merchantname");
            name.appendChild(doc.createTextNode(player.getMerchantName()));
            mtype = doc.createElement("merchanttype");
            mtype.appendChild(doc.createTextNode(Integer.toString(player.getMerchantType())));
            csteps = doc.createElement("currentsteps");
            csteps.appendChild(doc.createTextNode(Integer.toString(player.getCurrentSteps())));

            rootEle.appendChild(name);
            rootEle.appendChild(mtype);
            rootEle.appendChild(csteps);

            // current Location
            Location currentLoc = player.getCurrentLocation();
            cloc = doc.createElement("currentlocation");
            if (currentLoc != null) {

                cloc = doc.createElement("currentlocation");

                Element locName = doc.createElement("name");
                locName.appendChild(doc.createTextNode(currentLoc.getName()));
                Element locX = doc.createElement("locx");
                locX.appendChild(doc.createTextNode(Integer.toString(currentLoc.getLocationX())));
                Element locY = doc.createElement("locy");
                locY.appendChild(doc.createTextNode(Integer.toString(currentLoc.getLocationY())));

                cloc.appendChild(locName);
                cloc.appendChild(locX);
                cloc.appendChild(locY);

            }

            rootEle.appendChild(cloc);


            // current trip
            Trip currentTrip = player.getTrip();
            ctrip = doc.createElement("currenttrip");
            if (currentTrip != null)
            {
                Element sstep = doc.createElement("startingstepcount");
                sstep.appendChild(doc.createTextNode(Integer.toString(currentTrip.getStartingStepCount())));
                Element cstep = doc.createElement("currentstepcount");
                cstep.appendChild(doc.createTextNode(Integer.toString(currentTrip.getCurrentStepCount())));
                Element dist = doc.createElement("distance");
                dist.appendChild(doc.createTextNode(Integer.toString(currentTrip.getDistance())));

                Element destination = doc.createElement("destination");
                Location destLoc = currentTrip.getDestination();
                Element dName = doc.createElement("destinationname");
                dName.appendChild(doc.createTextNode(destLoc.getName()));
                Element dlocX = doc.createElement("destinationlocx");
                dlocX.appendChild(doc.createTextNode(Integer.toString(destLoc.getLocationX())));
                Element dlocY = doc.createElement("destinationlocy");
                dlocY.appendChild(doc.createTextNode(Integer.toString(destLoc.getLocationY())));
                destination.appendChild(dName);
                destination.appendChild(dlocX);
                destination.appendChild(dlocY);

                ctrip.appendChild(sstep);
                ctrip.appendChild(cstep);
                ctrip.appendChild(dist);
                ctrip.appendChild(destination);
            }
            rootEle.appendChild(ctrip);

            doc.appendChild(rootEle);
        }
        catch (Exception ex)
        {
            throw new InvalidParameterException("Error creating XML Document");
        }

        return doc;
    }

    private Player XMLToPlayer(Document doc)
    {
        Player player = new Player();
        Element pNode = doc.getDocumentElement();

        NodeList attr = pNode.getChildNodes();

        for (int n = 0; n < attr.getLength(); n++) {
            switch (attr.item(n).getNodeName())
            {
                case "merchantname":
                    player.setMerchantName(attr.item(n).getTextContent());
                    break;
                case "merchanttype":
                    player.setMerchantType(Integer.parseInt(attr.item(n).getTextContent()));
                    break;
                case "currentsteps":
                    player.setCurrentSteps(Integer.parseInt(attr.item(n).getTextContent()));
                    break;
                case "currentlocation":

                    if (attr.item(n).hasChildNodes())
                    {
                        Location currentLoc = new Location();
                        NodeList cNodes = attr.item(n).getChildNodes();
                        int x = -1;
                        int y = -1;
                        for (int i = 0; i < cNodes.getLength(); i++) {
                            switch (cNodes.item(i).getNodeName()) {
                                case "name":
                                    currentLoc.setName(cNodes.item(i).getTextContent());
                                    break;
                                case "locx":
                                    x = Integer.parseInt(cNodes.item(i).getTextContent());
                                    break;
                                case "locy":
                                    y = Integer.parseInt(cNodes.item(i).getTextContent());
                                    break;
                            }
                        }
                        if (x>-1 && y>-1) {
                            currentLoc.setLocationCoordinates(x, y);
                        }
                        player.setCurrentLocation(currentLoc);
                    }
                    else
                    {
                        player.setCurrentLocation(null);
                    }
                    break;
                case "currenttrip":
                    if (attr.item(n).hasChildNodes())
                    {
                        Trip currentTrip = new Trip();
                        NodeList cNodes = attr.item(n).getChildNodes();
                        for (int i = 0; i < cNodes.getLength(); i++) {
                            switch (cNodes.item(i).getNodeName()) {
                                case "startingstepcount":
                                    currentTrip.setStartingStepCount(Integer.parseInt(cNodes.item(i).getTextContent()));
                                    break;
                                case "currentstepcount":
                                    currentTrip.setCurrentStepCount(Integer.parseInt(cNodes.item(i).getTextContent()));
                                    break;
                                case "distance":
                                    currentTrip.setDistance(Integer.parseInt(cNodes.item(i).getTextContent()));
                                    break;
                                case "destination":
                                    if (cNodes.item(i).hasChildNodes()) {
                                        Location destination = new Location();
                                        NodeList dNodes = cNodes.item(i).getChildNodes();
                                        int dx = -1;
                                        int dy = -1;
                                        for (int k = 0; k < dNodes.getLength(); k++) {
                                            switch (dNodes.item(k).getNodeName())
                                            {
                                                case "destinationname":
                                                    destination.setName(dNodes.item(k).getTextContent());
                                                    break;
                                                case "destinationlocx":
                                                    dx = Integer.parseInt(dNodes.item(k).getTextContent());
                                                    break;
                                                case "destinationlocy":
                                                    dy = Integer.parseInt(dNodes.item(k).getTextContent());
                                                    break;
                                            }
                                        }
                                        if (dx > -1 && dy > -1) {
                                            destination.setLocationCoordinates(dx, dy);
                                        }
                                        currentTrip.setDestination(destination);
                                    }
                                    else {
                                        currentTrip.setDestination(null);
                                    }
                                    break;
                            }
                        }
                        player.setTrip(currentTrip);
                    }
                    else
                    {
                        player.setTrip(null);
                    }
                    break;
            }
        }

        return player;
    }

    //endregion

    public Player getPlayer()
    {
        String playerContents = readFileContents("localMerchant.xml");
        if (!playerContents.contains("[Error") && !playerContents.equals(""))
            return XMLToPlayer(xmlDocumentFromString(playerContents));
        else return new Player();
        //return new Player();
    }

    public boolean savePlayer(Player player)
    {
        Document savePlayer = null;
        try {
            savePlayer = PlayerToXML(player);
        } catch (Exception e)
        {
            String error = e.getLocalizedMessage();
        }
        boolean didWrite = writeFile(savePlayer, "localMerchant.xml");
        return didWrite;
    }

    public boolean syncLocations(List<Location> locations)
    {
        Document saveLocations = null;
        try {
            saveLocations = LocationsToXML(locations);
        } catch (Exception e)
        {
            String error = e.getLocalizedMessage();
        }
        boolean didWrite = writeFile(saveLocations, "locations.xml");
        return didWrite;
    }

    public Location getLocation(UUID id)
    {
        for (Location loc : getAllLocations())
        {
            if (loc.getId().equals(id))
            {
                return loc;
            }
        }
        return null;
    }

    public List<Location> getAllLocations()
    {
        String locationContents = readFileContents("locations.xml");
        if (!locationContents.contains("[Error") && !locationContents.equals(""))
            return XMLToLocations(xmlDocumentFromString(locationContents));
        else return new ArrayList<Location>();
    }
}
