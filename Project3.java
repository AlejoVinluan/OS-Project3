import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Project3 {

    public static int[][] jobsArr;
    public static int numJobs;
    
    public static void readJobs(String jobsFile){
        ArrayList<int[]> jobList = new ArrayList<int[]>();
        try{
            File file = new File(jobsFile);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] job = scanner.nextLine().split("\t");
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

        if(numJobs < 1){
            System.exit(0);
        }

        jobsArr = new int[numJobs][2];
        for(int i = 0; i < numJobs; i++){
            jobsArr[i] = jobList.get(i);
        }
    }

    public static void rr(int[][] jobs){
        System.out.println("=====Round Robin=====");
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }
        int currId = 0;
        ArrayList<jobNode> jobList = new ArrayList<jobNode>();
        jobList.add(new jobNode(currId, jobsArr[currId][1]));
        currId++;
        
        int time = 0;

        while(jobList.size() > 0){
            jobNode nextNode = jobList.get(0);
            System.out.println();
            for(int i = 0; i < nextNode.getJobId(); i++){
                System.out.print(" ");
            }
            System.out.print("#");
            nextNode.decrTime();
            jobList.remove(nextNode);
            if(nextNode.getTime() > 0){
                jobList.add(nextNode);
            }
            time++;
            if(currId < numJobs && time >= jobsArr[currId][0]){
                jobList.add(new jobNode(currId, jobsArr[currId][1]));
                currId++;
            }
        }
    }
    
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
    }

    public static void srt(int[][] jobs){
        System.out.println("=Shortest Process Next=");
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }

        int currId = 0;
        ArrayList<jobNode> jobList = new ArrayList<jobNode>();
        jobList.add(new jobNode(currId, jobsArr[currId][1]));
        currId++;

        int time = 0;

        while(jobList.size() > 0){
            jobNode shortestRemainingNode = findMinTime(jobList);
            System.out.println();
            for(int i = 0; i < shortestRemainingNode.getJobId(); i++){
                System.out.print(" ");
            }
            System.out.print("#");
            shortestRemainingNode.decrTime();
            jobList.remove(shortestRemainingNode);
            if(shortestRemainingNode.getTime() > 0){
                jobList.add(shortestRemainingNode);
            }
            time++;
            if(currId < numJobs && time >= jobsArr[currId][0]){
                jobList.add(new jobNode(currId, jobsArr[currId][1]));
                currId++;
            }
        }
    }

    public static void fb(int[][] jobs){
        System.out.println("=======Feedback=======");
        for(int i = 0; i < numJobs; i++){
            System.out.print(String.valueOf((char)(i + 'A')));
        }

        ArrayList<jobNode> zeroQ = new ArrayList<jobNode>();
        ArrayList<jobNode> oneQ = new ArrayList<jobNode>();
        ArrayList<jobNode> twoQ = new ArrayList<jobNode>();

        int completedJobs = 0;
        int time = 0;
        int currId = 0;

        zeroQ.add(new jobNode(currId,jobsArr[currId][1]));

        while(completedJobs < numJobs){
            System.out.println();
            if(zeroQ.size() > 0){
                jobNode nextJob = zeroQ.get(0);
                zeroQ.remove(0);
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                System.out.print("#");
                nextJob.decrTime();
                if(nextJob.getTime() > 0){
                    oneQ.add(nextJob);
                } else {
                    completedJobs++;
                }
                time++;
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    currId++;
                }
            }
            else if(oneQ.size() > 0){
                jobNode nextJob = oneQ.get(0);
                oneQ.remove(0);
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                System.out.print("#");
                nextJob.decrTime();
                if(nextJob.getTime() > 0){
                    oneQ.add(nextJob);
                } else {
                    completedJobs++;
                }
                time++;
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    currId++;
                }
            }
            else{
                jobNode nextJob = twoQ.get(0);
                twoQ.remove(0);
                for(int i = 0; i < nextJob.getJobId(); i++){
                    System.out.print(" ");
                }
                System.out.print("#");
                nextJob.decrTime();
                if(nextJob.getTime() > 0){
                    twoQ.add(nextJob);
                } else {
                    completedJobs++;
                }
                time++;
                if(currId < numJobs && time >= jobsArr[currId][0]){
                    zeroQ.add(new jobNode(currId, jobsArr[currId][1]));
                    currId++;
                }
            }
        }
    }

    public static jobNode findMinTime(ArrayList<jobNode> jobList){
        if(jobList.size() < 1){
            return null;
        }
        jobNode minNode = jobList.get(0);
        for(int i = 1; i < jobList.size(); i++){
            if(minNode.getTime() > jobList.get(i).getTime()){
                minNode = jobList.get(i);
            }
        }
        return minNode;
    }
    


    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Incorrect argument count.");
            System.out.println("1st argument should be file name (i.e. jobs.txt)");
            System.out.println("2nd argument should be implementation type (i.e. [RR/SRT/FB/ALL])");
            System.exit(1);
        }

        readJobs(args[0]);
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