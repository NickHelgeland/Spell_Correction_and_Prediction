import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class WordProcessor
{
	public WordTree wordTree;

	public WordTree dictionaryTree;

	private Boolean dictionaryIntitialized;

	/*
	 * This is the constructor for the WordProcessor class. It reads in our data and uses it
	 * to construct our initial WordTree.
	 */
	public WordProcessor()
	{
		wordTree = new WordTree(new Word("root"));

		dictionaryTree = new WordTree(new Word("root"));

		dictionaryIntitialized = false;

		try
		{
			constructDictionaryTree("/usr/share/dict/words");
		}
		catch(IOException e)
		{
			System.out.println("It didn't work");
		}

			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data1.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data2.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data3.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data4.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data5.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data6.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data7.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data8.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data9.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data10.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
			try {
				readFile("/Users/Nick/Downloads/HCI_Project1/Data/data11.txt");
			} catch (IOException e) {
				System.out.println("It didn't work");
			}
	}

	/*
	 * This class reads in a file and adds each word to the overall WordTree.
	 */
	private void readFile(String pathname) throws IOException
	{
		FileInputStream fstream = new FileInputStream(pathname);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  Word tempWord = new Word(strLine);
		  processWord(tempWord, wordTree);
		}

		//Close the input stream
		br.close();
		fstream.close();
	}

	private void constructDictionaryTree(String pathname) throws IOException
	{
		FileInputStream fstream = new FileInputStream(pathname);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  Word tempWord = new Word(strLine);
		  tempWord.setValidity(true);
		  processWord(tempWord, dictionaryTree);
		}

		dictionaryIntitialized = true;

		//Close the input stream
		br.close();
		fstream.close();
	}

	/*
	 * This class takes in a word and the root WordTree node and either adds the word to the
	 * tree or if the word is already there it increments the probability of that word.
	 */
	public void processWord(Word word, WordTree tree)
	{
		if(tree != null && !tree.children.isEmpty())
		{
			if(tree.level == word.value.length())
			{
				boolean found = false;
				for(WordTree child : tree.children)
				{
					if(child.data.getValue().equals(word.value))
					{
						found = true;
						if(checkWordValidity(child.data.getValue()) == true)
						{
							child.data.incrementProbability();
						}
					}
				}
				if(found == false)
				{
					tree.addChild(word);
				}
			}
			else if(tree.level < word.value.length())
			{
				boolean match = false;
				for(WordTree child : tree.children)
				{
					if(beginsWith(child.data.getValue(), word.value))
					{
						match = true;
						processWord(word, child);
					}
				}
				if(match == false)
				{
					addAllLettersOfWord(word, tree);
				}
			}
		}
		else if(tree != null && tree.children.isEmpty())
		{
			addAllLettersOfWord(word, tree);
		}
	}

	/*
	 * If the first letter of a word is not yet present in a tree, this function is called to
	 * quickly add the word to the tree properly.
	 */
	private void addAllLettersOfWord(Word word, WordTree tree)
	{
		if(tree.level <= word.value.length())
		{
			String temp = "";
			for(int i = 0; i < tree.level; i++)
			{
				temp = temp + word.value.charAt(i);
			}
			tree.addChild(new Word(temp));
			for(WordTree child : tree.children)
			{
				if(child.data.getValue() == temp)
				{
					addAllLettersOfWord(word, child);
				}
			}
		}
	}

	/*
	 * This function is responsible for finding a specific node in the overall tree
	 * and returning it.
	 */
	public WordTree findWord(String word, WordTree tree)
	{
		WordTree match = null;
		if(!tree.children.isEmpty())
		{
			for(WordTree child : tree.children)
			{
				if(child.data.getValue().equals(word))
				{
					match = child;
				}
			}
			if(match == null)
			{
				for(WordTree child : tree.children)
				{
					if(word.startsWith(child.data.getValue()))
					{
						match = findWord(word, child);
					}
				}
			}
		}

		return match;
	}

	/*
	 * This function is what returns the spelling corrections. It utilizes several other
	 * functions to return a list of all of our suggestions.
	 */
	public ArrayList<String> getSuggestions(WordTree tree)
	{
		String suggestion = "no suggestion";
		int greatestProbability = 0;
		int probabilityFactor = 0;
		ArrayList<String> suggestionList = new ArrayList<String>();
		ArrayList<Word> probabilityList = new ArrayList<Word>();

		for(Word probability : getLowerLevelMatches(wordTree, tree, probabilityList))
		{
			if(probability.getProbability() > 0)
			{
				probabilityFactor = probability.getProbability()
					+ (doBigramAnalysis(probability.value, tree.data.getValue()) * 2);
				if(probabilityFactor > greatestProbability)
				{
					greatestProbability = probabilityFactor;
					suggestion = probability.value;
				}
				else if(probabilityFactor == greatestProbability)
				{
					suggestion = getBestMatch(suggestion, probability.value, tree.data.getValue());
				}
			}
		}
		greatestProbability = 0;
		suggestionList.add(suggestion);
		suggestion = "no suggestion";
		probabilityList.clear();

		for(Word probability : getMatchesOnLevel(wordTree, tree, probabilityList))
		{
			if(probability.getProbability() > 0)
			{
				probabilityFactor = probability.getProbability()
						+ (doBigramAnalysis(probability.value, tree.data.getValue()) * 2);
				if(probabilityFactor > greatestProbability)
				{
					greatestProbability = probabilityFactor;
					suggestion = probability.value;
				}
				else if(probabilityFactor == greatestProbability)
				{
					suggestion = getBestMatch(suggestion, probability.value, tree.data.getValue());
				}
			}
		}
		greatestProbability = 0;
		suggestionList.add(suggestion);
		suggestion = "no suggestion";
		probabilityList.clear();

		for(Word probability : getUpperLevelMatches(wordTree, tree, probabilityList))
		{
			if(probability.getProbability() > 0)
			{
				probabilityFactor = probability.getProbability()
						+ (doBigramAnalysis(probability.value, tree.data.getValue()) * 2);
				if(probabilityFactor > greatestProbability)
				{
					greatestProbability = probabilityFactor;
					suggestion = probability.value;
				}
				else if(probabilityFactor == greatestProbability)
				{
					suggestion = getBestMatch(suggestion, probability.value, tree.data.getValue());
				}
			}
		}
		suggestionList.add(suggestion);


		return suggestionList;
	}

	/*
	 * This function finds all potential matches from the words that are lower in the tree
	 * then the given word.
	 */
	private ArrayList<Word> getLowerLevelMatches(WordTree tree, WordTree word, ArrayList<Word> probabilityList)
	{
		if (!tree.children.isEmpty())
		{
			for(WordTree child: tree.children)
			{
				getLowerLevelMatches(child, word, probabilityList);
				if(child.level > word.level)
				{
					if(checkSimilarities(word.data.getValue(), doBigramAnalysis(child.data.getValue(), word.data.getValue())))
					{
						probabilityList.add(child.data);
					}
				}

			}
		}
		return probabilityList;
	}

	/*
	 * This function looks for potential suggestions in words that are higher in the tree than
	 * the given word.
	 */
	private ArrayList<Word> getUpperLevelMatches(WordTree tree, WordTree word, ArrayList<Word> probabilityList)
	{
		if(word.level != 1 && word.level > 2)
		{
			recurseOnUpperLevels(tree, word, probabilityList, 2);
		}
		else if(word.level == 2)
		{
			recurseOnUpperLevels(tree, word, probabilityList, 1);
		}

		return probabilityList;
	}

	/*
	 * This is the function that actually performs the necessary recursion for the
	 * getUpperLevelMatches() function.
	 */
	private void recurseOnUpperLevels(WordTree tree, WordTree word,
			ArrayList<Word> probabilityList, int levelsToRecurse)
	{
		for(WordTree child : tree.children)
		{
			if(child.level < word.level)
			{
				recurseOnUpperLevels(child, word, probabilityList, levelsToRecurse);
			}
			if(child.level >= (word.level - levelsToRecurse) && child.level < word.level)
			{
				if(doBigramAnalysis(child.data.getValue(), word.data.getValue()) >= (child.data.getValue().length() / 2))
				{
					probabilityList.add(child.data);
				}
				else if(child.data.getValue().equals(word.data.getValue().charAt(0)))
				{
					probabilityList.add(child.data);
				}
			}
		}
	}

	/*
	 * this function looks for potential suggestions that are on the same level of the tree
	 * as the given word.
	 */
	private ArrayList<Word> getMatchesOnLevel(WordTree tree, WordTree word,
			ArrayList<Word> probabilityList)
	{
		for(WordTree child : tree.children)
		{
			if(child.level <= word.level)
			{
				getMatchesOnLevel(child, word, probabilityList);
			}
			if(child.level == word.level)
			{
				if(checkSimilarities(word.data.getValue(), doBigramAnalysis(child.data.getValue(), word.data.getValue()))
						&& !word.data.getValue().equals(child.data.getValue()))
				{
					probabilityList.add(child.data);
				}
			}
		}
		return probabilityList;
	}

	/*
	 * performs rounded division on two integer values and returns the result.
	 */
	private int roundedDivision(int numerator, int denominator)
	{
		int base = numerator / denominator;
		int decimalRounded = (numerator % denominator) / 2;
		int answer = base + decimalRounded;
		return answer;
	}

	public boolean checkWordValidity(String word)
	{
		boolean wordValidity = false;
		if (dictionaryIntitialized == false)
		{
			try {
	            BufferedReader in = new BufferedReader(new FileReader(
	                    "/usr/share/dict/words"));
	            String str;
	            while ((str = in.readLine()) != null) {
	                if (str.indexOf(word) != -1) {
	                    wordValidity = true;
	                    break;
	                }
	            }
	            in.close();
	        }
	        catch (IOException e) {}
		}
		else
		{
			if(findWord(word, dictionaryTree) != null)
			{
				if(findWord(word, dictionaryTree).data.validity == true)
				{
					wordValidity = true;
				}
			}
		}

        return wordValidity;
    }

	/*
	 * This function takes in two strings and determines whether one string begins with
	 * the other.
	 */
	private boolean beginsWith(String string1, String string2)
	{
		boolean correctness = false;

		for(int i = 0 ; i < string1.length() ; i++)
		{
			if(string1.charAt(i) == string2.charAt(i))
			{
				correctness = true;
			}
			else
			{
				correctness = false;
				break;
			}
		}

		return correctness;
	}

	/*
	 * This function checks two strings to see how many bigrams that they have in common.
	 */
	private int doBigramAnalysis(String newString, String oldString)
	{
		ArrayList<String> bigramList1 = new ArrayList<String>();
		ArrayList<String> bigramList2 = new ArrayList<String>();
		int similarities = 0;

		for(int i = 0 ; i < (newString.length() - 1); i++)
		{
			bigramList1.add(new StringBuilder().append(newString.charAt(i))
					.append(newString.charAt(i + 1)).toString());
		}
		for(int i = 0 ; i < (oldString.length() - 1); i++)
		{
			bigramList2.add(new StringBuilder().append(oldString.charAt(i))
					.append(oldString.charAt(i + 1)).toString());
		}

		for(String bigram : bigramList2)
		{
			if(bigramList1.contains(bigram))
			{
				similarities++;
			}
		}

		return similarities;
	}

	/*
	 * This checks to see if the number of similarities meets our minimum threshold.
	 */
	private boolean checkSimilarities(String word, int similarities)
	{
		boolean result = false;

		if(similarities >= (word.length() / 2))
		{
			result = true;
		}

		return result;
	}

	private String getBestMatch(String currentSuggestion, String challenger, String wordToCompare)
	{
		int currentSuggestionMatches = 0;
		int challengerMatches = 0;
		double challengerMatchPercentage;
		double currentSuggestionMatchPercentage;
		String bestMatch = currentSuggestion;

		for(int i = 0; i < wordToCompare.length(); i++)
		{
			if(challenger.indexOf(wordToCompare.charAt(i)) != -1)
			{
				challengerMatches++;
			}
		}

		for(int i = 0; i < currentSuggestion.length(); i++)
		{
			if(wordToCompare.indexOf(currentSuggestion.charAt(i)) != -1)
			{
				currentSuggestionMatches++;
			}
		}

		challengerMatchPercentage = (challengerMatches / challenger.length());
		currentSuggestionMatchPercentage = (currentSuggestionMatches
				/ currentSuggestion.length());

		if(challengerMatchPercentage > currentSuggestionMatchPercentage)
		{
			bestMatch = challenger;
		}

		return bestMatch;
	}


}
