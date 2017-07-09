## Applying filters and effects

This tutorial illustrates how to apply non-real-time effects to buffers of audio in both the domain-specific language and the framework.

### Adjusting and automating the gain

The gain of a buffer of samples can be adjusted and automated both in the domain-specific language and in the framework. The preferred method for automating gain, however, is through embedding the buffer in a track, where it is possible to automate the gain and panorama all at once, as well to give the buffer an start time within a piece. Here is how a simple gain change is done in the framework using the `Gain` class:

```
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] automation = new BreakpointFunction(512, new float[]{0, 0.5, 0, 1, 0, 0.5, 0}).get());
float[] gain = new Gain().process(lisa, automation);
new AudioTask().playMono(gain);
```

In the domain-specific language, we get the following:

![Gain](https://github.com/lufevida/Scandal/blob/master/doc/Gain.png)

### Reversing the direction of playback

In the framework, we use the `Reverse` class. It takes only one argument, the buffer you wish to see reversed. Below is an example:

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] reverse = new Reverse().process(lisa);
new AudioTask().playMono(reverse);
```

In the domain-specific language, the same in accomplished via the example below:

![Reverse](https://github.com/lufevida/Scandal/blob/master/doc/Reverse.png)

### Using the Speed class

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] speed = new Speed().process(lisa, 1.2);
new AudioTask().playMono(speed);
```

### Using the Loop class

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] loop = new Loop().process(lisa, 0, 10000, 8);
new AudioTask().playMono(loop);
```

### Using the Splice class

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] loop1 = new Loop().process(lisa, 0, 12000, 8);
double[] loop2 = new Loop().process(lisa, 0, 6000, 16);
double[] loop3 = new Loop().process(lisa, 0, 3000, 32);
double[] splice = new Splice().process(loop1, loop2, loop3);
new AudioTask().playMono(splice);
```

### Using the Delay class

```java
double[] lisa = new WaveFile("wav/stereoLisa.wav").getMonoSum();
double[] feedback = new BreakpointFunction(512, new double[]{0.5, 0, 0.5, 0}).get();
double[] mix = new BreakpointFunction(512, new double[]{0.7, 0, 0.5, 0, 0.5, 0, 0.7}).get();
new AudioTask().playMono(new Delay().process(lisa, 500, feedback, mix));
```

### Using the BiquadLowPass class

```java
double[] wave = new WavetableOscillator(new ClassicSawtooth()).get(4000, 0.5, 440);
double[] cutoff = new BreakpointFunction(512, new double[]{40, 10000, 40, 40}).get();
double[] resonance = new BreakpointFunction(512, new double[]{0, 6, 6, 0}).get();
double[] filter = new BiquadLowPass().process(wave, cutoff, resonance);
new AudioTask().playMono(filter);
```

### Using the RingModulator class

```java
double[] envelope = new BreakpointFunction(512, new double[]{0, 1, 0}).get();
double[] speedEnvelope = new BreakpointFunction(512, new double[]{0, 12, 0}).get();
double[] depthEnvelope = new BreakpointFunction(512, new double[]{1, 0.2, 1}).get();
double[] glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200}).get;
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
RingModulator sawRing = new RingModulator(new AdditiveSawtooth());
RingModulator cosineRing = new RingModulator(new WavetableCosine());
AudioTask task = new AudioTask(2);
task.playMono(sawRing.process(saw.get(6000, envelope, glide), 0.8, 10));
task.playMono(cosineRing.process(saw.get(6000, 0.8, 37), depthEnvelope, speedEnvelope));
task.stop(); // necessary whenever AudioTask deals with more than "zero" threads
```
