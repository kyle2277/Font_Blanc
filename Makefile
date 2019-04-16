#variable for java compiler

JCC = javac

CLASSES = EncoderDecoder_FB.java

#CLASSPATH = <path to all dependecy .jar files. See documentation for required dependencies>
CLASSPATH = ./bin/ejml-core-0.38.jar:./bin/ejml-ddense-.038.jar:./bin/ejml-simple-0.38.jar:./bin/commons-lang3-3.8.1.jar

#variable for flags
#-g compiles with debugging information
#-cp defines classpath

JFLAGS = -g -cp $(CLASSPATH)

default: FontBlancMain.class EncoderDecoder_FB.class

FontBlancMain.class: FontBlancMain.java
	$(JCC) $(JFLAGS) FontBlancMain.java $(CLASSES)
   
EncoderDecoder_FB.class: EncoderDecoder_FB.java
	$(JCC) $(JFLAGS) EncoderDecoder_FB.java $(CLASSES)
      
# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them

clean: 
	$(RM) *.class