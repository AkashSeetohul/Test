import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering
	
	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}
	

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.regions = new ArrayList<ArrayList<Point>>();
		
		// looping over all pixels
		for (int y=0; y < image.getHeight(); y++) {
			for (int x=0; x < image.getWidth(); x++) {
				
				ArrayList<Point> toVisit = new ArrayList<Point>();  //new arraylist of points keeping track of pixels to visit
				
				Color color = new Color(image.getRGB(x, y));  // color of that pixel
				
				//  if unvisited and the color is nearly the same
				if ((visited.getRGB(x, y) == 0) && (colorMatch(targetColor, color))) {
					
					// new sub-region to hold all points of similar color
					ArrayList<Point> region = new ArrayList<Point>();
					toVisit.add(new Point(x, y));
					
					// if there is more pixels to visit
					while (toVisit.size() > 0){
						
						Point tempPoint = toVisit.remove(toVisit.size()-1);
						
						//toVisit.remove(0);  // remove the current pixel from the list
						region.add(tempPoint);
						
						visited.setRGB((int)tempPoint.getX(), (int)tempPoint.getY(), 1); // mark as visited
						
						// looping over all the neighbors
						for (int cy = Math.max(0, (int)tempPoint.getY()-1); cy <= Math.min(image.getHeight()-1, (int)tempPoint.getY()+1) ; cy++) {
							for (int cx = Math.max(0, (int)tempPoint.getX()-1); cx <= Math.min(image.getWidth() -1, (int)tempPoint.getX()+1); cx++) {
								Color color1 = new Color(image.getRGB(cx, cy));  // color of neighbor
									if (colorMatch(targetColor, color1) && (visited.getRGB(cx, cy) == 0)) { // if color is almost the same, add the pixels to the toVisit list
										toVisit.add(new Point(cx, cy));
									}
							}
						}
					}
					
					// if the sub region is sufficiently big, add it to the region variable
					if (region.size() > minRegion) {
						this.regions.add(region);
						}
					}
				}
			}
		}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		return Math.abs(c1.getRed()-c2.getRed()) <= maxColorDiff && Math.abs(c1.getGreen()-c2.getGreen()) <= maxColorDiff && Math.abs(c1.getBlue()-c2.getBlue()) <= maxColorDiff;
		}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		
		ArrayList<Point> largeRegion = null;
		int maxSize = 0;
	
		if (this.regions.size() != 0) {
			for (ArrayList<Point> z: this.regions) {
				if (z.size() > maxSize) {
					maxSize = z.size();
					largeRegion = z;
				}
			}
		}
		return largeRegion;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		
		// looping over all regions
		for (ArrayList<Point> z: regions) {
			Color newColor = new Color((int) (Math.random()*16777216));
			for (Point temp : z) {
				recoloredImage.setRGB((int)temp.getX(), (int)temp.getY(), newColor.getRGB());
			}
		}
	}
}
