import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class Hospital {
    static Scanner inp = new Scanner(System.in);


    static Semaphore[] patientEnter;
    static Semaphore[] patientExit;
    static Semaphore ready;
    static Semaphore patient_mutex = new Semaphore(1, true);
    static Semaphore doctor_state_mutex = new Semaphore(1, true);

    static int[] doctor_state;

    static int N = 2;
    static int M = 4;
    static int capacity = M + N;
    static int patients_num = 0;
    static int total_num;
    static Patient_List[] patient_list;

    static long time;

    public static void main(String[] args) throws InterruptedException {

        getInputList();
        Patient[] patients = new Patient[total_num];

        ready = new Semaphore(1, true);
        patientEnter = new Semaphore[N];
        patientExit = new Semaphore[N];
        doctor_state = new int[N];

        for (int i = 0; i < N; i++) {
            doctor_state[i] = -1;
        }

        for (int i = 0; i < N; i++) {
            patientEnter[i] = new Semaphore(1, true);
            patientExit[i] = new Semaphore(1, true);
            Doctors doctor = new Doctors(i + 1);
            patientEnter[i].acquire();
            patientExit[i].acquire();
            doctor.start();
        }
        ready.acquire();


        time = System.currentTimeMillis();
        int temp=0;
        for (int i = 0; i <10000 ; i++) {
            System.err.println("                                               <<time : "+i+" >>");
            while (temp<total_num && patient_list[temp].enter_time==i){
                //System.out.println(patient_list[temp].name+patient_list[temp].visit_time);

                patients[temp]=new Patient(temp+1,patient_list[temp].name,patient_list[temp].visit_time);
                patients[temp].start();
                temp++;
            }
            delay(1);
        }
    }

    private static void getInputList() {
        total_num = inp.nextInt();
        patient_list = new Patient_List[total_num];
        for (int i = 0; i < total_num; i++) {
            patient_list[i]=new Patient_List(inp.next(),Integer.parseInt(inp.next()),Integer.parseInt(inp.next()));
        }
    }

    public static void delay(double sec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= sec * 1000) ;
    }

    public static int findFreeDoctor(int patients_code) {
        for (int i = 0; i < N; i++) {
            if (doctor_state[i] < 0) {
                doctor_state[i] = patients_code;
                return i;
            }
        }
        System.err.println("error findFreeDoctor");
        return -1;
    }
}

/*
10
ali 0 2000
reza 0 2000
ahmad 0 2000
majid 0 2000
mahmood 0 2000
qoros 0 2000
qoli 1 2000
vali 1 2000
korosh 2 2000
eskandar 2 2000
 */


