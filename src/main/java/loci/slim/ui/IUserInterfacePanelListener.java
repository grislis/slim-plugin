//
// IUserInterfacePanelListener.java
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

package loci.slim.ui;

/**
 * Listens for user input that triggers changes external to the ui panel.
 *
 * @author Aivar Grislis grislis at wisc.edu
 */
public interface IUserInterfacePanelListener {

    /**
     * Triggers a fit.
     */
    public void doFit();

    /**
     * Cancels ongoing fit.
     */
    public void cancelFit();

    /**
     * Quits running plugin.
     */
    public void quit();

    /**
     * Loads an excitation curve from file.
     *
     * @param fileName
     * @return whether successful
     */
    public boolean loadExcitation(String fileName);

    /**
     * Creates an excitation curve from current X, Y and saves to file.
     *
     * @param fileName
     * @return whether successful
     */
    public boolean createExcitation(String fileName);

    /**
     * Cancels the current excitation curve, if any.
     */
    public void cancelExcitation();

    /**
     * Estimates the prompt and decay cursors.
     */
    public void estimateCursors();
}
