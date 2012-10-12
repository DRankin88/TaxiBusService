package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vogella.Edge;
import vogella.Graph;
import vogella.Vertex;


/**
 * Utility class for parsing XML input files that represent the graphs being used.
 * @author David Rankin
 *
 */
public class Parser {

	File file;

	public Parser(String nameOfFile){

		this.file = new File(nameOfFile);

	}

	/**
	 * parses the xml file and returns the graph object for processing
	 * @return
	 */
	public Graph parseFile() throws Exception{

		List<Vertex> nodes = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("vertex");
		NodeList vList = doc.getElementsByTagName("edge");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				//	      System.out.println("id : " + getTagValue("id", eElement));
				//	      System.out.println("Name : " + getTagValue("name", eElement));
				Vertex location = new Vertex(getTagValue("id", eElement), getTagValue("name", eElement));
				nodes.add(location);
			}
		}

		for (int temp = 0; temp < vList.getLength(); temp++) {

			Node nNode = vList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				//	      System.out.println("id : " + getTagValue("id", eElement));
				//	      System.out.println("Name : " + getTagValue("name", eElement));

				Vertex sourceNode = null;
				Vertex targetNode = null;
				for (int i = 0; i < nodes.size(); i++){

					if (nodes.get(i).getId().equals(getTagValue("source", eElement))){

						sourceNode = nodes.get(i);

					}


					if (nodes.get(i).getId().equals(getTagValue("destination", eElement))){

						targetNode = nodes.get(i);

					}

				}

				Edge location = new Edge(getTagValue("id", eElement), sourceNode, targetNode, Integer.parseInt(getTagValue("weight", eElement)));
				edges.add(location);
			}
		}

		Graph graph = new Graph(nodes, edges);	
		return graph;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

}
