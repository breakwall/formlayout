/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ez.layout.formlayout.core;

import java.awt.Component;
import java.awt.Dimension;

/**
 *
 * @author Justis.Ren
 */
public class FormData {

    public FormAttachment left;
    public FormAttachment right;
    public FormAttachment top;
    public FormAttachment bottom;

    public FormAttachment cachedLeft, cachedRight, cachedTop, cachedBottom;

    public FormAttachment getLeftAttachment(FormLayout formLayout, Component component, int spacing) {
        if (cachedLeft != null) return cachedLeft;
        if (left == null) {
            if (right == null) {
                return cachedLeft = new FormAttachment(0, 0);
            }

            return cachedLeft = getRightAttachment(formLayout, component, spacing).minus (getWidth (component));
        }

        Component leftComponent = left.component;
        if (leftComponent != null && leftComponent.getParent() != component.getParent()) {
            leftComponent = null;
        }

        if (leftComponent == null) {
            return cachedLeft = left;
        }

        FormData leftData = formLayout.getFormData(leftComponent);
        FormAttachment rightAttachment = leftData.getRightAttachment(formLayout, leftComponent, spacing);
        return cachedLeft = rightAttachment.plus (left.offset + spacing);
    }

    public FormAttachment getRightAttachment(FormLayout formLayout, Component component, int spacing) {
        if (cachedRight != null) return cachedRight;
        if (right == null) {
            if (left == null) {
                return cachedRight = new FormAttachment(0, getWidth(component));
            }

            return cachedRight = getLeftAttachment(formLayout, component, spacing).plus(getWidth (component));
        }

        Component rightComponent = right.component;
        if (rightComponent != null && rightComponent.getParent () != component.getParent ()) {
                rightComponent = null;
        }
        if (rightComponent == null) {
            return cachedRight = right;
        }

        FormData rightData = formLayout.getFormData (rightComponent);
        FormAttachment leftAttachment = rightData.getLeftAttachment(formLayout, rightComponent, spacing);
        return cachedRight = leftAttachment.plus (right.offset - spacing);
    }

    public FormAttachment getTopAttachment(FormLayout formLayout, Component component, int spacing) {
        if (cachedTop != null) return cachedTop;
        if (top == null) {
            if (bottom == null) {
                return cachedTop = new FormAttachment(0, 0);
            }

            return cachedTop = getBottomAttachment(formLayout, component, spacing).minus(getHeight(component));
        }
        Component topComponent = top.component;
        if (topComponent != null && topComponent.getParent () != component.getParent ()) {
                topComponent = null;
        }
        if (topComponent == null) {
                return cachedTop = top;
            }
        FormData topData = formLayout.getFormData(topComponent);
        FormAttachment bottomAttachment = topData.getBottomAttachment(formLayout, topComponent, spacing);
        return cachedTop = bottomAttachment.plus (top.offset + spacing);
    }

    public FormAttachment getBottomAttachment(FormLayout formLayout, Component component, int spacing) {
        if (cachedBottom != null) {
            return cachedBottom;
        }

        if (bottom == null) {
            if (top == null) {
                return cachedBottom = new FormAttachment(0,getHeight(component));
            }

            return cachedBottom = getTopAttachment(formLayout, component, spacing).plus(getHeight(component));
        }

        Component bottomComponent = bottom.component;
        if (bottomComponent != null
                && bottomComponent.getParent() != component.getParent()) {
            bottomComponent = null;
        }

        if (bottomComponent == null) {
            return cachedBottom = bottom;
        }

        FormData bottomData = formLayout.getFormData(bottomComponent);
        FormAttachment topAttachment = bottomData.getTopAttachment(formLayout, bottomComponent, spacing);
        return cachedBottom = topAttachment.plus(bottom.offset - spacing);
    }

    int getWidth (Component component) {
        return component.getPreferredSize().width;
    }

    int getHeight (Component component) {
        return component.getPreferredSize().height;
    }

    public void flushCache() {
        cachedLeft = cachedRight = cachedTop = cachedBottom = null;
    }

    public Dimension getMinimumSize(FormLayout formLayout, Component component, int spacing) {
        FormAttachment leftF = getLeftAttachment(formLayout, component, spacing);
        FormAttachment rightF = getRightAttachment(formLayout, component, spacing);
        FormAttachment topF = getTopAttachment(formLayout, component, spacing);
        FormAttachment bottomF = getBottomAttachment(formLayout, component, spacing);

        int miniWidth;
        int yy = getWidth(component);
        // xx => minimum layout width,yy component width
        // cd + d - (axx + b) = yy
        if (rightF.numerator == leftF.numerator) {
           // c == a, xx can be any value theoretically,
            int denominator = FormAttachment.denominator;
            if (leftF.numerator == 0) {
                miniWidth = rightF.offset * denominator / (denominator - leftF.numerator);
            } else if (leftF.numerator == denominator) {
                miniWidth = -leftF.offset * denominator / leftF.numerator;
            } else {
                miniWidth = Math.max(-leftF.offset * denominator / leftF.numerator
                    , rightF.offset * denominator / (denominator - leftF.numerator));
            }
        } else {
            miniWidth = (yy - rightF.offset + leftF.numerator) * FormAttachment.denominator
                    / (rightF.numerator - leftF.numerator);
        }

        int miniHeight;
        yy = getHeight(component);
        if (bottomF.numerator == topF.numerator) {
            // c == a, xx can be any value theoretically
            int denominator = FormAttachment.denominator;
            if (topF.numerator == 0) {
                miniHeight = bottomF.offset * denominator / (denominator - topF.numerator);
            } else if (topF.numerator == denominator) {
                miniHeight = -topF.offset * denominator / topF.numerator;
            } else {
                miniHeight = Math.max(-topF.offset * denominator / topF.numerator, bottomF.offset * denominator / (denominator - topF.numerator));
            }
        } else {
            miniHeight = (yy - bottomF.offset + topF.numerator)
                    / (bottomF.numerator - topF.numerator);
        }
        return new Dimension(miniWidth, miniHeight);
    }

    @Override
    public String toString() {
        return "FormData [left=" + left + ", right=" + right + ", top=" + top
                + ", bottom=" + bottom + "]";
    }


}
