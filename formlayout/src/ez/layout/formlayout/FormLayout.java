/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ez.layout.formlayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Justis.Ren
 */
public final class FormLayout implements LayoutManager2 {

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
            final int w = parent.getWidth();
            final int h = parent.getHeight();
            final Component[] components = parent.getComponents();
            for (final Component comp : components) {
                final FormData formData = componentConstraints.get(comp);
                if (formData == null) {
                    continue;
                }

                final FormAttachment left = formData.getLeftAttachment(this, comp);
                final FormAttachment right = formData.getRightAttachment(this, comp);
                final FormAttachment top = formData.getTopAttachment(this, comp);
                final FormAttachment bottom = formData.getBottomAttachment(this, comp);

                final int x = left.solveX(w);     // x coordinate of left
                final int y = top.solveX(h);     // y coordinate of top

                final int width;
                final int height;
                final int x2 = right.solveX(w);          // x coordinate of right
                final int y2 = bottom.solveX(h);   // y coordinate of bottom
                width = x2 - x;
                height = y2 - y;
//                System.out.println(comp.getClass().getName() + ":(" + x + "," + y + "),(" + x2 + "," + y2 + ")");
//                System.out.println(width + ":"+ height);
//                System.out.println(w + ":" + h);
                comp.setBounds(x, y, width, height);
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
            Dimension d = fd.getMinimumSize(this, c);
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
