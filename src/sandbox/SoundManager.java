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

/**Hangok lejatszasaert felelos osztaly**/
public class SoundManager {
	/**Hatterzene**/
	private static Clip backgroundMusic;
	/**Zakatolas**/
	private static Clip trainSound;
	/**Liftes zene**/
	private static Clip menuSound;

	/**Elinditja a menu zenet loopolva*/
	public static void playMenuMusic() {
		AudioInputStream ais = null;
		try {
			menuSound = AudioSystem.getClip();

			if (menuSound != null) {
				ais = AudioSystem.getAudioInputStream(new File(GameGUI.imageURL + "Music/Menu.wav"));
			}
			if (ais != null) {
				menuSound.open(ais);
			}
			menuSound.loop(Clip.LOOP_CONTINUOUSLY);
			menuSound.start();

		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			System.out.println(e.getClass());
		}
	}
	
	/**Elinditja a hatter zenet loopolva*/
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
	
	/**Elinditja a zakatolast loopolva*/
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
	/**Megallitja a hatter zenet loopolva*/
	public static void stopBackgroundMusic() {
		backgroundMusic.stop();
		backgroundMusic.close();
	}
	/**Megallitja a zakatolast loopolva*/
	public static void stopTrainSound() {
		trainSound.stop();
		trainSound.close();
	}
	/**Megallitja a menu zenet loopolva*/
	public static void stopMenuMusic() {
		menuSound.stop();
		menuSound.close();
	}
	/**Megadja az adott zene hosszat masodpercben*/
	public static double durationInSeconds(AudioInputStream audioInputStream) {
		AudioFormat format = audioInputStream.getFormat();
		long frames = audioInputStream.getFrameLength();
		return (frames + 0.0) / format.getFrameRate();
	}

	/**Elinditja a megadott nevu hangfajlt**/
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
