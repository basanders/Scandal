### Using the StereoPanner class

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] pan = new BreakpointFunction(512, new double[]{-1, 1}).get();
double[] stereo = new StereoPanner().process(lisa, pan);
new AudioTask().playStereo(stereo);
```

### Creating a stereo ping-pong effect

```java
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
double[] pingPong = new NaiveSquare().getTable(512, 4);
double[] stereo = new StereoPanner().process(lisa, pingPong);
new AudioTask().playStereo(stereo);
```

### Using the AudioTrack and StereoMixer classes

```java
double[] saw = new WavetableOscillator(new ClassicSawtooth()).get(4000, 0.7, 880);
double[] lisa = new WaveFile("wav/monoLisa.wav").getMonoSum();
AudioTrack sawTrack = new AudioTrack(saw, 3000, 0.2, -0.8);
AudioTrack lisaTrack = new AudioTrack(lisa, 0, 1, 0.8);
double[] mixdown = new StereoMixer().render(sawTrack, lisaTrack);
new AudioTask().playStereo(mixdown);
new AudioTask().exportStereo("mix.wav", mixdown);
```
