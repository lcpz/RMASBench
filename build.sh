#!/bin/bash

set -e

cd BinaryMaxSum
mvn -DskipTests -Dmaven.javadoc.skip=true package
cd ..

cd MaxSum
ant clean jar
cd ..

cd roborescue
ant clean oldsims jars
cd ..

cd BlockadeLoader
ant clean jar
cd ..

cd RSLB2
ant clean jar
cd ..
