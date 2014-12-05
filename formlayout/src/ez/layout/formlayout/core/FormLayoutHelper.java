package ez.layout.formlayout.core;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FormLayoutHelper {

    public static final int LEFT = 1;

    public static final int TOP = 2;

    public static final int RIGHT = 3;

    public static final int BOTTOM = 4;

    public static final int DEFAULT_MARGIN = 10;

    private final int margin;

    public FormLayoutHelper() {
        this(DEFAULT_MARGIN);
    }

    public FormLayoutHelper(int margin) {
        this.margin = margin;
    }

    private LinkedHashMap<Component, FormData> components = new LinkedHashMap<Component, FormData>();
    private Component lastComponent = null;

    public void addComponent(Component component, int vertical, int horizental) {
        if ((vertical != TOP && vertical != BOTTOM)
                || (horizental != LEFT && horizental != RIGHT)) {
            throw new IllegalArgumentException(
                    "The argument vertical must be TOP or BOTTOM, horizental must be LEFT or RIGHT!");
        }

        Object leftData = null;
        Object rightData = null;
        Object topData = null;
        Object bottomData = null;

        if (vertical == TOP) {
            topData = TOP;
        } else {
            bottomData = BOTTOM;
        }

        if (horizental == LEFT) {
            leftData = LEFT;
        } else {
            rightData = RIGHT;
        }

        addComponent(component, topData, bottomData, leftData, rightData);
    }

    public void addComponent(Component component, FormData fd) {
        components.put(component, fd);
        lastComponent = component;
//        System.out.println(fd);
    }

    /**
     * @param component
     *            component to added
     * @param dComponent
     *            related component
     * @param position
     *            relative position
     */
    public void addComponent(Component component, int position, Component dComponent) {
        if (!components.containsKey(dComponent)) {
            throw new IllegalArgumentException(
                    "The given dComponent has never been added before!");
        }

        Object leftData = null;
        Object rightData = null;
        Object topData = null;
        Object bottomData = null;

        switch (position) {
        case LEFT:
            leftData = dComponent;
            break;
        case RIGHT:
            rightData = dComponent;
            break;
        case TOP:
            topData = dComponent;
            break;
        case BOTTOM:
        default:
            bottomData = dComponent;
            break;
        }

        addComponent(component, topData, bottomData, leftData, rightData);
    }

    public void addComponent(Component component, Object topData,  Object bottomData,  Object leftData,  Object rightData) {
        FormData fd = new FormData();

        if (topData != null) {
            if (topData.equals(TOP)) {
                fd.top = new FormAttachment(0, margin);
            } else if ((topData instanceof Component) && components.containsKey(topData)) {
                fd.top = new FormAttachment((Component)topData, margin);
            } else if (topData instanceof FormAttachment) {
                fd.top = (FormAttachment)topData;
            }
        }

        if (bottomData != null) {
            if (bottomData.equals(BOTTOM)) {
                fd.bottom = new FormAttachment(100, -margin);
            } else if ((bottomData instanceof Component) && components.containsKey(bottomData)) {
                fd.bottom = new FormAttachment((Component)bottomData, -margin);
            } else if (bottomData instanceof FormAttachment) {
                fd.bottom = (FormAttachment)bottomData;
            }
        }

        if (leftData != null) {
            if (leftData.equals(LEFT)) {
                fd.left = new FormAttachment(0, margin);
            } else if ((leftData instanceof Component) && components.containsKey(leftData)) {
                fd.left = new FormAttachment((Component)leftData, margin);
            } else if (leftData instanceof FormAttachment) {
                fd.left = (FormAttachment)leftData;
            }
        }

        if (rightData != null) {
            if (rightData.equals(RIGHT)) {
                fd.right = new FormAttachment(100, -margin);
            } else if ((rightData instanceof Component) && components.containsKey(rightData)) {
                fd.right = new FormAttachment((Component)rightData, -margin);
            } else if (rightData instanceof FormAttachment) {
                fd.right = (FormAttachment)rightData;
            }
        }

        if (fd.top == null && fd.bottom == null) {
            Component relatedComponent = null;
            if (fd.left != null && fd.left.component != null) {
                relatedComponent =  fd.left.component;
            } else if (fd.right != null && fd.right.component != null) {
                relatedComponent =  fd.right.component;
            }

            if (relatedComponent != null) {
                FormData relatedFd = components.get(relatedComponent);
                if (relatedFd.top != null) {
                    fd.top = relatedFd.top;
                } else if (relatedFd.bottom != null) {
                    fd.bottom = relatedFd.bottom;
                }
            }
        } else if (fd.left == null && fd.right == null) {
            Component relatedComponent = null;
            if (fd.top != null && fd.top.component != null) {
                relatedComponent =  fd.top.component;
            } else if (fd.bottom != null && fd.bottom.component != null) {
                relatedComponent =  fd.bottom.component;
            }

            if (relatedComponent != null) {
                FormData relatedFd = components.get(relatedComponent);
                if (relatedFd.left != null) {
                    fd.left = relatedFd.left;
                } else if (relatedFd.right != null) {
                    fd.right = relatedFd.right;
                }
            }
        }

        addComponent(component, fd);
    }

    public void addComponent(Component component, int position) {
        if (lastComponent == null) {
            throw new IllegalArgumentException(
                    "No related component, you should add a component to list fist");
        }

        addComponent(component, position, lastComponent);
    }

    public void fillPanel(JPanel panel) {
        for (Entry<Component, FormData> entry : components.entrySet()) {
            panel.add(entry.getKey(), entry.getValue());
        }
    }
}
