/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ez.layout.formlayout.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

/**
 *
 * @author Justis.Ren
 */
public final class FormLayout implements LayoutManager2 {

    public static final int DEFAULT_PADDING = 0;
    public static final int DEFAULT_MARGIN = 0;
    public int margin = DEFAULT_MARGIN;
    public int padding = DEFAULT_PADDING;

    private final Map<Component, FormData> componentConstraints = new HashMap<>();

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints == null) {
            throw new IllegalArgumentException("constraints can't be null");
        } else if (!(constraints instanceof FormData)) {
            throw new IllegalArgumentException("constraints must be a " + FormData.class.getName() + " instance");
        } else {
            synchronized (comp.getTreeLock()) {
                FormData formData = (FormData) constraints;
                if (formData.left == null && formData.top == null && formData.right == null && formData.bottom == null) {
                    throw new IllegalArgumentException("At least one side of attachment data should be assigned.");
                }
                componentConstraints.put(comp, (FormData) constraints);
            }
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        // do nothing
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @deprecated
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        //do nothing
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int w = Math.max(parent.getWidth() - margin * 2, 0);
            int h = Math.max(parent.getHeight() - margin * 2, 0);
            Component[] components = parent.getComponents();
            for (Component comp : components) {
                FormData formData = componentConstraints.get(comp);
                if (formData == null) {
                    continue;
                }

                FormAttachment left = formData.getLeftAttachment(this, comp, padding);
                FormAttachment right = formData.getRightAttachment(this, comp, padding);
                FormAttachment top = formData.getTopAttachment(this, comp, padding);
                FormAttachment bottom = formData.getBottomAttachment(this, comp, padding);

                int x = left.solveX(w);     // x coordinate of left
                int y = top.solveX(h);     // y coordinate of top
                int x2 = right.solveX(w);          // x coordinate of right
                int y2 = bottom.solveX(h);   // y coordinate of bottom
                int width = x2 - x;
                int height = y2 - y;
//                System.out.println("=========layout container");
//                System.out.println("comp:"+ ((JButton)comp).getText());
//                System.out.println("x:" + x + ", y:" + y + " ,width:" + width + ", height:"+ height);
                comp.setBounds(x + margin, y + margin, width, height);
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for(Component c: parent.getComponents()) {
            FormData fd = componentConstraints.get(c);
            Dimension d = fd.getMinimumSize(this, c, padding);
            if (width < d.width) {
                width = d.width;
            }
            if (height < d.height) {
                height = d.height;
            }
        }
        return new Dimension(width, height);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            componentConstraints.remove(comp);
        }
    }

    public FormData getFormData(Component component) {
        return componentConstraints.get(component);
    }
}
