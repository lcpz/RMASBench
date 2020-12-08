#!/usr/bin/env python3

# Creates the result files in the 'results' folder.
# Dependency: numpy.

import os
import numpy as np
from pathlib import Path

# constants
PATH = './results' # the path to the results
PATTERN = '*.dat'  # a result file extension
MATCH = '*agents'  # each subdir contains a scenario's results (e.g., 15agents)
TOT = 1618         # the number of buildings in the Paris map
DMAX = 300         # maximum number of time steps in a scenario run


'''
Capezzuto's score function, more punitive than RCRS
(rescuecore2.standard.score.BuildingDamageScoreFunction).
'''
def compute_score(time, nOnceBurned, totalBuildings, dmax):
    n = 100 * (nOnceBurned/totalBuildings) * (1 + time/dmax)
    if n > 100:
        n = 100
    return n


'''
We are interested in:
1. problem completion time;
2. number of buildings that have been on fire at least once;
3. average CPU time per time unit.
4. RCRS score.
5. Our score (Capezzuto's).
'''
def get_results_of_file(filepath, totalBuildings, dmax):
    lines = open(filepath, 'r').readlines()
    activated = False
    avgCpuTime = 0
    timeSteps = 0
    for line in lines:
        '''
        In the last column, all values after 'cpu_time' are the CPU
        times of each time step.
        '''
        ct_value = line.split()[-1]
        if ('cpu_time' in ct_value):
            activated = True
        elif activated is True:
            avgCpuTime += float(ct_value)
            timeSteps += 1

    avgCpuTime /= timeSteps
    lastLineArr = lines[-1].split()
    time = int(lastLineArr[0])
    nOnceBurned = int(lastLineArr[1])
    nBurning = int(lastLineArr[2])
    score0 = float(lastLineArr[-5]) # final RCRS score

    """
    As of July 2020, RCRS has an 11-years old bug: the score improves when fires
    are indomable. A fire is *indomable* when it cannot be contained by the
    given fire fighters and solving algorithm. Hence, the simulation runs until
    the latest deadline, and the more the fire fighters (partially) extinguish
    fires, the more the score is incorrectly decreased.

    To mitigate the bug, we detect runs with indomable fires, and recompute the
    score as the percentage of buildings that are burning at the latest
    deadline, multiplied by the normalised number of once-burned buildings.

    A run has indomable fires when there are still burning buildings at the
    latest deadline. Since each line in a RMASBench log reports the `t-1`-th
    system snapshot, we have to check that the latest time is at least `dmax-1`,
    and that the latest number of burning buildings is at least 2.
    """
    if time >= dmax - 1 and nBurning > 1:
        burntFactor = (nOnceBurned - nBurning)/totalBuildings
        score0 = 100 * nBurning/totalBuildings * (1 + burntFactor)
        if score0 > 100:
            score0 = 100

    return time, nOnceBurned, score0, avgCpuTime


def get_results(path, pattern, match, totalBuildings, dmax):
    if not os.path.exists(path):
        print('Sorry, that path does not exist. Try again.')
        return

    count = 1
    for directory in Path(path).rglob(match):
        dirname = os.path.basename(directory)
        result_file = open(f'{path}/{dirname}.txt', 'a+')
        posix_path = directory.as_posix()
        print('### %4d %s' % (count, posix_path), file=result_file)
        count += 1

        time_arr = []
        nOnceBurned_arr = []
        avgCpuTime_arr = []
        score0_arr = []
        score_arr = []

        for resultfile in os.listdir(posix_path):
            filepath = f'{posix_path}/{resultfile}'
            time, nOnceBurned, score0, avgCpuTime = get_results_of_file(filepath, totalBuildings, dmax)
            avgCpuTime = int(round(avgCpuTime))
            score = compute_score(time, nOnceBurned, totalBuildings, dmax)

            time_arr.append(time)
            nOnceBurned_arr.append(nOnceBurned)
            avgCpuTime_arr.append(avgCpuTime)
            score0_arr.append(score0)
            score_arr.append(score)

        print('\ntime: %.5f [+-%.5f]' % (np.mean(time_arr),
            np.std(time_arr)/np.sqrt(len(time_arr))), file=result_file)
        print('nOnceBurned: %.5f [+-%.5f]' % (np.mean(nOnceBurned_arr),
            np.std(nOnceBurned_arr)/np.sqrt(len(nOnceBurned_arr))), file=result_file)
        print('average CPU time: %.5f [+-%.5f]' % (np.mean(avgCpuTime_arr),
            np.std(avgCpuTime_arr)/np.sqrt(len(avgCpuTime_arr))), file=result_file)
        print('original score: %.5f [+-%.5f]' % (np.mean(score0_arr),
            np.std(score0_arr)/np.sqrt(len(score0_arr))), file=result_file)
        print('Capezzuto\'s score: %.5f [+-%.5f]\n' % (np.mean(score_arr),
            np.std(score_arr)/np.sqrt(len(score_arr))), file=result_file)


if __name__ == '__main__':
    get_results(PATH, PATTERN, MATCH, TOT, DMAX)
