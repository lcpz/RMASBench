#!/bin/bash

prev_id=$(qsub ./tests/test1)

for i in {2..450}
do
    curr_id=$(qsub -W depend=afterok:$prev_id ./tests/test$i)
    prev_id=$curr_id
done
