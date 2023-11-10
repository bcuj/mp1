package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;
import ch.epfl.cs107.utils.Bit;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 3.2: </b>Utility class to perform Text Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private TextSteganography(){}

    // ============================================================================================
    // =================================== EMBEDDING BIT ARRAY ====================================
    // ============================================================================================

    /**
     * Embed a bitmap message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedBitArray(int[][] cover, boolean[] message) {
        assert(cover != null && message != null);

        int[][] bitArrayEmbedded = new int[cover.length][cover[0].length];
        for(int i = 0; i < cover.length; ++i){
            assert(cover[i] != null);
            assert(cover[0].length == cover[i].length);
            for(int j = 0; j < cover[0].length; ++j){
                if (j + cover[0].length * i < message.length ) {
                    bitArrayEmbedded[i][j] = embedInLSB(cover[i][j], message[j + cover[0].length * i]);
                } else {
                    bitArrayEmbedded[i][j] = cover[i][j];
                }
            }
        }
        return bitArrayEmbedded;
    }

    /**
     * Extract a bitmap from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static boolean[] revealBitArray(int[][] image) {
        assert(image != null);
        if(image.length == 0) {return new boolean[0];}

        boolean[] extractedMessage = new boolean[image.length * image[0].length];
        for(int i = 0; i < image.length; ++i){
            assert(image[i] != null);
            assert(image[0].length == image[i].length);
            for(int j = 0; j < image[0].length; ++j){
                extractedMessage[j + image[0].length * i] = getLSB(image[i][j]);
            }
        }
        return extractedMessage;
    }



    // ============================================================================================
    // ===================================== EMBEDDING STRING =====================================
    // ============================================================================================

    /**
     * Embed a String message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedText(int[][] cover, byte[] message) {
        boolean[]  messageEmbedded= Bit.toBitArray(message);
        return embedBitArray(cover, messageEmbedded);
    }

    /**
     * Extract a String from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static byte[] revealText(int[][] image) {
        boolean[] revealedBitArray = revealBitArray(image);
        return Bit.toBytes(revealedBitArray);
    }
}
