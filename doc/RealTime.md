### Using the WavetableOscillator class for real-time audio

show the process vector method in the ring modulator class

### Using the AudioFlow class for continuous real-time recording

```java
AudioFlow flow = new AudioFlow("wav/test.wav", Settings.mono);
Thread.sleep(2000);
flow.quit();
```

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

### Using both real-time *and* non-real-time processes

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
AudioTask task = new AudioTask(10);
double[] buffer;
for (int i = 0; i < 200; i++) {
	buffer = sine.get(30, envelope, Math.abs((i + 2) * 440 / 2 * Math.pow(-1, i)) % 2000);
	task.playMono((2 * i) * 90, buffer);
	buffer = saw.get(30, envelope, Math.abs((i + 1) * 440 / 3 * Math.pow(-1, i)) % 3000);
	task.playMono((2 * i + 1) * 90, buffer);
	buffer = square.get(30, envelope, Math.abs(i * 440 / 4 * Math.pow(-1, i)) % 4000);
	task.playMono((2 * i + 2) * 90, buffer);
	if (i == 150) {
		buffer = new WavetableOscillator(new ClassicSquare()).get(9999, longEnvelope, glide);
		task.playMono(2 * i * 90, buffer);
	}
}
task.stop();
Thread.sleep(12000);
saws.setAmplitude(0);
Thread.sleep(6000);
saws.setAmplitude(0.4);
saws.setFrequency(18);
Thread.sleep(12000);
flow.quit();
```