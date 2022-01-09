import java.util.concurrent.Callable;

//Map type tasks
public class Map implements Callable<MyDictionary> {
    private int start, end;
    private final String file_content;
    private final StringBuilder stringBuilder;
    private final MyDictionary myDictionary;

    //Constructor
    public Map( int start, int end, String file_content) {
        this.start = start;
        this.end = end;
        this.file_content = file_content;
        stringBuilder = new StringBuilder();
        myDictionary = new MyDictionary();
    }

    //Method that implements the mapping part of the program
    public MyDictionary call() throws Exception {

        //Making sure that we are not going out of bound
        if(end > file_content.length() - 1){
            end = file_content.length() - 1;
        }

        //If there is a divided word on the left side of the fragment, the @start parameter will increment until it
        //finds one of the delimiters, or until it's about to go outside the entire text
        if(start != 0){
            if(Tema2.delimiters.indexOf(file_content.charAt(start - 1)) == -1 &&
                    Tema2.delimiters.indexOf(file_content.charAt(start)) == -1){
                while(Tema2.delimiters.indexOf(file_content.charAt(start)) == -1){
                    start ++;
                    if(start >= file_content.length()){
                        break;
                    }
                }
            }
        }

        //Same idea as above, but for the @end parameter
        if(end != file_content.length() - 1){
            if(Tema2.delimiters.indexOf(file_content.charAt(end + 1)) == -1){
                while(Tema2.delimiters.indexOf(file_content.charAt(end)) == -1){
                    end ++;
                    if(end >= file_content.length()){
                        break;
                    }
                }
            }
        }

        //Creating the final string form of the fragment
        for (int i = start; i <= end; i++) {
            if(i >= file_content.length()){
                break;
            }
            stringBuilder.append(file_content.charAt(i));
        }

        //Splitting the fragment in words by delimiters
        String[] words = stringBuilder.toString().split("[^\\w]+");

        //Going through all the words and adding them to @myDictionary
        for(String word : words) {
            myDictionary.addWordWithMaxLen(word);

            if(word.length() != 0) {
                myDictionary.addElementInHash(word.length(), 1);
            }
        }

        //Returning the final dictionary created with the words of the current fragment
        return myDictionary;
    }
}