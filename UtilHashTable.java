import java.util.HashMap;

public class UtilHashTable
{
	HashMap<Long,String> ht;

	public UtilHashTable(int n)
	{
		ht = new HashMap<>((int) (n*4/3) + 10);
	}

	public void add(String k, String v)
	{
		ht.put(Long.parseLong(k), v);
	}

	public String get(String k)
	{
		return ht.get(Long.parseLong(k));
	}
}
