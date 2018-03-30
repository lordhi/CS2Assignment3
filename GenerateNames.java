import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.Year;

public class GenerateNames
{

	private static ArrayList<String> femFirstNames;
	private static ArrayList<String> malFirstNames;
	private static ArrayList<String> surnames;

	public static void main(String[] args)
	{
		checkArgs(args);
		loadNames();

		int n = args.length == 0 ? 50 : Integer.parseInt(args[0]);
		String entries[] = generateEntries(n);
		writeEntriesToFile("../data/IDList.csv", entries);
	}

	public static String[] getEntries(int n)
	{
		loadNames();
		return generateEntries(n);
	}

	private static void writeEntriesToFile(String filename, String entries[])
	{
		try
		{
			BufferedWriter wr = new BufferedWriter(new FileWriter(filename));
			for (String ln : entries)
				wr.write(ln + "\r\n");
			wr.close();
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}

	private static String[] generateEntries(int n)
	{
		Random ran = new Random();
		int minAge = 18;
		int maxAge = 80;
		int year = Year.now().getValue();

		String entries[] = new String[n];
		for (int i=0; i < n; i++)
			entries[i] = generateEntry(ran, year-maxAge, year-minAge);
		return entries;
	}

	private static String generateEntry(Random ran, int minYear, int maxYear)
	{
		String name, surname, ID;
		boolean isFemale = ran.nextInt(1) == 1;

		name = isFemale ? femFirstNames.get(ran.nextInt(femFirstNames.size())) : malFirstNames.get(ran.nextInt(malFirstNames.size()));
		surname = surnames.get(ran.nextInt(surnames.size()));

		ID = generateID(ran, minYear, maxYear, isFemale);

		return ID + " " + name + " " + surname;
	}

	private static void loadNames()
	{
		femFirstNames = new ArrayList<>();
		malFirstNames = new ArrayList<>();
		surnames = new ArrayList<>();

		loadFile("../NameLists/femaleNames.csv", femFirstNames);
		loadFile("../NameLists/maleNames.csv", malFirstNames);
		loadFile("../NameLists/surnames.csv", surnames);
	}

	private static void loadFile(String filename, ArrayList<String> arrL)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
			String inp;
			while((inp = in.readLine()) != null)
				arrL.add(inp);
		}catch(Exception e){
			System.err.println("Exception encountered: " + e.getMessage());
			System.exit(1);
		}
	}

	private static String generateID(Random ran, int minYear, int maxYear, boolean isFemale)
	{
		int year = minYear + ran.nextInt(maxYear);
		int month = 1 + ran.nextInt(12);
		int day = 1 + ran.nextInt(maxDaysInMonth(month, year));

		int[] ID = new int[13];

		//Sets the first two elements to the last digits in the year
		ID[1] = year%10;
		ID[0] = (year%100 -ID[1])/10;

		//Sets the next two digits to the month
		ID[3] = month%10;
		ID[2] = (month - ID[3])/10;

		//Sets the next two digits to the day
		ID[5] = day%10;
		ID[4] = (day - ID[5])/10;

		//Adds in a genered digit, this is not uniformly distributed but is distributed towards 0
		ID[6] = genderDigit(ran, isFemale);


		//Adds in three random digits
		ID[7] = ran.nextInt(9);
		ID[8] = ran.nextInt(9);
		ID[9] = ran.nextInt(9);

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

	private static int luhn(int[] num)
	{
		int length = num.length - 1; //Keeps track of the length of the array, excluding the checksum
		int checkSum = 0;
		for (int i=0; i<length; i++)
			checkSum += num[i];
		checkSum *= 9;
		return checkSum%10;
	}

	private static int genderDigit(Random ran, boolean isFemale)
	{
		int tmp = isFemale ? 0 : 5;
		if (ran.nextInt(10) > 5)
			return tmp + 0;
		else if (ran.nextInt(10) > 5)
			return tmp + 1;
		else if (ran.nextInt(10) > 5)
			return tmp + 2;
		else if (ran.nextInt(10) > 5)
			return tmp + 3;
		else return tmp + 4;
	}

	private static int maxDaysInMonth(int month, int year)
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

	private static void checkArgs(String[] args)
	{
		if (args.length > 1)
		{
			System.err.println("Please supply only a single number as parameters (or no arguments to generate 50 ID numbers and names)");
			System.exit(0);
		}
	}
}
