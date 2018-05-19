/* halt.c
 *	Simple program to test whether running a user program works.
 *	
 *	Just do a "syscall" that shuts down the OS.
 *
 * 	NOTE: for some reason, user programs with global data structures 
 *	sometimes haven't worked in the Nachos environment.  So be careful
 *	out there!  One option is to allocate data structures as 
 * 	automatics within a procedure, but if you do this, you have to
 *	be careful to allocate a big enough stack to hold the automatics!
 */

#include "syscall.h"

int
main()
{
	/*//Open
	char *test = "Test1.txt";
	int openFile = open(test);*/

	/*//Creat
	char *create = "Test1.txt";
	int createFile = creat(create);*/

	/*//Read
    char *test = "Test1.txt";
    void *buffer[1];
    int read1 = open(test);
    int read2 = read(read1,buffer,11);
    printf("Contenido: %s\n",buffer);*/


	/*//Write
    char *test = "Test1.txt";
    char *escribir = "Wicho, Hit es tu papi";
    void *buffer[1];
    int openFile = open(test);
    int writeFile = write(openFile, escribir, strlen(escribir));*/

    /*//Close
    char *test = "Test1.txt";
    char *test2 = "Test.txt";
    char *test3 = "Test2.txt";
    int openFile = open(test);
    int openFile2 = open(test2);
    int closeFile = close(openFile);
    int openFile3 = open(test3);*/
    
    ///* //Close 2
    char *test = "Test1.txt";
    char *escribir = "Wicho, Hit es tu papi";
    void *buffer[1];
    int openFile = open(test);
    int writeFile = write(openFile, escribir, strlen(escribir));
    int closeFile = close(openFile);
    int read1 = open(test);
    int read2 = read(read1,buffer,strlen(escribir));
    printf("Contenido: %s\n",buffer);
    
    //*/
    
    halt();
    /* not reached */
}
