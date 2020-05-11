package de.proglabor.aufgabe6;

import java.util.ArrayList;
import java.util.List;

public abstract class IStone {
	// variables
	private List<Point2D> elements;
	private int width;
	private int key;
	private Point2D center;

	// Constructor
	public IStone(List<Point2D> in) {
		elements = new ArrayList<Point2D>();
		for (Point2D point : in) {
			elements.add(point);
		}
	};

	public IStone(Point2D p1, Point2D p2, Point2D p3, Point2D p4, int width, int key) {
		elements = new ArrayList<Point2D>();
		elements.add(p1);
		elements.add(p2);
		elements.add(p3);
		elements.add(p4);
		this.width = width;
		this.key = key;
		setStone();
	}
	//

	public boolean hasElement(Point2D element) {
		return elements.contains(element);
	}

	public void setStone() {
		for (Point2D point2d : elements) {
			point2d.setX((int) (point2d.getX() + 0.5 * (Tetris.XSIZE - getWidth())));
		}
	}

	public void moveX(int x) {
		for (Point2D point2d : elements) {
			point2d.setX(point2d.getX() + x);
		}
	}

	public void moveY(int y) {
		for (Point2D point2d : elements) {
			point2d.setY(point2d.getY() + y);
		}
	}

	public void rotate() {
		int centerX = center.getX();
		int centerY = center.getY();
		for (Point2D point : elements) {
			point.setX(centerX - point.getX());
			point.setY(centerY - point.getY());
			int tempX = point.getX();
			int tempY = point.getY();
			point.setX(-tempY);
			point.setY(tempX);
			point.setX(point.getX() + centerX);
			point.setY(point.getY() + centerY);
		}
	}

	public void movingLeft() {
		moveX(-1);
	}

	public void movingRight() {
		moveX(1);
	}

	public void falling() {
		moveY(1);
	}

	public boolean overRange() {
		for (Point2D point : elements) {
			if (point.getX() > Tetris.XSIZE - 1 || point.getX() < 0 || point.getY() > Tetris.YSIZE - 1) {
				return true;
			}
		}
		return false;

	}

	public void copyStone(IStone s) {
		List<Point2D> sElements = s.getElements();
		elements.clear();
		for (int i = 0; i < sElements.size(); i++) {
			elements.add(i, new Point2D(sElements.get(i).getX(), sElements.get(i).getY()));
		}
		if (elements.size() == 4) {
			setCenter(getElement(s.getElements().indexOf(s.center) + 1));
		}
	}

	public void deleteElements(int y) {
		List<Point2D> remove = new ArrayList<Point2D>();
		for (Point2D point : elements) {
			if (point.getY() == y) {
				remove.add(point);
			}
		}
		elements.removeAll(remove);
		if (elements.size() < 4) {
			rebuild();
		}
	}

	private void rebuild() {
		int setValue = 0;
		for (Point2D point : elements) {
			if (point.getY() > setValue) {
				setValue = point.getY() - 1;
			}
		}
		if (setValue > 0) {
			Point2D comparePoint = new Point2D(0, 0);
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getY() < setValue) {
					comparePoint.setY(setValue);
					comparePoint.setX(elements.get(i).getX());
					if (hasElement(comparePoint)) {
						setValue--;
					}
					elements.get(i).setY(setValue);
				}
			}
		}
	}

	public boolean checkIfNull() {
		if (elements.size() == 0) {
			return true;
		}
		return false;
	}
	// getter and setter

	public Point2D getElement(int input) {
		try {
			return elements.get(input - 1);
		} catch (IndexOutOfBoundsException e) {
//			System.out.println(
//					"Out of Bound Exception just happened when call getElement(input) method and input = " + input);
		}
		return elements.get(0);

	}

	/*
	 * set element point
	 */
	public void setElements(Point2D element, int input) {
		elements.set(input, element);
	}

	/*
	 * set element
	 */
	public void setElements(List<Point2D> in) {
		int centerPosition = getElements().indexOf(center);
		elements.clear();
		for (int i = 0; i < in.size(); i++) {
			setElements(new Point2D(in.get(i).getX(), in.get(i).getY()), i);
		}
		setCenter(getElement(centerPosition + 1));
	}

	public List<Point2D> getElements() {
		return elements;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Point2D getCenter() {
		return center;
	}

	public void setCenter(Point2D center) {
		this.center = center;
	}

	public String getType() {
		switch (getKey()) {
		case 0:
			return "StoneI";
		case 1:
			return "StoneJ";
		case 2:
			return "StoneL";
		case 3:
			return "StoneO";
		case 4:
			return "StoneS";
		case 5:
			return "StoneT";
		case 6:
			return "StoneZ";
		}
		return null;
	}
}
