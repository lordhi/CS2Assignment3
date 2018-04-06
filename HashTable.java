public class HashTable
//	implements java.io.Serializable
{
	protected int size;

	protected transient MyHash h;
	protected transient int i;
	protected transient int t;
	protected transient Long tmp;

	protected long[] keys;
	protected String[] values;

/*	public HashTable()
	{
		h = new MyHash();
	}*/

	public HashTable(int n)
	{
		size = (int)(n*2);
		h = new MyHash();

		keys = new long[size];
		values = new String[size];
	}

	public void add(String k, String v)
	{
		t = 1;

		do
		{
			i = h.hash(k, size, t++);
		}
		while(values[i] != null);

		keys[i] = Long.parseLong(k);
		values[i] = v;
	}

	public void clear()
	{
		for(int j=0; j<size; j++)
		{
			keys[j] = 0;
			values[j] = null;
		}
	}

	public String get(String k)
	{
		t = 1;
		tmp = Long.parseLong(k);

		do{
			i = h.hash(k, size, t++);
			if (values[i] == null)
				return null;
		}while(!tmp.equals(keys[i]));

		return values[i];
	}

	public String keyOf(String s)
	{
		for (i=0; i<size; i++)
		{
			if (values[i] != null && values[i].equals(s))
				return keys[i] + "";
		}
		return null;
	}
}
