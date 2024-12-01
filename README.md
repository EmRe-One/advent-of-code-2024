![Kotlin](https://img.shields.io/badge/Kotlin-grey?logo=Kotlin&style=for-the-badge)
![](https://img.shields.io/badge/📅%20days-00-005060?style=for-the-badge)
![](https://img.shields.io/badge/⭐%20stars-00-005060?style=for-the-badge)

# Advent-of-Code 2024

## Intro

You can find here my [Advent-of-Code 2024](https://adventofcode.com/2024) puzzle solutions written in Kotlin.

## Scripts

To prepare Boilerplate code for the next day, just execute the following gradle task with the day number
```bash 
gradle prepareNextDay -Pday=1
```

To generate the aoc_tiles, navigate to the aoc_tiles dir, activate python venv and the run the python script:
```bash
cd aoc_tiles
./venv/Scripts/activate.ps1 

python ./create_tiles.py
```

## Solutions

| Day                                           | Test                                                                        | Solution                                                            | Tile                               |
|-----------------------------------------------|-----------------------------------------------------------------------------|---------------------------------------------------------------------|------------------------------------|
| [Day 01](https://adventofcode.com/2024/day/1) | [Day01Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day01Test.kt) | [Day01.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day01.kt) | ![Day 01](./aoc_tiles/2024/01.png) |
<!-- $1 -->

---

** The AoC Tiles are created with an adjusted python script from [LiquidFun](https://github.com/LiquidFun/adventofcode/tree/main/AoCTiles)
