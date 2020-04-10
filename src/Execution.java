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
            //if the command is Get
            if (command.substring(0, 3).equals("Get")){
                int k = Integer.valueOf(command.substring(4));
                int fileId = (k-1)/100+1;//generate the file id from the input
                int recId = k-(k-1)/100*100;//generate the record id from the input
                //case 1
                if (bufferPool.searchBlock(fileId)!=-1){
                    int i = bufferPool.searchBlock(fileId)+1;
                    System.out.println(bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId));
                    System.out.println("This block is already in memory. " + "File "+fileId+" already in memory. "+
                            "Located in frame #"+i+".");
                }
                //case 2
                else if (bufferPool.searchBlock(fileId)==-1 && bufferPool.emptyFrame()!=-1){
                    bufferPool.setContentIfNotIn(fileId, bufferPool.emptyFrame());
                    int i = bufferPool.searchBlock(fileId)+1;
                    String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                    System.out.println(record);
                    System.out.println("This block is brought from disk. " + "Brought file "+fileId+" from disk. "+
                            "Located in frame #"+i+".");
                }

                else if (bufferPool.searchBlock(fileId)==-1 && bufferPool.emptyFrame()==-1){
                    //case 3
                    if (bufferPool.eligibleTakeOut()!=-1){
                        int oldblock = bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getBlockId();
                        //if the dirty value is false, then there is no need to write back to disk
                        if (bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).isDirty()==false){
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                            int i = bufferPool.searchBlock(fileId)+1;
                            System.out.println(record);
                            System.out.println("This block is already in memory. " + "Brought file "+fileId+" from disk. "+
                                    "Located in frame #"+i+"."+" The old file "+oldblock+" is overwritten.");
                        }
                        else {
                            //need to write back to disk
                            try {
                                String parentDir = System.getProperty("user.dir");
                                String currentDir = parentDir+"/F"+fileId+".txt";
                                PrintStream stream=null;
                                stream=new PrintStream(currentDir);
                                stream.print(bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getContent());
                            } catch (IOException e) {}
                            //after writing back to disk, reads the record
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            //don't forget to set the new block id after overwriting the buffer
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            String record = bufferPool.getBuffer(fileId).getRecord(bufferPool.getBuffer(fileId).getContent(), recId);
                            int i = bufferPool.searchBlock(fileId)+1;
                            System.out.println(record);
                            System.out.println("This block is brought from memory. " + "Brought file "+fileId+" from disk. "+
                                    "Located in frame #"+i+"."+" The old file "+oldblock+" is overwritten.");
                        }
                    }
                    //case 4
                    else System.out.println("The corresponding block "+fileId
                            +" cannot be accessed from disk " +
                            "because the memory buffers are full");
                }
            }

            //if the command is Set
            if (command.substring(0, 3).equals("Set")) {
                int temp = command.length()-45;
                int k = Integer.valueOf(command.substring(4, 4+temp));
                String setStr = command.substring(5+temp);
                int fileId = (k - 1) / 100 + 1;
                int recId = k - (k - 1) / 100 * 100;
                //case 1
                if (bufferPool.searchBlock(fileId) != -1 && bufferPool.getBuffer(fileId).isPinned()==false) {
                    bufferPool.getBuffer(fileId).updateRecord(recId, setStr);
                    int i = bufferPool.searchBlock(fileId)+1;
                    System.out.println("The write is successful. Brought file "+fileId+" from memory.");
                    System.out.println("Placed in frame #"+i);
                }
                //case 2
                else if (bufferPool.emptyFrame() != -1) {
                    bufferPool.setContentIfNotIn(fileId, bufferPool.emptyFrame());
                    bufferPool.getBuffer(fileId).updateRecord(recId, setStr);
                    int i = bufferPool.searchBlock(fileId)+1;
                    System.out.println("The write is successful. File " + fileId +
                            " is brought from disk.");
                    System.out.println("Placed in frame #"+i);
                //    System.out.println("That is it");
                } else if (bufferPool.emptyFrame() == -1) {
                    //case 3
                    if (bufferPool.eligibleTakeOut() != -1) {
                        int oldblock = bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getBlockId();
                        //if there is no need to write back to disk
                        if (bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).isDirty() == false) {
                            int i = bufferPool.eligibleTakeOut()+1;
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            //overwrite the content and set the new block id
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).updateRecord(recId, setStr);
                            System.out.println("The write is successful. Brought file "+fileId+" from disk.");
                            System.out.println("Placed in frame #"+i+" The old file "+oldblock+" is overwritten.");
                          //  System.out.println("this is it");
                        }
                        else {
                            //need to write back to disk
                            try {
                                String parentDir = System.getProperty("user.dir");
                                String currentDir = parentDir + "/F" + fileId + ".txt";
                                PrintStream stream = null;
                                stream = new PrintStream(currentDir);
                                stream.print(bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getContent());
                            } catch (IOException e) {
                            }
                            int i = bufferPool.eligibleTakeOut()+1;
                            //overwrite the new content and set the new block id
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).updateRecord(recId, setStr);
                            System.out.println("The write is successful. Brought file " +fileId+" from disk.");
                            System.out.println("Placed in frame #"+i+" The old file "+oldblock+" is overwritten.");
                           // System.out.println("This is it");
                        }
                    }
                    //the memory is full and does not allow further overwrites
                    else System.out.println("The write is not successful because the memory is full.");
                }
            }

            //if the command is Pin
            if (command.substring(0, 3).equals("Pin")){
                //checks if the memory is full
                if (bufferPool.ifPinMemoryFull()){
                    System.out.println("Cannot pin because memory is full.");
                }
                int fileId = Integer.valueOf(command.substring(4));
                //case 1
                if (bufferPool.searchBlock(fileId)!=-1){
                    if (bufferPool.getBuffer(fileId).isPinned()==true){
                        int i = bufferPool.searchBlock(fileId)+1;
                        System.out.println("Frame #"+i+" from file "+fileId+" is already pinned true.");
                    }
                    bufferPool.getBuffer(fileId).setPinned(true);
                    int i = bufferPool.searchBlock(fileId)+1;
                    if (bufferPool.getBuffer(fileId).isPinned()==true) {
                        System.out.println("Frame #" + i + " from file " + fileId + " is pinned.");
                    }
                }
                //case 2
                if (bufferPool.searchBlock(fileId)==-1){
                    if (bufferPool.emptyFrame()!=-1) {
                        int emptyId = bufferPool.emptyFrame();
                        //if the file is not in memory, put it into memory
                        bufferPool.setContentIfNotIn(fileId, bufferPool.emptyFrame());
                        bufferPool.getBuffer(fileId).setPinned(true);
                        int i = bufferPool.searchBlock(fileId)+1;
                        System.out.println("Frame #"+i+" from file "+fileId+" is not already pinned true.");
                        System.out.println("Frame #"+i+" from file "+fileId+" is pinned.");
                    }
                    if (bufferPool.emptyFrame()==-1 && bufferPool.eligibleTakeOut()!=-1){
                        int oldblock = bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getBlockId();
                        //if all memory occupies but some is able to be taken out
                        if (bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).isDirty()==false){
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            //set the new content and reset the new block id
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            int i = bufferPool.eligibleTakeOut()+1;
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setPinned(true);
                            System.out.println("This block is not already pinned true.");
                            System.out.println("Frame #"+i+" from file "+fileId+" is pinned."+" The old file "+oldblock+" is overwritten.");
                        }
                        else {
                            //there is a need to write back
                            try {
                                //write back to disk
                                String parentDir = System.getProperty("user.dir");
                                String currentDir = parentDir+"/F"+fileId+".txt";
                                PrintStream stream=null;
                                stream=new PrintStream(currentDir);
                                stream.print(bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).getContent());
                            } catch (IOException e) {}
                            int i = bufferPool.eligibleTakeOut()+1;
                            String overwriteStr = bufferPool.contentFromDisk(fileId);
                            //overwrite the buffer and set the new block id
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setContent(overwriteStr);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setBlockId(fileId);
                            bufferPool.nthBuffer(bufferPool.eligibleTakeOut()).setPinned(true);
                            System.out.println("This block is not already pinned true.");
                            System.out.println("Frame #"+i+" from file "+fileId+" is pinned."+" The old file "+oldblock+" is overwritten.");
                        }
                    }
                }
            }

            //if the command is Unpin
            if (command.substring(0, 5).equals("Unpin")){
                int fileId = Integer.valueOf(command.substring(6));
                if (bufferPool.searchBlock(fileId)!=-1) {
                    if (bufferPool.getBuffer(fileId).isPinned()==false){
                        System.out.println("This block is already unpinned.");
                    }
                    int i = bufferPool.searchBlock(fileId)+1;
                    bufferPool.getBuffer(fileId).setPinned(false);
                    System.out.println("Frame #"+i+" from file "+fileId+" is unpinned.");
                }
                //if the the target is not in memory
                else System.out.println("The corresponding block "+ fileId + " cannot be unpinned because it is not in memory");
            }
        }
    }
}
