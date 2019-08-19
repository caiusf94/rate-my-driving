package com.caiusf.ratemydriving;

import com.caiusf.ratemydriving.controllers.AccelerometerValuesFilter;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void filtering(){
         AccelerometerValuesFilter filter = new AccelerometerValuesFilter();

        float[] v1 = new float[3];
        float[] v2 = new float[3];
        float[] v3 = new float[3];
        float[] v4 = new float[3];
        float[] v5 = new float[3];

        v1[0] =  1 ;
        v1[1] =  2 ;
        v1[2] = 3 ;
        v2[0] =  5 ;
        v2[1] = 6  ;
        v2[2] =  11 ;
        v3[0] =  4 ;
        v3[1] = 10  ;
        v3[2] =  34 ;
        v4[0] =  1 ;
        v4[1] =  3 ;
        v4[2] =  5 ;
        v5[0] =  3 ;
        v5[1] =  5 ;
        v5[2] =  7 ;

        filter.put(v1);
        filter.put(v2);
        filter.put(v3);
        filter.put(v4);
        filter.put(v5);

//        Assert.assertEquals((float)12.0, AccelerometerValuesFilter.filterValues()[2]);


    }
}