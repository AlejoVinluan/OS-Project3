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
        jobsArr = new int[numJobs][2];
        for(int i = 0; i < numJobs; i++){
            jobsArr[i] = jobList.get(i);
        }
    }

    public static void rr(int[][] jobs){
        System.out.println("=====Round Robin=====");
    }
    public static void srt(int[][] jobs){
        System.out.println("=Shortest Remaining Time=");
    }
    public static void fb(int[][] jobs){
        System.out.println("=======Feedback=======");
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