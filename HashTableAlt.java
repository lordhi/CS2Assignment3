public class HashTableAlt
	implements java.io.Serializable
{
	private int size;

	private String[] keys;
	private String[] values;

	public HashTableAlt(int n)
	{
		size = n*2;

		keys = new String[size];
		values = new String[size];
	}

	public HashTableAlt()
	{

	}

	public void add(String k, String v)
	{
		int i = MyHash.hash(k, size);

		while(values[i] != null)
		{
			i++;
		}

		keys[i] = k;
		values[i] = v;
	}

	public String get(String k)
	{
		int i = MyHash.hash(k, size);

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
