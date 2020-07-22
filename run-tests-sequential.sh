#!/bin/bash

declare -a solvers=("D-CCF" "BMS" "DSA")
declare -a scenarios=("15agents" "21agents" "27agents" "33agents" "40agents")

count=1

cd RSLB2/boot

for solver in "${solvers[@]}"
do
    for scenario in "${scenarios[@]}"
    do
        for seed in {1..30}
        do
            printf "### %03d %s %s %02d\n" $count "$solver" "$scenario" $seed
            ./start.sh -c $solver -s $scenario --seed $seed
            count=$((count+1))
        done
    done
done
