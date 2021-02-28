class Doctors extends Thread {
    int doctor_code;
    int patient_code;

    public Doctors(int doctor_code) {
        super();
        this.doctor_code = doctor_code;
    }


    @Override
    public void run() {
        while (true) {
            Hospital.ready.release();
            System.err.println("Doctor "+doctor_code+" is ready");
            try {
                Hospital.patientEnter[doctor_code-1].acquire();
                patient_code=Hospital.doctor_state[doctor_code-1];
                System.err.println("Doctor "+doctor_code+" is visiting "+patient_code+"-"+Hospital.patient_list[patient_code-1].name);
                Hospital.patientExit[doctor_code-1].acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
