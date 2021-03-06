//
// HistogramTool.java
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

package loci.slim.histogram;

import java.awt.BorderLayout;
import java.awt.image.IndexColorModel;
import java.io.IOException;

import ij.IJ;
import ij.plugin.LutLoader;
import ij.process.LUT;

import java.awt.Color;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;


/**
 * This is the main class for the histogram tool.  It handles layout and wiring
 * of UI components and the logic of updating the histogram.
 *
 * @author Aivar Grislis grislis at wisc dot edu
 */
public class HistogramTool {
    private final static int WIDTH = PaletteFix.getSize();
    private final static int INSET = 5;
    private final static int HISTOGRAM_HEIGHT = 140;
    private final static int COLORBAR_HEIGHT = 20;
    private final static int TASK_PERIOD = 100;
    private final static String LUT = "lifetime.lut";
    private static HistogramTool INSTANCE = null;
    private final Object _synchObject = new Object();
    private JFrame _frame;
    private HistogramDataGroup _histogramDataGroup;
    private HistogramPanel _histogramPanel;
    private ColorBarPanel _colorBarPanel;
    private HistogramUIPanel _uiPanel;
 
    /**
     * Constructor, handles layout and wiring.
     */
    private HistogramTool() { //TODO ARG how do you pass this in if it is a singleton?  (cf getInstance() below)
        //TODO ARG need to be able to close this window and have it pop back up as needed
        // boolean hasChannels) {
        // create the histogram and color bar display panels
        _histogramPanel = new HistogramPanel(WIDTH, INSET, HISTOGRAM_HEIGHT);
        _histogramPanel.setListener(new HistogramPanelListener());
        _colorBarPanel = new ColorBarPanel(WIDTH, INSET, COLORBAR_HEIGHT);
        _colorBarPanel.setLUT(getLUT());
        boolean hasChannels = false;
        _uiPanel = new HistogramUIPanel(hasChannels);
        _uiPanel.setListener(new UIPanelListener());

        _frame = new JFrame("Histogram");
        _frame.setResizable(false);
        _frame.getContentPane().add(_histogramPanel, BorderLayout.NORTH);
        _frame.getContentPane().add(_colorBarPanel, BorderLayout.CENTER);
        _frame.getContentPane().add(_uiPanel, BorderLayout.SOUTH);
        _frame.pack();
        _frame.setVisible(true);
    }

    /**
     * Class is a singleton for convenience.
     * 
     * @return 
     */
    public static synchronized HistogramTool getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new HistogramTool();
        }
        return INSTANCE;
    }

    /**
     * Gets an IndexColorModel by loading a hardcoded LUT file.
     * This is just a temporary expedient, really belongs elsewhere.
     * 
     * @return 
     */
    public static IndexColorModel getIndexColorModel() {
        IndexColorModel colorModel = null;
 
        // 'getDirectory("luts")' works in IJ but not in NetBeans development
        String lutPath = IJ.getDirectory("luts");
        if (null == lutPath) {
            // when you run from a shortcut in Linux 'getDirectory("startup")'
            //   gives you the directory of the link!
            String startupPath = IJ.getDirectory("startup");
            lutPath = addSeparator(startupPath) + "luts";
        }
        lutPath = addSeparator(lutPath) + LUT;
        try {
            colorModel = LutLoader.open(lutPath);
        }
        catch (IOException e) {
            IJ.showMessage("Missing LUT", "Install lifetime.lut from LOCI FIJI update site.");
            IJ.log("Problem loading LUT " + lutPath);
            System.out.println("Problem loading LUT " + lutPath);
        }
        // IJ converts the FloatProcessor to 8-bits and then uses this palette
        // for display.  Unfortunately values less than or greater than the LUT
        // range still get displayed with LUT colors.  To work around this, use
        // only 254 of the LUT colors.  The first and last colors will represent
        // values less than and greater than the LUT range respectively.

        colorModel = PaletteFix.fixIndexColorModel(colorModel, Color.BLACK, Color.BLACK);
        return colorModel;
    }
    
    private static String addSeparator(String path) {
        if (!path.endsWith(File.separator)) {
            path += File.separatorChar;
        }
        return path;
    }

    /**
     * Gets a LUT.  Temporary expedient, belongs elsewhere.
     * 
     * @return 
     */
    public static LUT getLUT() {
        IndexColorModel colorModel = getIndexColorModel();
        LUT lut = new LUT(colorModel, Double.MIN_VALUE, Double.MAX_VALUE);
        return lut;
    }

    /**
     * This method should be called whenever a new set of histogram values is
     * to be displayed (i.e. when a different image gets focus).
     * 
     * @param histogramData 
     */
    public void setHistogramData(HistogramDataGroup histogramData) {
        double[] minMaxView;
        double[] minMaxLUT;
        synchronized (_synchObject) {
            _histogramDataGroup = histogramData;
            _histogramDataGroup.setListener(new HistogramDataListener());
            minMaxView = _histogramDataGroup.getMinMaxView();
            minMaxLUT  = _histogramDataGroup.getMinMaxLUT();
        }
        
        if (_frame.isVisible()) {
            _frame.setVisible(true);
        }
        _frame.setTitle(histogramData.getTitle());
        
        _histogramPanel.setBins(histogramData.binValues(WIDTH));
 
        boolean autoRange = histogramData.getAutoRange();
        _uiPanel.setAutoRange(autoRange);
        if (!autoRange) {
            _histogramPanel.setCursors(cursorPixelFromValue
                    (false, minMaxLUT[0]), cursorPixelFromValue(false, minMaxLUT[1])); //TODO ARG true, maxLUT)); in this case adding 1 to max is too much!
        }        
        _uiPanel.setExcludePixels(histogramData.getExcludePixels());
        _uiPanel.setCombineChannels(histogramData.getCombineChannels());
        _uiPanel.setDisplayChannels(histogramData.getDisplayChannels());
        _uiPanel.enableChannels(histogramData.hasChannels());        
        _uiPanel.setMinMaxLUT(minMaxLUT[0], minMaxLUT[1]);
        
        _colorBarPanel.setMinMax(minMaxView[0], minMaxView[1],
                minMaxLUT[0], minMaxLUT[1]);
    }

    /*
     * Converts histogram onscreen horizontal pixel amounts to image values.
     */
    private double pixelToValue(int pixel) {
        synchronized (_synchObject) {
            double[] minMaxView = _histogramDataGroup.getMinMaxView();
            double min = minMaxView[0];
            return min + pixelToValueRelative(pixel);
        }
    }
    
    private double pixelToValueRelative(int pixel) {
        synchronized (_synchObject) {
            double[] minMaxView = _histogramDataGroup.getMinMaxView();
            double min = minMaxView[0];
            double max = minMaxView[1];
            double valuePerPixel = (max - min) / PaletteFix.getSize();
            return pixel * valuePerPixel;
        }
    }

    /*
     * Converts image value to histogram onscreen horizontal pixel.
     */
    private int valueToPixel(double value) {
        synchronized (_synchObject) {
            double[] minMaxView = _histogramDataGroup.getMinMaxView();
            double min = minMaxView[0];
            double max = minMaxView[1];
            int pixel = (int)(PaletteFix.getSize() * (value - min) / (max - min));
            return pixel;
        }
    }

    /*
     * Updates histogram and color bar during the fit.
     */
    private void changed(double minView, double maxView,
                double minLUT, double maxLUT) {
        synchronized (_synchObject) {
            int[] bins = _histogramDataGroup.binValues(WIDTH);
            _histogramPanel.setBins(bins);
            _colorBarPanel.setMinMax(minView, maxView, minLUT, maxLUT);
            //TODO changed is currently called from two places:
            // i) the HistogramData listener will call it periodically during the
            // fit.
            // ii) if the user types in a new LUT range this gets called.
            // iii) in the future more UI interactions will wind up here
            //
            //TODO if the user drags a new LUT range this doesn't get called!
            
          System.out.println("changed min/maxView " + minView + " " + maxView + " min/maxLUT " + minLUT + " " + maxLUT);  
            
            
            _histogramDataGroup.redisplay();
        }
    }

    /*
     * Given a value representing the minimum or maximum cursor bound,
     * calculates a pixel location for the cursor.
     * 
     * @param max whether this is the maximum cursor or not
     */
    private int cursorPixelFromValue(boolean max, double value) {
        int pixel = INSET + valueToPixel(value);
        if (max) {
            // cursor brackets the value
            ++pixel;
        }
        return pixel;
    }

    /**
     * Inner class listens for changes during the fit.
     */
    private class HistogramDataListener implements IHistogramDataListener {
        
        @Override
        public void minMaxChanged(double minView, double maxView,
                double minLUT, double maxLUT) {
            changed(minView, maxView, minLUT, maxLUT);
            _uiPanel.setMinMaxLUT(minLUT, maxLUT);
        }
    }

    /**
     * Inner class to listen for the user moving the cursor on the histogram.
     */
    private class HistogramPanelListener implements IHistogramPanelListener {
        private Timer _timer = null;
        private volatile int _dragPixels;
 
        /**
         * Listens to the HistogramPanel, gets minimum and maximum cursor bar
         * positions in pixels.  Called when the cursor bar is moved and the
         * mouse button released.  A new LUT range has been specified.
         * 
         * @param min
         * @param max 
         */
        public void setMinMaxLUTPixels(int min, int max) {
            killTimer();

            // get new minimum and maximum values for LUT
            double minLUT = pixelToValue(min);
            double maxLUT = pixelToValue(max + 1);
            
            // set min and max on UI panel
            _uiPanel.setMinMaxLUT(minLUT, maxLUT);

            // redraw color bar
            _colorBarPanel.setMinMaxLUT(minLUT, maxLUT);
            
            // save and redraw image
            synchronized (_synchObject) {
                _histogramDataGroup.setMinMaxLUT(minLUT, maxLUT);
                if (_uiPanel.getExcludePixels()) {
                    _histogramDataGroup.excludePixels(true);
                }
            }
        }

        /**
         * Listens to the HistogramPanel, gets minimum and maximum cursor bar
         * positions in pixels.  Called during a drag operation.
         *
         * @param min
         * @param max
         */
        @Override
        public void dragMinMaxPixels(int min, int max) {
            if (min < 0 || max >= PaletteFix.getSize()) {
                // cursor is out of bounds, set up a periodic task to stretch
                // the bounds, if not already running
                if (min < 0) {
                    _dragPixels = min;
                }
                else {
                    _dragPixels = max - PaletteFix.getSize() + 1;
                }
                if (null == _timer) {
                    _timer = new Timer();
                    _timer.schedule(new PeriodicTask(), 0, TASK_PERIOD);
                }
            }
            else {
                // dragging within bounds now, kill the periodic task, if any
                killTimer();
                double minLUT = pixelToValue(min);
                double maxLUT = pixelToValue(max + 1);
                _uiPanel.dragMinMaxLUT(minLUT, maxLUT);
            }
        }

        @Override
        public void exited() {
            // dragged off the panel, kill the periodic task
            killTimer();
        }

        // stop our timer for animating view expansion
        private void killTimer() {
            if (null != _timer) {
                _timer.cancel();
                _timer = null;
            }
        }

        /**
         * Inner class used to animate view expansion, triggered by the initial
         * report of a mouse drag event off the edge of the histogram.
         */
        private class PeriodicTask extends TimerTask {

            @Override
            public void run() {
                // how much are we dragging, converted to our value
                double value = pixelToValueRelative(_dragPixels);
                synchronized (_synchObject) {
                    // get current LUT bounds
                    double[] minMaxLUT = _histogramDataGroup.getMinMaxLUT();
                    double minLUT = minMaxLUT[0];
                    double maxLUT = minMaxLUT[1];

                    // adjust the appropriate left or right side of the view
                    double[] minMaxView = _histogramDataGroup.getMinMaxView();
                    double minView = minMaxView[0];
                    double maxView = minMaxView[1];
                    if (value < 0) {
                        minView += value;
                        minLUT = minView;
                    }
                    else {
                        maxView += value;
                        maxLUT = maxView;
                    }

                    // update histogram data min and max view
                    _histogramDataGroup.setMinMaxView(minView, maxView);
                    
                    // keep other cursor fixed
                    int pixel;
                    if (value < 0) {
                        if (maxView != maxLUT) {
                            pixel = cursorPixelFromValue(true, maxLUT);
                            //pixel = valueToPixel(maxLUT);
                            //System.out.println("PeriodicTask.run maxLUT is " + maxLUT + " cursor is at " + pixel);
                            //_histogramPanel.setCursors(null, INSET + pixel + 1); //TODO ARG this is still a little bit off
                            _histogramPanel.setCursors(null, pixel); //TODO ARG this is still a little off
                        }
                    }
                    else {
                        if (minView != minLUT) {
                            pixel = cursorPixelFromValue(false, minLUT);
                            //pixel = valueToPixel(minLUT);
                            //System.out.println("PeriodicTask.run minLUT is " + minLUT + " cursor is at " + pixel);
                            //_histogramPanel.setCursors(INSET + pixel, null); //TODO ARG this is still a little bit off
                            _histogramPanel.setCursors(pixel, null); //TODO ARG this is still a little off
  
                        }
                    }
                    
                    // get updated histogram data and show it                 
                    int[] bins = _histogramDataGroup.binValues(WIDTH);
                    _histogramPanel.setBins(bins);
                    _colorBarPanel.setMinMax(minView, maxView, minLUT, maxLUT);
                    
                    // update numbers in UI panel
                    _uiPanel.dragMinMaxLUT(minLUT, maxLUT);
                }
            }
        }
    }

    private class UIPanelListener implements IUIPanelListener {
        @Override
        public void setAutoRange(boolean autoRange) {
            synchronized (_synchObject) {
                 _histogramDataGroup.setAutoRange(autoRange);
                 
                 if (autoRange) {
                     //TODO ARG:
                    // It is not always true that the cursors will be (null, null)
                    // [same as (0, 254)], the exception happens if you are showing
                    // all channels but only autoranging your channel.
                    _histogramPanel.setCursors(null, null);
                    
                  ///  _histogramData.recalcHistogram(); //TODO histogramData should just keep minData/maxData same as the others
                 }
                 else {
                    //TODO if you are autoranging, not combining channels, but showing
                    // all channels, these cursors would need to be calculated
                    _histogramPanel.setCursors(INSET, INSET + WIDTH - 1); //TODO I was expecting INSET-1 here??

                 }
            }
        }
        
        @Override
        public void setExcludePixels(boolean excludePixels) {
            //TODO ARG Mask selfMask = null;
            synchronized (_synchObject) {
                _histogramDataGroup.excludePixels(excludePixels);
            }
        }
        
        @Override
        public void setCombineChannels(boolean combineChannels) {
            synchronized (_synchObject) {
                _histogramDataGroup.setCombineChannels(combineChannels);
            }
            //TODO
            //System.out.println("HistogramTool.UIPanelListener.setCombineChannels(" + combineChannels + ")");
        }
        
        @Override
        public void setDisplayChannels(boolean displayChannels) {
            synchronized (_synchObject) {
                _histogramDataGroup.setDisplayChannels(displayChannels);
                
                // get updated histogram data and show it                 
                int[] bins = _histogramDataGroup.binValues(WIDTH);
                _histogramPanel.setBins(bins);
            }
            //TODO
            //System.out.println("HistogramTool.UIPanelListener.setDisplayChannels(" + displayChannels + ")");
        }

        @Override
        public void setMinMaxLUT(double minLUT, double maxLUT) {
            boolean changed = false;
            double minView = 0;
            double maxView = 0;
            
            // silently ignores errors; c/b temporary condition such as setting
            // maximum before minimum
            if (minLUT < maxLUT) {
                changed = true;
                
                synchronized (_synchObject) {
                    // if necessary, expand the view to fit the LUT
                    double[] minMaxView = _histogramDataGroup.getMinMaxView();
                    minView = Math.min(minLUT, minMaxView[0]);
                    maxView = Math.max(maxLUT, minMaxView[1]);
                    _histogramDataGroup.setMinMaxView(minView, maxView);
                    _histogramDataGroup.setMinMaxLUT(minLUT, maxLUT);

                    //TODO ARG this is quite a bit off:
                    // note that if you stretch the bounds on one side you need
                    // to adjust the position of the other side.
                    _histogramPanel.setCursors(cursorPixelFromValue(false, minLUT), cursorPixelFromValue(false, maxLUT)); //TODO ARG true, maxLUT)); in this case adding 1 to max is too much!
                }
            }

            if (changed) {
                System.out.println("CHANGED:" + minView + " " + maxView);
                // update histogram and color bar
                changed(minView, maxView, minLUT, maxLUT);
            }
            //System.out.println("new min LUT is " + minLUT + "new max LUT is " + maxLUT);   
        }
    }
}
