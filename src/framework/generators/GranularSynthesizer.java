package framework.generators;

import java.util.ArrayList;

import framework.waveforms.Wavetable;

public class GranularSynthesizer extends PolyphonicSynthesizer {
	
	public int playbackPosition = 4410;
	public int playbackDeviation = 441;
	public float playbackSpeed = 1;
	public int grainLength = 4410;
	public int interGrainTime = 44;
	public int grainCount = 256;
	public int windowSize = 8192;
	public double windowIncrement = (double) windowSize / grainLength;
	public float[] window = new HannWindow(windowSize).get();

	public GranularSynthesizer(int controller, Wavetable baseWavetable) throws Exception {
		super(controller, baseWavetable);
		attackSamples = 22050;
		sustainLevel = 1;
		releaseSamples = 44100;
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
		
		final float[] noteBuffer = new float[22050];
		ArrayList<Grain> grainArray = new ArrayList<>();
		int igtCounter = 0;
		int grainArrayCounter = 0;
		
		class Grain {
			
			boolean isBusy;
			private float index;
			private float sample;
			private double windowIndex;
			
			Grain() {
				reset();
			}
			
			void reset() {
				isBusy = false;
				index = playbackPosition + (((float) Math.random() * 2 - 1) * playbackDeviation);
				windowIndex = 0;
			}
			
			float getSample() {
				sample = noteBuffer[(int) index] * window[(int) windowIndex] * 0.1f;
				index += playbackSpeed;
				if (index >= noteBuffer.length) index -= noteBuffer.length;
				windowIndex += windowIncrement;
				if (windowIndex >= windowSize) reset();
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
		
		/*
		 * The GranularNote constructor is called at the moment the PolyphonicSynthesizer superclass
		 * is constructed, hence before the field grainCount has been initialized. So we have to wait
		 * until the superclass has been constructed if we need to use here any property belonging
		 * exclusively to the GranularSynthesizer subclass, thus the need for an extra init method.
		 */
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
				updateGrainArray();
				updateEnvelope();
				if (envelopeStage == ADSR.OFF) {
					for (Grain grain : grainArray) grain.reset();
					grainArray.get(0).isBusy = true;
					igtCounter = 0;
					grainArrayCounter = 0;
				}
			}
			return vector;
		}
		
	}

}
