/*
 * jobNode Object that will help with implementation of scheduling algorithms
 */
public class jobNode {
    // jobId will show which job this is
    //  Helps with functions like where to print "#"
    private int jobId;
    // remainingTime shows how much time is left to complete job
    //  Needed for scheduling algorithms
    private int remainingTime;
    
    // returns jobId
    public int getJobId(){
        return jobId;
    }

    // returns remainingTime
    public int getTime(){
        return remainingTime;
    }

    // decrementsTime, which helps keep track of remaining time on job
    public void decrTime(){
        remainingTime--;
    }

    // Constructor that helps create the jobNode
    //  There is no instance where an Object will be created without parameters
    public jobNode(int id, int time){
        this.jobId = id;
        this.remainingTime = time;
    }
}
