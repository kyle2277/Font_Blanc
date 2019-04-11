___
# Font Blanc
## Overview
Welcome to project Font Blanc. Font Blanc is a file encryption program powered by the Arkadiusz2.0 algorithm. For a detailed description of how the encryption algorithm works, please see [Arkadiusz2.0 documentation](https://github.com/kyle2277/Arkadiusz2.0/blob/master/README.md "Arkadiusz2.0 online documentaion").
### Dependencies
It is necessary to add the required .jar files from each of these libraries (included in the 'bin' directory) to your Java classpath before running:
* EJML (Efficient Java Matrix Library)
  * ejml-simple
  * ejml-core
  * ejml-ddense
* StringUtils (Apache Commons Lang3)
  * commons-lang3

For running in the terminal, add the paths of all dependencies to the CLASSPATH variable in the makefile.

The program takes three arguments:
1) Path (absolute or relative) to a file
2) Password used to encrypt or decrypt the file
3) The word "encrypt" or "decrypt"

For example, to encrypt a file called file1.txt, the terminal command would be:
<pre>
~$ java FontBlancMain file1.txt password encrypt
</pre>
This produces a file called 'encrypted_file1.txt.' To decrypt this file, the command would be:
<pre>
~$ java FontBlancMain file1.txt password decrypt
</pre>
This reproduces the original file.
The general form:
<pre>
~$ java FontBlancMain <path to file> <password> <"encrypt" or "decrypt">
</pre>
