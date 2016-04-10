JAR = Blackjack.jar
DEPENDENCIES = img cards
MANIFEST = Manifest.txt

JAVAC = javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes) $(JAR)

$(JAR):
	jar -cfm $(JAR) $(MANIFEST) *.class $(DEPENDENCIES)

clean:
	rm -f *.class *.jar

%.class: %.java
	$(JAVAC) $<