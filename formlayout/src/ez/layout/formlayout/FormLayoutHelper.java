package ez.layout.formlayout;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

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
		if ((vertical != TOP && vertical != BOTTOM) || (horizental != LEFT && horizental != RIGHT)) {
			throw new IllegalArgumentException(
					"The argument vertical must be TOP or BOTTOM, horizental must be LEFT or RIGHT!");
		}
		
		FormData fd = new FormData();
		if (vertical == TOP) {
			fd.top = new FormAttachment(0, margin);	
		} else {
			fd.bottom = new FormAttachment(100, -margin);
		}
		
		if (horizental == LEFT) {
			fd.left = new FormAttachment(0, margin);
		} else {
			fd.right = new FormAttachment(100, -margin);
		}

		addComponent(component, fd);
	}

	public void addComponent(Component component, FormData fd) {
		components.put(component, fd);
		lastComponent = component;
	}

	/**
	 * @param component component to added
	 * @param dComponent related component
	 * @param position relative position
	 */
	public void addComponent(Component component, Component dComponent,
			int position) {
		if (!components.containsKey(dComponent)) {
			throw new IllegalArgumentException(
					"The given dComponent has never been added before!");
		}

		FormData dComponentFd = components.get(dComponent);
		FormData fd = new FormData();
		
		FormAttachment fa1 = new FormAttachment(dComponent, margin);
		switch (position) {
		case LEFT:
			fd.right = fa1;
			break;
		case RIGHT:
			fd.left = fa1;
			break;
		case TOP:
			fd.bottom = fa1;
			break;
		case BOTTOM:
		default:
			fd.top = fa1;
			break;
		}
		
		if (fd.top == null && fd.bottom == null) {
			if (dComponentFd.top != null) {
				fd.top = dComponentFd.top;
			} else {
				fd.bottom = dComponentFd.bottom;
			}
		} else if (fd.left == null && fd.right == null) {
			if (dComponentFd.left != null) {
				fd.left = dComponentFd.left;
			} else {
				fd.right = dComponentFd.right;
			}
		}
		
		addComponent(component, fd);
	}
	
	public void addComponent(Component component, int position) {
		if (lastComponent == null) {
			throw new IllegalArgumentException("No related component, you should add a component to list fist");
		}
		
		addComponent(component, lastComponent, position);
	}

	public void addSubToParent(JPanel panel) {
		for (Entry<Component, FormData> entry : components.entrySet()) {
			panel.add(entry.getKey(), entry.getValue());
		}
	}
	
	public static FormLayoutHelper readFromFile(String fileName) {
		return null;
	}
}
