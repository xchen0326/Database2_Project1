import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
//        Integer [] a = {1,2,3};
//        List<Integer> lst = Arrays.stream(a).collect(Collectors.toList());
//        System.out.println(lst);
//    }
}
