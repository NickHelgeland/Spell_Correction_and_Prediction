import java.util.LinkedList;
import java.util.List;

public class WordTree
{
	public Word data;

	Integer level;

	WordTree parent;

	List<WordTree> children;

	public WordTree(Word word)
	{
		this.data = word;
		this.children = new LinkedList<WordTree>();
		this.level = 1;
	}

	public WordTree addChild(Word child)
	{
		WordTree childNode = new WordTree(child);
		childNode.parent = this;
		this.children.add(childNode);
		childNode.level = (this.level + 1);

		return parent;
	}
}
