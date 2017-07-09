# Scandal

[![Build Status](https://travis-ci.org/lufevida/Scandal.svg?branch=master)](https://travis-ci.org/lufevida/Scandal)

![Screenshot](https://raw.githubusercontent.com/lufevida/Scandal/master/doc/PrintInfo.png)

## Tutorials

- [The syntax of the domain-specific language](https://github.com/lufevida/Scandal/blob/master/doc/Syntax.md)
- [Recording and playing back audio files](https://github.com/lufevida/Scandal/blob/master/doc/Files.md)
- [Synthesizing classic waveforms](https://github.com/lufevida/Scandal/blob/master/doc/Synthesis.md)
- [Applying filters and effects](https://github.com/lufevida/Scandal/blob/master/doc/Effects.md)
- [Mixing, panning and automation](https://github.com/lufevida/Scandal/blob/master/doc/Mixer.md)
- [Using the framework for real-time audio applications](https://github.com/lufevida/Scandal/blob/master/doc/RealTime.md)
- [Polyphonic MIDI synthesizers](https://github.com/lufevida/Scandal/blob/master/doc/Synthesizers.md)

## Using the Compiler class

```java
Compiler compiler = new Compiler();
byte[] bytecode = compiler.compile("doc/NaivePrimeFinder.scandal", args);
compiler.print(bytecode);
compiler.save("bin/NaivePrimeFinder.class", bytecode);
```
