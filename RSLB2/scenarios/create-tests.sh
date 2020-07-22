#!/bin/bash

if [ $# -ne 1 ]
then
    echo -e "usage: `basename "$0"` HPC_user_ID"
    return 1
fi

declare -a solvers=("D-CCF" "BMS" "DSA")

user_id="$1"
start_path="/home/user_id/t00/rmasbench/RSLB2/boot"
count=1

[[ ! -d "./tests" ]] && mkdir tests

for solver in "${solvers[@]}"
do
    for scenario in $(ls -1 paris/)
    do
        for i in {1..30}
        do
            [[ -f "./tests/test$count" ]] && rm "./tests/test$count"
            echo "#!/bin/bash" >> tests/test$count
            echo "#PBS -l walltime=00:10:00" >> tests/test$count
            [[ $count -eq 450 ]] && echo "#PBS -m ae -M $user_id@soton.ac.uk" >> tests/test$count
            echo "module add jdk/1.8.0" >> tests/test$count
            echo "cd $start_path" >> tests/test$count
            echo "./start.sh --seed $i -s ${scenario%.*} -c $solver" >> tests/test$count
            count=$((count + 1))
        done
    done
done
