## java-sandbox

Resources and a barebones project to learn data engineering in Java.

## tools
* [A git client for Windows](https://desktop.github.com/)
* [An IDE for coding - IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download)

When I first tried to build this project in IntelliJ IDEA, I got a `release version 5 not supported` error. If that happens to you, [this stack overflow question should help](https://stackoverflow.com/questions/59601077/intellij-errorjava-error-release-version-5-not-supported).

## the java-sandbox project

This is just a place to put code to try things out and to share ideas back and forth. It has a very simple [Hello World](src/main/java/org.example.sandbox/Hello.java) example that reads a text file and picks a random item from it.

The project uses [Apache Maven](http://maven.apache.org/) as its build/dependecy system. Maven uses a file called [pom.xml](pom.xml) to define how to build the project. If you look at the [pom.xml](pom.xml) file in this folder, you'll see the list of libraries the project will download and compile with. The ones I included are:

* JSoup - a library for parsing web pages
* Apache httpclient - a library for pulling information from the web
* Apache commons-csv - a library for reading and writing CSV files
* SQLite - a portable SQL database. sqlite-jdbc lets you use it through Java's standard SQL API
* Apache POI - a library for reading and writing Excel files

The project also has a bunch of small data files useful for playing around. You can find these under [src/main/resources](src/main/resources).

## some learning resources

### Jetbrains Academy

If you like things that are organized as courses and use small example projects, JetBrains Academy might be a good place to start. It does a little test at the beginning to see where you're at and tailors things to your skill level. It's free for the time being.

<https://www.jetbrains.com/academy/>

### The Java Tutorial

Sure, it looks like it came straight out of the nineties, but the Java Tutorial is still a pretty good place to find out how to do stuff in Java.

<https://docs.oracle.com/javase/tutorial/>

### JSoup

JSoup is an extremely useful library for extracting information out of web pages. Here's their official cookbook:

<https://jsoup.org/cookbook/>

And here's another JSoup tutorial

<https://www.baeldung.com/java-with-jsoup>

### Apache httpclient

Apache httpclient is pretty much the standard for communicating with web servers from Java. Sadly, it has undergone a lot of changes, and so you have to make sure the information you're looking at is relevant to the version you're using (I included version 4.5). Here's the official page with some tutorials and examples:

<http://hc.apache.org/httpcomponents-client-4.5.x/index.html>

And here's a nice tutorial on github:

<https://github.com/RameshMF/apache-httpclient-4.5-tutorial>

### commons-csv

Apache's commons-csv library is very widely used for reading and writing CSV files. Here's the official user guide:

<http://commons.apache.org/proper/commons-csv/user-guide.html>

And here's a quick commons-csv tutorial:

<https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/>

### SQLite

I am a big fan of SQLite for data that's a little more complex - like when I need sorting or linking of multiple tables. It stores all its data in a single file, so it's very easy to copy data around. You will probably want to download and install it so that you can use it outside of Java.

<https://www.sqlite.org/index.html>

Inside of Java, you can create and access SQLite databases via Java's standard "JDBC" libraries, which are kind of old and clunky but still the standard. Here's a tutorial for using SQLite from Java:

<https://www.sqlitetutorial.net/sqlite-java/>

There are a ton of general JDBC tutorials that are valid for any database you might be using. Here's one:

<https://www.sqlitetutorial.net/sqlite-java/>

And here's the one from the Java Tutorial

<https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html>

### Apache POI

A lot of Apache projects in here. It's pretty common for popular open-source libraries to get picked up and supported by the Apache Software Foundation, so it's a great place to go if you're looking for a library that does something particular, and you want something that you'll be able to find information about and won't suddenly disappear on you.

Anyway, POI is a library for reading and writing Excel files, which data engineers tend to do a lot more often than they'd like. Here's POI's official page:

<https://poi.apache.org/>

And here's a tutorial:

<https://www.javatpoint.com/apache-poi-tutorial>

## Some data sources

### tvdb

Tvdb is an open-source database of TV shows (and movies) accessible via a REST API. Once you sign up for an API key, you should be able to access it using httpclient.

<https://thetvdb.com/api-information>

I haven't tried it myself, but someone created a Java library to make accessing tvdb easier. You should be able to use it by adding a new dependency to `pom.xml`. Here's the github project, which also includes information about how to add it to a Maven-based project:

<https://github.com/UweTrottmann/thetvdb-java>

### SSA "baby names" database

This simple and interesting database is put out by the Social Security administration based on Social Security card applications. It lists name, sex and number of applications for each year, and it goes back to 1880.

Some fun things to do with it:
* Do you have an unusual name? In what year was it first listed? How did its popularity change over time?
* Is your name more common in a particular state?
* What names are least sex-specific? How did that change over time?
* Many names become popular from fictional characters and celebrities. Can you find names that appear around the time that a particular character or celebrity was big?

<https://www.ssa.gov/OACT/babynames/limits.html>

