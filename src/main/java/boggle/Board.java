package boggle;

import java.util.ArrayList;
import java.util.Collections;

class Board implements Constants{
	
	private ArrayList<Dice> diceBag;
	int maxScore;
	char[][] tiles;
	ArrayList<String> wordList;

	Board(){
		setupDiceBag();
		this.maxScore = 0;
		SoundEffects.rattleDice();
		int rerolls = 0;
		while (maxScore < 70 || maxScore > 80){
			setupTiles();
			getFullWordList();
			getMaxScore();
			//System.out.println(maxScore);
			rerolls++;
		}
		System.out.println("max score: " + this.maxScore + " after " + rerolls + " rolls");
	}
	
	private void setupDiceBag(){
		diceBag = new ArrayList<>();
		for (String diceString : STANDARD_CUBES){
			diceBag.add(new Dice(diceString));
		}
		Collections.shuffle(diceBag);
	}
	
	private void setupTiles(){
		tiles = new char[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
		int diceNum = 0;
		for(int row = 0 ; row < BOARD_DIMENSIONS ; row++){
			for (int col = 0 ; col < BOARD_DIMENSIONS ; col++){
				tiles[row][col] =  diceBag.get(diceNum++).roll();
			}
		}
	}
	
	private void getMaxScore(){
		this.maxScore = getScore(wordList);
	}
	
	static int getTargetScore(int level, int maxScore){
		if (level < 5){
			return  5 + (level * 4);
		} else if (level < 15) {
			return 9 + (level * 3);
		}
		return (maxScore /10) * 9;
	}


	private void getFullWordList(){
		this.wordList = new ArrayList<>();
		for(int startRow = 0 ; startRow < BOARD_DIMENSIONS ; startRow++){
			for (int startCol = 0 ; startCol < BOARD_DIMENSIONS ; startCol++){
				char startChar = tiles[startRow][startCol];
				ArrayList<String> visitedTiles = new ArrayList<>();
				visitedTiles.add("" + startRow + ',' + startCol);
				getWordList("" +startChar, startRow, startCol, visitedTiles);
			}
		}
		Collections.sort(wordList);
	}
	
	private void getWordList(String prefix, int startRow, int startCol, ArrayList<String> previouslyVisitedTiles){
		for (int rowMod = -1 ; rowMod <= 1 ; rowMod++){
			if (startRow + rowMod < 0 || startRow + rowMod >= BOARD_DIMENSIONS)continue; //skip illegal rows
			for (int colMod = -1 ; colMod <=1 ; colMod++){
				if (startCol + colMod < 0 || startCol + colMod >= BOARD_DIMENSIONS) continue; //skip illegal columns
				if (previouslyVisitedTiles.contains(""+ (startRow + rowMod) + ',' + (startCol + colMod)))continue; //skip dice that have already been visited for this word

				String newWord = prefix + tiles[startRow + rowMod][startCol + colMod];
				int dictionarySearch = Dictionary.isPrefixOfWord(newWord);
				if (dictionarySearch >= 2 && !wordList.contains(newWord)){wordList.add(newWord);}
				
				if (dictionarySearch % 2 == 1){ //if the string is a possible prefix of a valid word
					ArrayList<String> visitedTiles = new ArrayList<>(previouslyVisitedTiles);
					visitedTiles.add(""+ (startRow + rowMod) + ',' + (startCol + colMod) );
					getWordList(newWord, startRow + rowMod, startCol + colMod, visitedTiles);
				} 
			}
		}
	}
	
	static int getScore(ArrayList<String> wordList){
		int score = 0;
		for (String word : wordList){
			score += getWordScore(word);
		}
		return score;
	}
	
	static int getWordScore(String word){
		switch(word.length()){
			case 3: return 1;
			case 4:	return 2;
			case 5: return 4;
			case 6:	return 6;
			case 7: return 9;
			default: return 15;
		}
	}
	
}
