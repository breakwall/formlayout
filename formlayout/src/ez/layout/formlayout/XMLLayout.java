package ez.layout.formlayout;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLLayout {
    private String id;
    private FormLayoutHelper helper;
    private Map<String, Component> componentsIdMap = new HashMap<>();

    public void parseElement(Element element) {
        this.id = element.getAttribute("id");
        String margin = element.getAttribute("margin");
        // check if margin is integer
        int marginInt = Integer.parseInt(margin);
        this.helper = new FormLayoutHelper(marginInt);

        NodeList components = element.getElementsByTagName("component");
        for (int i = 0; i < components.getLength(); i++) {
            Element componentElement = (Element) components.item(i);

            String componentId = componentElement.getAttribute("id");
            String componentType = componentElement.getAttribute("type");
            String layoutData = componentElement.getAttribute("layoutdata");
            String componentText = componentElement.getAttribute("text");
            if (componentText == null || componentText.isEmpty()) {
                componentText = componentId;
            }

            Component component = null;
            switch (componentType) {
            case "JButton":
                component = new JButton(componentText);
                break;
            default:
                break;
            }

            Object leftData = null;
            Object rightData = null;
            Object topData = null;
            Object bottomData = null;


            String[] layoutDataSplit = layoutData.split(":");
            for (int j = 0; j < layoutDataSplit.length; j++) {
                String formAttachmentStr = layoutDataSplit[j];
                String[] splits = formAttachmentStr.split(" ");

                switch (splits[0]) {
                case "top":
                    if (splits.length == 1) {
                        topData = FormLayoutHelper.TOP;
                    } else {
                        topData = componentsIdMap.get(splits[1]);
                    }
                    break;
                case "bottom":
                    if (splits.length == 1) {
                        bottomData = FormLayoutHelper.BOTTOM;
                    } else {
                        bottomData = componentsIdMap.get(splits[1]);
                    }
                    break;
                case "left":
                    if (splits.length == 1) {
                        leftData = FormLayoutHelper.LEFT;
                    } else {
                        leftData = componentsIdMap.get(splits[1]);
                    }
                    break;
                case "right":
                    if (splits.length == 1) {
                        rightData = FormLayoutHelper.RIGHT;
                    } else {
                        rightData = componentsIdMap.get(splits[1]);
                    }
                    break;
                default:
                }
            }

            this.helper.addComponent(component, topData, bottomData, leftData, rightData);
            this.componentsIdMap.put(componentId, component);
        }
    }

    public void fillPanel(JPanel panel) {
        helper.fillPanel(panel);
    }

    public String getId() {
        return id;
    }
}
