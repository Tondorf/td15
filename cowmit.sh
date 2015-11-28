#!/bin/bash
bla="$1"
git commit -am "`echo $bla | cowsay`"
