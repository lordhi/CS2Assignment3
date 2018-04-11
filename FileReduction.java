import java.lang.Runnable;
import java.lang.Thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class FileReduction
	implements Runnable
{
	Thread runner;

        int notCounted;
        int count[];
        int n;

        long sum[];
        String readPath;
        String writePath;

        int min, max;

	public FileReduction(int n, int min, int max, String readPath, String writePath)
	{
		this.min = min;
		this.max = max;
                this.n = n;
                this.readPath = readPath;
                this.writePath = writePath;

                count = new int[n];
                sum = new long[n];
                notCounted = 0;

                for(int i=0; i<n; i++)
                {
                        count[i] = 0;
                        sum[i] = 0;
                }
	}

	public void run()
	{
		for(int i=min; i<max; i++)
                {
                        readFile(i);
                }
                writeFile();
        }

        public void writeFile()
        {
                long[] avg = calculateAverage();
                try
                {
                        BufferedWriter wr = new BufferedWriter(new FileWriter(writePath));
                        for (int i=0; i<n; i++)
                                wr.write(avg[i] + "\r\n");
                        wr.close();

                        System.out.println(writePath);
                }catch(Exception e){
                        System.err.println("Error during writing of data.");
                        System.exit(0);
                }
        }

        public long[] calculateAverage()
        {
                long ans[] = new long[n];
                for(int i=0; i<count.length; i++)
                {
                        try
                        {
                                ans[i] = (long) (sum[i]/count[i]);
                        }catch(Exception e){
                                ans[i] = 0;
                                System.err.println("Divide by zero");
                        }
                }
                return ans;
        }

	public void readFile(int n)
	{
                try
                {
                	BufferedReader br = new BufferedReader(new FileReader(readPath + n + ".csv"));
                        int i=0;
                        String s;
                	while((s=br.readLine()) != null)
                        {
                                long t = Long.parseLong(s);
                                if(t < 10000000){
                                        count[i] += 1;
                                        sum[i] += t;
                                }else{
                                        notCounted++;
                                }

                                i++;
                        }
                	br.close();
                        System.out.println(notCounted);
                }catch(Exception e){
                	System.err.println("Error during reading of data.");
                        System.err.println(e.getMessage().substring(0,50));
                	System.exit(0);
                }
	}
}