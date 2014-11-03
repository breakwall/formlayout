/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ez.layout.formlayout;

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

    public int width;
    public int height;

    public FormAttachment getLeftAttachment(FormLayout formLayout, Component component) {
        if (cachedLeft != null) return cachedLeft;
        if (left == null) {
            if (right == null) {
                return cachedLeft = new FormAttachment(0, 0);
            } else {
                return cachedLeft = getRightAttachment (formLayout, component).minus (getWidth (component));
            }
        }
        Component leftComponent = left.component;
        if (leftComponent != null && leftComponent.getParent() != component.getParent()) {
            leftComponent = null;
        }

        if (leftComponent == null) {
            return cachedLeft = left;
        }

        FormData leftData = (FormData) formLayout.getFormData(leftComponent);
        FormAttachment rightAttachment = leftData.getRightAttachment (formLayout, leftComponent);
        return cachedLeft = rightAttachment.plus (left.offset);
    }

    public FormAttachment getRightAttachment(FormLayout formLayout, Component component) {
        if (cachedRight != null) return cachedRight;
        if (right == null) {
            if (left == null) {
                return cachedRight = new FormAttachment(0, getWidth(component));
            } else {
                return cachedRight = getLeftAttachment (formLayout, component).plus (getWidth (component));
            }
        }

        Component rightComponent = right.component;
    if (rightComponent != null && rightComponent.getParent () != component.getParent ()) {
            rightComponent = null;
    }
    if (rightComponent == null) {
            return cachedRight = right;
        }

        FormData rightData = (FormData) formLayout.getFormData (rightComponent);
        FormAttachment leftAttachment = rightData.getLeftAttachment (formLayout, rightComponent);
        return cachedRight = leftAttachment.plus (right.offset);
    }

    public FormAttachment getTopAttachment(FormLayout formLayout, Component component) {
        if (cachedTop != null) return cachedTop;
        if (top == null) {
            if (bottom == null) {
                return cachedTop = new FormAttachment(0, 0);
            } else {
                return cachedTop = getBottomAttachment(formLayout, component).minus(getHeight(component));
            }
        }
    Component topComponent = top.component;
    if (topComponent != null && topComponent.getParent () != component.getParent ()) {
            topComponent = null;
    }
    if (topComponent == null) {
            return cachedTop = top;
        }
    FormData topData = (FormData) formLayout.getFormData(topComponent);
    FormAttachment bottomAttachment = topData.getBottomAttachment (formLayout, topComponent);
    return cachedTop = bottomAttachment.plus (top.offset);
    }

    public FormAttachment getBottomAttachment(FormLayout formLayout, Component component) {
        if (cachedBottom != null) return cachedBottom;
    if (bottom == null) {
        if (top == null) {
                    return cachedBottom = new FormAttachment (0, getHeight (component));
                } else {
                    return cachedBottom = getTopAttachment (formLayout, component).plus (getHeight (component));
                }
    }
    Component bottomComponent = bottom.component;
    if (bottomComponent != null && bottomComponent.getParent () != component.getParent ()) {
            bottomComponent = null;
    }
    if (bottomComponent == null) {
            return cachedBottom = bottom;
        }
    FormData bottomData = (FormData) formLayout.getFormData(bottomComponent);
    FormAttachment topAttachment = bottomData.getTopAttachment (formLayout, bottomComponent);
        return cachedBottom = topAttachment.plus (bottom.offset);
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
    public Dimension getMinimumSize(FormLayout formLayout, Component component) {
        FormAttachment leftF = getLeftAttachment(formLayout, component);
        FormAttachment rightF = getRightAttachment(formLayout, component);
        FormAttachment topF = getTopAttachment(formLayout, component);
        FormAttachment bottomF = getBottomAttachment(formLayout, component);

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
