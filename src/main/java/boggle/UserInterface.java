package boggle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UserInterface implements ActionListener, Constants {

	private JPanel boardArea;
	private static JTextField inputBox;
	private JTextArea foundWords;
	private JLabel errorLabel;
	private static ArrayList<String> guessList;
	private Board board;
	private JButton newGame;
	private JButton[][] tiles;
	private JTextArea allWords;
	private int score;
	private JLabel scoreLabel;
	private int round;
	private JLabel roundLabel;
	private int target;
	private ImageIcon blankDice;
	private JButton giveUp;
	private int totalScore;
	private JLabel totalScoreLabel;
	private int highScore;
	private JLabel highScoreLabel;
	
	
	UserInterface() {
		round = 0;
		resizeDiceImage();
		drawUI();
		startNewGame();
	}

	private void resizeDiceImage(){
		blankDice = new ImageIcon("dice.jpg"); 
		java.awt.Image diceImage = blankDice.getImage();
		java.awt.Image scaledImage = diceImage.getScaledInstance(DICE_SIZE, DICE_SIZE, java.awt.Image.SCALE_SMOOTH);
		blankDice = new ImageIcon(scaledImage);
	}

	private void checkGameOver(){
		if (Board.getScore(guessList) < target){
			round = 0;
			if (totalScore > highScore){
				setNewHighScore(totalScore);
			} else {
				SoundEffects.buzzer();
			}
			totalScore = 0;
		}
	}

	private void setNewHighScore(int highScore){
		this.highScore = highScore;
		highScoreLabel.setText("High Score: " + highScore);
		errorLabel.setForeground(Color.GREEN);
		errorLabel.setText("New High Score!");
	}

	private void startNewGame(){
		if (round != 0 ){
			checkGameOver();
		}
		
		roundLabel.setText("Level " + ++round);
		this.board = new Board();
		target= Board.getTargetScore(round, board.maxScore);
		drawBoard(board.tiles);
		foundWords.setText("");
		allWords.setText("");
		guessList.clear();
		inputBox.setEditable(true);
		score = 0;
		incrementScore(0);
		scoreLabel.setText("Score: " + score + "/" + target);
	}

	private void drawBoard(char[][] dice){
        for(int y = 0; y < dice.length; y++) {
            for(int x = 0; x < dice[y].length; x++) {
                tiles[x][y].setText("" + dice[y][x]);
            }
        }
	}

	void showAllWords(){
		checkGameOver();
		inputBox.setEditable(false);
		Collections.sort(board.wordList, new Dictionary.wordLengthComparator());
		for (String word : board.wordList){
			if (!guessList.contains(word.toLowerCase())){
				allWords.setForeground(Color.RED);
				allWords.append(word.toUpperCase() + " (" + Board.getWordScore(word) + ")" + "\n");
			}
		}
	}

	private void drawUI(){
	
		guessList = new ArrayList<>();
		JFrame f = new JFrame("Tom's Boggle");
		f.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel header = setUpHeader();	
		f.add(header, BorderLayout.NORTH);
		
		JPanel footer = setUpFooter();
		f.add(footer, BorderLayout.SOUTH);
		
		JPanel sideBar = setUpSideBar();
		f.add(sideBar, BorderLayout.EAST);
		
		boardArea = new JPanel();
		boardArea.setSize(DICE_SIZE * BOARD_DIMENSIONS, DICE_SIZE * BOARD_DIMENSIONS);
		boardArea.setLayout(new GridLayout( BOARD_DIMENSIONS, BOARD_DIMENSIONS));
		
        drawBoardArea();
		f.add(boardArea, BorderLayout.CENTER);
		
		//f.add(BoardArea, BorderLayout.CENTER);
		f.setSize(1200,770);  
        f.setVisible(true);
        f.setLocation(100, 100);
		f.requestFocus();
	}

	private JPanel setUpSideBar() {
		JPanel sideBar = new JPanel();
		sideBar.setLayout(new BorderLayout());
		
		foundWords = new JTextArea(30, 10);
		foundWords.setFont(new Font("Calibri", Font.BOLD, 35));
		foundWords.setEditable(false);
		foundWords.setAlignmentY(10.0f);
		sideBar.add(foundWords, BorderLayout.WEST);
		
		allWords = new JTextArea(30,10);
		allWords.setFont(new Font("Calibri", Font.BOLD, 35));
		allWords.setEditable(false);
		sideBar.add(allWords, BorderLayout.EAST);
		
		
		JPanel textArea = new JPanel();
		textArea.setLayout(new BorderLayout());
		errorLabel= new JLabel("noone should see this");
		errorLabel.setVisible(false);
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setFont(new Font("Calibri", Font.BOLD, 35));
		textArea.add(errorLabel, BorderLayout.NORTH);
		
		inputBox = new JTextField();
		inputBox.setFont(new Font("Calibri", Font.BOLD, 35));
		inputBox.addActionListener(this);
		inputBox.setHorizontalAlignment(JTextField.CENTER);	
		inputBox.requestFocusInWindow();
		textArea.add(inputBox,BorderLayout.SOUTH);
		
		sideBar.add(textArea, BorderLayout.SOUTH);
		
		return sideBar;
	}

	private JPanel setUpHeader() {
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(1, 3));
		newGame= new JButton("Start New Game");
		newGame.setFont(new Font("Calibri", Font.PLAIN, 30));
		newGame.addActionListener(e -> {
			StopWatch.timer.cancel();
			startNewGame();
		});
		header.add(newGame);
		
		highScoreLabel = new JLabel("High Score: 0");
		highScoreLabel.setFont(new Font("Calibri", Font.PLAIN, 30));
		highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		header.add(highScoreLabel);
		
		giveUp = new JButton("Give Up");
		giveUp.setFont(new Font("Calibri", Font.PLAIN, 30));
		giveUp.addActionListener(e -> {
			StopWatch.timer.cancel();
			showAllWords();
		});
		header.add(giveUp);
		return header;
	}
		
	private JPanel setUpFooter() {
		JPanel footer = new JPanel();
		footer.setLayout(new GridLayout(1, 4));
		
		roundLabel = new JLabel("Level " + round);
		roundLabel.setFont(new Font("Calibri", Font.BOLD, 35));
		roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
		footer.add(roundLabel);
		
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setFont(new Font("Calibri", Font.BOLD, 35));
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		footer.add(scoreLabel);
		
		totalScoreLabel = new JLabel("Total Score: " + totalScore );
		totalScoreLabel.setFont(new Font("Calibri", Font.BOLD, 35));
		totalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		footer.add(totalScoreLabel);

		JLabel time = new JLabel("Time: ");
		time.setFont(new Font("Calibri", Font.BOLD, 35));
		time.setHorizontalAlignment(SwingConstants.CENTER);
		footer.add(time);
		
		return footer;
	}

	private void drawBoardArea() {
		tiles = new JButton[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        
        for(int y = 0; y < tiles.length; y++) {

            for(int x = 0; x < tiles[y].length; x++) {

                tiles[x][y] = new JButton("", blankDice);
                tiles[x][y].setVerticalTextPosition(SwingConstants.CENTER);
                tiles[x][y].setHorizontalTextPosition(SwingConstants.CENTER);
                tiles[x][y].setFont(new Font("Calibri", Font.BOLD, 100));
                
                boardArea.add(tiles[x][y]);
            }
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String guess =  inputBox.getText().toLowerCase().trim();
		errorLabel.setVisible(false);
		errorLabel.setForeground(Color.RED);
		//System.err.println();
		if (guess.length() < 3){
			errorLabel.setText("'" + guess.toUpperCase() + "' is too short,");
			errorLabel.setVisible(true);
			System.err.println("words must be at least 3 letters long");
			//SoundEffects.buzzer();
			
		} else if(guessList.contains(guess)){
			errorLabel.setText(guess +" has already been found");
			errorLabel.setVisible(true);
			//SoundEffects.buzzer();
			
		} else {
		
			if (board.wordList.contains(guess.toUpperCase())){
				guessList.add(guess);
				int wordScore = Board.getWordScore(guess);
				foundWords.append(guess.toUpperCase() + " (" + wordScore + ")" + "\n");
				incrementScore(wordScore);
				SoundEffects.ringBell(wordScore);
			} else if (Dictionary.isRealWord(guess)) {
				errorLabel.setText(guess +" is not on the board");
				errorLabel.setVisible(true);
				System.err.println(guess +" is not on the board");
				//SoundEffects.buzzer();
			} else if (guess.charAt(guess.length()-1) == 's') {
				errorLabel.setText("Plural nouns are not allowed");
				errorLabel.setVisible(true);
				//SoundEffects.buzzer();
			} else {
				errorLabel.setText("'" + guess.toUpperCase() +"' is not a real word");
				errorLabel.setVisible(true);
				System.err.println(guess +" is not a real word");
				//SoundEffects.buzzer();
			}
			
		}
		inputBox.setText("");	
	}

	private void incrementScore(int wordScore){
		score += wordScore;
		totalScore += wordScore;
		scoreLabel.setText("Score: " + score + "/" + target);
		totalScoreLabel.setText("Total Score: " + totalScore );
		if (score < target){
			scoreLabel.setForeground(Color.RED);
			giveUp.setText("Give Up");
			newGame.setText("Start New Game");
		} else {
			scoreLabel.setForeground(Color.GREEN);
			giveUp.setText("End Round");
			newGame.setText("Next Round");
		}
	}
}
