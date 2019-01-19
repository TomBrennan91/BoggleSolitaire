package boggle;


public class Game {
	
	public static void main(String[] args){
		Dictionary dictionary = new Dictionary();
		dictionary.ReadWordList("wordlist.txt");
		new UserInterface();
	
	}
}
