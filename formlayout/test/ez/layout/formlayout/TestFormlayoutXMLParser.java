package ez.layout.formlayout;

import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TestFormlayoutXMLParser {
    public static void main(String[] args) {
        JFrame frame = new JFrame("test swing form layout");
        JPanel panel = new JPanel(new FormLayout());
        frame.setContentPane(panel);

        Map<String, XMLLayout> map = FormLayoutHelper.readFromFile("test/ez/layout/formlayout/formlayout.xml");
        XMLLayout xmlPanel = map.get("panel");

        xmlPanel.fillPanel(panel);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
