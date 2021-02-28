class Patient extends Thread {
    String patient_name;
    int patient_code;
    int patient_visitTime = 2000;

    public Patient(int patient_code,String patient_name, int patient_visitTime) {
        super();
        this.patient_name=patient_name;
        this.patient_code = patient_code;
        this.patient_visitTime = patient_visitTime;
    }

    public Patient(int patient_code) {
        super();
        this.patient_code = patient_code;
    }

    @Override
    public void run() {
        //entered to hospital
        try {
            Hospital.patient_mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(patient_code+"-"+patient_name + " entered");
        if (Hospital.capacity == Hospital.patients_num) {
            System.out.println(patient_code + " left");
            Hospital.patient_mutex.release();
            return;
        }
        //entered to lobby
        if (Hospital.patients_num >= Hospital.N) {
            System.out.println(patient_code +"-"+patient_name +  " is waiting in lobby");
        }
        Hospital.patients_num++;
        Hospital.patient_mutex.release();

        //ready
        try {
            Hospital.ready.acquire();
            Hospital.doctor_state_mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //find doctor room
        int doctor_code = Hospital.findFreeDoctor(patient_code);
        Hospital.doctor_state_mutex.release();

        //enter doctor room
        Hospital.patientEnter[doctor_code].release();

        long entryTime = System.currentTimeMillis() - Hospital.time, exitTime = System.currentTimeMillis() - Hospital.time;
        while (exitTime - entryTime < patient_visitTime) {
            exitTime = System.currentTimeMillis() - Hospital.time;
        }

        //left doctor room

        try {
            Hospital.patient_mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Hospital.doctor_state[doctor_code] = -1;
        Hospital.patientExit[doctor_code].release();
        Hospital.patients_num--;

        Hospital.patient_mutex.release();


        System.out.println(patient_code +"-"+patient_name +  " ---> entry : " + (entryTime) + " exit :" + (exitTime) + " time in:" + (exitTime - entryTime) + " Doctor :" + (doctor_code + 1));
    }
}


class Patient_List {
    String name;
    int enter_time;
    int visit_time;

    public Patient_List(String name, int enter_time, int visit_time) {
        this.name = name;
        this.enter_time = enter_time;
        this.visit_time = visit_time;
    }
}