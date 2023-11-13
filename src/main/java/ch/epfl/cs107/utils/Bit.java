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
 * <b>Task 1.1: </b>Utility class to manipulate bits
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Bit {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Bit(){}

    // ============================================================================================
    // ==================================== BIT MANIPULATION ======================================
    // ============================================================================================

    /**
     * Embed a bit in a given integer bits
     * <p>
     * @param value value to embed in
     * @param m <code>true</code> to embed 1, <code>false</code> to embed 0
     * @param pos position of the bit to change
     * @return embedded value
     */
    public static int embedInXthBit(int value, boolean m, int pos) {
        assert (0<=pos && pos<Integer.SIZE);

        if (m) {
            return value | (int) Math.pow(2, pos);
        } else {
            return value & (~ (int) Math.pow(2, pos));
        }
    }

    /**
     * Embed a bit in the "least significant bit" (LSB)
     * <p>
     * @param value value to embed in
     * @param m <code>true</code> to embed 1, <code>false</code> to embed 0
     * @return embedded value
     */
    public static int embedInLSB(int value, boolean m){
        return embedInXthBit(value, m, 0);
    }

    /**
     * Extract a bit in a given position from a given value
     * <p>
     * @param value value to extract from
     * @param pos position of the bit to extract
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getXthBit(int value, int pos) {
        assert (0<=pos && pos<Integer.SIZE);

        int shiftedValue = value >>> pos;
        return (shiftedValue & 0b1) == 1;
    }

    /**
     * Extract the 'least significant bit' from a given value
     * <p>
     * @param value value to extract from
     * @return <code>true</code> if the bit is '1' and <code>false</code> otherwise
     */
    public static boolean getLSB(int value) {
        return getXthBit(value, 0);
    }

    // ============================================================================================
    // ==================================== BYTE MANIPULATION =====================================
    // ============================================================================================

    /**
     * Convert a byte value to the bit array representation.
     * <p>
     * Suppose we have the following input <b><code>0b00_11_01_10</code></b>. We can expect this function
     * to return the following array :
     * <b>[<code>false</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>]</b>
     * @param value byte representation of the value
     * @return bit array representation of the value
     */
    public static boolean[] toBitArray(byte value){
        boolean[] bitArray = new boolean[Byte.SIZE];
        for (int i = 0; i < Byte.SIZE; ++i)
            bitArray[i] = getXthBit(value, Byte.SIZE-i-1);

        return bitArray;
    }

    /**
     * Convert a given byte array into a boolean array
     * @param byteArray array to convert
     * @return byte array in the <b>bit array</b> format
     */
    public static boolean[] toBitArray(byte[] byteArray){
        assert (byteArray != null);

        boolean[] byteAsBits; //Temporary byte containing new bits to unpack at each iteration
        boolean[] bitArray = new boolean[byteArray.length * Byte.SIZE];

        for (int i = 0; i < byteArray.length; ++i) {
            byteAsBits = Bit.toBitArray(byteArray[i]);
            for (int j = 0; j < Byte.SIZE; ++j)
                bitArray[Byte.SIZE * i + j] = byteAsBits[j];
        }

        return bitArray;
    }

    /**
     * Convert a boolean array to a byte
     * <p>
     * Suppose we have the following input :
     * <b>[<code>false</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>,
     *     <code>true</code>,
     *     <code>true</code>,
     *     <code>false</code>]</b>
     * We can expect this function to return the following byte : <b><code>0b00_11_01_10</code></b>.
     * @param bitArray bit array representation to convert
     * @return the byte representation of the bit array
     */
    public static byte toByte(boolean[] bitArray){
        assert (bitArray != null);
        assert (bitArray.length == Byte.SIZE);

        // Initialise sum with -128 if the MSB is 1, else 0
        int sum = bitArray[0] ? (-1*(int) Math.pow(2, Byte.SIZE-1)) : 0;

        // Iterate through the rest of the byte "normally"
        for (int i = 1; i < Byte.SIZE; ++i) {
            if (bitArray[i])
                sum += (int) Math.pow(2, Byte.SIZE-1-i);
        }

        return (byte) sum;
    }

    public static byte[] toBytes(boolean[] bitArray){
        assert (bitArray != null);

        byte[] byteArray = new byte[bitArray.length/Byte.SIZE]; //Integer division to avoid reading any trailing bits
        for (int i = 0; i < byteArray.length; ++i) {
            boolean[] charAsBitArray = java.util.Arrays.copyOfRange(bitArray, Byte.SIZE*i, Byte.SIZE*(i+1));
            byteArray[i] = Bit.toByte(charAsBitArray);
        }

        return byteArray;
    }
}
