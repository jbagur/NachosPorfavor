#include "syscall.h"
void main() {

    char *test = "Test1.txt";
    char *escribir = "Wicho, Hit es tu papi";
    void *buffer[1];
    int openFile = open(test);
    int writeFile = write(openFile, escribir, strlen(escribir));
}

