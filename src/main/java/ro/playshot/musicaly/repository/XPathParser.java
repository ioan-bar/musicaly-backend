package ro.playshot.musicaly.repository;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.playshot.musicaly.NamespaceResolver;
import ro.playshot.musicaly.model.Singer;
import ro.playshot.musicaly.model.Song;

public class XPathParser {

    private static Document doc;
    private static XPath xpath;
    private static final String FILE_NAME= "musicCatalog.xml";

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

//    public static void main(String[] args) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
//        DocumentBuilder builder;
//        Document doc = null;
//        try {
//            builder = factory.newDocumentBuilder();
//            doc = builder.parse(new FileInputStream(new File("musicCatalog.xml")));
//
//            // Create XPathFactory object
//            XPathFactory xpathFactory = XPathFactory.newInstance();
//
//            // Create XPath object
//            XPath xpath = xpathFactory.newXPath();
//            xpath.setNamespaceContext(new NamespaceResolver(doc));
//            //Expresii simple
//            System.out.println("--- Expresii simple: ");
//            System.out.println();
//
//
//            getAllSongs(doc, xpath);
//
//
////            // 1
////            List<String> allSingers = getAllSingers(doc, xpath);
////            System.out.println("Lista nume cantareti: " + Arrays.toString(allSingers.toArray()));
//
//////	            // 2
//////	            List<String> allSongs = getAllSongs(doc, xpath);
//////	            System.out.println("Lista titluri melodii: "+ Arrays.toString(allSongs.toArray()));
//////
//////	            // 3
//////	            List<String> allGenres = getAllGenres(doc, xpath);
//////	            System.out.println("Lista genurilor muzicale: "+ Arrays.toString(allGenres.toArray()));
//////
//////	            // 4
//////	            List<String> singerAge = getSingersAge(doc, xpath);
//////	            System.out.println("Lista varstelor cantaretilor: "+ Arrays.toString(singerAge.toArray()));
//////
//////	            // 5
//////	            Set<String> citySongs = getCitySong(doc, xpath);
//////	            System.out.println("Lista oraselor in care au fost inregistrate melodiile: "+ Arrays.toString(citySongs.toArray()));
//////
//////	            // 5
//////	            Set<String> allProducers = getAllProducers(doc, xpath);
//////	            System.out.println("Lista producatorilor: "+ Arrays.toString(allProducers.toArray()));
//////
//////
//////	            System.out.println();
//////
//////	            //Expresii medii
//////	            System.out.println("--- Expresii medii: ");
//////	            System.out.println();
//////
//////	            // 1
//////	            String firstSong = getFirstSongs(doc, xpath);
//////	            System.out.println("Prima melodie din catalog: " + firstSong);
//////
//////	            // 2
//////	            List<String> songsFrom2020 = getSongs2020(doc, xpath);
//////	            System.out.println("Lista melodiilor din anul 2020: "+ Arrays.toString(songsFrom2020.toArray()));
//////
//////	            // 3
//////	            List<String> singersUnder35 = getSingers35(doc, xpath);
//////	            System.out.println("Lista cantarestilor sub 35 de ani: "+ Arrays.toString(singersUnder35.toArray()));
//////
//////	            // 4
//////	            String lastSinger = getLastSinger(doc, xpath);
//////	            System.out.println("Ultimul cantaret adaugat in catalog: " + lastSinger);
//////
//////	            // 5
//////	            List<String> songsBefore2015 = getSongsBefore2015(doc, xpath);
//////	            System.out.println("Lista melodiilor aparute inainte de 2015: "+ Arrays.toString(songsBefore2015.toArray()));
//////
//////	            // 6
//////	            int songsCounter = getNumberOfSongs(doc, xpath);
//////	            System.out.println("Numarul melodiilor din catalog: "+ songsCounter);
//////
////
////	            System.out.println();
////
////	            //Expresii medii
////	            System.out.println("--- Expresii complexe: ");
////	            System.out.println();
////
////	            // 1
////	            List<String> popSongs = getPopSongs(doc, xpath);
////	            System.out.println("Lista de melodii cu genul muzical pop: "+ Arrays.toString(popSongs.toArray()));
////
////	            // 2
////	            List<String> specificSingerSong = getSpecificSingerSong(doc, xpath);
////	            System.out.println("Lista de melodii ale canteretei Miley Cyrus: "+ Arrays.toString(specificSingerSong.toArray()));
////
//
//
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
//        }
//
//    }
public static void main(String[] args) {
        XPathParser.startConfiguration();
    XPathParser.removeSong("Midnight sky");
}
    public static void removeSong(String songName)  {
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

    private static List<String> getSpecificSingerSong(Document doc, XPath xpath) {
        String mileyID = null;
        List<String> list = new ArrayList<>();

        try {
            XPathExpression expr =
                    xpath.compile("/catalog/singers/singer[name='Miley Cyrus']/@id");
            mileyID = (String) expr.evaluate(doc, XPathConstants.STRING);

            XPathExpression expr2 =
                    xpath.compile("/catalog/songs/song[singer[@idref='" + mileyID + "']]/title/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr2.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static List<String> getPopSongs(Document doc, XPath xpath) {
        String popId = null;
        List<String> list = new ArrayList<>();

        try {
            XPathExpression expr =
                    xpath.compile("/catalog/genres[genre='Pop']/genre/@id");
            popId = (String) expr.evaluate(doc, XPathConstants.STRING);

            XPathExpression expr2 =
                    xpath.compile("/catalog/songs/song[genre[@idref='" + popId + "']]/title/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr2.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Set<String> getAllProducers(Document doc, XPath xpath) {
        Set<String> list = new HashSet<String>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song/producer/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Set<String> getCitySong(Document doc, XPath xpath) {
        Set<String> list = new HashSet<String>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song/city/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getSingersAge(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/singers/singer/age/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static int getNumberOfSongs(Document doc, XPath xpath) {
        double counter = 0;

        try {
            XPathExpression expr =
                    xpath.compile("count(//catalog/songs/song/title)");
            counter = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        return (int) counter;
    }

    private static List<String> getSongsBefore2015(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song[year<" + 2015 + "]/title/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getLastSinger(Document doc, XPath xpath) {
        String lastSinger = null;

        try {
            XPathExpression expr =
                    xpath.compile("/catalog/singers/singer[last()]/name/text()");
            lastSinger = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        return lastSinger;
    }

    private static List<String> getSingers35(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression expr =
                    xpath.compile("/catalog/singers/singer[age<" + 35 + "]/name/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getSongs2020(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song[year='2020']/title/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getFirstSongs(Document doc, XPath xpath) {
        String firstBook = null;

        try {
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song[1]/title/text()");
            firstBook = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


        return firstBook;
    }

    private static List<String> getAllGenres(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =                    xpath.compile("/catalog/genres/genre/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getAllSongsAdela(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("/catalog/songs/song/title/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getAllSingers(Document doc, XPath xpath) {
        List<String> result = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                    xpath.compile("//tns:singer/tns:name/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("nodes = " + nodes.item(i).getNodeValue());

                result.add(nodes.item(i).getNodeValue());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> getAllSongsOfGenre(String genre){
        List<String> result = new ArrayList<>();

        try {
            XPathExpression expressionGenreId = xpath.compile("//tns:genres/tns:genre[text()='"+genre+"']/@id");
            System.out.println("genre = " + genre);
            String genreId = (String) expressionGenreId.evaluate(doc, XPathConstants.STRING);
            System.out.println("genreId = " + genreId);
            XPathExpression expressionGenreIdRefSongs = xpath.compile("//tns:songs/tns:song[tns:genre[@idref='" + genreId + "']]/tns:title/text()");
            NodeList nodes = (NodeList) expressionGenreIdRefSongs.evaluate(doc, XPathConstants.NODESET);
            System.out.println("nodes = " + nodes.item(0));

            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(nodes.item(i).getNodeValue());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Song> getAllSongsOfSinger(String singer){
        List<Song> result = new ArrayList<>();

        try {
            XPathExpression expressionSingerId = xpath.compile("//tns:singers/tns:singer[tns:name='"+singer+"']/@id");
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


//            NodeList nodes = (NodeList) expressionSingerIdRefSongs.evaluate(doc, XPathConstants.NODESET);
//            System.out.println("nodes = " + nodes.item(0));
//
//            for (int i = 0; i < nodes.getLength(); i++) {
//                result.add(nodes.item(i).getNodeValue());
//            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<String> getAllGenres(){
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
        songYear.setTextContent(song.getYear()+"");

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
        singerAge.setTextContent(song.getSinger().getAge()+"");

        newSinger.appendChild(singerName);
        newSinger.appendChild(singerAge);
        return newSinger;
    }
}
