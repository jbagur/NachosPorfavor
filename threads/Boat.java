package nachos.threads;
import nachos.ag.BoatGrader;

import nachos.machine.Lib;

public class Boat {
    static BoatGrader bg;

    public static void selfTest() {
        BoatGrader b = new BoatGrader();

//        System.out.println("\n ***Testing Boats with only 2 children***");
//        begin(0, 2 b);

        System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
        begin(2, 2, b);

        //System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
        //begin(3, 3, b);
    }

    public static void begin( int adults, int children, BoatGrader b )
    {
        //Debe haber almenos dos nenecos
        Lib.assertTrue(children >= 2);
        // Store the externally generated autograder in a class

        // variable to be accessible by children.
        bg = b;
        //Total de adultos
        Totaladults = adults;
        //Total de ninos
        Totalchildren = children;
        //Igualamos adultsonoahu a 0
        adultsOnOahu = adults;
        //Igualamos childrenonoahu a 0
        childrenOnOahu = children;

        // Instantiate global variables here
        //Ninos y adultos en molokai es igualado a 0
        adultsOnMolokai = 0;
        childrenOnMolokai = 0;

        childrenToCreate = 0;
        adultsToCreate = 0;

        //Creamos un nuevo lock y lo guardamos en boat
        boat = new Lock();
        //Creamos condiciones para dormir y despertar threads
        sleepAdultdOahu = new Condition2(boat);
        sleepChildOahu = new Condition2(boat);
        sleepChildMolokai = new Condition2(boat);
        checkFinish = new Condition2(boat);



        // Create threads here. See section 3.4 of the Nachos for Java
        // Walkthrough linked from the projects page.

        //Creamos un runnable para child que llama al metodo de child
        Runnable ChildThread = new Runnable() {
            public void run() {
                ChildItinerary();
            }
        };
        //Creamos un runnable para adults que llama al metodo de adult
        Runnable AdultThread = new Runnable() {
            public void run() {
                AdultItinerary();
            }
        };
        //Usamos for loops para instanciar una cantidad de threads igual al numero de ninos y adultos mandados en el parametro

        while (childrenToCreate < Totalchildren) {
            KThread Child = new KThread(ChildThread);
            childrenToCreate +=1;
            Child.setName("Child " + childrenToCreate).fork();

        }

        while (adultsToCreate < Totaladults) {
            KThread Adult = new KThread(AdultThread);
            adultsToCreate +=1;
            Adult.setName("Adult " + adultsToCreate).fork();

        }

        //Corremos este bloque hasta que termine de transportar a los ninos y adultos
        boat.acquire();

        do {
            checkFinish.sleep();
        } while (childrenOnMolokai + adultsOnMolokai != adults + children);
        boat.release();
    }


    static void AdultItinerary() {
	/* This is where you should put your solutions. Make calls
	   to the BoatGrader to show that it is synchronized. For
	   example:
	       bg.AdultRowToMolokai();
	   indicates that an adult has rowed the boat across to Molokai
	*/
        //adult adquiere el lock
        boat.acquire();
        //Ohau se va poblando con adultos usando el for loop en el begin
        //adultsOnOahu ++;
        //System.out.println("Adult has appeared on Oahu");
	/* This is where you should put your solutions. Make calls
	   to the BoatGrader to show that it is synchronized. For
	   example:
	       bg.AdultRowToMolokai();
	   indicates that an adult has rowed the boat across to Molokai
	*/
        //Si el barco esta en molokai o la cantidad de ninos es distinta a 1 el adulto se va a dormir
        while (!boatOnOahu || childrenOnOahu != 1){
            sleepAdultdOahu.sleep();
            //sleepChildOahu.wake();

        }
        //Al salir del while el adulto va a molokai por lo cual restamos
        adultsOnOahu -- ;
        //bg.AdultRowToMolokai();
        System.out.println(KThread.currentThread().getName() + " rowing to Molokai");
        //Como el barco se movio a Molokai ponemos el flag a falso
        boatOnOahu = false;
        //Agregamos un adulto a molokai
        adultsOnMolokai ++;
        sleepChildMolokai.wake();
        //Despertamos al nino en molokai para que pueda regresar a Ohau a un nino
        //Soltamos el lock
        boat.release();
    }



    static void ChildItinerary()
    {
        //El nino adquiere el lock
        boat.acquire();
        //Ohau se va poblando con ninos usando el for loop en el begin
        //childrenOnOahu ++;
        //System.out.println("Child has appeared on Oahu");
        //Este while busca que la cantidad de ninos y adultos en molokai sea igual a la poblacion general
        //Mientras que esta condicion no se cumpla realizamos movimientos
        while (Totaladults + Totalchildren != childrenOnMolokai + adultsOnMolokai) {

            if (!boatOnOahu) {
                //Si el barco esta en molokai despertamos al nino en molokai para que lleve el barco a oahu
                //y pueda traer a un nino o permitir que un adulto se mueva
                //Restamos un nino de molokai
                //sleepChildMolokai.wake();
                childrenOnMolokai -- ;
                System.out.println(KThread.currentThread().getName() + " rowing to Oahu");
                //bg.ChildRowToOahu();
                //El barco se mueve a oahu
                boatOnOahu = true;
                //Sumamos un nino a oahu
                childrenOnOahu ++;
                sleepChildOahu.wake();
                //El nino en oahu despierta y el nino en molokai va a dormir


            } else if (boatOnOahu && childrenOnOahu >= 2) {
                //Si el barco esta en oahu y hay mas de un nino en oahu hay dos opciones
                if (!HasPassenger) {
                    //Si solo hay un nino en el barco agregamos un nuevo y despertamos al nino de oahu y dormimos al de molokai
                    HasPassenger = true;
                    System.out.println(KThread.currentThread().getName() + " rowing to Molokai");
                    //sleepChildOahu.sleep();
                    sleepChildMolokai.sleep();
                    // este sleepChildMolokai.sleep();

                } else {
                    sleepChildOahu.wake();
                    //de lo contrario subimos dos ninos al barco
                    childrenOnOahu -= 2;

                    //bg.ChildRowToMolokai();
                    //bg.ChildRideToMolokai();
                    //El barco deja oahu y dos ninos se suman a molokai
                    childrenOnMolokai += 2;
                    boatOnOahu = false;
                    //Como un nino se baja en molokai y otro se queda en el barco regresamos al pasajero a falso
                    HasPassenger = false;
                    checkFinish.wake();
                    System.out.println(KThread.currentThread().getName() + " riding to Molokai as passenger");
                    //Despertamos al nino en molokai para que pueda regresar a oahu
                    // este sleepChildMolokai.wake();
                    //Levantamos al adulto en oahu para que se mueva
                    sleepAdultdOahu.wake();

                    //Dormimos al nino en molokai
                    //sleepChildMolokai.sleep();
                }

            } else if (boatOnOahu && childrenOnOahu == 1){
                sleepAdultdOahu.wake();
                sleepChildOahu.sleep();


            }
        }

        /** vuelve el estado de boat en false*/
        boat.release();
        return;

    }
    //Ya viene
    static void SampleItinerary()
    {
        // Please note that this isn't a valid solution (you can't fit
        // all of them on the boat). Please also note that you may not
        // have a single thread calculate a solution and then just play
        // it back at the autograder -- you will be caught.
        System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
        bg.AdultRowToMolokai();
        bg.ChildRideToMolokai();
        bg.AdultRideToMolokai();
        bg.ChildRideToMolokai();
    }


    //Variables
    //Adultos y ninos en Oahu
    static int adultsOnOahu, childrenOnOahu;
    //Adultos y ninos en Molokai
    static int adultsOnMolokai, childrenOnMolokai;
    //Total de adultos y de ninos
    static int Totaladults, Totalchildren;
    static int adultsToCreate, childrenToCreate;
    //Revisa donde esta el barco
    static boolean boatOnOahu = true;
    //Tiene pasajeros el barco
    static boolean HasPassenger = false;
    //Candado
    static Lock boat;
    //Dormir adultos y ninos en Oahu
    static Condition2 sleepChildOahu,sleepAdultdOahu;
    //Dormir adultos y ninos en Molokai
    static Condition2 sleepChildMolokai;
    //Ver que terminamos
    static Condition2 checkFinish;

}