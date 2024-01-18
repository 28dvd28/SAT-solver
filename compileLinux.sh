#!/bin/bash

mkdir outCompiled

javac -d outCompiled -cp lib/antlr4-runtime-4.13.1.jar src/propLogicRecognizer/*.java
cp src/propLogicRecognizer/propLogic.interp outCompiled/propLogicRecognizer
cp src/propLogicRecognizer/propLogic.tokens outCompiled/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexer.interp outCompiled/propLogicRecognizer
cp src/propLogicRecognizer/propLogicLexer.tokens outCompiled/propLogicRecognizer

javac -d outCompiled -cp lib/antlr4-runtime-4.13.1.jar:src src/*.java