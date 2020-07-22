# RMASBench: Multi-Agent Coordination Benchmark

This is the RMASBench benchmarking tool, extended for the paper [Anytime, Efficient and Distributed Multi-Agent Coordination in Urban Search and Rescue](https://lcpz.gitlab.io/publications).

Following are brief instructions on how to install, build and produce the results.

For reference, see the original [repository](https://github.com/rmasbench/rmasbench)
and the RCR [manual](https://roborescue.sourceforge.io/docs/rcrs-manual.pdf).

## Requirements

- GNU/Linux or macOS
- Java **1.7**
- Ant
- Maven

## Installation

```shell
git clone --recursive https://github.com/cmi/gopal/rmasbench.git
```

## Build

```shell
cd rmasbench
./build.sh
```

## Note

The simulator creates some cache files at its first execution. This procedure
can take hours on slow machines. To skip it, download the
[cache-rays.zip](https://git.soton.ac.uk/cmi/gopal/rmasbench-cache) archive and
unzip the contents into `RSLB2/boot`, overriting the folders `cache/` and
`rays/` if they already exists.
