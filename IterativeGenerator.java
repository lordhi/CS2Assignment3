import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.NoSuchElementException;
import java.util.ArrayList;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.time.Year;

public class IterativeGenerator
	implements Iterator<String[]>
{
	private ArrayList<String> femFirstNames;
	private ArrayList<String> malFirstNames;
	private ArrayList<String> surnames;
	
	private int currSize;
	private int maxSize;
	
	private boolean isFemale;
	private String[] entry; 
	
	private int entriesForYear;
	private int extraEntriesYear;
	private int entriesForDay;
	private int extraEntriesDay;
	
	private int femInDay;
	private int malInDay;
	private int entriesInYear;
	
	private int currDay;
	private int currMonth;
	private int currYear;
	private Random ran;
	
	private boolean print;
	private String printFile;
	private int bufferSize;
	private LinkedList<String> printQueue;
	
	public IterativeGenerator(int n, String filename)
	{
		init(n);
		print = true;
		printFile = filename;
		bufferSize = 100000;
		printQueue = new LinkedList<>();
		
		try
		{
			FileWriter fOut = new FileWriter(printFile);
			fOut.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public IterativeGenerator(int n)
	{
		init(n);
	}
	
	private void init(int n)
	{
		print = false;

		currSize = 0;
		maxSize = n;

		femFirstNames = new ArrayList<>();
		malFirstNames = new ArrayList<>();
		surnames = new ArrayList<>();
		
		loadFile("../NameLists/femaleNames.csv", femFirstNames);
		loadFile("../NameLists/maleNames.csv", malFirstNames);
		loadFile("../NameLists/surnames.csv", surnames);
		
		currYear = Year.now().getValue() - 80;

		currDay = 1;
		currMonth = 1;

		femInDay = 0;
		malInDay = 0;
		entriesInYear = 0;

		entriesForYear = (int) (n/62);
		extraEntriesYear = n - entriesForYear*62;

		if (extraEntriesYear > 0)
			entriesForYear++;
		
		entriesForDay = (int)(entriesForYear/365);
		extraEntriesDay = entriesForYear - entriesForDay;

		ran = new Random();	
		entry = new String[2];	
	}
	
	private void loadFile(String filename, ArrayList<String> arr)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String inp;
			while((inp = br.readLine()) != null)
				arr.add(inp);
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}
	
	private int daysInMonth(int month, int year)
	{
		if (month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12)
		{
			return 31;
		}else if (month==2){
			if (isLeapYear(year))
			{
				return 29;
			}else{
				return 28;
			}
		}else{
			return 30;
		}
	}
	
	private static boolean isLeapYear(int year)
	{
		return (((year%4) == 0) && ((year%100) != 0) || ((year%400) ==0));
	}
	
	private int luhn(int[] num)
	{
		int length = num.length - 1; //Keeps track of the length of the array, excluding the checksum
		int checkSum = 0;
		for (int i=0; i<length; i++)
			checkSum += num[i];
		checkSum *= 9;
		return checkSum%10;
	}
	
	private String generateName(boolean isFemale)
	{
		String s;
		if (isFemale)
			s = femFirstNames.get(ran.nextInt(femFirstNames.size()));
		else
			s = malFirstNames.get(ran.nextInt(malFirstNames.size()));
		return s + " " + surnames.get(ran.nextInt(surnames.size()));
	}
	
	private void emptyPrintQueue()
	{
		try
		{
			BufferedWriter wr = new BufferedWriter(new FileWriter(printFile, true));
			while (printQueue.size() > 0)
			{
				wr.write(printQueue.pop() + "\r\n");
			}
			wr.flush();
			wr.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}
	
	private String generateID(int year, int month, int day, boolean isFemale)
	{
		int[] ID = new int[13];

		//Sets the first two elements to the last digits in the year
		ID[1] = year%10;
		ID[0] = (year%100 - ID[1])/10;

		//Sets the next two digits to the month
		ID[3] = month%10;
		ID[2] = (month - ID[3])/10;

		//Sets the next two digits to the day
		ID[5] = day%10;
		ID[4] = (day - ID[5])/10;

		if (isFemale)
		{
			ID[9] = (int) femInDay%10; 
			ID[8] = (int) (femInDay%100)/10;
			ID[7] = (int) (femInDay%1000)/100;
			ID[6] = (int) (femInDay%10000)/1000;

			femInDay++;
		}else{
			ID[9] = (int) malInDay%10; 
			ID[8] = (int) (malInDay%100)/10;
			ID[7] = (int) (malInDay%1000)/100;
			ID[6] = (int) (malInDay%10000)/1000 + 5;
			malInDay++;
		}

		//Adds in a digit to show citizenship. As only citizens will vote, we can set this to 0
		ID[10] = 0;

		/*The following digit used to be used for racial classification, this was changed in 1986.
		Therefore, we perform a check to see if the person was born before or after this year to see if this digit should be generated,
		or if it should be 8 (as used now)
		*/
		if (year > 1986)
			ID[11] = 8;
		else
			ID[11] = ran.nextInt(7);

		//The last digit is a checksum, using the Luhn Algorithm
		ID[12] = luhn(ID);

		String sID = "";
		for (int i=0; i < 13; i++)
			sID += ID[i];
		return sID;
	}
	
	public String[] next()
	throws NoSuchElementException
	{
		
		if(currSize == maxSize)
			throw new NoSuchElementException();
		
		isFemale = ran.nextBoolean();
		
		entry[0] = generateID(currYear, currMonth, currDay, isFemale);
		entry[1] = generateName(isFemale);
		
		if((femInDay + malInDay) == entriesForDay)
		{
			currDay++;
			femInDay = 0;
			malInDay = 0;
			
			if (extraEntriesDay > 0)
				extraEntriesDay--;
			else if (extraEntriesDay == 0)
			{
				extraEntriesDay--;
				entriesForDay--;
			}
			
			if(currDay == daysInMonth(currMonth, currYear)+1)
			{
				currMonth++;
				currDay = 1;
				if(currMonth == 13)
				{
					currMonth = 1;
					currYear++;
					
					entriesForDay = (int)(entriesForYear/365);
					extraEntriesDay = entriesForYear - entriesForDay;
					
					if (extraEntriesDay > 0)
						entriesForDay++;
					
					if(extraEntriesYear > 0)
						extraEntriesYear--;
					else if(extraEntriesYear == 0)
					{
						extraEntriesYear--;
						entriesForYear--;
					}
				}
			}			
		}

		currSize++;
		
		if(print)
		{
			printQueue.add(entry[0] + " " + entry[1]);
			if(printQueue.size() == bufferSize)
				emptyPrintQueue();
			if(currSize == maxSize && printQueue.size() > 0)
				emptyPrintQueue();
		}	
		return entry;
	}
	
	public boolean hasNext()
	{
		return (maxSize - currSize) > 0;
	}
}
