#include "syscall.h"
void
main()
{
char *escribir = "Holas";
char *create = "Test1.txt";
int createFile = creat(create);    
int writeFile = write(createFile, escribir, strlen(escribir));
int closeFile = close(createFile);
}
