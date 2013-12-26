#!/usr/bin/env bash

NEWNAME=$1

echo Changing project name to $NEWNAME

grep -rl snaply ./src | xargs sed -i s/snaply/$NEWNAME/g
grep -rl snaply ./test | xargs sed -i s/snaply/$NEWNAME/g
grep -rl snaply ./resources | xargs sed -i s/snaply/$NEWNAME/g

grep -rl snaply ./project.clj | xargs sed -i s/snaply/$NEWNAME/g
grep -rl snaply ./bower.json | xargs sed -i s/snaply/$NEWNAME/g
grep -rl snaply ./package.json | xargs sed -i s/snaply/$NEWNAME/g



mv ./src/snaply ./src/$NEWNAME
mv ./test/snaply ./test/$NEWNAME
