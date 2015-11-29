package org.deepserver.td15.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;
import org.lwjgl.stb.STBVorbisInfo;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.ALUtil.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;

public class AudioManager {

	private static Logger log = Logger.getLogger(AudioManager.class);

	/**
	 * I am a Singleton.
	 * God, i loooooove design patterns <3
	 */
	private static AudioManager inst;

	static {
		inst = new AudioManager();
	}

	private AudioManager() {
		initialize();
	}

	public static AudioManager getInstance() {
		return inst;
	}

	/**
	 * End of Singleton stuff
	 */

	protected ALDevice device;
	protected ALCCapabilities caps;
	protected ALContext context;


	public static void main(String[] args) throws InterruptedException {
		AudioManager am = new AudioManager();
		am.initialize();
		am.printDebugStuff();

		try {
			am.play(SoundEffect.ENGINE);

			Thread.sleep(200000);
		} finally {
			am.shutdown();
		}
	}

	public void initialize() {
		device = ALDevice.create(null);
		caps = device.getCapabilities();
		context = ALContext.create(device);
	}

	public void shutdown() {
		context.destroy();
		device.destroy();
	}

	public void play(SoundEffect s) {
		switch (s) {
		case SHOT:
			new Thread() {
				public void run() {
					playOGG("shot", false);
				}
			}.start();
			break;
		case KILL:
			new Thread() {
				public void run() {
					playOGG("explosion", false);
				}
			}.start();
			break;
		case FLY:
			new Thread() {
				public void run() {
					playOGG("flying", false);
				}
			}.start();
			break;
		case ENGINE:
			new Thread() {
				public void run() {
					playOGG("woauwoauwoau", false);
				}
			}.start();
			break;
		case MUSIC:
			new Thread() {
				public void run() {
					playOGG("09_Life_on_Mars", true);
				}
			}.start();
			break;
		case FRR:
			new Thread() {
				public void run() {
					playOGG("frr", true);
				}
			}.start();
			break;
		case BREMSE:
			new Thread() {
				public void run() {
					playOGG("bremse", false);
				}
			}.start();
			break;
		case CONFIRM:
			new Thread() {
				public void run() {
					playOGG("confirm", true);
				}
			}.start();
			break;
		default:
			break;
		}
	}

	private void playOGG(String soundname, boolean stereo) {
		STBVorbisInfo info = STBVorbisInfo.malloc();
		ByteBuffer pcm = readVorbis("res/"+soundname+".ogg", 32 * 1024, info);

		// generate buffers and sources
		int buffer = alGenBuffers();
		checkALError();
		int source = alGenSources();
		checkALError();

		// copy to buffer
		alBufferData(buffer, stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16, pcm, info.sample_rate());
		checkALError();

		// free vorbis info
		info.free();

		// set up source input
		alSourcei(source, AL_BUFFER, buffer);
		checkALError();

		// play our source
		alSourcePlay(source);
		checkALError();

		int bufferSize = alGetBufferi(buffer, AL_SIZE);
		int frequency = alGetBufferi(buffer, AL_FREQUENCY);
		int channels = alGetBufferi(buffer, AL_CHANNELS);
		int bitsPerSample = alGetBufferi(buffer, AL_BITS);
		double length = (1d * bufferSize) / (frequency * channels * (bitsPerSample / (stereo?16:8)));
		int millis = (int) (length * 1000);
		log.debug("AL_BUFFER: " + buffer);
		log.debug("AL_SOURCE: " + source);
		log.debug("AL_SIZE: " + bufferSize);
		log.debug("AL_FREQUENCY: " + frequency);
		log.debug("AL_CHANNELS: " + channels);
		log.debug("AL_BITS: " + bitsPerSample);
		log.debug("Length in Millis: " + millis);

		// wait for sound to finish
		try {
			log.info("Waiting " + millis + " milliseconds for sound to complete");
			Thread.sleep(millis);
		} catch (InterruptedException inte) {
			//
			log.warn("Playing sound " + soundname + " was aborted!");
		} finally {
			// stop source
			alSourceStop(source);
			checkALError();

			// delete buffers and sources
			alDeleteSources(source);
			checkALError();
			alDeleteBuffers(buffer);
			checkALError();
		}
	}

	private static ByteBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) {
		ByteBuffer vorbis;
		try {
			vorbis = ioResourceToByteBuffer(resource, bufferSize);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		if (decoder == NULL)
			throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

		stb_vorbis_get_info(decoder, info);

		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

		ByteBuffer pcm = BufferUtils.createByteBuffer(lengthSamples * 2);

		stb_vorbis_get_samples_short_interleaved(decoder, info.channels(), pcm, lengthSamples);
		stb_vorbis_close(decoder);

		return pcm;
	}

	private static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;
		File file = new File(resource);

		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);

			while (fc.read(buffer) != -1)
				;

			fc.close();
			fis.close();
		} else {
			buffer = BufferUtils.createByteBuffer(bufferSize);

			InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (source == null)
				throw new FileNotFoundException(resource);

			try {
				ReadableByteChannel rbc = Channels.newChannel(source);
				try {
					while (true) {
						int bytes = rbc.read(buffer);
						if (bytes == -1)
							break;
						if (buffer.remaining() == 0)
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				} finally {
					rbc.close();
				}
			} finally {
				source.close();
			}
		}

		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	private void printDebugStuff() {
		log.debug("OpenALC10: " + caps.OpenALC10);
		log.debug("OpenALC11: " + caps.OpenALC11);
		log.debug("caps.ALC_EXT_EFX = " + caps.ALC_EXT_EFX);
		if (caps.OpenALC11) {
			List<String> devices = ALC.getStringList(0L, ALC_ALL_DEVICES_SPECIFIER);
			for (int i = 0; i < devices.size(); i++) {
				log.debug("Device No. " + i + ": " + devices.get(i));
			}
		}
		log.debug("Default device: " + alcGetString(0L, ALC_DEFAULT_DEVICE_SPECIFIER));
		log.debug("ALC_FREQUENCY: " + alcGetInteger(device.address(), ALC_FREQUENCY) + "Hz");
		log.debug("ALC_REFRESH: " + alcGetInteger(device.address(), ALC_REFRESH) + "Hz");
		log.debug("ALC_SYNC: " + (alcGetInteger(device.address(), ALC_SYNC) == ALC_TRUE));
		log.debug("ALC_MONO_SOURCES: " + alcGetInteger(device.address(), ALC_MONO_SOURCES));
		log.debug("ALC_STEREO_SOURCES: " + alcGetInteger(device.address(), ALC_STEREO_SOURCES));
	}

}
