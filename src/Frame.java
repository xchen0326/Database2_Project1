import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Frame {
    private String content;
    private boolean dirty;
    private boolean pinned;
    private int blockId;

    public String getRecord(int recId){
        String record = "";
        if(recId>0&&recId<=100){
            for (int
                 i = (recId-1)*40; i < (recId-1)*40+40; i++){
                record += content.charAt(i);
            }
        }
        return record;
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

    public static void main(String[] args) throws IOException {


    }
}
