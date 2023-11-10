package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 1.3: </b>Utility class to manipulate ARGB images
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Image {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Image(){}

    // ============================================================================================
    // ==================================== PIXEL MANIPULATION ====================================
    // ============================================================================================

    /**
     * Build a given pixel value from its respective components
     *
     * @param alpha alpha component of the pixel
     * @param red red component of the pixel
     * @param green green component of the pixel
     * @param blue blue component of the pixel
     * @return packed value of the pixel
     */
    public static int argb(byte alpha, byte red, byte green, byte blue){
        int shiftedAlpha = Byte.toUnsignedInt(alpha) << 24;
        int shiftedRed = Byte.toUnsignedInt(red) << 16;
        int shiftedGreen = Byte.toUnsignedInt(green) << 8;
        int shiftedBlue = Byte.toUnsignedInt(blue);

        // After that all we have to do is to "add up" everything using a sequence of ORs
        return (shiftedAlpha | shiftedRed | shiftedGreen | shiftedBlue);
    }

    /**
     * Extract the alpha component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the alpha component of the pixel
     */
    public static byte alpha(int pixel){
        return (byte) (pixel >>> 24 & 0xFF);
    }

    /**
     * Extract the red component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the red component of the pixel
     */
    public static byte red(int pixel){
        return (byte) (pixel >>> 16 & 0xFF);
    }

    /**
     * Extract the green component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the green component of the pixel
     */
    public static byte green(int pixel){
        return (byte) (pixel >>> 8 & 0xFF);
    }

    /**
     * Extract the blue component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the blue component of the pixel
     */
    public static byte blue(int pixel){
        return (byte) (pixel & 0xFF);
    }

    /**
     * Compute the gray scale of the given pixel
     *
     * @param pixel packed value of the pixel
     * @return gray scaling of the given pixel
     */
    public static int gray(int pixel){
        int redAsInt = Byte.toUnsignedInt(red(pixel));
        int greenAsInt = Byte.toUnsignedInt(green(pixel));
        int blueAsInt = Byte.toUnsignedInt(blue(pixel));

        return (redAsInt + greenAsInt + blueAsInt) / 3;
    }

    /**
     * Compute the binary representation of a given pixel.
     *
     * @param gray gray scale value of the given pixel
     * @param threshold when to consider a pixel white
     * @return binary representation of a pixel
     */
    public static boolean binary(int gray, int threshold){
        assert(gray <= 255 && gray >= 0);

        return (gray >= threshold);
    }

    // ============================================================================================
    // =================================== IMAGE MANIPULATION =====================================
    // ============================================================================================

    /**
     * Build the gray scale version of an ARGB image
     *
     * @param image image in ARGB format
     * @return the gray scale version of the image
     */
    public static int[][] toGray(int[][] image){
        assert (image != null);
//        if (image.length == 0) {return image;}
        //↑This case separation allows for us to use this↓ lookup without going OutOfBounds
        //(when the image is empty, its grayscale version must be empty too)
        int[][] toGrayImage = new int[image.length][];//image[0].length];
        for (int i = 0; i < image.length; ++i){
            assert (image[i] != null);

            toGrayImage[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; ++j)
                toGrayImage[i][j] = gray(image[i][j]);
        }
        return toGrayImage;
    }

    /**
     * Build the binary representation of an image from the gray scale version
     *
     * @param image Image in gray scale representation
     * @param threshold Threshold to consider
     * @return binary representation of the image
     */
    public static boolean[][] toBinary(int[][] image, int threshold){
        assert (image != null);
        //↓Here the return type being different, we create an empty bit array on the fly
        if (image.length == 0) {return new boolean[0][];}

        boolean[][] toBinaryImage = new boolean[image.length][image[0].length];
        for (int i = 0; i < image.length; ++i) {
            assert (image[i] != null);
            for (int j = 0; j < image[i].length; ++j)
                toBinaryImage[i][j] = binary(image[i][j], threshold);
        }
        return toBinaryImage;
    }

    /**
     * Build an ARGB image from the gray-scaled image
     * @implNote The result of this method will a gray image, not the original image
     * @param image grayscale image representation
     * @return <b>gray ARGB</b> representation
     */
    public static int[][] fromGray(int[][] image){
        assert (image != null);
        if (image.length == 0) {return image;}

        int[][] fromGrayImage = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; ++i){
            assert (image[i] != null);
            for (int j = 0; j < image[i].length; ++j)
                fromGrayImage[i][j] = argb((byte)0xFF, (byte) image[i][j], (byte) image[i][j], (byte) image[i][j]);
        }
        return fromGrayImage;
    }

    /**
     * Build an ARGB image from the binary image
     * @implNote The result of this method will a black and white image, not the original image
     * @param image binary image representation
     * @return <b>black and white ARGB</b> representation
     */
    public static int[][] fromBinary(boolean[][] image){
        assert (image != null);
        if (image.length == 0) {return new int[0][];}

        int[][] binaryToGray = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; ++i){
            assert (image[i] != null);
            for (int j = 0; j < image[i].length; ++j)
                binaryToGray[i][j] = (image[i][j]) ? 255 : 0;
        }
        return fromGray(binaryToGray);
    }

}
