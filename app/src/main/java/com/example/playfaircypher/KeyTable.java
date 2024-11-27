package com.example.playfaircypher; /**
 * NAME: SIMON KUANG
 * ID: 116687560
 * RECITATION: R04
 */

import java.util.ArrayList;

/**
 * Constructor of the keytable, will initialize a 5 by 5 char array
 */
public class KeyTable {
    private static char[][] key;
    public KeyTable() {
        char[][] key = new char[5][5];
    }

    /**
     * Gets rid of characters that aren't a letter and gets rid of duplicates
     * @param keyPhrase the phrase to convert into a valid key
     * @return a valid keytable to use for encryption or decryption
     * @throws IllegalArgumentException if the keyphrase is null, throw this exception
     */
    public static KeyTable buildFromString(String keyPhrase) throws IllegalArgumentException {
        if (keyPhrase == null) {
            throw new IllegalArgumentException("Key phrase is null.");
        }

        KeyTable keyTable = new KeyTable();

        keyPhrase = keyPhrase.toUpperCase().replaceAll(" ", "");
        char[] keyPhrase1DArray = keyPhrase.toCharArray();
        ArrayList<Character> noDupes = new ArrayList<>();
        char[][] keyPhraseArray = new char[5][5];

        for (char c : keyPhrase1DArray) {
            if (!noDupes.contains(c) && c >= 'A' && c <= 'Z')
                noDupes.add(c);
        }

        keyPhrase1DArray = new char[noDupes.size()];
        for(int i = 0; i < keyPhrase1DArray.length; i++) {
                keyPhrase1DArray[i] = noDupes.get(i);
        }

        int index = 0;
        int alpha = 'A';

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (index < noDupes.size()) {
                    keyPhraseArray[row][col] = noDupes.get(index);
                    index++;
                } else {
                    while (noDupes.contains((char) alpha) || alpha == 'J') {
                        alpha++;
                    }
                    keyPhraseArray[row][col] = (char) alpha;
                    alpha++;
                }
            }
        }
        key = keyPhraseArray;

        return keyTable;
    }

    /**
     *
     * @return returns key
     */
    public static char[][] getKeyTable()
    {
        return key;
    }

    /**
     *
     * @param c the char to be found
     * @return the row index of the char
     * @throws IllegalArgumentException throws this exception if the char is not a valid letter in the key matrix
     */
    public int findRow(char c)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                if(c == key[row][col])
                    return row;
            }
        }
        throw new IllegalArgumentException(c + " is not a valid letter in the key matrix");
    }

    /**
     *
     * @param c the char to be found
     * @return the col index of the char
     * @throws IllegalArgumentException throws this exception if the char is not a valid letter in the key matrix
     */
    public int findCol(char c)
    {
        for(int row = 0; row < 5; row++)
        {
            for(int col = 0; col < 5; col++)
            {
                if(c == key[row][col])
                    return col;
            }
        }
        throw new IllegalArgumentException(c + " is not a valid letter in the key matrix");
    }

    /**
     *
     * @param row the inputed row of the char
     * @param col the inputed col of the char
     * @return the char at the specified row and col
     */
    public char findChar(int row, int col)
    {
        return key[row][col];
    }

    /**
     *
     * @return the keytable turned into a 5x5 string
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int row = 0; row < 5; row++)
            for(int col = 0; col < 5; col++)
            {
                if(col%5 == 0)
                    sb.append("\n");
                sb.append(key[row][col]).append(" ");
            }
        return sb.toString();
    }

}
