[![Build Status](https://travis-ci.org/lufevida/Scandal.svg?branch=master)](https://travis-ci.org/lufevida/Scandal)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# Scandal

Scandal is both a Java framework and a domain-specific language designed to manipulate sounds.

![Screenshot](https://raw.githubusercontent.com/lufevida/Scandal/master/doc/Screenshot.png)

## Acknowledgments

Scandal utilizes the [ASM](http://asm.ow2.org) allppurpose Java bytecode manipulation and analysis framework for compiling its domain-specific language. Plots are made possible by the [JFreeChart](http://www.jfree.org/jfreechart) and the [JCommon](http://www.jfree.org/jcommon) libraries. Scandal is being developed by [Luis F. Vieira Damiani](http://vieira-damiani.com) under the orientation of [Dr. Beverly Sanders](https://www.cise.ufl.edu/people/faculty/sanders).

## Using the domain-specific language

### Printing device and settings information

```
string devices = info
print(devices)
```

### Plotting waveforms

```
array lisa = read("monoLisa.wav", mono)
plot("Lisa", lisa, 2000)
```

### Capturing audio into a buffer and exporting to a file

```
/* start talking... */
array recording = record(3000)
/* stop talking... */
play(recording, mono)
write(recording, "test.wav", mono)
```

### Adjusting the gain

```
array lisa = read("monoLisa.wav", mono)
lisa = gain(lisa, 0.5)
play(lisa, mono)
```

### Adjusting the panorama

```
array lisa = read("monoLisa.wav", mono)
lisa = pan(lisa, -0.3)
play(lisa, stereo)
```

### Reversing the direction of playback

```
array lisa = read("monoLisa.wav", mono)
lisa = reverse(lisa)
play(lisa, mono)
```

### Changing the speed of playback

```
array lisa = read("monoLisa.wav", mono)
lisa = speed(lisa, 0.7)
play(lisa, mono)
```

### Looping portions of a buffer

```
array lisa = read("monoLisa.wav", mono)
/* Parameters: array, start sample, end sample, count */
lisa = loop(lisa, 0, 4410, 9)
play(lisa, mono)
```

### Splicing buffers together

```
array lisa = read("monoLisa.wav", mono)
array loop1 = loop(0, 4000, 8)
array loop2 = loop(4000, 8000, 8)
array loop3 = loop(8000, 12000, 8)
lisa = splice(loop1, loop2, loop3)
play(lisa, mono)
```

### Creating a delay effect

```
array lisa = read("monoLisa.wav", mono)
/* Parameters: array, milliseconds, feedback, mix */
lisa = delay(lisa, 700, 0.5, 0.5)
play(lisa, mono)
```

### Producing a tremolo effect

```
format channels = mono
array lisa = read("monoLisa.wav", channels)
float depth = 0.9
int speed = 12
waveform shape = cosine
lisa = tremolo(lisa, depth, speed, shape)
play(lisa, channels)
```

### Applying a low-pass filter

```
int cutoff = 1000
float resonance = 1.0
filter mode = hipass /* allpass, bandpass, bandstop, lowpass, hipass, lowshelf, hishelf, peaking */
lisa = biquad(lisa, cutoff, resonance, mode)
play(lisa, channels)
```

### Synthesizing classic waveforms

```
int duration = 2000
float amplitude = 0.5
float frequency = 440.0
waveform shape = sawtooth /* cosine, sawtooth, square, triangle, noise */
array saw = oscillator(duration, amplitude, frequency, shape)
play(saw, mono)
```

### Breakpoint functions

```
int size = 512
array breakpoints = [2, 1, 3.14, 0, 5]
array envelope = line(size, breakpoints)
plot("A scandalous automation line", envelope, size)
```

![Screenshot](https://raw.githubusercontent.com/lufevida/Scandal/master/doc/Plot.png)

## The language syntax

### Concrete syntax

- program := (declaration | statement)\*
- block := LBRACE ( declaration | statement )\* RBRACE
- declaration := unassignedDeclaration | assignmentDeclaration
- unassignedDeclaration := type IDENT
- assignmentDeclaration := type IDENT ASSIGN expression
- type := KW\_INT | KW\_FLOAT | KW\_BOOL | KW\_STRING | KW\_ARRAY | KW\_FORMAT
- statement := assignmentStatement | ifStatement | whileStatement | frameworkStatement
- assignmentStatement := IDENT ASSIGN expression
- ifStatement := KW\_IF expression block
- whileStatement := KW\_WHILE expression block
- frameworkStatement := printStatement | plotStatement | playStatement | writeStatement
- printStatement := KW\_PRINT LPAREN expression RPAREN
- plotStatement := KW\_PLOT LPAREN expression COMMA expression COMMA expression RPAREN
- playStatement := KW\_PLAY LPAREN expression COMMA expression RPAREN
- writeStatement := KW\_WRITE LPAREN expression COMMA expression COMMA expression RPAREN
- expression := term (termOperator term)\*
- termOperator := LT | LE | GT | GE | EQUAL | NOTEQUAL
- term := summand (summandOperator summand)\*
- summandOperator := PLUS | MINUS | OR
- summand := factor (factorOperator factor)\*
- factorOperator := TIMES | DIV | MOD | AND
- factor := LPAREN expression RPAREN | identExpression | literalExpression | unaryExpression | frameworkExpression
- identExpression := IDENT
- literalExpression := intLitExpression | floatLitExpression | boolLitExpression | stringLitExpression | arrayLitExpression
- intLitExpression := INT\_LIT
- floatLitExpression := FLOAT\_LIT
- boolLitExpression := KW\_TRUE | KW\_FALSE
- stringLitExpression := STRING\_LIT
- arrayLitExpression := LBRACKET ((FLOAT\_LIT | INT\_LIT) (COMMA (FLOAT\_LIT | INT\_LIT))\*)\* RBRACKET
- unaryExpression := (KW\_MINUS | KW\_NOT) expression
- frameworkExpression := infoExpression | readExpression | formatExpression | gainExpression | lineExpression
- frameworkExpression := reverseExpression | speedExpression | spliceExpression | loopExpression | delayExpression
- frameworkExpression := filterExpression | biquadExpression | waveformExpression | oscillatorExpression | tremoloExpression
- frameworkExpression := panExpression | recordExpression
- infoExpression := KW\_INFO
- readExpression := KW\_READ LPAREN expression COMMA expression RPAREN
- formatExpression := KW\_MONO | KW\_STEREO
- reverseExpression := KW\_REVERSE LPAREN expression RPAREN
- speedExpression := KW\_SPEED LPAREN expression COMMA expression RPAREN
- loopExpression := KW\_LOOP LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- delayExpression := KW\_DELAY LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- spliceExpression := KW\_SPLICE LPAREN expression (COMMA expression)\* RPAREN
- gainExpression := KW\_GAIN LPAREN expression COMMA expression RPAREN
- lineExpression := KW\_LINE LPAREN expression COMMA expression RPAREN
- filterExpression := KW\_ALLPASS | KW\_LOWPASS | KW\_HIPASS | KW\_BANDPASS
- filterExpression := KW\_BANDSTOP | KW\_LOWSHELF | KW\_HISHELF | KW\_PEAKING
- biquadExpression := KW\_BIQUAD LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- waveformExpression := KW\_COSINE | KW\_SAWTOOTH | KW\_SQUARE | KW\_TRIANGLE | KW\_NOISE
- oscillatorExpression := KW\_OSCILLATOR LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- tremoloExpression := KW\_TREMOLO LPAREN expression COMMA expression COMMA expression COMMA expression RPAREN
- panExpression := KW\_PAN PAREN expression COMMA expression RPAREN
- recordExpression := KW\_RECORD PAREN expression RPAREN

### Abstract syntax

- Program := (Declaration | Statement)\*
- Block := (Declaration | Statement)\*
- Declaration := UnassignedDeclaration | AssignmentDeclaration
- UnassignedDeclaration := Type IDENT
- AssignmentDeclaration := Type IDENT Expression
- Type := INT | FLOAT | BOOL | STRING | ARRAY | FORMAT
- Statement := AssignmentStatement | IfStatement | WhileStatement | FrameworkStatement
- AssignmentStatement := IDENT Expression
- IfStatement := Expression Block
- WhileStatement := Expression Block
- FrameworkStatement := PrintStatement | PlotStatement | PlayStatement | WriteStatement
- PrintStatement := Expression
- PlotStatement := Expression\_0 Expression\_1 Expression\_2
- PlayStatement := Expression\_0 Expression\_1
- WriteStatement := Expression\_0 Expression\_1 Expression\_2
- Expression := BinaryExpression | IdentExpression | LiteralExpression | UnaryExpression | FrameworkExpression
- UnaryExpression := Expression
- BinaryExpression := Expression\_0 (termOperator | summandOperator | factorOperator) Expression\_1
- ReadExpression := Expression\_0 Expression\_1
- ReverseExpression := Expression
- SpeedExpression := Expression\_0 Expression\_1
- LoopExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- DelayExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- SpliceExpression := Expression+
- GainExpression := Expression\_0 Expression\_1
- LineExpression := Expression\_0 Expression\_1
- BiquadExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- OscillatorExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- TremoloExpression := Expression\_0 Expression\_1 Expression\_2 Expression\_3
- PanExpression := Expression\_0 Expression\_1
- RecordExpression := Expression

### TypeChecker rules

- UnassignedDeclaration:
	+ Variable may not be declared more than once in the same scope
- AssignmentDeclaration:
	+ Variable may not be declared more than once in the same scope
	+ Type = Expression.type
- AssignmentStatement:
	+ Variable must have been declared in some enclosing scope
	+ Declaration.Type = Expression.type
- IfStatement:
	+ Expression.type = BOOL
- WhileStatement:
	+ Expression.type = BOOL
- PrintStatement:
	+ Expression.type != ARRAY | FORMAT | FILTER | WAVEFORM
- PlotStatement:
	+ Expression\_0.type = STRING
	+ Expression\_1.type = ARRAY
	+ Expression\_2.type = INT | FLOAT
- PlayStatement:
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = FORMAT
- WriteStatement:
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = STRING
	+ Expression\_2.type = FORMAT
- UnaryExpression:
	+ Type = Expression.type
	+ Expression.type = INT | FLOAT | BOOL
- BinaryExpression:
	+ (INT | FLOAT) (MOD | PLUS | MINUS | TIMES | DIV) (FLOAT | INT) ==> FLOAT
	+ INT (summandOperator | factorOperator) INT ==> INT
	+ (INT | BOOL) (AND | OR) (BOOL | INT) ==> BOOL
	+ !(ARRAY | STRING) termOperator !(STRING | ARRAY) ==> BOOL
- IdentExpression:
	+ Variable must have been declared in some enclosing scope
	+ Type = Declaration.type
- IntLitExpression:
	+ Type = INT
- FloatLitExpression:
	+ Type = FLOAT
- BoolLitExpression:
	+ Type = BOOL
- StringLitExpression:
	+ Type = STRING
- ArrayLitExpression:
	+ Type = ARRAY
- InfoExpression:
	+ Type = STRING
- FormatExpression:
	+ Type = FORMAT
- WaveformExpression:
	+ Type = WAVEFORM
- FilterExpression:
	+ Type = FILTER
- ReadExpression:
	+ Type = ARRAY
	+ Expression\_0.type = STRING
	+ Expression\_1.type = FORMAT
- ReverseExpression:
	+ Type = ARRAY
	+ Expression.type = ARRAY
- SpeedExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT
- LoopExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT
	+ Expression\_2.type = INT | FLOAT
	+ Expression\_3.type = INT | FLOAT
- DelayExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = INT | FLOAT | ARRAY
- SpliceExpression:
	+ Type = ARRAY
	+ Expression.type = ARRAY
- GainExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
- LineExpression:
	+ Type = ARRAY
	+ Expression\_0.type = INT | FLOAT
	+ Expression\_1.type = ARRAY
- BiquadExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = FILTER
- OscillatorExpression:
	+ Type = ARRAY
	+ Expression\_0.type = INT | FLOAT
	+ Expression\_1.type = INT | FLOAT | ARRAY
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = WAVEFORM
- TremoloExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
	+ Expression\_2.type = INT | FLOAT | ARRAY
	+ Expression\_3.type = WAVEFORM
- PanExpression:
	+ Type = ARRAY
	+ Expression\_0.type = ARRAY
	+ Expression\_1.type = INT | FLOAT | ARRAY
- RecordExpression:
	+ Type = ARRAY
	+ Expression.type = INT | FLOAT

## Using the framework

### Printing device, settings and file information

```java
System.out.println(Settings.getInfo());
new WaveFile("doc/monoLisa.wav").printInfo();
```

### Plotting waveforms and functions

```java
new AdditiveSquare().plot(512, 2);
WavetableResidual.getSharedInstance().plot(500, 1);
new BreakpointFunction(512, new float[]{0, 0.5, 0, 1, 0, 1, 0, 0.5, 0}).plot();
new BiquadPeak().plotMagnitudeResponse(1000, 100, 3);
new WaveFile("doc/monoLisa.wav").plot(1000);
```

### Using the AudioTask class for recording and exporting audio

```java
AudioTask task = new AudioTask();
float[] buffer = task.record(2000);
int channels = 1;
task.export(buffer, "doc/test.wav", channels);
task.playMono(buffer);
```

### Using the Gain class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] gain = new Gain().process(lisa, 0.5);
new AudioTask().playMono(gain);
```

### Using the StereoPanner class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] pingPong = new NaiveSquare().getTable(512, 4);
float[] stereo = new StereoPanner().process(lisa, pingPong);
new AudioTask().playStereo(stereo);
```

### Using the Reverse class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] reverse = new Reverse().process(lisa);
new AudioTask().playMono(reverse);
```

### Using the Speed class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] speed = new Speed().process(lisa, 1.2);
new AudioTask().playMono(speed);
```

### Using the Loop class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] loop = new Loop().process(lisa, 0, 10000, 8);
new AudioTask().playMono(loop);
```

### Using the Splice class

```java
float[] lisa = new WaveFile("doc/monoLisa.wav").getMonoSum();
float[] loop1 = new Loop().process(lisa, 0, 12000, 8);
float[] loop2 = new Loop().process(lisa, 0, 6000, 16);
float[] loop3 = new Loop().process(lisa, 0, 3000, 32);
float[] splice = new Splice().process(loop1, loop2, loop3);
new AudioTask().playMono(splice);
```

### Using the Delay class

```java
float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
new AudioTask().playMono(new Delay().process(lisa, 500, 0.9, 0.7));
```

### Using the RingModulator class

```java
float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
RingModulator tremolo = new RingModulator(new WavetableCosine());
new AudioTask().playMono(tremolo.process(lisa, 0.8, 10));
```

### Using the Biquad class

```java
float[] lisa = new WaveFile("doc/stereoLisa.wav").getMonoSum();
float[] filter = new BiquadLowPass().process(lisa, 1000, 1);
new AudioTask().playMono(filter);
```

### Using the NaiveOscillator class

```java
new AudioTask().playMono(new NaiveOscillator(new NaiveSawtooth()).get(2000, 0.5, 440));
```

### Using the NoiseGenerator class

```java
new AudioTask().playMono(new NoiseGenerator(new WavetableWhite()).get(2000, 0.5));
```

### Using the WavetableOscillator class

```java
new AudioTask().playMono(new WavetableOscillator(new ClassicSawtooth()).get(2000, 0.5, 440));
```

### Using the BreakpointFunction class

```java
float[] longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 0.5, 0}).get();
float[] glide = new BreakpointFunction(512, new double[]{880, 110, 2200, 2200}).get();
float[] saw = new WavetableOscillator(new ClassicSawtooth()).get(3000, longEnvelope, glide);
new AudioTask().playMono(saw);
```

### Managing multiple threads

```java
float[] envelope = new BreakpointFunction(512, new double[]{0, 1, 0}).get();
float[] longEnvelope = new BreakpointFunction(512, new double[]{0, 0.5, 0, 1, 0, 0.5, 0}).get();
float[] glide = new BreakpointFunction(512, new double[]{880, 55, 2200, 1100, 4400}).get();
WavetableOscillator saw = new WavetableOscillator(new ClassicSawtooth());
WavetableOscillator square = new WavetableOscillator(new ClassicSquare());
WavetableOscillator sine = new WavetableOscillator(new WavetableCosine());
AudioTask task = new AudioTask(10); // use 10 threads
float[] buffer;
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
task.stop(); // necessary whenever managing more than one thread
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

### Using the AudioFlow class for real-time recording

```java
AudioFlow flow = new AudioFlow("doc/test.wav", Settings.mono);
Thread.sleep(2000);
flow.quit();
```

### Using the WavetableOscillator class for real-time synthesis

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

### Using the Compiler class

```java
Compiler compiler = new Compiler();
byte[] bytecode = compiler.compile("doc/Screenshot.scandal", args);
compiler.print(bytecode);
compiler.save("bin/Example.class", bytecode);
```
