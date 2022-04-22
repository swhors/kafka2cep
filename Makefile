clean:
	mvn clean


compile:
	mvn compile

dependency:
	mvn assembly:assembly

build: compile 
	mvn package

build-with-dep: compile dependency build
