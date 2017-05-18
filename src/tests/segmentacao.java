package tests;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class segmentacao {
	
	public static void main(String[] args) {
		
		 try{

	         System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	         Mat source = Imgcodecs.imread("C:\\Users\\leowa\\Pictures\\Saved Pictures\\baloes.jpg",  Imgcodecs.CV_LOAD_IMAGE_COLOR);
	         Mat destination = new Mat(source.rows(),source.cols(),source.type());

	         destination = source;
	         Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_OTSU);
	         Imgcodecs.imwrite("ThreshZero.jpg", destination);
	         
	      }catch (Exception e) {
	         System.out.println("error: " + e.getMessage());
	      }
		
	}

}
