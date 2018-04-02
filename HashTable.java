public class HashTable
{
	private int size;

	private MyHash h;
	private String[] keys;
	private String[] values;

	public HashTable(int n)
	{
		size = n*4;

		h = new MyHash();
		keys = new String[size];
		values = new String[size];
	}

	public void add(String k, String v)
	{
		int i = h.hash(k, size);
		int t = 1;

		while(values[i] != null)
		{
			i++;
		}

		keys[i] = k;
		values[i] = v;
	}

	public String get(String k)
	{
		int i = h.hash(k, size);
		int t = 1;

		while(!k.equals(keys[i]))
		{
			i++;
		}

		return values[i];
	}

	public String keyOf(String s)
	{
		for (int i=0; i<size; i++)
		{
			if (values[i] != null && values[i].equals(s))
				return keys[i];
		}
		return null;
	}
}
