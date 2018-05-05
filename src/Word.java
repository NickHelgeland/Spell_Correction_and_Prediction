
public class Word
{
	public int probability;

	public String value;

	public Word(String word)
	{
		probability = 0;
		value = word;
	}

	public int getProbability()
	{
		return probability;
	}

	public void incrementProbability()
	{
		probability++;
	}

	public String getValue()
	{
		return value;
	}
}
