import java.util.ArrayList;
import java.util.Hashtable;

//Class that implements the dictionary which contains the map between the dimensions of the words and their number of
//apparitions and a list with the words with maximum length
public class MyDictionary {
    public Hashtable<Integer, Integer> dimensions_nrOfApparitions;
    public ArrayList<String> words_with_max_len;

    //Constructor
    public MyDictionary() {
        dimensions_nrOfApparitions = new Hashtable<>();
        words_with_max_len = new ArrayList<>();
        words_with_max_len.add("");
    }

    //Checks if the dictionary is empty by checking if the list with words is empty or not
    public boolean isEmpty(){
        return words_with_max_len.isEmpty();
    }

    //Adding element in map. It checks if the map already contains the key (case in which it's adding the given
    //parameter @addedValue to the old value of the key) and if it doesn't, it will add it with the value = @addedValue
    public void addElementInHash(int key, int addedValue){
        if (dimensions_nrOfApparitions.containsKey(key)) {
            int oldValue = dimensions_nrOfApparitions.get(key);
            dimensions_nrOfApparitions.replace(key, oldValue, oldValue + addedValue);
        } else {
            dimensions_nrOfApparitions.put(key, addedValue);
        }
    }

    //Updating the list of words with maximum length. If the given word (@word) has a greater length than the one the
    //list had until now, it will clear the list and add the current word in it. Else, if the lengths are equal it will
    //just add the word to the list. Otherwise, it just ignores the word.
    public void addWordWithMaxLen(String word){
        if (word.length() > words_with_max_len.get(0).length()) {
            words_with_max_len.clear();
            words_with_max_len.add(word);
        } else if (word.length() == words_with_max_len.get(0).length()){
            words_with_max_len.add(word);
        }
    }
}
