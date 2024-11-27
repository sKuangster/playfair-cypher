/**
 * NAME: SIMON KUANG
 * ID: 116687560
 * RECITATION: R04
 */

public class Bigram {
    char first;
    char second;

    /**
     * Bigram constructor, will set default values of first and second to '\0'
     */
    public Bigram()
    {
        this.first = '\0';
        this.second = '\0';
    }

    /**
     * Constructor that takes in first and second chars and turns them into bigrams
     * @param first first letter in the bigram
     * @param second second character in the bigram
     */
    public Bigram(char first, char second) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     * @return first character of bigram
     */
    public char getFirst() {
        return first;
    }

    /**
     *
     * @return second character of bigram
     */
    public char getSecond() {
        return second;
    }

    /**
     *
     * @param first the character to set the first character of the bigram into
     */
    public void setFirst(char first) {
        this.first = first;
    }

    /**
     *
     * @param second the character to set the second character of the bigram into
     */
    public void setSecond(char second) {
        this.second = second;
    }

    /**
     *
     * @return converts the bigram into a string
     */
    public String toString()
    {
        return "" + first + second;
    }

    /**
     *
     * @return whether the bigram has two characters inside it or not
     */
    public boolean isFull()
    {
        return first != '\0' && second != '\0';
    }

    /**
     * The bigram's first character will be filled but if it's already filled then the second one will be filled by c instead
     * @param c the character to be added into the bigram
     */
    public void add(char c)
    {
        if(this.first == '\0') {
            this.first = c;
        }
        else if(this.second == '\0') {
            this.second = c;
        }
    }

    /**
     *
     * @return whether the bigram is empty or not
     */
    public boolean isEmpty() {
        if(first == '\0' && second == '\0')
            return true;
        return false;
    }
}
