import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 *@author Tooryanand Seetohul
 *with partner Hanna A. Kidanemaria
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * 
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children
	//private ArrayList<E> pointsInCircle = new ArrayList<E>();
	//List<E> listOfPoints = new ArrayList<E>();
	//int size = 0;
	

	

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		//if the point is on the right side
		if (this.compareX(p2)>0) {
			 //if the point is on the upper right
			if (this.compareY(p2)<0) {
				//create a new child if child is null, else insert in the child quadtree
				if (c1 == null) {
					c1 = new PointQuadtree<E>(p2, (int) point.getX(), y1, x2, (int) point.getY());
					}
				else {c1.insert(p2);
				}
			 }
			 //if the point is on the lower right 
			else if (this.compareY(p2)>0) {
				 //create a new child if child is null, else insert in the child quadtree
				if (c4 == null) {
					c4 = new PointQuadtree<E>(p2, (int) point.getX(), (int) point.getY(), x2, y2 );
					}
				else {c4.insert(p2);}
			 }
		 }
		//if the point is on the left side
		 else if (this.compareX(p2)<0) {
			 //if the point is on the upper left
			 if (this.compareY(p2)<0) {
				//create a new child if child is null, else insert in the child quadtree
				if (c2 == null) {
					c2 = new PointQuadtree<E>(p2, x1, y1 , (int) point.getX(), (int) point.getY() );
					}
				else {c2.insert(p2);}
			 }
			//if the point is on the lower left
			 else if (this.compareY(p2)>0) {
				//create a new child if child is null, else insert in the child quadtree
				if (c3 == null) {
					c3 = new PointQuadtree<E>(p2, x1, (int) point.getY(), (int) point.getX(), y2);
				}
				else {c3.insert(p2);} 
			 }
		 }
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {		
		int size = 1;    		//for the point itself
		// increase by one for every child
		if (this.hasChild(1)) size++;
		if (this.hasChild(2)) size++;
		if (this.hasChild(3)) size++; 
		if (this.hasChild(4)) size++; 
		
		return size;
		
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * @return a list of all points
	 */
	public List<E> allPoints() {
		// create a list to store the all points and add to the list after calling addToList
		ArrayList<E> p = new ArrayList<E>();
		this.addToList(p);
		
		return p;
	}	

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// create a list to store the points in circle and add to the list after calling pointsInCircle
		ArrayList<E>pointsInCircle = new ArrayList<E>();
		this.pointsInCircle(pointsInCircle,  cx,  cy,  cr);
	
		return pointsInCircle; 
	}

//	public boolean pointInCircle(double cx, double cy, double cr) {
//		if (cx-cr <= this.getPoint().getX() && this.getPoint().getX() <= cx+cr 
//				&& cy-cr <= this.getPoint().getY() && this.getPoint().getY() <= cy+cr) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
	/** 
	 * Compares max x value of point of quadtree with comparepoint
	 * @param comparePoint
	 * @return
	 */
	public int compareX(E comparePoint) {
		if (comparePoint.getX()<this.getPoint().getX()) {
			return -1;
		}
		else if (comparePoint.getX()>this.getPoint().getX()) {
			return +1;
		}
		else {
			return 0;
		}
	}
	/** 
	 * Compares max y value of point of quadtree with comparepoint
	 * @param comparePoint
	 * @return
	 */
	public int compareY(E comparePoint) {
		if (comparePoint.getY()<this.getPoint().getY()) {
			return -1;
		}
		else if (comparePoint.getY()>this.getPoint().getY()) {
			return +1;
		}
		else {
			return 0;
		}
	}
	/**
	 * Helper method for allPoints()
	 * @param points
	 */
	public void addToList(ArrayList<E> points) {
		// add the point itself
		points.add(this.getPoint());
		
		// recurse with every child
		if (this.hasChild(1)) {
			this.getChild(1).addToList(points);
			} 
		if (this.hasChild(2)) {
			this.getChild(2).addToList(points);
			} 
		if (this.hasChild(3)) {
			this.getChild(3).addToList(points);
			} 
		if (this.hasChild(4)) {
			this.getChild(4).addToList(points);
			} 	
		}
	/** 
	 * Helper method for findInCircle
	 * @param points an ArrayList to store points found in circle
	 * @param cx cicrle's x
	 * @param cy circle's y
	 * @param cr circle's radius
	 */
	public void pointsInCircle(ArrayList<E> points, double cx, double cy, double cr) {
		// if the circle intersects the major PointQuadTree
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, this.getX1(), this.getY1(), this.getX2(), this.getY2())) {
			// if the point of the major QuadTree is in the circle add it into points
			if (Geometry.pointInCircle(this.getPoint().getX(), this.getPoint().getY(), cx, cy, cr)) {
				points.add(this.getPoint());
			}
			// recurse for every child
			if (this.hasChild(1)) {
				this.getChild(1).pointsInCircle(points, cx, cy, cr);
			}
			if (this.hasChild(2)) {
				this.getChild(2).pointsInCircle(points, cx, cy, cr); 
			}
			if (this.hasChild(3)) {
				this.getChild(3).pointsInCircle(points, cx, cy, cr); 
			}
			if (this.hasChild(4)) {
				this.getChild(4).pointsInCircle(points, cx, cy, cr); 
			}
		}
	}
	/**
	 * Checks the height of the QuadTree
	 * @return height (0 for no child, 1 for its children, 2 for it's children's children)
	 */
	public int getHeight() {
		// base case (when the QuadTree had no children)
		if (!this.hasChild(1) && !this.hasChild(2) && !this.hasChild(3) && !this.hasChild(4)) {
			return 0;
		}
		// if it has children, for each child,
		// increase the height by the max value between the current height and the child's height
		// and increase by one (for the children)
		int height = 0;
		if (this.hasChild(1)) {
			height = Math.max(height, getChild(1).getHeight());
		}
		if (this.hasChild(2)) {
			height = Math.max(height, getChild(2).getHeight());
		}
		if (this.hasChild(3)) {
			height = Math.max(height, getChild(3).getHeight());
		}
		if (this.hasChild(4)) {
			height = Math.max(height, getChild(4).getHeight());
		}
		return height+1;
	}
	/* 
	 * test for insert, allPoints and size
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PointQuadtree<Dot> testMe = new PointQuadtree<Dot>(new Dot(300.0,600.0), 0, 0, 600, 800);
				testMe.insert(new Dot(400, 700));
				testMe.insert(new Dot(50,100));
				System.out.println(testMe.allPoints());
				System.out.println(testMe.size());
				
			}
		});
	}
}
