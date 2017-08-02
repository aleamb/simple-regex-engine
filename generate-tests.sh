#!/bin/bash

# Script to generate unit tests for each regex in regex.txt
c=1
while read line
do
	regex=`echo "$line" | tr -d '\n'`
	echo "@Test public void test$c() { compareMatchExecution(\"$regex\"); }"
	c=`expr $c + 1`
done

