#!/usr/bin/bash

for x in {a..z}
do
    cat $1 | grep "^$x" > $x.txt
done
