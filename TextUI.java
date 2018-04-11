import java.util.Scanner;

public class TextUI
{
	private static HashTable ht;

	public static void main(String[] args)
	{
		System.out.print("\033[H\033[2J");  //Clears the screen
		System.out.flush();

		ht = HashTableLoader.loadIDs();
		for(String arg : args)
		{
			System.out.println(arg);
			search(arg);
		}
		System.out.println("Please type an ID number below (or q to exit)");
		
		Scanner scan = new Scanner(System.in);
		boolean ignore = false;

		String inp = "";

		inp = scan.nextLine();


		while(!inp.equals("q"))
		{
			search(inp);

			inp = scan.nextLine();

		}

		System.out.print(String.format("\033[%dA",1)); // Move up
		System.out.print("\033[2K"); // Erase line content
		System.out.println("Exitting.");
	}

	public static void search(String s)
	{
		System.out.print(String.format("\033[%dA",1)); // Move up
		System.out.print("\033[2K");// Erase line content
		
		if (s.matches("\\d{13}|q"))
		{			
			String ans = ht.get(s);

			

			if (ans == null)
			{
				System.out.println("No entry with ID " + s + " exists.");
			}else{
				System.out.println(s + " : " + ans);
			}
		}else{
			System.out.println(s + " is not a valid ID number.");
		}
	}
};