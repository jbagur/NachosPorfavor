#include "syscall.h"
#include "stdio.h"

void main()
{
	char *create = "TestOS.txt";
	int createFile = creat(create);
    	char *escribir = "O.S.45";
    	void *buffer[1];
    	int writeFile = write(createFile, escribir, strlen(escribir));
    	int closeFile = close(createFile);    
}
