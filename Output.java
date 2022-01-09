

//Class used to simplify the way we get the output. It contains the document name, the calculated rang, the maximum
//length a word from file has and the number of this kind of words.
public class Output implements Comparable{
    public String docName;
    public double rang;
    public int maxLen;
    public int nrOfWordsWithMaxLen;

    //Constructors
    public Output() {}

    public Output(String docName, double rang, int maxLen, int nrOfWordsWithMaxLen) {
        this.docName = docName;
        this.rang = rang;
        this.maxLen = maxLen;
        this.nrOfWordsWithMaxLen = nrOfWordsWithMaxLen;
    }

    //Method used for sorting the outputs by the rang
    public int compareTo(Object o) {
        Output output = (Output)o;
        return Double.compare(output.rang, rang);
    }
}
