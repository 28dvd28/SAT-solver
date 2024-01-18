@echo off

mkdir outCompiled

javac -d outCompiled -cp lib/antlr4-runtime-4.13.1.jar src/propLogicRecognizer/*.java
copy "src\propLogicRecognizer\propLogic.interp" "outCompiled\propLogicRecognizer"
copy "src\propLogicRecognizer\propLogic.tokens" "outCompiled\propLogicRecognizer"
copy "src\propLogicRecognizer\propLogicLexer.interp" "outCompiled\propLogicRecognizer"
copy "src\propLogicRecognizer\propLogicLexer.tokens" "outCompiled\propLogicRecognizer"

javac -d outCompiled -cp "lib/antlr4-runtime-4.13.1.jar;src" src/*.java