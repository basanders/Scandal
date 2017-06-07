# Scandal

[![Build Status](https://travis-ci.org/lufevida/Scandal.svg?branch=master)](https://travis-ci.org/lufevida/Scandal)

## Plotting waveforms and functions

```java
new AdditiveSquare().plot(2, 512);
WavetableResidual.getSharedInstance().plot(1, 500);
new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0}).plot();
new BiquadPeak().plotMagnitudeResponse(1000, 100, 3);
new WaveFile("monoLisa.wav").plot(1000);
```

## Printing device, settings and file information

```java
Settings.printInfo();
Settings.printDeviceList();
Settings.printMidiDeviceList();
new WaveFile("monoLisa.wav").printInfo();
```

## Using the AudioTask class for timed real-time recording

```java
double[] buffer = new AudioTask(0).record(2000);
new AudioTask(0).playMono(0, buffer);
```

## Using the AudioFlow class for continuous real-time recording

```java
AudioFlow flow = new AudioFlow("test", Settings.mono);
Thread.sleep(2000);
flow.quit();
```

## Using the NaiveOscillator class for non-real-time audio

```java
new AudioTask(0).playMono(0, new NaiveOscillator(new NaiveSawtooth()).get(2000, 0.5, 440));
```

## Using the NoiseGenerator class for non-real-time audio

```java
new AudioTask(0).playMono(0, new NoiseGenerator(new WavetableWhite()).get(2000, 0.5));
```

## Using the WavetableOscillator class for non-real-time audio

```java
new AudioTask(0).playMono(0, new WavetableOscillator(new ClassicSawtooth()).get(2000, 0.5, 440));
```

## Using the WavetableOscillator class for real-time audio

```java
WavetableOscillator saw = new WavetableOscillator(new ClassicSquare());
AudioFlow flow = saw.start();
saw.setFrequency(440);
saw.setAmplitude(0.5);
for (int i = 0; i < 100; i++) {
	Thread.sleep((i * 100) % 500);
	saw.setFrequency((440 * i) % 2000);
}
flow.quit();
```

## Using the BreakpointFunction class

```java
double[] longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 0.5, 0}).get();
double[] glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200}).get();
double[] saw = new WavetableOscillator(new ClassicSawtooth()).get(3000, longEnvelope, glide);
new AudioTask(0).playMono(0, saw);
```

## Using the Reverse class

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] reverse = new Reverse().process(lisa);
new AudioTask(0).playMono(0, reverse);
```

## Using the Speed class

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] speed = new Speed().process(lisa, 1.2);
new AudioTask(0).playMono(0, speed);
```

## Using the Loop class

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] loop = new Loop().process(lisa, 0, 10000, 8);
new AudioTask(0).playMono(0, loop);
```

## Using the Splice class

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] loop1 = new Loop().process(lisa, 0, 12000, 8);
double[] loop2 = new Loop().process(lisa, 0, 6000, 16);
double[] loop3 = new Loop().process(lisa, 0, 3000, 32);
double[] splice = new Splice().process(loop1, loop2, loop3);
new AudioTask(0).playMono(0, splice);
```

## Using the Delay class

```java
double[] lisa = new WaveFile("stereoLisa.wav").getMonoSum();
double[] feedback = new BreakpointFunction(512, new double[]{0.5, 0, 0.5, 0}).get();
double[] mix = new BreakpointFunction(512, new double[]{0.7, 0, 0.5, 0, 0.5, 0, 0.7}).get();
new AudioTask(0).playMono(0, new Delay().process(lisa, 500, feedback, mix));
```

## Using the RingModulator class

```java
double[] envelope = new BreakpointFunction(512, new double[]{0, 1, 0}).get();
double[] speedEnvelope = new BreakpointFunction(512, new double[]{0, 12, 0}).get();
double[] depthEnvelope = new BreakpointFunction(512, new double[]{1, 0.2, 1}).get();
double[] glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200}).get;
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
RingModulator sawRing = new RingModulator(new AdditiveSawtooth());
RingModulator cosineRing = new RingModulator(new WavetableCosine());
AudioTask player = new AudioTask(2);
player.playMono(0, sawRing.process(saw.get(6000, envelope, glide), 0.8, 10));
player.playMono(0, cosineRing.process(saw.get(6000, 0.8, 37), depthEnvelope, speedEnvelope));
player.stop(); // necessary whenever BufferPlayer deals with more than "zero" threads
```

## Using the BiquadLowPass class

```java
double[] wave = new WavetableOscillator(new ClassicSawtooth()).get(4000, 0.5, 440);
double[] cutoff = new BreakpointFunction(512, new double[]{40, 10000, 40, 40}).get();
double[] resonance = new BreakpointFunction(512, new double[]{0, 6, 6, 0}).get();
double[] filter = new BiquadLowPass().process(wave, cutoff, resonance);
new AudioTask(0).playMono(0, filter);
```

## Using both real-time *and* non-real-time processes

```java
WavetableOscillator saws = new WavetableOscillator(new ClassicSawtooth());
AudioFlow flow = saws.start();
saws.setFrequency(37);
saws.setAmplitude(0.1);
double[] envelope = new BreakpointFunction(512, new double[]{0, 1, 0}).get();
double[] longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 0.5, 0}).get();
double[] glide = new BreakpointFunction(512, new double[]{880, 55, 2200, 1100, 4400}).get();
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
WavetableOscillator square = new WavetableOscillator(new ClassicSquare());
WavetableOscillator sine = new WavetableOscillator(new WavetableCosine());
AudioTask player = new AudioTask(10);
double[] buffer;
for (int i = 0; i < 200; i++) {
	buffer = sine.get(30, envelope, Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
	player.playMono((2 * i) * 90, buffer);
	buffer = saw.get(30, envelope, Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
	player.playMono((2 * i + 1) * 90, buffer);
	buffer = square.get(30, envelope, Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
	player.playMono((2 * i + 2) * 90, buffer);
	if (i == 150) {
		buffer = new WavetableOscillator(new ClassicSquare()).get(9999, longEnvelope, glide);
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
flow.quit();
```

## Using the StereoPanner class

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] pan = new BreakpointFunction(512, new double[]{-1, 1}).get();
double[] stereo = new StereoPanner().process(lisa, pan);
new AudioTask(0).playStereo(0, stereo);
```

## Creating a stereo ping-pong effect

```java
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
double[] pingPong = new NaiveSquare().getTable(4, 512);
double[] stereo = new StereoPanner().process(lisa, pingPong);
new AudioTask(0).playStereo(0, stereo);
```

## Using the AudioTrack and StereoMixer classes

```java
double[] saw = new WavetableOscillator(new ClassicSawtooth()).get(4000, 0.7, 880);
double[] lisa = new WaveFile("monoLisa.wav").getMonoSum();
AudioTrack sawTrack = new AudioTrack(saw, 3000, 0.2, -0.8);
AudioTrack lisaTrack = new AudioTrack(lisa, 0, 1, 0.8);
double[] mixdown = new StereoMixer().render(sawTrack, lisaTrack);
new AudioTask().playStereo(0, mixdown);
new AudioTask().exportStereo("mix", mixdown);
```

## Using the PolyphonicSynthesizer class

```java
PolyphonicSynthesizer synth = new PolyphonicSynthesizer(1);
AudioFlow flow = synth.start();
Thread.sleep(10000);
flow.quit();
synth.close();
System.exit(0);
```

### Tasks

- Exponential functions;
- Antialiased triangle generator;
- Test antialiased waveforms with a sweep;
