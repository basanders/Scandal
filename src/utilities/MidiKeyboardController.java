package utilities;

import java.io.File;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public abstract class MidiKeyboardController implements Receiver {

	private final MidiDevice device;

	public MidiKeyboardController(int number) throws Exception {
		device = MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[number]);
		device.open();
		device.getTransmitter().setReceiver(this);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		int statusByte = message.getMessage()[0];
		int firstByte = message.getMessage()[1];
		int secondByte = message.getMessage()[2];
		int rawStatus = statusByte & 0xF0; // without channel
		int channelByte = statusByte & 0x0F;
		switch (rawStatus) {
		case 0x80: {
			handleNoteOff(firstByte, secondByte, channelByte);
		} break;
		case 0x90: {
			handleNoteOn(firstByte, secondByte, channelByte);
		} break;
		case 0xA0: {
			handleAftertouch(firstByte, secondByte, channelByte);
		} break;
		case 0xB0: {
			handleControlChange(firstByte, secondByte, channelByte);
		} break;
		case 0xC0: {
			handleProgramChange(firstByte, channelByte);
		} break;
		case 0xD0: {
			handleChannelAftertouch(firstByte, channelByte);
		} break;
		case 0xE0: {
			handlePitchBend(firstByte, secondByte, channelByte);
		} break;
		default: break;
		}
	}

	public abstract void handleNoteOff(int note, int velocity, int channel);

	public abstract void handleNoteOn(int note, int velocity, int channel);

	public abstract void handleAftertouch(int note, int pressure, int channel);

	public abstract void handleControlChange(int controller, int value, int channel);

	public abstract void handleProgramChange(int program, int channel);

	public abstract void handleChannelAftertouch(int pressure, int channel);

	public abstract void handlePitchBend(int lsb, int msb, int channel);

	public double midiToFrequency(int note) {
		return 440 * Math.pow(2, ((double) note - 69) / 12);
	}

	public void playSequence(File file) throws Exception {
		Sequence sequence = MidiSystem.getSequence(file);
		for (Track track : sequence.getTracks()) {
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				long timeStamp = event.getTick();
				MidiMessage message = event.getMessage();
				send(message, timeStamp);
			}
		}
	}

	@Override
	public void close() {
		device.close();
	}

}
