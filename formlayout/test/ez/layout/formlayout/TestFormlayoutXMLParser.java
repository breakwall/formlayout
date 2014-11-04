package ez.layout.formlayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ez.layout.formlayout.core.FormLayout;
import ez.layout.formlayout.xml.XMLLayout;
import ez.layout.formlayout.xml.XMLParser;

public class TestFormlayoutXMLParser {
    public static void main(String[] args) {
        JFrame frame = new JFrame("test swing form layout (using XML to create layout)");
        JPanel panel = new JPanel(new FormLayout());
        frame.setContentPane(panel);

        XMLParser xmlParser = new XMLParser("test/ez/layout/formlayout/formlayout.xml");
        XMLLayout xmlPanel = xmlParser.readPanel("panel");
        xmlPanel.fillPanel(panel);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
