#!/bin/bash

javac -d outTest src/*.java
javac -d outTest/propLogicRecognizer src/propLogicRecognizer/*.java
java -jar lib/antlr4-runtime-4.13.1.jar src/propLogic.g4
cp src/propLogicRecognizer/propLogic.interp outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogic.tokens outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexer.interp outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexere.tokens outTest/propLogicRecognizer

