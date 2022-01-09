import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Tema2 {
    //Defining the delimiters used to split the words
    public static String delimiters = new String(";:/?~\\.,><`[]{}()!@#$%^&-_+'=*\"| \t\r\n");

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        //In case there are less than 3 arguments given the program is stopped and an exception is thrown
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }

        //Parsing arguments
        int threads_nr = Integer.parseInt(args[0]);
        BufferedReader input_file = new BufferedReader(new FileReader(args[1]));
        try {
            File myObj = new File(args[2]);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.err.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
        FileWriter myWriter = new FileWriter(args[2]);

        //Initialising other variables needed for running
        BufferedReader docs_reader;
        ArrayList<String> doc_names = new ArrayList<>();
        ExecutorService executorService_forMap = Executors.newFixedThreadPool(threads_nr);
        ExecutorService executorService_forReduce = Executors.newFixedThreadPool(threads_nr);
        ArrayList<ReduceInput> reduceInputs = new ArrayList<>();
        int fragment_dimension = Integer.parseInt(input_file.readLine());
        int doc_nr = Integer.parseInt(input_file.readLine());

        //Reading the input files and generating Map tasks for workers to do
        for(int i = 0 ; i < doc_nr ; i ++){
            StringBuilder file_content = new StringBuilder();
            String s;

            doc_names.add(input_file.readLine());
            int length = doc_names.get(i).split("/").length;
            reduceInputs.add(new ReduceInput(doc_names.get(i).split("/")[length - 1]));
            docs_reader = new BufferedReader(new FileReader("../" + doc_names.get(i)));

            //Copying in @file_content the content of the files that have been read
            while((s = docs_reader.readLine()) != null){
                if(file_content.length() != 0){
                    file_content.append(System.getProperty("line.separator"));
                }
                file_content.append(s);
            }

            int start = 0;
            int end = fragment_dimension - 1;
            List<Future<MyDictionary>> resultList = new ArrayList<>();

            //Creating file_content.length() / fragment_dimension number of Map tasks and saving their results
            while (start < file_content.length()) {
                Future<MyDictionary> helper =  executorService_forMap.submit(new Map(start, end,
                        file_content.toString()));
                resultList.add(helper);

                start += fragment_dimension;
                end += fragment_dimension;
            }

            reduceInputs.get(i).resultList = resultList;
        }

        //Closing the executor service
        executorService_forMap.awaitTermination(1, TimeUnit.MILLISECONDS);
        executorService_forMap.shutdown();

        //Creating Reduce tasks for workers and saving their results in a list (with the type Future)
        List<Future<Output>> futureOutputs = new ArrayList<>();
        for(int i = 0 ; i < doc_nr ; i ++){
            futureOutputs.add(executorService_forReduce.submit(new Reduce(reduceInputs.get(i))));
        }

        //Creating a List of the final outputs which uses method "get" to obtain the results from the Future type
        List<Output> outputs = new ArrayList<>();
        for(int i = 0 ; i < doc_nr ; i ++){
            try {
                outputs.add(futureOutputs.get(i).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //Sorting the outputs as asked
        Collections.sort(outputs);

        //Printing final results in the output file
        for(int i = 0 ; i < doc_nr ; i ++) {
            String rangString = String.format("%.2f", outputs.get(i).rang);
            myWriter.write(outputs.get(i).docName + "," + rangString + "," + outputs.get(i).maxLen +
                    "," + outputs.get(i).nrOfWordsWithMaxLen + "\n");
        }

        //Closing the executorService and writer
        executorService_forReduce.awaitTermination(1, TimeUnit.MILLISECONDS);
        executorService_forReduce.shutdown();
        myWriter.close();
    }

}