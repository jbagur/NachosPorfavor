#include "syscall.h"
#include "stdio.h"

void main()
{
    printf("------Leer-------"); 
    char *test = "TestOS.txt";
    int openFile = open(test);
    void *buffer[1];
    int readFile = read(openFile, buffer, 6);
    printf("Contenido: \n");
    int readt = write(1,buffer,6);
    printf("\n");
}

