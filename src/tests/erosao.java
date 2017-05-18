package tests;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class erosao {
	
	public static void main(String[] args) {
		
		 try{

	         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	         Mat source = Imgcodecs.imread("C:\\Users\\leowa\\Pictures\\Saved Pictures\\baloes.jpg",  Imgcodecs.CV_LOAD_IMAGE_COLOR);
	         Mat destination = new Mat(source.rows(),source.cols(),source.type());

	         destination = source;

	         int erosion_size = 5;
	         int dilation_size = 5;
	         
	         Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
	         Imgproc.erode(source, destination, element);
	         Imgcodecs.imwrite("erosion.jpg", destination);

	         source = Imgcodecs.imread("digital_image_processing.jpg",  Imgcodecs.CV_LOAD_IMAGE_COLOR);
	         
	         destination = source;
	         
	         Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*dilation_size + 1, 2*dilation_size+1));
	         Imgproc.dilate(source, destination, element1);
	         Imgcodecs.imwrite("dilation.jpg", destination);

	      }catch (Exception e) {
	         System.out.println("error: " + e.getMessage());
	      }
		
	}

}
