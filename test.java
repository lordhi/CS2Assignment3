import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.lang.Thread;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.esotericsoftware.kryo.io.UnsafeInput;

public class test
{
	static int n = 10000000;
	public static void main(String[] args)
	{
		//hashTableTest();
		//hashTableFromFileTest();
		//hashTable1FromFileTest();
		//hashTableSerialisedOutTest();
		//hashTableSerialisedInTest();
		testAllItems();
		reduceDataToOneFile();
	}

	public static void reduceDataToOneFile()
	{
		n = 30000000;

		Thread thread1 = new Thread(new FileReduction(n, 0, 2500, "./Report/Data/All/", "./Report/Data/All/Partial/1.csv"), "Thread1");
		Thread thread2 = new Thread(new FileReduction(n, 2500, 5000, "./Report/Data/All/", "./Report/Data/All/Partial/2.csv"), "Thread2");
		Thread thread3 = new Thread(new FileReduction(n, 5000, 7500, "./Report/Data/All/", "./Report/Data/All/Partial/3.csv"), "Thread3");
		Thread thread4 = new Thread(new FileReduction(n, 7500, 10000, "./Report/Data/All/", "./Report/Data/All/Partial/4.csv"), "Thread4");

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		while(thread1.isAlive() || thread2.isAlive() || thread3.isAlive() || thread4.isAlive())
		{
			try
			{
				Thread.sleep(10000);
			}catch(Exception e){
				System.err.println("Sleep interuppted!");
			}
		}

		Thread thread = new Thread(new FileReduction(n, 1, 5, "./Report/Data/All/Partial/", "./Report/Data/All.csv"), "Thread1");
		thread.start();
	}

	public static void testAllItems()
	{
		n = 30000000;
		HashTable ht = new HashTable(n);
		String arr[] = new String[n];

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("./data/IDList.csv")));
            String s, k;
            int i = 0, j=1, p = (int)(n/100);
            br.readLine();
            while((s=br.readLine()) != null)
            {
            	k = s.substring(0,13);
                ht.add(k, s.substring(14));
                arr[i] = k;
                i++;
                if(i%p == 0)
                {
                	System.out.print("\033[H\033[2J");  //Clears the screen
    				System.out.flush();
                    System.out.println(j);
                    j++;
                }
            }
            br.close();
        }catch(Exception e){
            System.err.println("Error during reading of data");
            System.err.println(e.getMessage());
            System.exit(0);
        }

        Thread thread1 = new Thread(new TestSearches(0,2500, arr, ht), "Thread1");
        Thread thread2 = new Thread(new TestSearches(2500,5000, arr, ht), "Thread1");
        Thread thread3 = new Thread(new TestSearches(5000,7500, arr, ht), "Thread1");
        Thread thread4 = new Thread(new TestSearches(7500,10000, arr, ht), "Thread1");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        while(thread1.isAlive() || thread2.isAlive() || thread3.isAlive() || thread4.isAlive())
		{
			try
			{
				Thread.sleep(10000);
			}catch(Exception e){
				System.err.println("Sleep interuppted!");
			}
		}
	}

	public static void hashTableSerialisedInTest()
	{
		long t1 = System.nanoTime();
		/*try
		{
			FileInputStream fileIn = new FileInputStream("../testdata/ht.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			HashTable ht = (HashTable) in.readObject();
			in.close();
			fileIn.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}*/
		long t2 = System.nanoTime();
		System.out.println("Serialised Hashtable read in " + (t2-t1)/1000000 + " ms");

		t1 = System.nanoTime();
		String inp = "";
		try
		{
			HashTable ht = new HashTable(n);
			BufferedReader in = new BufferedReader(new FileReader(new File("../testdata/test.csv")));
			inp = in.readLine();
			while(true)
			{
				ht.add(inp.substring(0,13), inp.substring(14));
				inp = in.readLine();
				if (inp == null || inp.equals(null) || inp.equals("null"))
				{
					break;
				}
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		t2 = System.nanoTime();
		System.out.println("Hashtable read and created in " + (t2-t1)/1000000 + " ms");

		t1 = System.nanoTime();
		long m1 = Runtime.getRuntime().freeMemory();
		HashTable ht;
		try
		{
			Kryo kryo = new Kryo();
			UnsafeInput in = new UnsafeInput(new FileInputStream("../testdata/kryo.bin"));
			ht = kryo.readObject(in, HashTable.class);
			in.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		System.gc();
		long m2 = Runtime.getRuntime().freeMemory();
		t2 = System.nanoTime();
		System.out.println("Kryo Hashtable read in " + (t2-t1)/1000000 + " ms");
		System.out.println("Hashtable uses " + (m2-m1)/(1024*1024) + " megabytes");
	}

	public static void hashTableSerialisedOutTest()
	{
        HashTable ht = new HashTable(n);

        MyHash hash = new MyHash();

        String k[] = new String[n];
		String id[] = new String[n];
		Long t1, t2;

        try
		{
			String inp;
			BufferedReader in = new BufferedReader(new FileReader(new File("../data/IDList.csv")));
			for(int i=0; i<n; i++)
			{
				inp = in.readLine();
				k[i] = inp.substring(0,13);
				id[i] = inp.substring(14);
			};
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}

        t1 = System.nanoTime();
        for (int i=0; i<n; i++)
            ht.add(k[i],id[i]);
        t2 = System.nanoTime();
        System.out.println("Hashtable created in " + (t2-t1)/1000000 + " ms");

		/*t1 = System.nanoTime();
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../testdata/ht.ser"));
			out.writeObject(ht);
			out.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		t2 = System.nanoTime();
		System.out.println("Serialised Hashtable written in " + (t2-t1)/1000000 + " ms");*/

		t1 = System.nanoTime();
		try
		{
			UnsafeOutput out = new UnsafeOutput(new FileOutputStream("../testdata/kryo.bin"));
			Kryo kryo = new Kryo();
			kryo.writeObject(out, ht);
			out.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		t2 = System.nanoTime();
		System.out.println("Kryo Hashtable written in " + (t2-t1)/1000000 + " ms");
	}

	public static void hashTable1FromFileTest()
	{
		HashTable ht = new HashTable(n);
		long t1 = System.nanoTime();
                try
                {
                        BufferedReader br = new BufferedReader(new FileReader(new File("../data/IDList.csv")));
                        String s;
                        int i = 0, j=1;
                        while((s=br.readLine()) != null && (i + j*1000000) < n)
                        {
                                ht.add(s.substring(0,13), s.substring(14));
                                i++;
                                if(i==1000000)
                                {
                                        System.out.println(j);
                                        j++;
                                        i=0;
                                }
                        }
                        br.close();
                }catch(Exception e){
                        System.err.println("Error during reading of data");
                }
                long t2 = System.nanoTime();
                System.out.println("Data read from file and hashtable1 generated in " + (t2-t1)/1000000 + " ms");

		t1 = System.nanoTime();

                t1 = System.nanoTime();
                try
                {
                        UnsafeOutput out = new UnsafeOutput(new FileOutputStream("../testdata/kryo.bin"));
                        Kryo kryo = new Kryo();
                        kryo.writeObject(out, ht);
                        out.close();
                }catch(Exception e){
                        System.out.println(e.getMessage());
                        System.exit(0);
                }
                t2 = System.nanoTime();
                System.out.println("Kryo Hashtable written in " + (t2-t1)/1000000 + " ms");

	}

	public static void hashTableFromFileTest()
	{
		HashTable ht = new HashTable(n);
		long t1 = System.nanoTime();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("../data/fakeZAPopulation.csv")));
			String s;
			int i = 0, j=1;
			while((s=br.readLine()) != null && (i + j*1000000) < n)
			{
				ht.add(s.substring(0,13), s.substring(14));
				i++;
				if(i==1000000)
				{
					System.out.println(j);
					j++;
					i=0;
				}
			}
			br.close();
		}catch(Exception e){
			System.err.println("Error during reading of data");
		}
		long t2 = System.nanoTime();
		System.out.println("Data read from file and hashtable generated in " + (t2-t1)/1000000 + " ms");
	}

	public static void hashTableTest()
	{
		int n = 10000000;
		HashTable ht = new HashTable(n);

		MyHash hash = new MyHash();

		long t1 = System.nanoTime();
		String id[] = GenerateNames.getEntries(n+1);
		long t2 = System.nanoTime();
		System.out.println("List generated in " + (t2-t1)/1000000 + " ms");

		String k[] = new String[n];
		for (int i=0; i<n; i++)
		{
			k[i] = id[i].substring(0,13);
			id[i] = id[i].substring(14);
		}

		long t3 = System.nanoTime();
		for (int i=0; i<n; i++)
			ht.add(k[i],id[i]);
		long t4 = System.nanoTime();
		System.out.println("Hashtable created in " + (t4-t3)/1000000 + " ms");

		long t5 = System.nanoTime();
		for (int i=0; i<n; i++)
		{
			ht.get(k[i]);
		}
		long t6 = System.nanoTime();
		System.out.println("Hashtable searched in " + (t6-t5)/1000000 + " ms");
	}

	public static void hashTest()
	{
                MyHash hs = new MyHash();
                int n = 1000;
		double factor = 1;

		int size = (int) (n*factor);

                String id[] = GenerateNames.getEntries(n+1);

                int coll[] = new int[3];
                boolean duplicates=false;
                for (int j=0;j<n;j++)
                        for (int k=j+1;k<n;k++)
                        {
                                if (k!=j && (hs.hash(id[k], size) == hs.hash(id[j], size)))
                                {
                                        coll[0]++;
                                        /*if (hs.hash(id[k], size, 17) == hs.hash(id[j], size,19))
                                        {
                                                coll[1]++;
                                                if (hs.hash(id[k], size, 19) == hs.hash(id[j],size, 19))
                                                        coll[2]++;
                                        }*/
                                }
                        }
                System.out.println("0: " + coll[0]);
                System.out.println("1: " + coll[1]);
                System.out.println("2: " + coll[2]);

	}
}
