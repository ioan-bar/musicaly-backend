package ro.playshot.musicaly.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import ro.playshot.musicaly.model.Song;

public class DOMParser3 {

	public List<String> genuriCautate = new ArrayList<String>();
	public List<String> cantaretiCautati = new ArrayList<String>();
	public List<String> melodiiCautate = new ArrayList<String>();
	private static String FILE_NAME= "musicCatalog.xml";

	public static void main(String[] args) {
		DOMParser3 parser = new DOMParser3();
		parser.run();
	}

	public List<String> getSongs(){
		List<Song> result = new ArrayList<>();
		try {
			File inputFile = new File(FILE_NAME);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList songsList = doc.getElementsByTagName("tns:songs");

			NodeList songs = songsList.item(0).getChildNodes();

			for (int j = 0; j < songs.getLength(); j++) {
				Node songNode = songs.item(j);
				if (songNode.getNodeType() == Node.ELEMENT_NODE) {

					Element song = (Element) songNode;
					String songTitle = song.getElementsByTagName("tns:title").item(0).getTextContent();
					String songYear = song.getElementsByTagName("tns:year").item(0).getTextContent();
					Element singer = (Element) song.getElementsByTagName("tns:singer").item(0);
					String singerRefId = singer.getAttribute("idref");

//					result.add();

//					Element singer = (Element) song.getElementsByTagName("tns:singer").item(0);
//					Element genre = (Element) song.getElementsByTagName("tns:genre").item(0);
//					if(cantaretiCautati.contains(singer.getAttribute("idref")) && genuriCautate.contains(genre.getAttribute("idref"))) {
//						melodiiCautate.add(song.getElementsByTagName("tns:title").item(0).getTextContent());
//					}

				}
			}


		} catch (Exception e) {
			System.out.println("e = " + e);
		}

//		return result;
		return null;
	}

	private void run() {

		try {
			File inputFile = new File("musicCatalog.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			NodeList songsList = doc.getElementsByTagName("tns:songs");
			NodeList singersList = doc.getElementsByTagName("tns:singers");
			NodeList genresList = doc.getElementsByTagName("tns:genres");

			addGenersWanted(genresList);
			addSingersWanted(singersList);
			addSongsWanted(songsList);
			
			System.out.println("Identificarea tuturor melodiilor care se incadreaza in genul pop sau latin music si au cantareti cu varsta intre 28-45: ");
			System.out.println(melodiiCautate.toString().substring(1, melodiiCautate.toString().length() - 1));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSongsWanted(NodeList songsList) {
		NodeList songs = songsList.item(0).getChildNodes();

		for (int j = 0; j < songs.getLength(); j++) {
			Node songNode = songs.item(j);
			if (songNode.getNodeType() == Node.ELEMENT_NODE) {
		
				Element song = (Element) songNode;
			//	System.out.println("Melodie curenta: " + song.getTextContent());
				
				Element singer = (Element) song.getElementsByTagName("tns:singer").item(0);
				Element genre = (Element) song.getElementsByTagName("tns:genre").item(0);
				if(cantaretiCautati.contains(singer.getAttribute("idref")) && genuriCautate.contains(genre.getAttribute("idref"))) {
					melodiiCautate.add(song.getElementsByTagName("tns:title").item(0).getTextContent());
				}
				
			}
		}
		//System.out.println(melodiiCautate);

	}

	private void addGenersWanted(NodeList genresList) {
		NodeList genres = genresList.item(0).getChildNodes();

		for (int g = 0; g < genres.getLength(); g++) {
			Node genNode = genres.item(g);
			if (genNode.getNodeType() == Node.ELEMENT_NODE) {
				Element genre = (Element) genNode;
				// System.out.println("Gen curent: " + genre.getTextContent());
				if (genre.getTextContent().equals("Pop") || genre.getTextContent().equals("Latin music")) {
					genuriCautate.add(genre.getAttribute("id"));
				}
			}
		}

		//System.out.println(genuriCautate);
	}

	private void addSingersWanted(NodeList singersList) {
		NodeList singers = singersList.item(0).getChildNodes();

		for (int i = 0; i < singers.getLength(); i++) {
			Node singerNode = singers.item(i);
			if (singerNode.getNodeType() == Node.ELEMENT_NODE) {
				Element singer = (Element) singerNode;
				// System.out.println("Cantaret curent: " + singer.getTextContent());
				String age = singer.getElementsByTagName("tns:age").item(0).getTextContent();
				int ageAsNumber = Integer.parseInt(age);
				if (ageAsNumber >=28 && ageAsNumber <=45) {
					cantaretiCautati.add(singer.getAttribute("id"));
				}
			}
		}
		
	//	System.out.println(cantaretiCautati);
	}

}
