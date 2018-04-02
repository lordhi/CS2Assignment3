import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


public class test
{
	public static void main(String[] args)
	{
		hashTableFromFileTest();
	}

	public static void hashTableFromFileTest()
	{
		HashTable ht = new HashTable(50000000);
		long t1 = System.nanoTime();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("../data/IDList.csv")));
			String s;
			while((s=br.readLine()) != null)
			{
				ht.add(s.substring(0,13), s.substring(14));
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
		int n = 2000000;
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
