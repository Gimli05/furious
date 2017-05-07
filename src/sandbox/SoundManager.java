package sandbox;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	private static Clip backgroundMusic;
	private static Clip trainSound;

	public static void playBackgroundMusic() {
		AudioInputStream ais = null;
		try {
			backgroundMusic = AudioSystem.getClip();

			if (backgroundMusic != null) {
				ais = AudioSystem.getAudioInputStream(new File(GameGUI.imageURL + "Music/Background.wav"));
			}
			if (ais != null) {
				backgroundMusic.open(ais);
			}
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
			backgroundMusic.start();

		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			System.out.println(e.getClass());
		}
	}
	
	public static void playTrainSound() {
		AudioInputStream ais = null;
		try {
			trainSound = AudioSystem.getClip();

			if (trainSound != null) {
				ais = AudioSystem.getAudioInputStream(new File(GameGUI.imageURL + "Music/Train.wav"));
			}
			if (ais != null) {
				trainSound.open(ais);
			}
			FloatControl gainControl = 
				    (FloatControl) trainSound.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-15.0f);
			trainSound.loop(Clip.LOOP_CONTINUOUSLY);
			trainSound.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			System.out.println(e.getClass());
		}
	}

	public static void stopBackgroundMusic() {
		backgroundMusic.stop();
		backgroundMusic.close();
	}

	public static void stopTrainSound() {
		trainSound.stop();
		trainSound.close();
	}
	
	public static double durationInSeconds(AudioInputStream audioInputStream) {
		AudioFormat format = audioInputStream.getFormat();
		long frames = audioInputStream.getFrameLength();
		return (frames + 0.0) / format.getFrameRate();
	}

	public static void playSound(final String Name) {
		Thread sound = new Thread() {
			String name=Name;
			public void run() {
				AudioInputStream ais = null;
				Clip clip = null;
				try {
					clip = AudioSystem.getClip();

					if (clip != null) {
						ais = AudioSystem.getAudioInputStream(new File(GameGUI.imageURL + "Music/" + name + ".wav"));
					}
					if (ais != null) {
						clip.open(ais);
					}
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.start();

					Thread.sleep((long) (durationInSeconds(ais) * 1000));
					clip.stop();
					clip.close();

				} catch (LineUnavailableException | UnsupportedAudioFileException | IOException
						| InterruptedException e) {
					System.out.println(e.getClass());
				}
			}
			
		};
		sound.start();
	}

}
