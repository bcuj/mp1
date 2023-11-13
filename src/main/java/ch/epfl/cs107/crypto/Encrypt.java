package ch.epfl.cs107.crypto;

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
 * <b>Task 2: </b>Utility class to encrypt a given plain text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Encrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Encrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to encode a byte array message using a single character key
     * the key is simply added to each byte of the original message
     *
     * @param plainText The byte array representing the string to encode
     * @param key the byte corresponding to the char we use to shift
     * @return an encoded byte array
     */
    public static byte[] caesar(byte[] plainText, byte key) {
        assert (plainText != null);

        byte[] cipher = new byte[plainText.length];
        for (int i = 0; i < plainText.length; ++i)
            cipher[i] = (byte) (plainText[i] + key);

        return cipher;
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * The keyword is repeated along the message to encode
     * The bytes of the keyword are added to those of the message to encode
     * @param plainText the byte array representing the message to encode
     * @param keyword the byte array representing the key used to perform the shift
     * @return an encoded byte array
     */
    public static byte[] vigenere(byte[] plainText, byte[] keyword) {
        assert (plainText != null);
        assert (keyword!=null && keyword.length!=0);

        byte[] cipher = new byte[plainText.length];
        for (int i = 0; i < plainText.length; ++i)
            cipher[i] = (byte) (plainText[i] + keyword[i % keyword.length]);
            //↑The modulo operator enables us to cycle through the characters in the keyword

        return cipher;
    }

    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method applying a basic chain block counter of XOR without encryption method.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return an encoded byte array
     */
    public static byte[] cbc(byte[] plainText, byte[] iv) {
        assert (plainText != null);
        assert (iv != null);
        int T = iv.length;
        assert (T > 0);

        byte[] pad = iv.clone(); //← Avoid modifying arguments
        byte[] cipher = new byte[plainText.length];
        //↓ We want the loop to run once per whole block; +1 if some characters smaller than a block remain
        int numberOfBlocks = (int) Math.ceil((double) plainText.length/T);
        //↓1. Iterate over the blocks
        for (int i = 0; i < numberOfBlocks; ++i) {
            //↓2. Iterate over the characters in block i (making sure you don't go out of bounds for the last block)
            for (int j = 0; (j < T) && (T*i + j < plainText.length); ++j) {
                cipher[T*i + j] = (byte) (plainText[T*i + j] ^ pad[j]);
                //↓ Once the pad's j'th coordinate has been used to encrypt the i'th block's j'th coordinate,
                // it can be replaced on the fly with the newly found value for the next iteration of i
                pad[j] = cipher[T*i + j];
            }
        }

        return cipher;
    }

    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a XOR with a single byte long key
     * @param plainText the byte array representing the string to encode
     * @param key the byte we will use to XOR
     * @return an encoded byte array
     */
    public static byte[] xor(byte[] plainText, byte key) {
        assert (plainText != null);
        // No assertion is needed on `key` because it's a primitive type and thus can't be used with invalid input

        byte[] cipher = new byte[plainText.length];
        for (int i = 0; i < plainText.length; ++i)
            cipher[i] = (byte) (plainText[i] ^ key);

        return cipher;
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to encode a byte array using a one-time pad of the same length.
     *  The method XOR them together.
     * @param plainText the byte array representing the string to encode
     * @param pad the one-time pad
     * @return an encoded byte array
     */
    public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
        assert (plainText != null);
        assert (pad!=null && pad.length==plainText.length);

        byte[] cipher = new byte[plainText.length];
        for (int i = 0; i < plainText.length; ++i)
            cipher[i] = (byte) (plainText[i] ^ pad[i]);

        return cipher;
    }

    /**
     * Method to encode a byte array using a one-time pad
     * @param plainText Plain text to encode
     * @param pad Array containing the used pad after the execution
     * @param result Array containing the result after the execution
     */
    public static void oneTimePad(byte[] plainText, byte[] pad, byte[] result) {
        assert (result != null);
        assert (plainText!=null && plainText.length==result.length);
        assert (pad!=null && pad.length!=plainText.length);

        for (int i = 0; i < plainText.length; ++i)
            result[i] = (byte) (plainText[i] ^ pad[i]);
    }

}