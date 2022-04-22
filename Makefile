clean:
	mvn clean


compile:
	mvn compile

build-with-dep: compile
	mvn assembly:assembly
	mvn package

build: compile
	mvn package
