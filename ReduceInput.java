import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

//Class used to simplify the input for the Reduce tasks. It contains the document name and a list with the dictionaries
//of this document, created by the Map tasks
public class ReduceInput {
    public String docName;
    public List<Future<MyDictionary>> resultList;

    //Constructors
    public ReduceInput() {
        docName = "";
        resultList = new ArrayList<>();
    }

    public ReduceInput(String docName) {
        this.docName = docName;
    }
}
