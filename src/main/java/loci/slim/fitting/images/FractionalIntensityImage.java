//
// FractionalIntensityImage.java
//

/*
SLIMPlugin for combined spectral-lifetime image analysis.

Copyright (c) 2010, UW-Madison LOCI
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the UW-Madison LOCI nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package loci.slim.fitting.images;

import java.awt.image.IndexColorModel;

import loci.slim.IGrayScalePixelValue;
import loci.slim.mask.IMaskGroup;

/**
 * This class builds a fitted image that shows the fractional intensity.
 * 
 * Fractional Intensity Fi = Ai / sum of all Aj.
 *
 * @author Aivar Grislis
 */
public class FractionalIntensityImage extends AbstractBaseFittedImage {
    private int _component;
    private int _components;

    /**
     * Create the fitted image.  Specifies number of components which should
     * be 2 or 3 and the current component which should be 0..1 or 0..2
     * respectively.
     * 
     * @param title
     * @param dimension
     * @param component
     * @param components 
     */
    public FractionalIntensityImage(String title, int[] dimension,
            IndexColorModel indexColorModel, int component, int components,
            boolean colorizeGrayScale, IGrayScalePixelValue grayScalePixelValue,
            IMaskGroup[] maskGroup) {
        super(title, dimension, indexColorModel, colorizeGrayScale,
                grayScalePixelValue, maskGroup);
        _component = component;
        _components = components;
    }
    
    public double getValue(double[] parameters) {
        double value = 0.0;
        double sum = 0.0;
        switch (_components) {
            case 2:
                sum = parameters[FittedImageFitter.A1_INDEX]
                        + parameters[FittedImageFitter.A2_INDEX];
                switch (_component) {
                    case 0:
                        value = parameters[FittedImageFitter.A1_INDEX];
                        break;
                    case 1:
                        value = parameters[FittedImageFitter.A2_INDEX];;
                        break;
                }
                break;
            case 3:
                sum = parameters[FittedImageFitter.A1_INDEX]
                        + parameters[FittedImageFitter.A2_INDEX]
                        + parameters[FittedImageFitter.A3_INDEX];
                switch (_component) {
                    case 0:
                        value = parameters[FittedImageFitter.A1_INDEX];
                        break;
                    case 1:
                        value = parameters[FittedImageFitter.A2_INDEX];
                        break;
                    case 2:
                        value = parameters[FittedImageFitter.A3_INDEX];
                        break;
                }
                break;
        }
        return value / sum;
    }   
}
