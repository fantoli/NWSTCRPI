/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwstcrpi;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import nwstcrpi.clsPreferences;

/**
 *
 * @author amartinez
 */
public class readXMLFiles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        File xmlFile = new File("D:\\Profiles\\Documentos\\amartinez\\Documents\\NetBeansProjects\\NWSTCRPI\\src\\nwstcrpi\\preferencesData.xml");
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        NodeList list = document.getElementsByTagName("root");

        for (int i=0; i<list.getLength(); i++){

            Node node = list.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element element = (Element) node;

                System.out.print("Frecuencia: " + element.getElementsByTagName("frequency").item(0).getTextContent());
            }
        }
    }
    
}
