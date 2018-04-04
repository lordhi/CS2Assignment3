public class HashTable
	implements java.io.Serializable
{
	protected int size;

	protected  transient MyHash h;

	protected long[] keys;
	protected String[] values;

	public HashTable()
	{
		h = new MyHash();
	}

	public HashTable(int n)
	{
		size = (int)(n*4);

		h = new MyHash();
		keys = new long[size];
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

		keys[i] = Long.parseLong(k);
		values[i] = v;
	}

	public String get(String k)
	{
		int i = h.hash(k, size);
		int t = 1;
		Long tmp = Long.parseLong(k);

		while(!tmp.equals(keys[i]))
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
				return keys[i] + "";
		}
		return null;
	}
}
