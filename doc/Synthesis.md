### Using the NaiveOscillator class for non-real-time audio

```java
new AudioTask().playMono(new NaiveOscillator(new NaiveSawtooth()).get(2000, 0.5, 440));
```

### Using the NoiseGenerator class for non-real-time audio

```java
new AudioTask().playMono(new NoiseGenerator(new WavetableWhite()).get(2000, 0.5));
```

### Using the WavetableOscillator class for non-real-time audio

```java
new AudioTask().playMono(new WavetableOscillator(new ClassicSawtooth()).get(2000, 0.5, 440));
```