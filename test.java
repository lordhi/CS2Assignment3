import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.esotericsoftware.kryo.io.UnsafeInput;

public class test
{
	static int n = 50000000;
	public static void main(String[] args)
	{
		//hashTableTest();
		//hashTableFromFileTest();
		//hashTableSerialisedInTest();
		//hashTable1FromFileTest();
		//hashTableSerialisedOutTest();
		hashTableSerialisedInTest();
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
		//System.out.println("Serialised Hashtable read in " + (t2-t1)/1000000 + " ms");

/*		t1 = System.nanoTime();
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
		System.out.println("Hashtable read and created in " + (t2-t1)/1000000 + " ms");*/

		t1 = System.nanoTime();
		HashTable1 ht = new HashTable1();
		try
		{
			Kryo kryo = new Kryo();
			UnsafeInput in = new UnsafeInput(new FileInputStream("../testdata/kryo.bin"));
			ht = kryo.readObject(in, HashTable1.class);
			in.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		t2 = System.nanoTime();
		System.out.println("Kryo Hashtable read in " + (t2-t1)/1000000 + " ms");

                t1 = System.nanoTime();
                System.out.println(ht.get("1704155584082"));
                System.out.println(ht.get("9709125722088"));
                System.out.println(ht.get("3609146863086"));
                System.out.println(ht.get("4107207110089"));
                t2 = System.nanoTime();
                System.out.println("4 data reads done in " + (t2-t1)/1000000 + " ms");

	}

	public static void hashTableSerialisedOutTest()
	{
                HashTable ht = new HashTable(n);

                MyHash hash = new MyHash();

                long t1 = System.nanoTime();
                String entries[] = GenerateNames.getEntries(n+100);
                long t2 = System.nanoTime();
                System.out.println("List generated in " + (t2-t1)/1000000 + " ms");

                String k[] = new String[n];
		String id[] = new String[n];

                for (int i=0; i<n; i++)
                {
                        k[i] = entries[i].substring(0,13);
                        id[i] = entries[i].substring(14);
		}

		t1 = System.nanoTime();
		try
		{
			BufferedWriter wr = new BufferedWriter(new FileWriter("../testdata/test.csv"));
			for(String ln : entries)
				wr.write(ln + "\r\n");
			wr.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		t2 = System.nanoTime();
		System.out.println("Entries written in " + (t2-t1)/1000000 + " ms");

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
		System.out.println("Serialised Hashtable written in " + (t2-t1)/1000000 + " ms");
*/
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
		HashTable1 ht = new HashTable1(n);
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
                System.out.println("Data read from file and hashtable1 generated in " + (t2-t1)/1000000 + " ms");

		t1 = System.nanoTime();
		System.out.println(ht.get("1704155584082"));
		System.out.println(ht.get("9709125722088"));
		System.out.println(ht.get("3609146863086"));
		System.out.println(ht.get("4107207110089"));
		t2 = System.nanoTime();
		System.out.println("4 data reads done in " + (t2-t1)/1000000 + " ms");

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
