# RMASBench: Multi-Agent Coordination Benchmark

This is the RMASBench benchmarking tool, extended for the paper [Anytime and Efficient Multi-Agent Coordination in Urban Disaster Response](https://lcpz.gitlab.io/publications).

Following are brief instructions on how to install, build and produce the results.

For reference, see the original [repository](https://github.com/rmasbench/rmasbench)
and the RoboCup Rescue simulator [manual](https://roborescue.sourceforge.io/docs/rcrs-manual.pdf).

## Requirements

- GNU/Linux or macOS
- Java **1.7**
- Ant
- Maven

## Installation

```shell
git clone --recursive https://gitlab.com/lcpz/rmasbench.git
```

## Build and run tests

```shell
cd rmasbench
./build.sh && ./run-tests-sequential.sh
```

## Notes

- The configuration files of Binary Max-Sum and DSA have been retrieved
  from [this repository](https://github.com/RMASBench/aamas-2015-efficient).
- In general, each test runs at most for 5 minutes.
- The simulator creates some cache files at its first execution. This procedure
  can take hours on slow machines. To skip it, download the
  [cache-rays.zip](https://gitlab.com/lcpz/rmasbench-cache) archive
  and unzip the contents into `RSLB2/boot`, overriting the folders `cache/` and
  `rays/` if they already exists.
