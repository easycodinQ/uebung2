/**
 * Visualization and HCI
 * Exercise 2: Data Representation and Interpolation
 * Volker Ahlers, HS Hannover (volker.ahlers@hs-hannover.de)
 */

package vish;

import javafx.geometry.Point2D;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Interpolator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private int    xSize;			// grid size
	private int    ySize;
	private double xMin;			// grid origin
	private double yMin;
	private double dx;				// grid spacing
	private double dy;
	private double[][] data;		// attribute values f(x,y)
	private BufferedImage image;	// image for visualization
	
	/**
	 * Main method
	 */
	public static void main(String[] args) {				
		// mathematical function
		Interpolator mathFunc = new Interpolator(
				"Math Function", 800, 800, 50, 50, -1.0, 1.0, -1.0, 1.0);
		mathFunc.createMathFuncData();
		mathFunc.setVisible(true);
	}
	
	/**
	 * Constructor
	 */
	public Interpolator(String title, int width, int height,
			int xSize, int ySize, double xMin, double xMax, double yMin, double yMax) {
		super(title);
		setSize(width, height);
		setResizable(false);
		this.xSize = xSize;
		this.ySize = ySize;
		this.xMin = xMin;
		this.yMin = yMin;
		dx = (xMax - xMin) / (float)(xSize - 1);
		dy = (yMax - yMin) / (float)(ySize - 1);
		data = new double[xSize][ySize];
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Create data
	 */
	public void createMathFuncData() {
		for (int i = 0; i < xSize; i++) {
			double x = xMin + i * dx;
			for (int j = 0; j < ySize; j++) {
				double y = yMin + j * dy;				
				data[i][j] = Math.sin(2.5 * Math.PI * x) * Math.sin(2.5 * Math.PI * y);
			}
		}
	}
		
	/**
	 * Interpolate attribute value f(x,y)
	 */
	public double getInterpolatedData(double x, double y) {
		// (x,y) coordinates of value to be interpolated
		// indices of closest sample points in x direction, x0 <= x <= x1
		int i0 = (int)((x - xMin) / dx);
		i0 = Math.min(Math.max(i0, 0), xSize - 1);	// ensure 0 <= i0 < xSize 
		int i1 = Math.min(i0 + 1, xSize - 1);
		// indices of closest sample points in y direction, y0 <= y <= y1
		int j0 = (int)((y - yMin) / dy);
		j0 = Math.min(Math.max(j0, 0), ySize - 1);	// ensure 0 <= j0 < ySize 
		int j1 = Math.min(j0 + 1, ySize - 1);
		// to do: bilinear interpolation of f(x,y)

		double x0 = xMin + i0 *dx;
		double x1 = xMin + i1 *dx;
		double y0 = yMin + j0 *dy;
		double y1 = yMin + j1 *dy;

		Point2D p = new Point2D(x,y);
		Point2D p1 = new Point2D(x0,y0);
		Point2D p2 = new Point2D(x0,y1);
		Point2D p3 = new Point2D(x1,y1);
		Point2D p4 = new Point2D(x1,y0);

		double r = p.subtract(p1).dotProduct(p2.subtract(p1)) / p2.distance(p1);

		double s = p.subtract(p1).dotProduct(p4.subtract(p1)) / p4.distance(p1);


		//return (1-r)*(1-s);//TODO interpolation


		// dummy: return non-interpolated value f(x0,y0)
		return data[i0][j0];
	}
	//public

	/**
	 * Create visualization image and show it in frame 
	 */
	@Override
	public void paint(Graphics graphics) {
		int width = getWidth();
		int height = getHeight();
		double dxImage = dx * xSize / (double) width;
		double dyImage = dy * ySize / (double) height;
		for (int i = 0; i < width; i++) {
			double x = xMin + i * dxImage;
			for (int j = 0; j < height; j++) {
				double y = yMin + j * dyImage;
				// map f(x,y) in [-1,1] to grayscale RGB value in [0,1]^3
				float color = 0.5f * ((float) getInterpolatedData(x, y) + 1.0f);
				image.setRGB(i, j, new Color(color, color, color).getRGB());
			}
		}
		graphics.drawImage(image, 0, 0, null);
	}

}
