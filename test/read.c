#include "syscall.h"
#include "stdio.h"

void main()
{
    printf("-------------Contenido-------"); 
         //Unlink
    char *test = "Test4.txt";
    int deleteFile = unlink(test);
    int openFile = open(test);
    
}

