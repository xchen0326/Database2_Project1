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
            buffers[i].initialize();
        }
    }

    public String getContentIfIn(int fileId){
        return buffers[searchBlock(fileId)].getContent();
    }

    public String getContentIfNotIn(int fileId){
        return "";
    }
}
