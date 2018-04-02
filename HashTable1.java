public class HashTable1
	implements java.io.Serializable
{
	protected final int size;

	protected final MyHash h;
	protected String[] keys;
	protected String[] values;

	public HashTable1()
	{
	}

	public HashTable1(int n)
	{
		size = n*2;

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
			i = h.hash(k, size, t++);
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
			i = h.hash(k, size, t++);
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
