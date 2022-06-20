# Most Active Cookies
Active cookie is a command line tool to process cookies file and return the most active cookie for the specified date.

## Example
Given a cookie log file in the following format:

~~~
cookie,timestamp
AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00
5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00
AtY0laUfhglK3lC7,2018-12-09T06:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00
4sMM2LxV07bPJzwf,2018-12-08T21:30:00+00:00
fbcn5UAVanZf6UtG,2018-12-08T09:30:00+00:00
4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00
~~~

and the target date 2018-12-09

It will write the following to the STD_OUT

~~~
AtY0laUfhglK3lC7
~~~

## Assumptions
- If multiple cookies meet that criteria, it returns all of them on separate lines.
- -d parameter is assumed to be in UTC time zone.
- Csv File is only large enough that it will fully fit into memory.
- Cookies in the log file are sorted by timestamp (most recent occurrence is the first line of the file).

## Technology
It is written using Java 8, Springboot framework and Maven for build automation and dependency management.


## Build

- Using Maven to build and package source code into a jar file: 

~~~
mvn clean package
~~~


## Run

- Run the compiled jar file:

~~~
java -jar active-cookie-${version}.jar -f csv-file-path -d target-date
~~~

- Example:

~~~
java -jar active-cookie-1.0.0.jar -f cookie_log.csv -d 2018-12-09
~~~
