#!/bin/bash

declare -a solvers=("S-CTS" "BMS" "DSA")

count=1

[[ ! -d "./inputs" ]] && mkdir inputs

for solver in "${solvers[@]}"
do
    for scenario in $(ls -1 paris/)
    do
        for i in {1..30}
        do
            #count_str=$(printf "%04d" $count)
            echo "--seed $i -s ${scenario%.*} -c $solver" > inputs/input$count
            count=$((count + 1))
        done
    done
done
