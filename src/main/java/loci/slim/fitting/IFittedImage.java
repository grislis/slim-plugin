//
// IFittedImage.java
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
    * Neither the names of the ImageJDev.org developers nor the
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

package loci.slim.fitting;

import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.numeric.real.DoubleType;

/**
 *
 * @author Aivar Grislis grislis at wisc dot edu
 */
public interface IFittedImage {

    /**
     * Gets width of image.
     * 
     * @return 
     */
    public int getWidth();
    
    /**
     * Gets height of image.
     * 
     * @return 
     */
    public int getHeight();
    
    /**
     * Gets number of channels of image.
     * 
     * @return 
     */
    public int getChannels();

    /**
     * Gets number of parameters of image.
     * 
     * @return 
     */
    public int getParameters();

    /**
     * Gets dimensions.
     *
     * @return
     */
    public int[] getDimension();

    /**
     * Gets pixel values at location.
     *
     * @param location
     * @return
     */
    public double[] getPixel(int[] location);

    /**
     * Sets pixel value at location.
     *
     * @param location
     * @param value
     */
    public void setPixel(int[] location, double[] value);   

    /**
     * Gets associated image.
     * 
     * @return 
     */
    public Image<DoubleType> getImage();   
}
