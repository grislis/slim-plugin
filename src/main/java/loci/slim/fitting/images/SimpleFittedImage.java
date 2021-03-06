//
// SimpleFittedImage.java
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
 * Simple image that just displays one of the input parameters.
 *
 * @author Aivar Grislis grislis at wisc dot edu
 */
public class SimpleFittedImage extends AbstractBaseFittedImage {
    private int _parameterIndex;

    /**
     * Constructor, giving index of input parameter to display
     *
     * @param title
     * @param dimension
     * @param parameterIndex
     */
    public SimpleFittedImage(String title, int[] dimension,
            IndexColorModel indexColorModel, int parameterIndex,
            boolean colorizeGrayScale, IGrayScalePixelValue grayScalePixelValue,
            IMaskGroup[] maskGroup) {
        super(title, dimension, indexColorModel, colorizeGrayScale,
                grayScalePixelValue, maskGroup);
        _parameterIndex = parameterIndex;
    }
    
    /**
     * Given the array of fitted parameters, get the value for this image.
     * 
     * @param parameters
     * @return 
     */
    @Override
    public double getValue(double[] parameters) {
        return parameters[_parameterIndex];
    }
}