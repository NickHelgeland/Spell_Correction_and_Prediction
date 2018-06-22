
public class Word
{
	public int probability;

	public String value;

	public Boolean validity;

	public Word(String word)
	{
		probability = 0;
		value = word;
		validity = false;
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

	public void setValidity(Boolean bool)
	{
		validity = bool;
	}
}
