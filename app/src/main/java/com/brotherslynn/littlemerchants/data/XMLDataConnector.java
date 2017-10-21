package com.brotherslynn.littlemerchants.data;

import android.content.Context;

import com.brotherslynn.littlemerchants.objects.Location;
import com.brotherslynn.littlemerchants.objects.Player;
import com.brotherslynn.littlemerchants.objects.Trip;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by danielmlynn on 10/14/17.
 */

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
        if (!playerContents.contains("[Error") && playerContents != "")
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

    public Location getLocation(UUID id)
    {
        throw new UnsupportedOperationException("Method Not Implemented on XML Data connector.");
    }
}
