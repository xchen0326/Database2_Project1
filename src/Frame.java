import java.io.*;

public class Frame {
    private String content;
    private boolean dirty;
    private boolean pinned;
    private int blockId;

    public String getRecord(String content, int recId){
       return Execution.getRecord(content, recId);
    }

    public void updateRecord(int recId, String newRec){
       String temp = "";
       if(recId==1){
           temp += newRec;
           temp += content.substring(40);
       }
       else {
           temp += content.substring(0, (recId-1)*40);
           temp += newRec;
           temp += content.substring((recId-1)*40+40);
       }
       setContent(temp);
       setDirty(true);
    }

    public void initialize(){
        this.content="";
        this.dirty=false;
        this.pinned=false;
        this.blockId=-1;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }


//    public static void main(String[] args) throws IOException {
//        BufferPool bufferPool = new BufferPool();
//        try {
//            String parentDir = System.getProperty("user.dir");
//            String currentDir = parentDir+"/F"+1+".txt";
////            BufferedWriter out = new BufferedWriter(new FileWriter(currentDir, true));
////            out.write("Hello");
////            out.close();
//
//            PrintStream stream=null;
//            stream=new PrintStream(currentDir);//写入的文件path
//            stream.print("Hello");//写入的字符串
//        } catch (IOException e) {}
//    }
}
