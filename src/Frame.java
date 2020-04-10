
public class Frame {
    private String content;
    private boolean dirty;
    private boolean pinned;
    private int blockId;

    public String getRecord(String content, int recId){
       return Execution.getRecord(content, recId);
    }

    /**
     * update a specific record
     * @param recId
     * @param newRec
     */
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

    /**
     * Initialize the frame object
     */
    public void initialize(){
        this.content="";
        this.dirty=false;
        this.pinned=false;
        this.blockId=-1;
    }

    /**
     * gets the full content from a frame
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * set the content to a new one
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * checks if the frame has already been changed
     * @return
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * sets the dirty value
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * checks if a frame allows update on it
     * @return
     */
    public boolean isPinned() {
        return pinned;
    }

    /**
     * sets pin to new value
     * @param pinned
     */
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    /**
     * gets the block id
     * @return
     */
    public int getBlockId() {
        return blockId;
    }

    /**
     * sets the block id
     * @param blockId
     */
    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }


//    public static void main(String[] args) throws IOException {
//        Integer [] a = {1,2,3};
//        List<Integer> lst = Arrays.stream(a).collect(Collectors.toList());
//        System.out.println(lst);
//    }
}
