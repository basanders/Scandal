## Plotting waveforms and functions

```java
new AdditiveSquare().plot(2, 512);
WavetableResidual.getSharedInstance().plot(1, 500);
new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0}).plot();
```

## Using the WavetableOscillator class for real-time audio

```java
AudioFlow saw = new WavetableOscillator(SAWTOOTH).start();
saw.setFrequency(440);
saw.setAmplitude(0.5);
for (int i = 0; i < 100; i++) {
	Thread.sleep((i * 100) % 500);
	saw.setFrequency((440 * i) % 2000);
}
saw.quit();
```

## Using the WavetableOscillator class for non-real-time audio

```java
BufferPlayer player = new BufferPlayer(0);
player.playMono(0, saw.get(2000, 0.5, 440));
```
## Using the BreakpointFunction class

```java
BreakpointFunction longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0});
BreakpointFunction glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200});
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
BufferPlayer player = new BufferPlayer(0);
player.playMono(0, saw.get(3000, longEnvelope.get(), glide.get()));
```

## Using the Tremolo class

```java
BreakpointFunction envelope = new BreakpointFunction(512, new double[]{0, 1, 0});
BreakpointFunction speedEnvelope = new BreakpointFunction(512, new double[]{0, 12, 0});
BreakpointFunction depthEnvelope = new BreakpointFunction(512, new double[]{1, 0.2, 1});
BreakpointFunction glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200});
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
BufferPlayer player = new BufferPlayer(2);
player.playMono(0, new Tremolo(new AdditiveSawtooth()).process(saw.get(6000, envelope.get(), glide.get()), 0.8, 10));
player.playMono(0, new Tremolo(new WavetableCosine()).process(saw.get(6000, 0.8, 37), depthEnvelope.get(), speedEnvelope.get()));
player.stop(); // necessary whenever BufferPlayer deals with more than "zero" threads
```

## Using both real-time and non-real-time processes

```java
AudioFlow saws = new WavetableOscillator(new ClassicSawtooth()).start();
saws.setFrequency(37);
saws.setAmplitude(0.1);
BreakpointFunction envelope = new BreakpointFunction(512, new double[]{0, 1, 0});
BreakpointFunction longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0});
BreakpointFunction glide = new BreakpointFunction(512, new double[]{880, 55, 2200, 1100, 4400});
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
WavetableOscillator square = new WavetableOscillator(new ClassicSquare());
WavetableOscillator sine = new WavetableOscillator(new WavetableCosine());
BufferPlayer player = new BufferPlayer(10);
double[] buffer;
for (int i = 0; i < 200; i++) {
	buffer = sine.get(30, envelope.get(), Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
	player.playMono((2 * i) * 90, buffer);
	buffer = saw.get(30, envelope.get(), Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
	player.playMono((2 * i + 1) * 90, buffer);
	buffer = square.get(30, envelope.get(), Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
	player.playMono((2 * i + 2) * 90, buffer);
	if (i == 150) {
		buffer = new WavetableOscillator(new ClassicSquare()).get(10000, longEnvelope.get(), glide.get());
		player.playMono(2 * i * 90, buffer);
	}
}
player.stop();
Thread.sleep(12000);
saws.setAmplitude(0);
Thread.sleep(6000);
saws.setAmplitude(0.4);
saws.setFrequency(18);
Thread.sleep(12000);
saws.quit();
```

### Tasks

- Test antialiased waveforms with a sweep;
- Antialiased sawtooth unit test;
- Antialiased triangle generator;
- Antialiased triangle unit test;
- Antialiased square unit test;
- Audio buffer loop;
- Audio file splice;
- Audio buffer splice;
- Audio file reverse;
- Audio buffer reverse;
- Audio file delay;
- Audio buffer delay;
- Audio file speed change;
- Audio buffer speed change;
- Audio buffer recording to file;
- Audio buffer recording to buffer;
- Amplitude modulator;
- Frequency modulator;
- Amplitude controls;
- Reverb controls;
- Filters;
- Convert amplitudes to dB and frequencies to MIDI notes
- Make envelopes and glides logarithmic
- Allow negative frequencies?
