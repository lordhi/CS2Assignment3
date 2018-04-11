import java.lang.Runnable;
import java.lang.Thread;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TestSearches
	implements Runnable
{
	Thread runner;
	HashTable ht;
	String[] arr;
	int min;
	int max;

	public TestSearches(int min, int max, String[] arr, HashTable ht)
	{
		this.min = min;
		this.max = max;
		this.arr = arr;
		this.ht = ht;
	}

	public void run()
	{
		for(int i=min; i<max; i++)
			testAndWrite(i, arr, ht);
	}


	public static void testAndWrite(int f, String[] arr, HashTable ht)
	{
                int n =30000000;

                long times[] = new long[n];
                long t1, t2;

                int j=1, p=(int)(n/100);

                System.gc();

                System.out.println(f);


                for (int i=0; i<n; i++)
                {        	
                	t1 = System.nanoTime();
                	ht.get(arr[i]);
                	t2 = System.nanoTime();

                	times[i] = t2-t1;
                	/*if(i%p == 0)
                	{
                		System.out.println(f + "-" + j + "%");
                		j++;
                	}*/
                }

                try
                {
                	BufferedWriter wr = new BufferedWriter(new FileWriter("./Report/Data/All/" + f + ".csv"));
                	for(long l : times)
                	{
                		wr.write(l + "\r\n");
                	}
                	wr.close();
                }catch(Exception e){
                	System.err.println("Error during writing of data.");
                	System.exit(0);
                }
	}
}