package ez.layout.formlayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ez.layout.formlayout.core.FormAttachment;
import ez.layout.formlayout.core.FormData;
import ez.layout.formlayout.core.FormLayout;

public class TestFormLayout {
    public static void main(String[] args) {
        JFrame frame = new JFrame("test Java Swing form layout (SWT formlayout like)");
        FormLayout formLayout = new FormLayout();
        formLayout.margin = 10;
//        formLayout.padding = 10;
        JPanel panel = new JPanel(formLayout);
        frame.setContentPane(panel);

        JButton button1 = new JButton("top left 1");
        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 10);
        fd.top = new FormAttachment(0, 10);
        panel.add(button1, fd);

        JButton button2 = new JButton("top left below");
        fd = new FormData();
        fd.left = new FormAttachment(0, 10);
        fd.top = new FormAttachment(button1, 10);
        panel.add(button2, fd);

        JButton button3 = new JButton("top left below right");
        fd = new FormData();
        fd.left = new FormAttachment(button2, 10);
        fd.top = new FormAttachment(button1, 10);
        panel.add(button3, fd);

        JButton button4 = new JButton("top right");
        fd = new FormData();
        fd.right = new FormAttachment(100, -10);
        fd.top = new FormAttachment(0, 10);
        panel.add(button4, fd);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
