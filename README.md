# Advent-of-Code-Java
Repo to hold all advent of code years and days for Java.  
2018: Up to day 5 added  
2019: Not added  
2020: Completed  
2021: Up to day 18 completed  
2022: In progress
2023: In progress

How to run a day:  
`./gradlew run --args "2020 1"`

How to run a year:  
`./gradlew run --args "2020"`

How to run all:  
`./gradlew run`

This uses Java 17 and Java 17 features. Please use the correct SDK or it will not compile. Java 21 also works.

IntelliJ is the recommended IDE for using with this project.

Todo:
- Add sample/input and part 1/2 separation so they can be measured independently
- Make sample run first and die if incorrect, allow specifying the value that should be valid
- Write measurement times to a file and print a comparison of X recent runs on run
- Add optional unit tests that run before sample/input and die first if incorrect
- Add measurements to unit test and show prior measurements, to allow easily seeing optimization efforts