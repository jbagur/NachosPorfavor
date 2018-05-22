Proyecto realizado por Sebastian Enriquez 20140061
		       Julio Rafael Bagur 20150440
		       
Para correr mas pruebas de la fase 2, en la clase UserKernel modificar el run()

ej 
    public void run() {
        super.run();

        UserProcess process = UserProcess.newUserProcess();
        UserProcess process2 = UserProcess.newUserProcess();
        String shellProgram = Machine.getShellProgramName();

        Lib.assertTrue(process2.execute("prueba1.coff", new String[] { }));
        Lib.assertTrue(process.execute("prueba2.coff", new String[] { }));
        KThread.currentThread().finish();
    }
    
previamente haber compilado los archivos en c en la carpeta test

con los comandos make clean 
		  make
		  
y en la carpeta proj2
		make
		nachos    
