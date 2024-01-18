#!/bin/bash

mkdir outTest
mkdir outTest/propLogicRecognizer

javac -d outTest/propLogicRecognizer -cp lib/antlr4-runtime-4.13.1.jar src/propLogicRecognizer/*.java
cp src/propLogicRecognizer/propLogic.interp outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogic.tokens outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexer.interp outTest/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexer.tokens outTest/propLogicRecognizer

javac -d outTest -cp lib/antlr4-runtime-4.13.1.jar:src src/*.java