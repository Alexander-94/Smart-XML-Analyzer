# Smart-XML-Analyzer
A simple web crawler that locates a user-selected element on a web site with frequently changing information

If you are using Windows specify the jdk location  
set "path=%path%;C:\Program Files\Java\jdk1.8.0_121\bin"

1. Compile the sorce code, include the jsoup library. Output .class files will be created in the out/com/example folder  
javac -d out -sourcepath src -cp lib/jsoup-1.11.3.jar src/com/example/Task.java

2. Make the .jar archieve, it will be created in the out folder  
jar cvfm out/Task.jar src/resources/MANIFEST.MF -C out/ com

3. Put other samples to the out directory and run the .jar file with different sample.  
   The result pathes to the origin button and button from the samples will be printed into the standard output.  
java -cp out\Task.jar;lib\* com.example.Task out\sample-0-origin.html out\sample-1-evil-gemini.html  
java -cp out\Task.jar;lib\* com.example.Task out\sample-0-origin.html out\sample-2-container-and-clone.html  
java -cp out\Task.jar;lib\* com.example.Task out\sample-0-origin.html out\sample-3-the-escape.html  
java -cp out\Task.jar;lib\* com.example.Task out\sample-0-origin.html out\sample-4-the-mash.html  

The target element id is provided in the source code in: String targetElementId = "make-everything-ok-button";  

The program is searching the button by using the tag of origin button and  
it looks if the first word of the class of origin button is contained in the class attribute and  
if a word-digit value of href of origin button is contained in the href attribute and  
is a word-digit value of href of origin button in contained in the onclick attribute.  
