/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loci.slim.heuristics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Aivar Grislis
 */
public class CursorEstimatorTest {
    
    public CursorEstimatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of estimateDecayCursors method, of class CursorEstimator.
     */
    //@Test
    public void testEstimateDecayCursors() {
        System.out.println("estimateDecayCursors");
        double xInc = 0.0;
        double[] decay = null;
        int[] expResult = null;
        int[] result = CursorEstimator.estimateDecayCursors(xInc, decay);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of estimateCursors method, of class CursorEstimator.
     * 
     * Since the method under test uses curve fitting this test may break if
     * the fitting process is tweaked.
     */
    @Test
    public void testEstimateCursors() {
        System.out.println("estimateCursors");
        double xInc = 0.048828125;
        // note that prompt and decay are the same:
        double[] prompt = { 1.0, 2.0, 1.0, 3.0, 2.0, 2.0, 0.0, 0.0, 0.0, 1.0, 4.0, 2.0, 1.0, 1.0, 2.0, 1.0, 2.0, 0.0, 1.0, 0.0, 0.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0, 5.0, 9.0, 10.0, 18.0, 17.0, 17.0, 35.0, 37.0, 32.0, 33.0, 28.0, 39.0, 36.0, 29.0, 32.0, 37.0, 38.0, 27.0, 31.0, 30.0, 32.0, 26.0, 29.0, 25.0, 25.0, 25.0, 21.0, 35.0, 23.0, 13.0, 15.0, 21.0, 18.0, 8.0, 16.0, 14.0, 20.0, 12.0, 18.0, 17.0, 17.0, 13.0, 15.0, 14.0, 16.0, 12.0, 18.0, 14.0, 10.0, 8.0, 10.0, 18.0, 7.0, 10.0, 8.0, 11.0, 11.0, 12.0, 10.0, 13.0, 7.0, 15.0, 8.0, 6.0, 10.0, 8.0, 7.0, 9.0, 11.0, 15.0, 6.0, 6.0, 10.0, 3.0, 8.0, 5.0, 7.0, 9.0, 7.0, 5.0, 3.0, 5.0, 4.0, 6.0, 5.0, 6.0, 7.0, 5.0, 8.0, 3.0, 11.0, 5.0, 5.0, 7.0, 10.0, 3.0, 6.0, 11.0, 5.0, 10.0, 3.0, 5.0, 4.0, 7.0, 2.0, 3.0, 3.0, 4.0, 4.0, 4.0, 5.0, 9.0, 8.0, 5.0, 7.0, 5.0, 4.0, 2.0, 9.0, 5.0, 2.0, 3.0, 7.0, 5.0, 4.0, 4.0, 0.0, 3.0, 5.0, 6.0, 7.0, 2.0, 2.0, 0.0, 5.0, 6.0, 1.0, 7.0, 5.0, 5.0, 1.0, 8.0, 4.0, 3.0, 7.0, 3.0, 1.0, 3.0, 2.0, 0.0, 2.0, 9.0, 3.0, 3.0, 3.0, 3.0, 0.0, 3.0, 2.0, 3.0, 4.0, 5.0, 2.0, 1.0, 1.0, 1.0, 2.0, 3.0, 4.0, 2.0, 1.0, 4.0, 2.0, 3.0, 2.0, 4.0, 1.0, 1.0, 6.0, 1.0, 3.0, 0.0, 2.0, 2.0, 3.0, 1.0, 0.0, 1.0, 2.0, 1.0, 1.0, 2.0, 2.0, 3.0, 3.0, 4.0, 0.0, 2.0, 2.0, 1.0, 0.0, 0.0, 3.0, 3.0, 1.0, 0.0, 2.0, 1.0, 2.0, 2.0, 3.0, 0.0, 2.0, 1.0, 2.0, 2.0, 2.0, 2.0, 0.0, 4.0, 0.0, 2.0, 2.0, 1.0, 1.0, 1.0, 2.0, 0.0, 2.0 };
        double[] decay = { 1.0, 2.0, 1.0, 3.0, 2.0, 2.0, 0.0, 0.0, 0.0, 1.0, 4.0, 2.0, 1.0, 1.0, 2.0, 1.0, 2.0, 0.0, 1.0, 0.0, 0.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0, 5.0, 9.0, 10.0, 18.0, 17.0, 17.0, 35.0, 37.0, 32.0, 33.0, 28.0, 39.0, 36.0, 29.0, 32.0, 37.0, 38.0, 27.0, 31.0, 30.0, 32.0, 26.0, 29.0, 25.0, 25.0, 25.0, 21.0, 35.0, 23.0, 13.0, 15.0, 21.0, 18.0, 8.0, 16.0, 14.0, 20.0, 12.0, 18.0, 17.0, 17.0, 13.0, 15.0, 14.0, 16.0, 12.0, 18.0, 14.0, 10.0, 8.0, 10.0, 18.0, 7.0, 10.0, 8.0, 11.0, 11.0, 12.0, 10.0, 13.0, 7.0, 15.0, 8.0, 6.0, 10.0, 8.0, 7.0, 9.0, 11.0, 15.0, 6.0, 6.0, 10.0, 3.0, 8.0, 5.0, 7.0, 9.0, 7.0, 5.0, 3.0, 5.0, 4.0, 6.0, 5.0, 6.0, 7.0, 5.0, 8.0, 3.0, 11.0, 5.0, 5.0, 7.0, 10.0, 3.0, 6.0, 11.0, 5.0, 10.0, 3.0, 5.0, 4.0, 7.0, 2.0, 3.0, 3.0, 4.0, 4.0, 4.0, 5.0, 9.0, 8.0, 5.0, 7.0, 5.0, 4.0, 2.0, 9.0, 5.0, 2.0, 3.0, 7.0, 5.0, 4.0, 4.0, 0.0, 3.0, 5.0, 6.0, 7.0, 2.0, 2.0, 0.0, 5.0, 6.0, 1.0, 7.0, 5.0, 5.0, 1.0, 8.0, 4.0, 3.0, 7.0, 3.0, 1.0, 3.0, 2.0, 0.0, 2.0, 9.0, 3.0, 3.0, 3.0, 3.0, 0.0, 3.0, 2.0, 3.0, 4.0, 5.0, 2.0, 1.0, 1.0, 1.0, 2.0, 3.0, 4.0, 2.0, 1.0, 4.0, 2.0, 3.0, 2.0, 4.0, 1.0, 1.0, 6.0, 1.0, 3.0, 0.0, 2.0, 2.0, 3.0, 1.0, 0.0, 1.0, 2.0, 1.0, 1.0, 2.0, 2.0, 3.0, 3.0, 4.0, 0.0, 2.0, 2.0, 1.0, 0.0, 0.0, 3.0, 3.0, 1.0, 0.0, 2.0, 1.0, 2.0, 2.0, 3.0, 0.0, 2.0, 1.0, 2.0, 2.0, 2.0, 2.0, 0.0, 4.0, 0.0, 2.0, 2.0, 1.0, 1.0, 1.0, 2.0, 0.0, 2.0 };
        double chiSqTarget = 1.5; 
        double[] expResult = { 27.0, 90.0, 2.477064220183486, 27.0, 37.0, 230.0 };
        double[] result = CursorEstimator.estimateCursors(xInc, prompt, decay, chiSqTarget);
        //TODO here you would like the baseline to match approximately and the bin
        //    numbers to match exactly.
        TestHelper.assertArrayComparable(expResult, result, 0.1);        
    }
}
