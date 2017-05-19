# Tasks

- Allow negative frequencies?
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

## Plotting a waveform

```java
new AdditiveSquare().plot(2, 512);
```

## Using the `WavetableOscillator` class

```java
BreakpointFunction envelope = new BreakpointFunction(512, new double[]{0, 1, 0});
BreakpointFunction longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0});
BreakpointFunction glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200});
WavetableOscillator sine = new WavetableOscillator(COSINE);
WavetableOscillator saw = new WavetableOscillator(SAWTOOTH);
WavetableOscillator square = new WavetableOscillator(SQUARE);
for (int i = 0; i < 300; i++) {
	sine.start((2 * i) * 60, 40, envelope.getArray(), Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
	saw.start((2 * i + 1) * 60, 30, envelope.getArray(), Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
	square.start((2 * i + 2) * 60, 20, envelope.getArray(), Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
	if (i == 200) new WavetableOscillator(SAWTOOTH).start(2 * i * 60, 10000, longEnvelope.getArray(), glide.getArray());
}
```