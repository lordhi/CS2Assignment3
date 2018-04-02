public class MyHash
{
	public static int hash(String s, int size)
	{
		return hash(s, size, 0);
	}

	public static int hash(String s, int size, int hn)
	{
		int tmp=0;
		for(int i=0; i<10; i++)
		{
			tmp += s.charAt(i);
			tmp += (tmp << (hn+15));
			tmp ^= (tmp >> 8);
		}

		tmp += (tmp << 5);
		tmp ^= (tmp >> 3);
		tmp += (tmp << 17);

		tmp = tmp > 0 ? tmp : -tmp;

		return tmp%size;
	}
}
