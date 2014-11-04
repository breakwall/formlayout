package ez.layout.formlayout.xml;

import java.awt.Component;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ez.layout.formlayout.core.FormLayoutHelper;

public class XMLLayout {
    private String id;
    private FormLayoutHelper helper;
    private Map<String, Component> componentsIdMap = new HashMap<>();
    private Map<String, XMLLayoutData> componentsLayoutMap = new LinkedHashMap<>();

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
            String componentText = componentElement.getAttribute("text");
            if (componentText == null || componentText.isEmpty()) {
                componentText = componentId;
            }

            Component component = null;
            switch (componentType) {
            case "JLabel":
                component = new JLabel(componentText);
                break;
            case "JTextField":
                component = new JTextField(10);
                break;
            case "JTextArea":
                component = new JTextArea(1, 1);
                break;
            case "JButton":
                component = new JButton(componentText);
                break;
            case "custom":
            default:
                break;
            }

            XMLLayoutData xmlLayoutData = getXMLayoutData(componentElement);

            this.componentsIdMap.put(componentId, component);
            this.componentsLayoutMap.put(componentId, xmlLayoutData);
        }
    }

    private XMLLayoutData getXMLayoutData(Element componentElement) {
        XMLLayoutData xmlLayoutData = new XMLLayoutData();
        NodeList topElements = componentElement.getElementsByTagName("top");
        if (topElements.getLength() != 0) {
            Element topElement = (Element)topElements.item(0);
            String component = topElement.getAttribute("component");
            xmlLayoutData.topData = component;
        }

        NodeList bottomElements = componentElement.getElementsByTagName("bottom");
        if (bottomElements.getLength() != 0) {
            Element bottomElement = (Element)bottomElements.item(0);
            String component = bottomElement.getAttribute("component");
            xmlLayoutData.bottomData = component;
        }

        NodeList leftElements = componentElement.getElementsByTagName("left");
        if (leftElements.getLength() != 0) {
            Element leftElement = (Element)leftElements.item(0);
            String component = leftElement.getAttribute("component");
            xmlLayoutData.leftData = component;
        }

        NodeList rightElements = componentElement.getElementsByTagName("right");
        if (rightElements.getLength() != 0) {
            Element rightElement = (Element)rightElements.item(0);
            String component = rightElement.getAttribute("component");
            xmlLayoutData.rightData = component;
        }

        return xmlLayoutData;
    }

    private boolean isAllComponentsCreated() {
        for (Map.Entry<String, Component> comp : componentsIdMap.entrySet()) {
            if (comp.getValue() == null) {
                System.out
                        .println("The component '"
                                + comp.getKey()
                                + "' defined in XML file is not created, you should create it manually.");
                return false;
            }
        }

        return true;
    }

    private void createHelper() {
        for (Map.Entry<String, XMLLayoutData> entry : componentsLayoutMap.entrySet()) {
            String componentId = entry.getKey();
            XMLLayoutData xmlLayoutData = entry.getValue();

            Object topData = null;
            Object bottomData = null;
            Object leftData = null;
            Object rightData = null;

            if (xmlLayoutData.topData == "") {
                topData = FormLayoutHelper.TOP;
            } else if (xmlLayoutData.topData != null) {
                topData = componentsIdMap.get(xmlLayoutData.topData);
            }

            if (xmlLayoutData.bottomData == "") {
                bottomData = FormLayoutHelper.BOTTOM;
            } else if (xmlLayoutData.bottomData != null) {
                bottomData = componentsIdMap.get(xmlLayoutData.bottomData);
            }

            if (xmlLayoutData.leftData == "") {
                leftData = FormLayoutHelper.LEFT;
            } else if (xmlLayoutData.leftData != null) {
                leftData = componentsIdMap.get(xmlLayoutData.leftData);
            }

            if (xmlLayoutData.rightData == "") {
                rightData = FormLayoutHelper.RIGHT;
            } else if (xmlLayoutData.rightData != null) {
                rightData = componentsIdMap.get(xmlLayoutData.rightData);
            }

            helper.addComponent(componentsIdMap.get(componentId), topData, bottomData, leftData, rightData);
        }
    }

    public void fillPanel(JPanel panel) {
        if (!isAllComponentsCreated()) {
            createComponents(this.componentsIdMap);
        }

        createHelper();

        helper.fillPanel(panel);
    }

    public String getId() {
        return id;
    }

    protected void createComponents(Map<String, Component> componentsIdMap) {
        // user should create custom components manually
    }

    class XMLLayoutData {
        String topData;
        String bottomData;
        String leftData;
        String rightData;
    }
}
