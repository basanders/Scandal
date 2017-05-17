# Tasks

- [ ] Remove modulo counters from classic waveforms;
- Test antialiased waveforms with a sweep;
- Antialiased sawtooth unit test;
- Antialiased triangle generator;
- Antialiased triangle unit test;
- Antialiased square unit test;
- Naive oscillator class;
- Wavetable oscillator class;
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
- Audio file recorder class;
- Audio file player class;
- Audio buffer recorder class;
- Audio buffer player class;
- Amplitude modulator;
- Frequency modulator;
- Amplitude controls;
- Reverb controls;
- Filters

## Plotting a waveform

```java
new AdditiveSquare().plot(2, 512);
```

## Using the `WavetableOscillator` class

```java
WavetableOscillator sine = new WavetableOscillator(COSINE);
WavetableOscillator saw = new WavetableOscillator(SAWTOOTH);
WavetableOscillator square = new WavetableOscillator(SQUARE);
for (int i = 0; i < 400; i++) {
	sine.start((2 * i) * 60, 40, 0.5, Math.abs((i + 1) * 440 / 2 * Math.pow(-1, i)) % 2000);
	saw.start((2 * i + 1) * 60, 30, 0.5, Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
	square.start((2 * i + 2) * 60, 20, 0.5, Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
}
```