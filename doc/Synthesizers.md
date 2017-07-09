### Using the PolyphonicSynthesizer class

```java
int keyboard = Settings.midiController;
PolyphonicSynthesizer synth = new PolyphonicSynthesizer(keyboard, new ClassicSawtooth());
AudioFlow flow = synth.start();
Thread.sleep(10000);
flow.quit();
synth.close();
System.exit(0);
```

### Using the KarplusStrong class

```java
KarplusStrong synth = new KarplusStrong(Settings.midiController);
AudioFlow flow = synth.start();
Thread.sleep(10000);
flow.quit();
synth.close();
System.exit(0);
```