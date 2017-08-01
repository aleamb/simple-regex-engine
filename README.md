# simple-regex-engine


Simplified regex engine for educational purposes.

## Table of Contents.


## Description.

simple-regex-engine is a engine implemented in Java to parse a tiny (very, very tiny) subset of PCRE. This implementation is only for educational purposes.

This file describes architecture of engine and algorimths and strategies implemented. Basically the engine parses regular expression through a recursive-descent parser and builds a deterministic finite automaton. Then execute that automaton for the input .

### Características regexp soportadas.


| x        | a character                               |
|----------|-------------------------------------------|
| \t       | Tab character                             |
| \s       | White space                               |
| [abc]    | One of characters inside [ ]              |
| [a-z]    | character range 'a' to 'z'                |
| [a-zA-Z] | character range 'a' to 'z' and 'A' to 'Z' |
| *        | Matches between zero and unlimited times  |
| +        | Maches between one and unlimited times    |
| ?        | Maches between zero and one times         |
| \|        | OR Alternative                            |
| \x       | Esscape character 'x'                     |
| .        | Any imprimible character (Unicode)        |

Orden de precedencia (de mayor a menor)

1. (EXPR) Group.
2. 'x' (character 'x')
3. Quantifier `(*, + , ?)`
4. OR operator,


## Build.

Project builds using maven with this dependencies:


* sfl4j-log4j 1.7.7 for logging. Mainly show automatons data to string.
* Apache Commons IO 2.4.
* JUnit 4.4.


maven version must be 3.X

For build project type:

`mvn package`

Images for this file have build using Graphviz/DOT. Classes for modeling automatons have got toString() methods to serialize its data to DOT language.

Bibliography used (in spanish):

* Compiladores: principios, técnicas y herramientas	(aka "El libro del dragón") (Alfred V. Aho, Monica S. Lam, Ravi Sethi y Jeffrey D. Ullman) ISBN 978-970-26-1133-2
* Introducción a la teoría de autómatas, lenguajes y computación. (John E. Hopcroft, Rajeev Motwani and Jeffrey D.) ISBN 978-84-7829-088-8

## Usage

## Architecture
