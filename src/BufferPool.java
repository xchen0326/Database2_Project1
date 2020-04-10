import java.io.*;

public class BufferPool {
    private Frame[] buffers;

    /**
     * search for the slot number with the file id provided
     * @param fileId
     * @return
     */
    public int searchBlock(int fileId){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i].getBlockId()==fileId){
                return i;
            }
        }
        return -1;
    }

    /**
     * gets the frame object provided with the slot number
     * @param i
     * @return
     */
    public Frame nthBuffer(int i){
        return buffers[i];
    }

    /**
     * initialize the buffer pool
     * @param input
     */
    public void initialize(int input){
        buffers = new Frame[input];
        for (int i = 0; i < buffers.length; i++){
            buffers[i] = new Frame();
            buffers[i].initialize();
        }
    }

    /**
     * gets the full content from a file in disk
     * @param fileId
     * @return
     * @throws IOException
     */
    public String contentFromDisk(int fileId) throws IOException {
        String parentDir = System.getProperty("user.dir");
        String currentDir = parentDir+"/F"+fileId+".txt";

        File newfile = new File(currentDir);
        BufferedReader reader = new BufferedReader(new FileReader(newfile));
        String result = reader.readLine();
        return result;
    }

    /**
     * if a block is not in memory, put it into the memory
     * @param fileId
     * @param emptyId
     * @throws IOException
     */
    public void setContentIfNotIn(int fileId, int emptyId) throws IOException {
        String result = contentFromDisk(fileId);
        buffers[emptyId].setContent(result);
        buffers[emptyId].setBlockId(fileId);
    }

    /**
     * gets the empty block
     * @return
     */
    public int emptyFrame(){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i].getContent().equals("")){
                return i;
            }
        }
        return -1;
    }

    /**
     * checks if a block is eligible for taking out
     * @return
     */
    public int eligibleTakeOut(){
        for (int i = 0; i < buffers.length; i++){
            if (buffers[i]!=null && buffers[i].isPinned()==false){
                return i;
            }
        }
        return -1;
    }

    /**
     * gets the block provided with file id
     * @param fileId
     * @return
     */
    public Frame getBuffer(int fileId){
        if (searchBlock(fileId)!=-1) {
            int i = searchBlock(fileId);
            return buffers[i];
        }
        return null;
    }

    /**
     * checks if all buffers in the pool are pinned
     * @return
     */
    public boolean ifPinMemoryFull(){
        boolean flag = true;
        for (int i = 0; i < buffers.length; i++){
            if (!buffers[i].isPinned()){
                flag = false;
            }
        }
        return flag;
    }

//    public static void main(String[] args) throws IOException {
////        BufferPool bufferPool = new BufferPool();
////        System.out.println(bufferPool.getContentIfNotIn(3));
//        System.out.println((99-1)/100+1);
//    }
}
