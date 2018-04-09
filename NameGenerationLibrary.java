import java.util.NoSuchElementException;
import java.util.ArrayList;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.time.Year;

public class NameGenerationLibrary
{
	public static void loadFile(String filename, ArrayList<String> arr)
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

	public static int daysInMonth(int month, int year)
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

	public static boolean isLeapYear(int year)
	{
		return (((year%4) == 0) && ((year%100) != 0) || ((year%400) ==0));
	}

	public static int luhn(int[] num)
	{
		int length = num.length - 1; //Keeps track of the length of the array, excluding the checksum
		int checkSum = 0;
		for (int i=0; i<length; i++)
			checkSum += num[i];
		checkSum *= 9;
		return checkSum%10;
	}
}