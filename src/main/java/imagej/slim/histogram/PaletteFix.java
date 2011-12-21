/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imagej.slim.histogram;

import java.awt.Color;
import java.awt.image.IndexColorModel;

/**
 * This class fixes a problem in the way ImageJ assigns palette colors to
 * FloatProcessor images.  Basically a FloatProcessor is converted to a 8-bit
 * image using the assigned minimum and maximum values.  Anything below min or
 * above max gets scrunched into the 0 or 255 value.
 *
 * This class supports viewing FloatProcessor images with a palette so that
 * values below min or above max are displayed in different colors.  The key to
 * this is to add the below min and above max colors to the palette.  Then you
 * essentially wind up with a 254 color palette to display a range of
 * FloatProcessor values.
 *
 * @author Aivar Grislis grislis at wisc dot edu
 */
public class PaletteFix {
    public static final int NATIVE_SIZE = 256;
    public static final int ADJUSTED_SIZE = 254;

    /**
     * Given a 256-color palette, turns it into a 254-color palette, using the
     * first and last palette entries for the below and above colors.
     *
     * @param colorModel
     * @param below
     * @param above
     * @return
     */
    public static IndexColorModel fixIndexColorModel(IndexColorModel colorModel,
            Color below, Color above) {
        // get the RGB colors for this color model
        byte[] reds   = new byte[NATIVE_SIZE];
        byte[] greens = new byte[NATIVE_SIZE];
        byte[] blues  = new byte[NATIVE_SIZE];
        colorModel.getReds(reds);
        colorModel.getBlues(blues);
        colorModel.getGreens(greens);

        // make the first entry the below color and the last the above color
        reds  [0] = (byte) below.getRed();
        greens[0] = (byte) below.getGreen();
        blues [0] = (byte) below.getBlue();

        reds  [NATIVE_SIZE - 1] = (byte) above.getRed();
        greens[NATIVE_SIZE - 1] = (byte) above.getGreen();
        blues [NATIVE_SIZE - 1] = (byte) above.getBlue();

        // make a new color model
        colorModel = new IndexColorModel(8, NATIVE_SIZE, reds, greens, blues);
        return colorModel;
    }

    /**
     * Given a min and max specification for a 254-color palette, turns it into
     * a 256-color palette min and max.
     * 
     * @param min
     * @param max
     * @return
     */
    public static double[] adjustMinMax(double min, double max) {
        double adjust = (max - min) / ADJUSTED_SIZE;
        return new double[] { min - adjust, max + adjust };
    }

    /**
     * Gets the adjusted palette size.
     *
     * @return
     */
    public static int getSize() {
        return ADJUSTED_SIZE;
    }
}