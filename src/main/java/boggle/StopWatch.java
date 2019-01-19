package boggle;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

class StopWatch {
	static Timer timer;


	StopWatch(final int seconds, final JLabel time, final UserInterface ui){
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = seconds;
			public void run() {
				time.setText("Time: " + i--);
				if (i == 2 || i == 4|| i == 6)
					SoundEffects.hurryUp();
				if (i< 0){
					timer.cancel();
					ui.showAllWords();
				}

			}
		}, 0, 1000);
	}
}
