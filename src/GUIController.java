import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class GUIController {

	@FXML
	private Button thirdSuggestion;

	@FXML
	private Button firstSuggestion;

	@FXML
	private Button secondSuggestion;

	@FXML
	private Button submitButton;

	@FXML
	private TextField inputField;

	private WordProcessor wordProcessor;

	@FXML
	private HBox hbox;

	public GUIController()
	{

	}

	@FXML
	private void initialize()
	{
		wordProcessor = new WordProcessor();

		submitButton.setOnAction(e -> {
			Word input = new Word(inputField.getText().toLowerCase());
			wordProcessor.processWord(input, wordProcessor.wordTree);

			firstSuggestion.setText("");
			secondSuggestion.setText("");
			thirdSuggestion.setText("");

			if(wordProcessor.getSuggestions(wordProcessor.
					findWord(input.value, wordProcessor.wordTree)).size() >= 1)
			{
				firstSuggestion.setText(wordProcessor
					.getSuggestions(wordProcessor.findWord(input.value, wordProcessor.wordTree)).get(0));
			}
			if(wordProcessor.getSuggestions(wordProcessor.
					findWord(input.value, wordProcessor.wordTree)).size() >= 2)
			{
				secondSuggestion.setText(wordProcessor
					.getSuggestions(wordProcessor.findWord(input.value, wordProcessor.wordTree)).get(1));
			}
			if(wordProcessor.getSuggestions(wordProcessor.
					findWord(input.value, wordProcessor.wordTree)).size() >= 3)
			{
				thirdSuggestion.setText(wordProcessor
					.getSuggestions(wordProcessor.findWord(input.value, wordProcessor.wordTree)).get(2));
			}
		});
	}
}
