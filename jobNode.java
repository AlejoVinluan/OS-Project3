public class jobNode {
    private int jobId;
    private int remainingTime;
    
    public int getJobId(){
        return jobId;
    }

    public int getTime(){
        return remainingTime;
    }

    public void decrTime(){
        remainingTime--;
    }

    public jobNode(int id, int time){
        this.jobId = id;
        this.remainingTime = time;
    }
}
