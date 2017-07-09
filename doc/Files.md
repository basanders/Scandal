### Printing device, settings and file information

```java
System.out.println(Settings.getInfo());
new WaveFile("wav/monoLisa.wav").printInfo();
```

### Using the BreakpointFunction class

```java
double[] longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 0.5, 0}).get();
double[] glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200}).get();
double[] saw = new WavetableOscillator(new ClassicSawtooth()).get(3000, longEnvelope, glide);
new AudioTask().playMono(saw);
```

### Using the AudioTask class for timed real-time recording

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] reverse = new Reverse().process(lisa);
new AudioTask().playMono(reverse);
```

```java
double[] buffer = new AudioTask().record(2000);
new AudioTask().playMono(buffer);
```


### Plotting waveforms and functions

```java
new AdditiveSquare().plot(512, 2);
WavetableResidual.getSharedInstance().plot(500, 1);
new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0}).plot();
new BiquadPeak().plotMagnitudeResponse(1000, 100, 3);
new WaveFile("wav/monoLisa.wav").plot(1000);
```