import java.io.*;

public class BufferPool {
    private Frame[] buffers;

    public int searchBlock(int fileId){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i].getBlockId()==fileId){
                return i;
            }
        }
        return -1;
    }

    public void initialize(int input){
        buffers = new Frame[input];
        for (int i = 0; i < buffers.length; i++){
            buffers[i] = new Frame();
            buffers[i].initialize();
        }
    }

    public String getContentIfIn(int fileId){
        return buffers[searchBlock(fileId)].getContent();
    }

    public String contentFromDisk(int fileId) throws IOException {
        String parentDir = System.getProperty("user.dir");
        String currentDir = parentDir+"/F"+fileId+".txt";

        File newfile = new File(currentDir);
        BufferedReader reader = new BufferedReader(new FileReader(newfile));
        String result = reader.readLine();
        return result;
    }

    public void setContentIfNotIn(int fileId, int emptyId) throws IOException {
        String result = contentFromDisk(fileId);
        buffers[emptyId].setContent(result);
        buffers[emptyId].setBlockId(fileId);
    }

    public int emptyFrame(){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i].getContent().equals("")){
                return i;
            }
        }
        return -1;
    }

    public int eligibleTakeOut(){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i].isPinned()==false){
                return i;
            }
        }
        return -1;
    }

    public Frame getBuffer(int fileId){
        if (searchBlock(fileId)!=-1) {
            int i = searchBlock(fileId);
            return buffers[i];
        }
        return null;
    }

//    public static void main(String[] args) throws IOException {
////        BufferPool bufferPool = new BufferPool();
////        System.out.println(bufferPool.getContentIfNotIn(3));
//        System.out.println((99-1)/100+1);
//    }
}
