
REM ##############################################################################

REM Invoke the the PacketPro JMRI application, which provide a Jython
REM command line.

REM There must be a local jython interpreter and JMRI libraries available

REM $Revision: 1.8 $ (CVS maintains this line, do not edit please)

SET CLASSPATH=.;classes;jmriplugins.jar;jmri.jar;comm.jar;Serialio.jar;log4j.jar;jhall.jar;crimson.jar;jdom.jar;jython.jar;javacsv.jar;MRJAdapter.jar;vecmath.jar
     
jython jython/PacketPro.py %1 %2 %3 %4 %5 %6 %7 %8 %9

