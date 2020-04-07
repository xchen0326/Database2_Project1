import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Execution {

    public static String getRecord(String content, int recId){
        String record = "";
        if(recId>0&&recId<=100){
            for (int i = (recId-1)*40; i < (recId-1)*40+40; i++){
                record += content.charAt(i);
            }
        }
        return record;
    }

    public static void main(String[] args) throws IOException {
        BufferPool bufferPool = new BufferPool();
        bufferPool.initialize(Integer.valueOf(args[0]));
        System.out.println("The program is ready for the next command.");

        Scanner reader = new Scanner(System.in);
        while (reader.hasNext()){
            String command = reader.nextLine();
            if (command.substring(0, 3).equals("Get")){
                int k = Integer.valueOf(command.substring(4));
                int fileId = (k-1)/100+1;
                int recId = k-(k-1)/100*100;
                //case 1
                if (bufferPool.searchBlock(fileId)!=-1){
                    System.out.println(bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId));
                }
                //case 2
                else if (bufferPool.searchBlock(fileId)==-1 && bufferPool.emptyFrame()!=-1){
                    bufferPool.setContentIfNotIn(fileId, bufferPool.emptyFrame());
                    String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                    System.out.println(record);
                }

                else if (bufferPool.searchBlock(fileId)==-1 && bufferPool.emptyFrame()==-1){
                    //case 3
                    if (bufferPool.eligibleTakeOut()!=-1){
                        if (bufferPool.getBuffer(bufferPool.eligibleTakeOut()).isDirty()==false){
                            String overwriteStr = getRecord(bufferPool.contentFromDisk(fileId), recId);
                            bufferPool.getBuffer(bufferPool.eligibleTakeOut()).updateRecord(recId, overwriteStr);
                            String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                            System.out.println(record);
                        }
                        else {
                            try {
                                String parentDir = System.getProperty("user.dir");
                                String currentDir = parentDir+"/F"+fileId+".txt";
                                PrintStream stream=null;
                                stream=new PrintStream(currentDir);
                                stream.print(bufferPool.getBuffer(bufferPool.eligibleTakeOut()).getContent());
                            } catch (IOException e) {}
                            String overwriteStr = getRecord(bufferPool.contentFromDisk(fileId), recId);
                            bufferPool.getBuffer(bufferPool.eligibleTakeOut()).updateRecord(recId, overwriteStr);
                            String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                            System.out.println(record);
                        }
                    }
                    //case 4
                    else System.out.println("The corresponding block "+k+" cannot be accessed from disk " +
                            "because the memory buffers are full");
                }
            }
        }
    }
}
