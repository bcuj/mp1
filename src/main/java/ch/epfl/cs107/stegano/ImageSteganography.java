package ch.epfl.cs107.stegano;

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
 * <b>Task 3.1: </b>Utility class to perform Image Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImageSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private ImageSteganography(){}

    // ============================================================================================
    // ================================== EMBEDDING METHODS =======================================
    // ============================================================================================

    /**
     * Embed an ARGB image on another ARGB image (the cover)
     * @param cover Cover image
     * @param argbImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedARGB(int[][] cover, int[][] argbImage, int threshold){
        int[][] argbToGray = toGray(argbImage);
        return embedGray(cover, argbToGray, threshold);
    }

    /**
     * Embed a Gray scaled image on another ARGB image (the cover)
     * @param cover Cover image
     * @param grayImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedGray(int[][] cover, int[][] grayImage, int threshold){
        boolean[][] grayImageToBinary = toBinary(grayImage, threshold);
        return embedBW(cover, grayImageToBinary);
    }

    /**
     * Embed a binary image on another ARGB image (the cover)
     * @param cover Cover image
     * @param load Embedded image
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedBW(int[][] cover, boolean[][] load){
        assert(cover != null && load != null);
        assert ((cover.length != 0) && (load.length != 0));
        assert(load.length <= cover.length);
        assert(load[0].length <= cover[0].length);

        int[][] embeddedBwImage = new int[cover.length][cover[0].length];
        for(int i = 0; i < cover.length; ++i){
            for(int j = 0; j < cover[i].length; ++j){
                if(i < load.length && j < load[0].length) {
                    embeddedBwImage[i][j] = embedInLSB(cover[i][j], load[i][j]);
                }else {
                    embeddedBwImage[i][j] = cover[i][j];
                }
            }
        }
        return embeddedBwImage;
    }

    // ============================================================================================
    // =================================== REVEALING METHODS ======================================
    // ============================================================================================

    /**
     * Reveal a binary image from a given image
     * @param image Image to reveal from
     * @return binary representation of the hidden image
     */
    public static boolean[][] revealBW(int[][] image) {
        assert(image != null);
        assert(image.length != 0);
        boolean[][] revealedImage = new boolean[image.length][image[0].length];
        for(int i = 0; i < image.length; ++i){
            for(int j = 0; j < image[0].length; ++j){
                revealedImage[i][j] = getLSB(image[i][j]);
            }
        }
        return revealedImage;
    }

}
