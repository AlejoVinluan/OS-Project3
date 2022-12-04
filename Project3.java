import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Project3 {

    /*
     * jobsArr - This variable is used to store all of the job that is passed in via command line.
     *  Each job is stored as [jobNumber, remainingTime] where jobNumber is the order of the job
     *  and remainingTime is the time remaining to complete the job.
     * 
     * numJobs - This variable is used to store the amount of jobs passed in from the command line.
     *  It is used so to prevent out of bounds exceptions and build the initial jobs list.
     */
    public static int[][] jobsArr;
    public static int numJobs;
    
    /*
     * readJobs is used to read the text file that is passed in from command line.
     */
    public static void readJobs(String jobsFile){
        /*
         * jobList is used to store the list of jobs being read in within the function.
         *  An ArrayList is utilized since the initial length of the file is not known.
        */
        ArrayList<int[]> jobList = new ArrayList<int[]>();

        /*
         * try/catch block is used to validate that the file exists and the file name was spelled properly.
         * This will also store each job read in within the jobList ArrayList.
         */
        try{
            File file = new File(jobsFile);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                // Regex will split each line of the job by the tab delimeter
                String[] job = scanner.nextLine().split("\t");
                // jobStartAndDuration will format each job as [jobId, remainingTime]
                int[] jobStartAndDuration = new int[]{Integer.parseInt(job[1]), Integer.parseInt(job[2])};
                jobList.add(jobStartAndDuration);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("Could not find file " + jobsFile);
            e.printStackTrace();
            System.exit(1);
        }

        numJobs = jobList.size();

        // This covers the edge case in the instance there are 0 jobs.
        if(numJobs < 1){
            System.exit(0);
        }

        // This initializes the jobsArr since we know the number of jobs at this point.
        jobsArr = new int[numJobs][2];
        // Copies the jobsList into the jobsArr global variable
        for(int i = 0; i < numJobs; i++){
            jobsArr[i] = jobList.get(i);
        }
    }

    /*
     * Round Robin - This function will implement the Round Robin scheduling algorithm with a quantum of 1.
     *  It will rotate through each job, doing 1 unit of work on each, then placing it at the end of the queue.
     */
    public static void rr(int[][] jobs){
        System.out.println("=====Round Robin=====");
        // This will print the letters for each job below. If there are 5 jobs, it prints "ABCDE".
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }

        // currId will be used to point at which job we are currently considering.
        int currId = 0;
        // jobList creates the current "queue" that Round Robin will utilize.
        ArrayList<jobNode> jobList = new ArrayList<jobNode>();
        // Add the first job into the queue.
        jobList.add(new jobNode(currId, jobsArr[currId][1]));
        // Increment currentId since we are no longer consideringt he current job
        currId++;
        
        // Time will be incremented after each unit of work done. This is used so that
        //  arrival time of other jobs can be considered. 
        int time = 0;

        // While jobs exist within the jobList
        while(jobList.size() > 0){
            // This will take the next job in the queue
            jobNode nextNode = jobList.get(0);
            System.out.println();
            // Print the number of spaces required to show which job is being done.
            //  This is found by utilizing the jobId stored within jobNode.
            //  This jobId is also stored as jobsArr[currId][0]
            for(int i = 0; i < nextNode.getJobId(); i++){
                System.out.print(" ");
            }
            // Print the "#" that displays which job is being worked on
            System.out.print("#");
            // Decrements the remaining time needed for the job.
            nextNode.decrTime();
            // Remove the node from the current "queue"
            jobList.remove(nextNode);
            // Increment time to show that 1 unit of work was completed.
            time++;
            // currId < numJobs ensures that we don't go out of bounds within jobsArr
            // time >= jobsArr[currId][0] is used so that we can add the next job the queue when it's arrival time has reached
            if(currId < numJobs && time >= jobsArr[currId][0]){
                // Create new job and add into the queue
                jobList.add(new jobNode(currId, jobsArr[currId][1]));
                // Increment the current ID of the jobs we are considering from jobsArr
                currId++;
            }
            // Add the job we just worked on back to the queue IF there is still remaining work on that job.
            // The order of this if statement is important so that a new arriving job will be scheduled before this returning job.
            if(nextNode.getTime() > 0){
                jobList.add(nextNode);
            }
        }
        System.out.println("\n");
    }
    
    /*
     * There will be no notes for SPN. I accidentally wrote this instead of SRT and don't want to delete it.
     */
    public static void spn(int[][] jobs){
        System.out.println("=Shortest Process Next=");
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }
        int currId = 0;
        ArrayList<jobNode> jobList = new ArrayList<jobNode>();
        jobList.add(new jobNode(currId,jobsArr[currId][1]));
        currId++;

        int time = 0;
        int jobsArrPtr = 0;

        while(jobList.size() > 0){
            // Find job with lowest time
            jobNode currLowestNode = jobList.get(0);
            int currLowestTime = currLowestNode.getTime();
            for(int currJobIdx = 1; currJobIdx < jobList.size(); currJobIdx++){
                if(jobList.get(currJobIdx).getTime() < currLowestTime){
                    currLowestTime = jobList.get(currJobIdx).getTime();
                    currLowestNode = jobList.get(currJobIdx);
                }
            }
            
            // Found job with shortest remaining time. Now print while increment time
            for(int i = 0; i < currLowestTime; i++){
                System.out.println();
                for(int j = 0; j < currLowestNode.getJobId(); j++){
                    System.out.print(" ");
                }
                System.out.print("#");
                time++;
                if(jobsArrPtr < numJobs && currId < numJobs && time >= jobsArr[jobsArrPtr][0]){
                    jobList.add(new jobNode(currId,jobsArr[currId][1]));
                    jobsArrPtr++;
                    currId++;
                }
            }
            jobList.remove(currLowestNode);
        }
        System.out.println("\n");
    }

    /*
     * Shortest Remaining Time - This function will implement Shortest Remaining Time scheduling algorithm with a quantum of 1.
     *  It will scan the queue and find the job with the shortest remaining time. It will then work on that job.
     *  If a new job arrives at the same time, it will be inserted into the queue. Then if there is remaining work on the job,
     *  then it will also be returned to the queue.
     */
    public static void srt(int[][] jobs){
        System.out.println("=Shortest Remanining Time=");
        // This will print the letters for each job below. If there are 5 jobs, it prints "ABCDE".
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }

        // currId will be used to point at which job we are currently considering.
        int currId = 0;
        // jobList creates the current "queue" that Round Robin will utilize.
        ArrayList<jobNode> jobList = new ArrayList<jobNode>();
        // Add the first job into the queue.
        jobList.add(new jobNode(currId, jobsArr[currId][1]));
        // Increment currentId since we are no longer consideringt he current job
        currId++;
        
        // Time will be incremented after each unit of work done. This is used so that
        //  arrival time of other jobs can be considered. 
        int time = 0;

        // While jobs exist within the jobList
        while(jobList.size() > 0){
            // We find the next node by utilizing findMinTime and passing in the jobList.
            jobNode shortestRemainingNode = findMinTime(jobList);
            System.out.println();
            // Print the number of spaces required to show which job is being done.
            //  This is found by utilizing the jobId stored within jobNode.
            //  This jobId is also stored as jobsArr[currId][0]
            for(int i = 0; i < shortestRemainingNode.getJobId(); i++){
                System.out.print(" ");
            }
             // Print the "#" that displays which job is being worked on
            System.out.print("#");
            // Decrements the remaining time needed for the job.
            shortestRemainingNode.decrTime();
            // Remove the job that was just worked on from the queue.
            jobList.remove(shortestRemainingNode);
            // Increment time to show that 1 unit of work was completed.
            time++;
            // currId < numJobs ensures that we don't go out of bounds within jobsArr
            // time >= jobsArr[currId][0] is used so that we can add the next job the queue when it's arrival time has reached
            if(currId < numJobs && time >= jobsArr[currId][0]){
                // Create new job and add into the queue
                jobList.add(new jobNode(currId, jobsArr[currId][1]));
                // Increment the current Id that is being considered.
                currId++;
            }
            // Add the job we just worked on back to the queue IF there is still remaining work on that job.
            // The order of this if statement is important so that a new arriving job will be scheduled before this returning job.
            if(shortestRemainingNode.getTime() > 0){
                jobList.add(shortestRemainingNode);
            }
        }
        System.out.println("\n");
    }

    /*
     * Feedback - This function will implement the Feedback scheduling algorithm with 3 queues. It will put each job in seperate
     *  priority queues. A job will initially enter the zeroQ. After work is done on that job, it is added to the oneQ. Finally,
     *  twoQ is used for jobs that need more than 2 units of work. Within twoQ, round robin is used.
     */
    public static void fb(int[][] jobs){
        System.out.println("=======Feedback=======");
        // This will print the letters for each job below. If there are 5 jobs, it prints "ABCDE".
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }

        /*
         * Initializes each of the 3 priorty queues needed for feedback algorithm.
         */
        ArrayList<jobNode> zeroQ = new ArrayList<jobNode>();
        ArrayList<jobNode> oneQ = new ArrayList<jobNode>();
        ArrayList<jobNode> twoQ = new ArrayList<jobNode>();

        // completedJobs is used to keep track of how many jobs have been completed.
        int completedJobs = 0;
        // time will be used to keep track of arrival time for all jobs.
        int time = 0;
        // currId will be used to consider which job in jobsArr needs to be added.
        int currId = 0;

        // Insert the first job from jobsArr.
        //  It is inserted as a jobNode where jobId is the order it was inserted, and the remainingTime of job
        zeroQ.add(new jobNode(currId,jobsArr[currId][1]));

        // While all jobs have not yet been completed
        while(completedJobs < numJobs){
            System.out.println();
            // First consider the zeroQ. If there is nothing in the high priority queue, move on to the medium priorty queue.
            if(zeroQ.size() > 0){
                // Take the next job within the high priorty queue
                jobNode nextJob = zeroQ.get(0);
                // Remove the job from the high priorty queue.
                zeroQ.remove(0);
                // Print the number of spaces required to show which job is being done.
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                // Print the "#" that displays which job is being worked on
                System.out.print("#");
                // Decrements the remaining time needed for the job.
                nextJob.decrTime();
                // If there is still work for the job, add it to the medium priorty queue.
                if(nextJob.getTime() > 0){
                    oneQ.add(nextJob);
                // If there is no more work for the job, increment completed jobs.
                } else {
                    completedJobs++;
                }
                // Increment time
                time++;
                // currId < numJobs ensures that we don't go out of bounds within jobsArr
                // time >= jobsArr[currId][0] is used so that we can add the next job the queue when it's arrival time has reached
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    // Add the next job to the high priorty queue
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    // Increment the current ID of the jobs we are considering from jobsArr
                    currId++;
                }
            }
            // This will only be checked if there is nothing in the high priorty queue. Then, check this medium priorty queue.
            else if(oneQ.size() > 0){
                // Take the next job within the high priorty queue
                jobNode nextJob = oneQ.get(0);
                // Remove the job from the medium priorty queue.
                oneQ.remove(0);
                // Print the number of spaces required to show which job is being done.
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                // Print the "#" that displays which job is being worked on
                System.out.print("#");
                // Decrement the time to show that work was completed
                nextJob.decrTime();
                // If there is still remainingTime on the job, add it to the low priortiy queue
                if(nextJob.getTime() > 0){
                    twoQ.add(nextJob);
                // If there is still time for the job, increment completedJobs++
                } else {
                    completedJobs++;
                }
                // Increment time to show a job was completed
                time++;
                // currId < numJobs ensures that we don't go out of bounds within jobsArr
                // time >= jobsArr[currId][0] is used so that we can add the next job the queue when it's arrival time has reached
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    // If there is a new job available, add it to the high priority queue
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    // Increment currId to show what the next job should be
                    currId++;
                }
            // Finally, if there is nothing in the high or medium priorty queues, check the low priorty queue.
            }else{
                // Get the nextJob from the low priortiy queue
                jobNode nextJob = twoQ.get(0);
                // Remove the job from the queue
                twoQ.remove(0);
                // Print the number of spaces required to show which job is being done.
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                // Print "#" to show that which job is being completed
                System.out.print("#");
                // Decrement time to show that 1 unit of work was completed on job
                nextJob.decrTime();
                // If there is time left on the job, add it back to the queue
                if(nextJob.getTime() > 0){
                    twoQ.add(nextJob);
                // Else, increment completedJobs
                } else {
                    completedJobs++;
                }
                // Increment time to show that we completed 1 unit of work
                time++;
                // currId < numJobs ensures that we don't go out of bounds within jobsArr
                // time >= jobsArr[currId][0] is used so that we can add the next job the queue when it's arrival time has reached
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    // If there is a new job, add it back to zeroQ
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    currId++;
                }
            }
        }
        System.out.println("\n");
    }

    /*
     * findMinTime is used to find the job in jobList with the shortest remaining time.
     *  This will return the jobNode with the shortest remaining time.
     */
    public static jobNode findMinTime(ArrayList<jobNode> jobList){
        // If there is no jobs within jobList, we reurn Null.
        if(jobList.size() < 1){
            return null;
        }
        // This will set the first job in jobList as the minNode.
        jobNode minNode = jobList.get(0);
        // Check the rest of jobList to see if a new minNode can be found.
        for(int i = 1; i < jobList.size(); i++){
            if(minNode.getTime() > jobList.get(i).getTime()){
                // If there is a new minNode, set it as minNode
                minNode = jobList.get(i);
            }
        }
        // Returns the node with the shortest remaining time from jobList.
        return minNode;
    }
    


    public static void main(String[] args){
        // Check to see if there are enough arguments provided within the command line input
        //  Command should be "Project3 jobs.txt ALL" as an example
        if(args.length < 2){
            System.out.println("Incorrect argument count.");
            System.out.println("1st argument should be file name (i.e. jobs.txt)");
            System.out.println("2nd argument should be implementation type (i.e. [RR/SRT/FB/ALL])");
            System.exit(1);
        }

        // Read the jobs and store them in jobsArr
        readJobs(args[0]);

        // Find which command line argument is needed. Run the function
        //  Default will just print error, then exit with Status: 1
        switch(args[1].toLowerCase()){
            case "rr":  rr(jobsArr);
                        break;
            case "srt": srt(jobsArr);
                        break;
            case "fb":  fb(jobsArr);
                        break;
            case "all": rr(jobsArr);
                        srt(jobsArr);
                        fb(jobsArr);
                        break;
            default:    System.out.println("Unable to find implementation.");
                        System.exit(1);
        }
        System.exit(0);
    }
}