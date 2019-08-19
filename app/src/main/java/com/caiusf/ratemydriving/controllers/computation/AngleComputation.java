package com.caiusf.ratemydriving.controllers.computation;


/**Utility class for computing angle values
 *
 * @author Caius Florea, 2017
 */
public class AngleComputation {

    /**
     * Rotates a vector by angle theta.
     * @param theta
     *          the rotation angle in radians.
     * @param indexOfX
     *              index in the array of the X coordinate
     * @param indexOfY
     *              index in the array of the Y coordinate
     *
     * @param accelerationVectors
     *                      acceleration vector array.
     */
    public static void rotateVectors(double theta, int indexOfX, int indexOfY, float[] accelerationVectors){
        accelerationVectors[indexOfX] = (float) (accelerationVectors[indexOfX] * Math.cos(theta) - accelerationVectors[indexOfY] * Math.sin(theta));
        accelerationVectors[indexOfY] = (float) (accelerationVectors[indexOfX] * Math.sin(theta) +  accelerationVectors[indexOfY]  * Math.cos(theta));

    }
}
