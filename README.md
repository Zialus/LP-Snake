# Snake

[![CodSpeed](https://img.shields.io/endpoint?url=https://codspeed.io/badge.json)](https://app.codspeed.io/Zialus/LP-Snake?utm_source=badge)

## Assignment 1 - Laboratórios de Programação (MIERSI - FCUP)

Simple Java implementation (using Lanterna API) of the classic game Snake for a college class.

## Benchmarks

JMH benchmarks for the game logic live in `src/jmh/java` and are tracked with [CodSpeed](https://codspeed.io).
They use the CodSpeed JMH fork, included as a git submodule:

```sh
git submodule update --init --recursive
gradle jmh
```
