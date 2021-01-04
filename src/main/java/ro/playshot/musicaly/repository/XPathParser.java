package ro.playshot.musicaly.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.playshot.musicaly.NamespaceResolver;
import ro.playshot.musicaly.model.Singer;
import ro.playshot.musicaly.model.Song;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class XPathParser {

    private static final String FILE_NAME = "musicCatalog.xml";
    private static Document doc;
    private static XPath xpath;

    public static void startConfiguration() {
        System.out.println("Start configuration...");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new FileInputStream(new File(FILE_NAME)));

            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();

            // Create XPath object
            xpath = xpathFactory.newXPath();
            xpath.setNamespaceContext(new NamespaceResolver(doc));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Configuration done.");
    }

    public static void main(String[] args) {
        XPathParser.startConfiguration();
        XPathParser.removeSong("Midnight sky");
    }

    public static void removeSong(String songName) {
        try {
            XPathExpression expression = xpath.compile("/tns:catalog/tns:songs/tns:song[tns:title='" + songName + "']");

            Node node = (Node) expression.evaluate(doc, XPathConstants.NODE);
            node.getParentNode().removeChild(node);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(FILE_NAME)));

            startConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Song> getAllSongs() {
        List<Song> result = new ArrayList<>();

        try {
            XPathExpression expression = xpath.compile("//tns:songs/tns:song");
            NodeList songsNodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < songsNodes.getLength(); i++) {
                Element songElement = (Element) songsNodes.item(i);

                String songGenreRef = ((Element) songElement.getElementsByTagName("tns:genre").item(0)).getAttribute("idref");
                XPathExpression genreExpression = xpath.compile("//tns:genres/tns:genre[@id='" + songGenreRef + "']");
                NodeList genresNodes = (NodeList) genreExpression.evaluate(doc, XPathConstants.NODESET);
                Element foundedGenre = (Element) genresNodes.item(0);

                String songSingerRef = ((Element) songElement.getElementsByTagName("tns:singer").item(0)).getAttribute("idref");
                XPathExpression singerExpression = xpath.compile("//tns:singers/tns:singer[@id='" + songSingerRef + "']");
                NodeList singersNodes = (NodeList) singerExpression.evaluate(doc, XPathConstants.NODESET);
                Element foundedSingerElement = (Element) singersNodes.item(0);


                String songTitle = songElement.getElementsByTagName("tns:title").item(0).getTextContent();
                String songYear = songElement.getElementsByTagName("tns:year").item(0).getTextContent();
                String songSingerName = foundedSingerElement.getElementsByTagName("tns:name").item(0).getTextContent();
                String songSingerAge = foundedSingerElement.getElementsByTagName("tns:age").item(0).getTextContent();
                String songGenre = foundedGenre.getTextContent();

                Singer singer = new Singer(songSingerName, Integer.parseInt(songSingerAge));
                Song song = new Song(songTitle, singer, Integer.parseInt(songYear), songGenre);
                result.add(song);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> getAllSingers() {
        List<String> result = new ArrayList<>();
        try {
            XPathExpression expression = xpath.compile("//tns:singers/tns:singer/tns:name");
            NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(nodes.item(i).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<String> getAllSongsOfGenre(String genre) {
        List<String> result = new ArrayList<>();

        try {
            XPathExpression expressionGenreId = xpath.compile("//tns:genres/tns:genre[text()='" + genre + "']/@id");
            String genreId = (String) expressionGenreId.evaluate(doc, XPathConstants.STRING);
            XPathExpression expressionGenreIdRefSongs = xpath.compile("//tns:songs/tns:song[tns:genre[@idref='" + genreId + "']]/tns:title/text()");
            NodeList nodes = (NodeList) expressionGenreIdRefSongs.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(nodes.item(i).getNodeValue());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Song> getAllSongsOfSinger(String singer) {
        List<Song> result = new ArrayList<>();

        try {
            XPathExpression expressionSingerId = xpath.compile("//tns:singers/tns:singer[tns:name='" + singer + "']/@id");
            String singerId = (String) expressionSingerId.evaluate(doc, XPathConstants.STRING);
            XPathExpression expressionSingerIdRefSongs = xpath.compile("//tns:songs/tns:song[tns:singer[@idref='" + singerId + "']]");

            NodeList songsNodes = (NodeList) expressionSingerIdRefSongs.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < songsNodes.getLength(); i++) {
                Element songElement = (Element) songsNodes.item(i);

                String songGenreRef = ((Element) songElement.getElementsByTagName("tns:genre").item(0)).getAttribute("idref");
                XPathExpression genreExpression = xpath.compile("//tns:genres/tns:genre[@id='" + songGenreRef + "']");
                NodeList genresNodes = (NodeList) genreExpression.evaluate(doc, XPathConstants.NODESET);
                Element foundedGenre = (Element) genresNodes.item(0);

                String songSingerRef = ((Element) songElement.getElementsByTagName("tns:singer").item(0)).getAttribute("idref");
                XPathExpression singerExpression = xpath.compile("//tns:singers/tns:singer[@id='" + songSingerRef + "']");
                NodeList singersNodes = (NodeList) singerExpression.evaluate(doc, XPathConstants.NODESET);
                Element foundedSingerElement = (Element) singersNodes.item(0);


                String songTitle = songElement.getElementsByTagName("tns:title").item(0).getTextContent();
                String songYear = songElement.getElementsByTagName("tns:year").item(0).getTextContent();
                String songSingerName = foundedSingerElement.getElementsByTagName("tns:name").item(0).getTextContent();
                String songSingerAge = foundedSingerElement.getElementsByTagName("tns:age").item(0).getTextContent();
                String songGenre = foundedGenre.getTextContent();

                Singer singerX = new Singer(songSingerName, Integer.parseInt(songSingerAge));
                Song song = new Song(songTitle, singerX, Integer.parseInt(songYear), songGenre);
                result.add(song);
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<String> getAllGenres() {
        List<String> result = new ArrayList<>();
        try {
            XPathExpression expression = xpath.compile("//tns:genres/tns:genre/text()");
            NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(nodes.item(i).getNodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void addSong(Song song) {

        try {
            Element newSong = createNewSongElement(song);
            Element newSinger = createSingerFromSong(song);
            Element newGenre = createGenreFromSong(song);

            XPathExpression expression = xpath.compile("//tns:songs");
            NodeList songsNodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            songsNodes.item(0).appendChild(newSong);

            expression = xpath.compile("//tns:singers");
            NodeList singersNodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            singersNodes.item(0).appendChild(newSinger);

            expression = xpath.compile("//tns:genres");
            NodeList genresNodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            genresNodes.item(0).appendChild(newGenre);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(FILE_NAME)));

            startConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static Element createNewSongElement(Song song) {
        Element newSong = doc.createElement("tns:song");
        newSong.setAttribute("id", song.getTitle());


        Element songTitle = doc.createElement("tns:title");
        songTitle.setTextContent(song.getTitle());

        Element songSinger = doc.createElement("tns:singer");
        songSinger.setAttribute("idref", song.getSinger().getName());

        Element songYear = doc.createElement("tns:year");
        songYear.setTextContent(song.getYear() + "");

        Element songGenre = doc.createElement("tns:genre");
        songGenre.setAttribute("idref", song.getGenre());

        newSong.appendChild(songTitle);
        newSong.appendChild(songSinger);
        newSong.appendChild(songYear);
        newSong.appendChild(songGenre);
        return newSong;
    }

    private static Element createGenreFromSong(Song song) {
        Element newGenre = doc.createElement("tns:genre");

        newGenre.setAttribute("id", song.getGenre());
        newGenre.setTextContent(song.getGenre());

        return newGenre;
    }

    private static Element createSingerFromSong(Song song) {
        Element newSinger = doc.createElement("tns:singer");
        newSinger.setAttribute("id", song.getSinger().getName());

        Element singerName = doc.createElement("tns:name");
        singerName.setTextContent(song.getSinger().getName());

        Element singerAge = doc.createElement("tns:age");
        singerAge.setTextContent(song.getSinger().getAge() + "");

        newSinger.appendChild(singerName);
        newSinger.appendChild(singerAge);
        return newSinger;
    }
}
