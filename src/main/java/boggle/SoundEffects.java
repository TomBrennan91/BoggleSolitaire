package boggle;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class SoundEffects extends Thread {

	private static void playSound(String url){
		try{
			File bell = new File(url);
			Clip clip = AudioSystem.getClip();
			clip.open( AudioSystem.getAudioInputStream(bell));
			clip.start();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	static void ringBell(final int numRings){
		Thread thread = new Thread(() -> {
			int ring = 0;
			while (ring++ < numRings){
				playSound("bell.wav");
				try{
					Thread.sleep((200));
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}
	
	static void rattleDice(){
		playSound("dice rattle.wav");
	}
	
	static void buzzer(){
		playSound("buzzer.wav");
	}
	
	static void hurryUp(){
		playSound("ticktock.wav");
	}
}
