/**
 * NAME: SIMON KUANG
 * ID: 116687560
 * RECITATION: R04
 */

import java.util.LinkedList;

public class Phrase extends LinkedList<Bigram>
{
    /**
     * Adds a Bigram to the queue (back of the linkedList)
     *
     * @param b the Bigram to be added to the queue
     */
    public void enqueue(Bigram b)
    {
        this.offer(b);
    }

    /**
     * Removes and returns the Bigram at the front of the queue.
     *
     * @return the Bigram at the front of the queue, or null if the queue is empty
     */
    public Bigram dequeue()
    {
        return this.poll();
    }

    /**
     * Retrieves the Bigram at the front of the queue without removing it.
     *
     * @return the Bigram at the front of the queue, or null if the queue is empty
     */
    public Bigram peek()
    {
        return super.peek();
    }

    /**
     * Returns the number of Bigram objects in the queue.
     *
     * @return the number of elements in the queue
     */
    public int size()
    {
        return super.size();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * Removes all bigrams from the queue and turns them into strings (FIFO order)
     * @return A string of all bigrams in the queue
     */
    public String toString()
    {
        StringBuilder bigrams = new StringBuilder();
        while(!this.isEmpty())
        {
            bigrams.append(this.dequeue().toString());
        }
        return bigrams.toString();
    }


    /**
     * Adds bigrams into a phrase and returns it, checking if the letter is valid or duplicate
     * @param s String to be converted into a phrase split into bigrams following the rules of playfair encryption
     * @return a phrase containing bigrams created from the string
     */
    public static Phrase buildPhraseFromStringforEnc(String s) {
        Phrase p = new Phrase();
        s = s.toUpperCase().trim().replaceAll("J", "I");

        char[] chars = s.toCharArray();
        Bigram bg = new Bigram();
        LinkedList<Character> temp = new LinkedList<>();
        for(char c : chars) {
            if(c >= 'A' && c <= 'Z' || c == ' ')
            {
                temp.add(c);
            }
        }

        char[] temporary = new char[temp.size()];
        for(int i = 0; i < temp.size(); i++)
        {
            temporary[i] = temp.get(i);
        }

        chars = temporary;

        for (int i = 0; i < chars.length; i++) {
            char current = chars[i];
            char next;
            if(i + 1 < chars.length) {
                next = chars[i + 1];
            }
            else
            {
                next = '\0';
            }

            if(chars[i] == ' ')
            {
                next = ' ';
                continue;
            }

            if (current == next) {
                {
                    p.enqueue(new Bigram(current, 'X'));
                }
            } else {
                bg.add(current);
                if (bg.isFull()) {
                    p.enqueue(bg);
                    bg = new Bigram();
                }
            }
        }

        if (!bg.isEmpty() && !bg.isFull()) {
            bg.setSecond('X');
            p.enqueue(bg);
        }
        return p;
    }

    /**
     * @param s The inputed string to be converted into a queue of bigrams
     * @return Phrase ready to be decrypted
     */
    public static Phrase buildPhraseFromStringforDec(String s) {
        Bigram bg = new Bigram();
        Phrase p = new Phrase();
        for(int i = 0; i < s.length(); i++)
        {
            bg.add(s.charAt(i));
            if(bg.isFull())
            {
                p.enqueue(bg);
                bg = new Bigram();
            }
        }
        return p;
    }

    /**
     * Checks the chararcters of the bigram and sees if their row indexes or column indexes match.
     * Goes through all 3 rules in a playfair cypher (if they're same row, col, or if they form a rectangle)
     * @param key the keytable that determines the result of each encrypted bigram
     * @return a fully encrypted Phrase
     */
    public Phrase encrypt(KeyTable key)
    {
        if(key == null)
            throw new IllegalArgumentException("Key is null");
        Phrase encryptedBigrams = new Phrase();

        //Might be good to have all these conditions in different helper functions
        while(!this.isEmpty())
        {
            Bigram bigramEnc = this.dequeue();
            char first = bigramEnc.getFirst();
            char second = bigramEnc.getSecond();
            int colfirst = key.findCol(first);
            int rowfirst = key.findRow(first);
            int colsecond = key.findCol(second);
            int rowsecond = key.findRow(second);

            if (rowfirst == rowsecond) {
                encryptedBigrams.enqueue(sameRowEn(key, rowfirst, rowsecond, colfirst, colsecond));
            } else if (colfirst == colsecond) {
                encryptedBigrams.enqueue(sameColEn(key, rowfirst, rowsecond, colfirst, colsecond));
            } else if (colfirst != colsecond && rowfirst != rowsecond) {
                encryptedBigrams.enqueue(rectangleEn(key, rowfirst, rowsecond, colfirst, colsecond));
            }
        }
        return encryptedBigrams;
    }

    /**
     * The characters of the bigram which are in the same column will be shifted down by 1 and wrapped around if
     * there's no more space
     * @param key the keytable that determines the result of each encrypted bigram
     * @param rowfirst the row index of the first character in the bigram
     * @param rowsecond the row index of the con character in the bigram
     * @param colfirst the col index of the first character in the bigram
     * @param colsecond the col index of the second character in the bigram
     * @return Returns a bigram that is encrypted with the key
     */
    public static Bigram sameColEn(KeyTable key, int rowfirst, int rowsecond, int colfirst, int colsecond) //WORKS
    {
        char[][] keyTable = key.getKeyTable();
        boolean enoughSpaceFirst = rowfirst + 1 < keyTable.length;
        boolean enoughSpaceSecond = rowsecond + 1 < keyTable.length;

        if(enoughSpaceFirst && enoughSpaceSecond)
        {
            return new Bigram(keyTable[rowfirst+1][colfirst], keyTable[rowsecond+1][colsecond]);
        }
        else if(enoughSpaceFirst && !enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst+1][colfirst], keyTable[0][colsecond]);
        else if(!enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[0][colfirst], keyTable[rowsecond+1][colsecond]);
        else //Not even possible I dont think but there just in case
            return new Bigram(keyTable[0][colfirst], keyTable[0][colsecond]);
    }

    /**
     * The characters of the bigram which are in the same row will be shifted right by 1 and wrapped around if
     * there's no more space
     * @param key the keytable that determines the result of each encrypted bigram
     * @param rowfirst the row index of the first character in the bigram
     * @param rowsecond the row index of the con character in the bigram
     * @param colfirst the col index of the first character in the bigram
     * @param colsecond the col index of the second character in the bigram
     * @return Returns a bigram that is encrypted with the key
     */
    public static Bigram sameRowEn(KeyTable key, int rowfirst, int rowsecond, int colfirst, int colsecond)
    {
        char[][] keyTable = key.getKeyTable();
        boolean enoughSpaceFirst = colfirst + 1 < keyTable.length;
        boolean enoughSpaceSecond = colsecond + 1 < keyTable.length;

        if(enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][colfirst+1], keyTable[rowsecond][colsecond+1]);
        else if(enoughSpaceFirst && !enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][colfirst+1], keyTable[rowsecond][0]);
        else if(!enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][0], keyTable[rowsecond][colsecond+1]);
        else //Not even possible I dont think but there just in case
            return new Bigram(keyTable[rowfirst][0], keyTable[rowsecond][0]);
    }

    /**
     * The characters will form a rectangle and this method will return the two characters on opposite corners of
     * the first then second character
     * @param key the keytable that determines the result of each encrypted bigram
     * @param rowfirst the row index of the first character in the bigram
     * @param rowsecond the row index of the con character in the bigram
     * @param colfirst the col index of the first character in the bigram
     * @param colsecond the col index of the second character in the bigram
     * @return Returns a bigram that is encrypted with the key
     */
    public static Bigram rectangleEn(KeyTable key, int rowfirst, int rowsecond, int colfirst, int colsecond)
    {
        if (colfirst == colsecond || rowfirst == rowsecond) {
            throw new IllegalArgumentException("Doesn't form a rectangleEn");
        }

        char firstChar = key.findChar(rowfirst, colsecond);  // The character at the top-right corner
        char secondChar = key.findChar(rowsecond, colfirst); // The character at the bottom-left corner

        return new Bigram(firstChar, secondChar);
    }

    /**
     * Checks the chararcters of the bigram and sees if their row indexes or column indexes match.
     * Goes through all 3 rules in a playfair cypher (check if they're same row, col, or if they form a rectangle)
     * but reverses each condition to undo it
     * @param key the keytable that determines the result of each encrypted bigram
     * @return returns a decrypted phrase
     */
    public Phrase decrypt(KeyTable key)
    {
        if(key == null)
            throw new IllegalArgumentException("Key is null");
        Phrase decryptedBigrams = new Phrase();
        while(!this.isEmpty())
        {
            Bigram bigramEnc = this.dequeue();
            char first = bigramEnc.getFirst();
            char second = bigramEnc.getSecond();
            int colfirst = key.findCol(first);
            int rowfirst = key.findRow(first);
            int colsecond = key.findCol(second);
            int rowsecond = key.findRow(second);

            if (rowfirst == rowsecond) {
                decryptedBigrams.enqueue(sameRowDe(key, rowfirst, rowsecond, colfirst, colsecond));
            } else if (colfirst == colsecond) {
                decryptedBigrams.enqueue(sameColDe(key, rowfirst, rowsecond, colfirst, colsecond));
            } else if (colfirst != colsecond && rowfirst != rowsecond) {
                decryptedBigrams.enqueue(rectangleEn(key, rowfirst, rowsecond, colfirst, colsecond));
            }
        }
        return decryptedBigrams;
    }

    /**
     *  The characters of the bigram will be in the same column and will be shifted up by 1 and wraps around
     *  if there is no more space
     * @param key the keytable that determines the result of each encrypted bigram
     * @param rowfirst the row index of the first character in the bigram
     * @param rowsecond the row index of the con character in the bigram
     * @param colfirst the col index of the first character in the bigram
     * @param colsecond the col index of the second character in the bigram
     * @return Returns a bigram that is decrypted with the key
     */
    public static Bigram sameColDe(KeyTable key, int rowfirst, int rowsecond, int colfirst, int colsecond)
    {
        char[][] keyTable = key.getKeyTable();
        boolean enoughSpaceFirst = rowfirst - 1 >= 0;
        boolean enoughSpaceSecond = rowsecond - 1 >= 0;

        if(enoughSpaceFirst && enoughSpaceSecond)
        {
            return new Bigram(keyTable[rowfirst-1][colfirst], keyTable[rowsecond-1][colsecond]);
        }
        else if(enoughSpaceFirst && !enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst-1][colfirst], keyTable[4][colsecond]);
        else if(!enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[4][colfirst], keyTable[rowsecond-1][colsecond]);
        else
            return new Bigram(keyTable[4][colfirst], keyTable[4][colsecond]);
    }

    /**
     *  The characters of the bigram will be in the same row and will be shifted left by 1 and wraps around if
     *  there is no more space
     * @param key the keytable that determines the result of each encrypted bigram
     * @param rowfirst the row index of the first character in the bigram
     * @param rowsecond the row index of the con character in the bigram
     * @param colfirst the col index of the first character in the bigram
     * @param colsecond the col index of the second character in the bigram
     * @return Returns a bigram that is decrypted with the key
     */
    public static Bigram sameRowDe(KeyTable key, int rowfirst, int rowsecond, int colfirst, int colsecond)
    {
        char[][] keyTable = key.getKeyTable();
        boolean enoughSpaceFirst = colfirst - 1 >= 0;
        boolean enoughSpaceSecond = colsecond - 1 >= 0;

        if(enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][colfirst-1], keyTable[rowsecond][colsecond-1]);
        else if(enoughSpaceFirst && !enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][colfirst-1], keyTable[rowsecond][4]);
        else if(!enoughSpaceFirst && enoughSpaceSecond)
            return new Bigram(keyTable[rowfirst][4], keyTable[rowsecond][colsecond-1]);
        else //Not even possible I dont think but there just in case
            return new Bigram(keyTable[rowfirst][4], keyTable[rowsecond][4]);
    }
}
