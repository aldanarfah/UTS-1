import java.util.ArrayList;
import java.util.Scanner;

public class HospitalQueueSystem {
    // ArrayList untuk menyimpan antrean pasien
    private static ArrayList<Patient> patientQueue = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Selamat datang di Sistem Manajemen Antrean Rumah Sakit");

        while (running) {
            displayMenu(); // Tampilkan menu utama
            int choice = getValidIntInput("Masukkan pilihan Anda: ");

            switch (choice) {
                case 1:
                    addPatient(); // Tambah pasien baru
                    break;
                case 2:
                    serveNextPatient(); // Layani pasien berikutnya
                    break;
                case 3:
                    displayQueue(); // Tampilkan antrean
                    break;
                case 4:
                    updatePriority(); // Ubah prioritas pasien
                    break;
                case 5:
                    searchPatient(); // Cari pasien
                    break;
                case 6:
                    System.out.println("Terima kasih telah menggunakan sistem ini. Sampai jumpa!");
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }

        scanner.close();
    }

    // Menampilkan menu pilihan pengguna
    private static void displayMenu() {
        System.out.println("\n===== SISTEM ANTREAN RUMAH SAKIT =====");
        System.out.println("1. Tambah pasien baru ke antrean");
        System.out.println("2. Layani pasien berikutnya");
        System.out.println("3. Tampilkan antrean saat ini");
        System.out.println("4. Ubah prioritas pasien");
        System.out.println("5. Cari pasien");
        System.out.println("6. Keluar");
        System.out.println("======================================");
    }

    // Menambahkan pasien baru ke dalam antrean berdasarkan prioritas
    private static void addPatient() {
        System.out.println("\n--- Tambah Pasien Baru ---");
        String name = getValidStringInput("Masukkan nama pasien: ");
        int age = getValidIntInput("Masukkan umur pasien: ");
        String condition = getValidStringInput("Masukkan deskripsi kondisi pasien: ");
        int priority = getValidIntInRange("Masukkan prioritas (1-Kritis sampai 5-Tidak mendesak): ", 1, 5);

        Patient newPatient = new Patient(name, age, condition, priority);

        // Menyisipkan berdasarkan prioritas (angka lebih kecil = prioritas lebih tinggi)
        int index = 0;
        while (index < patientQueue.size() && patientQueue.get(index).getPriority() <= priority) {
            index++;
        }
        patientQueue.add(index, newPatient);
        System.out.println("Pasien berhasil ditambahkan.");
    }

    // Melayani pasien berikutnya dari antrean (prioritas tertinggi berada di depan)
    private static void serveNextPatient() {
        if (patientQueue.isEmpty()) {
            System.out.println("Tidak ada pasien dalam antrean.");
        } else {
            Patient next = patientQueue.remove(0);
            System.out.println("Melayani pasien: " + next.getName() +
                " | Umur: " + next.getAge() +
                " | Kondisi: " + next.getCondition() +
                " | Prioritas: " + getPriorityText(next.getPriority()));
        }
    }

    // Menampilkan seluruh antrean pasien
    private static void displayQueue() {
        if (patientQueue.isEmpty()) {
            System.out.println("Tidak ada pasien dalam antrean.");
        } else {
            System.out.println("\n--- Antrean Pasien Saat Ini ---");
            for (int i = 0; i < patientQueue.size(); i++) {
                Patient p = patientQueue.get(i);
                System.out.println((i + 1) + ". " + p.getName() +
                    " | Umur: " + p.getAge() +
                    " | Kondisi: " + p.getCondition() +
                    " | Prioritas: " + getPriorityText(p.getPriority()));
            }
        }
    }

    // Memperbarui prioritas pasien berdasarkan nama
    private static void updatePriority() {
        if (patientQueue.isEmpty()) {
            System.out.println("Tidak ada pasien untuk diperbarui.");
            return;
        }

        String name = getValidStringInput("Masukkan nama pasien yang ingin diperbarui: ");
        boolean found = false;

        for (int i = 0; i < patientQueue.size(); i++) {
            Patient p = patientQueue.get(i);
            if (p.getName().equalsIgnoreCase(name)) {
                int newPriority = getValidIntInRange("Masukkan prioritas baru (1-Kritis sampai 5-Tidak mendesak): ", 1, 5);
                patientQueue.remove(i); // Hapus data lama
                p.setPriority(newPriority);

                // Masukkan kembali dengan prioritas baru
                int index = 0;
                while (index < patientQueue.size() && patientQueue.get(index).getPriority() <= newPriority) {
                    index++;
                }
                patientQueue.add(index, p);
                System.out.println("Prioritas berhasil diperbarui.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Pasien tidak ditemukan.");
        }
    }

    // Mencari pasien berdasarkan nama
    private static void searchPatient() {
        if (patientQueue.isEmpty()) {
            System.out.println("Tidak ada pasien dalam antrean.");
            return;
        }

        String name = getValidStringInput("Masukkan nama pasien yang ingin dicari: ");
        boolean found = false;

        for (Patient p : patientQueue) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.out.println("Ditemukan: " + p.getName() +
                    " | Umur: " + p.getAge() +
                    " | Kondisi: " + p.getCondition() +
                    " | Prioritas: " + getPriorityText(p.getPriority()));
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Pasien tidak ditemukan.");
        }
    }

    // Mengembalikan teks deskripsi dari angka prioritas
    private static String getPriorityText(int priority) {
        switch (priority) {
            case 1:
                return "1-Kritis";
            case 2:
                return "2-Mendesak";
            case 3:
                return "3-Tinggi";
            case 4:
                return "4-Sedang";
            case 5:
                return "5-Tidak mendesak";
            default:
                return "Tidak diketahui";
        }
    }

    // Validasi input angka
    private static int getValidIntInput(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
            }
        }
        return value;
    }

    // Validasi input angka dengan rentang
    private static int getValidIntInRange(String prompt, int min, int max) {
        int value;
        while (true) {
            value = getValidIntInput(prompt);
            if (value >= min && value <= max) {
                break;
            }
            System.out.println("Masukkan angka antara " + min + " dan " + max + ".");
        }
        return value;
    }

    // Validasi input string
    private static String getValidStringInput(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                break;
            }
            System.out.println("Input tidak boleh kosong. Silakan coba lagi.");
        }
        return value;
    }
}

// Kelas pasien yang menyimpan data dasar pasien
class Patient {
    private String name;
    private int age;
    private String condition;
    private int priority; // 1 (Kritis) sampai 5 (Tidak mendesak)

    public Patient(String name, int age, String condition, int priority) {
        this.name = name;
        this.age = age;
        this.condition = condition;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCondition() {
        return condition;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
