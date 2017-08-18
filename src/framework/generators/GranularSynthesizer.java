package framework.generators;

import java.util.ArrayList;

import framework.waveforms.Wavetable;

public class GranularSynthesizer extends PolyphonicSynthesizer {
	
	public int playbackPosition = 4410;
	public int playbackDeviation = 441;
	public float playbackSpeed = 1;
	public int grainLength = 4410;
	public int interGrainTime = 44;
	public int grainCount = 128;
	public float[] window = new HannWindow(grainLength).get(); // TODO decimate

	public GranularSynthesizer(int controller, Wavetable baseWavetable) throws Exception {
		super(controller, baseWavetable);
		attackSamples = 22050;
		sustainLevel = 1;
		releaseSamples = 88200;
		for (MidiNote note : midiNotes) {
			GranularNote granularNote = (GranularNote) note;
			granularNote.init();
		}
	}
	
	@Override
	public void fillMidiNotesArray() {
		for (int i = 0; i < 128; i++) midiNotes.add(new GranularNote(i));
	}
	
	class GranularNote extends PolyphonicSynthesizer.MidiNote {
		
		final float[] noteBuffer = new float[88200]; // TODO scale
		ArrayList<Grain> grainArray = new ArrayList<>();
		int igtCounter = 0;
		int grainArrayCounter = 0;
		
		class Grain {
			
			boolean isBusy;
			private float index;
			private float sample;
			private int windowIndex;
			
			Grain() {
				reset();
			}
			
			void reset() {
				isBusy = false;
				index = playbackPosition + (((float) Math.random() * 2 - 1) * playbackDeviation);
				windowIndex = 0;
			}
			
			float getSample() {
				sample = noteBuffer[(int) index] * window[windowIndex++] * 0.1f;
				index += playbackSpeed;
				if (index >= noteBuffer.length) index -= noteBuffer.length;
				if (windowIndex >= grainLength) reset();
				return sample;
			}
			
		}

		GranularNote(int midiNoteNumber) {
			super(midiNoteNumber);
			for (int i = 0; i < noteBuffer.length; i++) {
				noteBuffer[i] = baseWavetable.getSample(phase, phaseIncrement);
				phase += phaseIncrement;
				if (phase >= baseWavetable.tableSize) phase -= baseWavetable.tableSize;
			}
		}
		
		void init() {
			for (int i = 0; i < grainCount; i++) grainArray.add(i, new Grain());
			grainArray.get(0).isBusy = true;
		}
		
		void updateGrainArray() {
			igtCounter++;
			if (igtCounter >= interGrainTime) {
				igtCounter -= interGrainTime;
				grainArray.get(grainArrayCounter).isBusy = true;
				grainArrayCounter++;
				if (grainArrayCounter >= grainCount) grainArrayCounter = 0;
			}
		}
		
		@Override
		float[] get() {
			for (int i = 0; i < vector.length; i++) {
				for (Grain grain : grainArray) if (grain.isBusy) vector[i] += grain.getSample();
				vector[i] *= amplitude * envelopeLevel;
				updateEnvelope();
				updateGrainArray();
			}
			return vector;
		}
		
	}

}
