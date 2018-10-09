import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import javax.swing.JOptionPane;


/**
* Metode som oppretter og viser frem et kumulativt histogram
*/
public class Kum_Histogram implements PlugInFilter {
	ImagePlus imp;

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G + NO_CHANGES; // No changes betyr at ingen endringer vil gj�res p� bildet
	}

	public void run(ImageProcessor ip) {
		int h = ip.getHeight();
		int w = ip.getWidth();

		visHistogram(ip, h, w);

		
	}

	public void visHistogram(ImageProcessor ip, int h, int w) {
		int[] arr = initializeHistogram();
		fyllHistogram(ip, arr, h, w);
		int[] kum_hist = kumulativtHistogram(arr);

		int STARThist = 50;
		int ENDhist = 350;

		ImageProcessor nyP = new ByteProcessor(400, 400);
		setWhiteBackground(nyP, h, w);
		
		// Tegner y og x-aksen
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x == STARThist && y > STARThist && y < ENDhist) {
					nyP.putPixel(x, y, 0);
				}
				if (y == ENDhist && x > STARThist && x < ENDhist) {
					nyP.putPixel(x, y, 0);
				}	
			}
		}
		
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {			
				if ( y > 360 && y < 380
				&& x > STARThist && x < ENDhist) {
					nyP.putPixel(x, y, 100);
						
				}
			}			
		
		}
	
		
		new ImagePlus("bilde", nyP).show();
	}

	public int[] kumulativtHistogram(int[] arr) {
		int[] kum_hist = new int[arr.length];
		int sum = 0;

		for (int i = 0; i < kum_hist.length; i++) {
			sum += arr[i];
			kum_hist[i] = sum;
		}
		return kum_hist;
	}

	public int[] initializeHistogram() {
		int[] arr = new int[256];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = 0;
		}
		
		return arr;
	}

	public void fyllHistogram(ImageProcessor ip, int[] arr, int h, int w) {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int a = ip.getPixel(y, x);
				arr[a] = arr[a] +1;
			}
		}
	}

	public void setWhiteBackground(ImageProcessor ip, int h, int w) {
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				ip.putPixel(x, y, 255);
			}
		}
	}
}
