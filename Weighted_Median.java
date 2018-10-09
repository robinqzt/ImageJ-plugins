import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.*;
import javax.swing.*;

public class Weighted_Median implements PlugInFilter {

    final int[][] filter = {
        {1, 1, 2, 1, 1},
        {1, 1, 2, 1, 1},
        {2, 2, 5, 2, 2},
        {1, 1, 2, 1, 1},
        {1, 1, 2, 1, 1}
     };

	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;
	}
    
    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();

        ImageProcessor copy = ip.duplicate();

        int filterWidth = filter[0].length;
        int filterHeight = filter.length;

        int halfWidth = filterWidth / 2;
        int halfHeight = filterHeight / 2;

        for (int y = halfHeight; y < h - halfHeight; y++) {
            for (int x = halfWidth; x < w - halfWidth; x++) {
                ArrayList<Integer> values = new ArrayList<Integer>();

                for (int i = 0; i < filterWidth; i++) {
                    for (int j = 0; j < filterHeight; j++) {

                        int pixelValue = copy.getPixel(x + i - halfWidth, y + j - halfHeight);
                        int timesToAdd = filter[j][i];

                        for (int k = 0; k < timesToAdd; k++) {
                            values.add(pixelValue);
                        }

                    }
                }

                Collections.sort(values);

                int numValues = values.size();
                int newValue = (numValues % 2 == 1)
                    ? values.get(numValues / 2)
                    : (values.get(numValues / 2) + values.get(numValues / 2 + 1)) / 2;

                ip.putPixel(x, y, newValue);
                
            }
        }
    }

}
