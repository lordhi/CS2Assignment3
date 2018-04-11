import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import javax.swing.ProgressMonitor;

import java.awt.Component;

public class HashTableLoader
{	
	public static HashTable loadIDs(HashTable ht, Component th, int size)
	{
		try
		{
			if (ht == null)
				ht = new HashTable(size);
			else
				ht.clear();

			ProgressMonitor pm;

			long t1 = System.nanoTime();

			pm = new ProgressMonitor(th, "Loading data file", null,0, 100);
			pm.setProgress(0);
			BufferedReader br = new BufferedReader(new FileReader(new File("./data/IDList.csv")));

			int n = Integer.parseInt(br.readLine());

			String s;
			int i = 0, j=1;
			int p = (int)(n/100) + 1;
			while ((s=br.readLine()) != null)
			{
				ht.add(s.substring(0,13), s.substring(14));

				i++;
				if (pm.isCanceled())
					System.exit(0);
				if (i==p)
				{
					System.out.print(String.format("\033[%dA",1)); // Move up
					System.out.print("\033[2K"); // Erase line content();
					System.out.println(j + "%");
					pm.setProgress(j);
					i=0;
					j++;
				}
			}
			
			pm.close();
			br.close();

			System.out.println("Hashtable created from file in " + (System.nanoTime()-t1)/1000000 + " ms");
			return ht;
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public static HashTable loadIDs()
	{
		try
		{
			long t1 = System.nanoTime();

			BufferedReader br = new BufferedReader(new FileReader(new File("./data/IDList.csv")));

			int n = Integer.parseInt(br.readLine());

			HashTable ht = new HashTable(n);

			String s;
			int i = 0, j=1;
			int p = (int)(n/100) + 1;
			while ((s=br.readLine()) != null)
			{
				ht.add(s.substring(0,13), s.substring(14));

				i++;

				if (i==p)
				{
					if (j > 1)
					{
						System.out.print(String.format("\033[%dA",1)); // Move up
						System.out.print("\033[2K"); // Erase line content
					}
					System.out.println("Loading (" + j + "%)");
					i=0;
					j++;
				}
			}
			
			System.out.print(String.format("\033[%dA",1)); // Move up
			System.out.print("\033[2K"); // Erase line content
			System.out.println("Loaded!");

			br.close();

			return ht;
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
		return null;
	}
}