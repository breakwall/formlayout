package ez.layout.formlayout.xml;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLParser {
    private Document document;

    private NodeList panels;

    public XMLParser(String xmlFileName) {
        read(xmlFileName);
    }

    private void read(String xmlFileName) {
        File file = new File(xmlFileName);
        try (FileInputStream fis = new FileInputStream(file);) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(fis);

            Element element = document.getDocumentElement();
            panels = element.getElementsByTagName("panel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XMLLayout readPanel(String panelId) {
        return readPanel(panelId, new XMLLayout());
    }

    public XMLLayout readPanel(String panelId, XMLLayout panel) {
        for (int i = 0; i < panels.getLength(); i++) {
            Element ele = (Element) panels.item(i);
            if (panelId.equals(ele.getAttribute("id"))) {
                panel.parseElement(ele);
                return panel;
            }
        }

        return null;
    }
}
