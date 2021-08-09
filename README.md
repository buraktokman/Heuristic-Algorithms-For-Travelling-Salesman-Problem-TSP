# Heuristic Algorithms For Travelling Salesman Problem (TSP) [![GitHub stars](https://img.shields.io/github/stars/badges/shields.svg?style=social&label=Stars)](https://github.com/buraktokman/Heuristic-Algorithms-For-Travelling-Salesman-Problem-TSP/)

[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://github.com/buraktokman/Heuristic-Algorithms-For-Travelling-Salesman-Problem-TSP)
[![Repo](https://img.shields.io/badge/source-GitHub-303030.svg?maxAge=3600&style=flat-square)](https://github.com/buraktokman/Heuristic-Algorithms-For-Travelling-Salesman-Problem-TSP)
[![Requires.io](https://img.shields.io/requires/github/celery/celery.svg)](https://requires.io/github/buraktokman/Heuristic-Algorithms-For-Travelling-Salesman-Problem-TSP/requirements/?branch=master)
[![Scrutinizer](https://img.shields.io/scrutinizer/g/filp/whoops.svg)](https://github.com/buraktokman/Heuristic-Algorithms-For-Travelling-Salesman-Problem-TSP)
[![DUB](https://img.shields.io/dub/l/vibe-d.svg)](https://choosealicense.com/licenses/mit/)
[![Donate with Bitcoin](https://img.shields.io/badge/Donate-BTC-orange.svg)](https://blockchain.info/address/17dXgYr48j31myKiAhnM5cQx78XBNyeBWM)
[![Donate with Ethereum](https://img.shields.io/badge/Donate-ETH-blue.svg)](https://etherscan.io/address/91dd20538de3b48493dfda212217036257ae5150)

Python script that fetches all user information from existing chats, channels and enrolled Telegram groups then invites to your own selected group.

### INTRODUCTION
------
Travelling Salesman Problem (TSP) is an NP-complete problem which is one of the most intensively studied problems in the fields of computational mathematics. TSP is an algorithmic problem task to find the shortest possible route that visits every city exactly once and returns to the starting point.



### PROJECT DEFINITION
------
In this project, we implemented three different heuristics to solve the TSP problem for the 4,663 cities in the Canada:
1. Nearest Neighbor Algorithm
2. Greedy Algorithm
3. Divide and Conquer Strategy


### OBJECTIVES
------
Primary objectives of the project ordered by their importance:
1. Correctness of the implemented heuristic algorithm.
2. Best result for the shortest tour.
3. Running speed (efficiency) of the algorithm.
4. Readability of the code.



### DESIRED OUTPUTS
------
The developed program will print following information on the console upon the execution:
 - Name of the used heuristic.
 - Tour path.
 - Total tour length.
 - Execution time.



### HOW TO USE THE PROGRAM
------
We generated a .jar file for each heuristic algorithm (nearest.jar, greedy.jar, divide.jar),
since executing the Main class from the terminal was a bit problematic. Jar files stored in “out/artifacts/Workspace” directory of the project.
Executing a jar file can be done by giving following command to terminal,

    `cd [PROJECT_FOLDER] &&`
    `Java -jar out/artifacts/Workspace/greedy.jar`

Path of the coordinates file (.tsp) hardcoded into the program, **“src/com/company/ca4663_only_coordinates.tsp”.**


### LICENSE
------

EULA License


