package ez.layout.formlayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TestFormLayoutHelper {
	public static void main(String[] args) {
		JFrame frame = new JFrame("test FormLayoutHelper");
		JPanel panel = new JPanel(new FormLayout());
		frame.setContentPane(panel);
		
		FormLayoutHelper helper = new FormLayoutHelper();
		
		JButton button1 = new JButton("top left 1");
		helper.addComponent(button1, FormLayoutHelper.TOP, FormLayoutHelper.LEFT);
		
		JButton button2 = new JButton("top left below");
		helper.addComponent(button2, FormLayoutHelper.BOTTOM);
		
		JButton button3 = new JButton("top left below right");
		helper.addComponent(button3, FormLayoutHelper.RIGHT);
		
		JButton button4 = new JButton("top right");
		helper.addComponent(button4, FormLayoutHelper.TOP, FormLayoutHelper.RIGHT);
		
		helper.addSubToParent(panel);
		
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
