package boggle;

import java.util.Random;

class Dice {
	private String letters;
	
	Dice(String letters) {
		this.letters = letters;
	}
	
	char roll(){
		Random r = new Random();
		return letters.charAt(r.nextInt(6));
	} 	
}
