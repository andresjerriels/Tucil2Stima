import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RencanaKuliah {

    // Representasi graf sebagai adjacency list
    private Map<String, List<String>> adjMataKuliah;

    // default ctor
    RencanaKuliah() {
        this.adjMataKuliah = new HashMap<>();
    }

    // Method untuk menambahkan mata kuliah dari graf.
    void addMataKuliah(String matkul) {
        adjMataKuliah.putIfAbsent(matkul, new ArrayList<>());
    }

    // Method untuk menghapus mata kuliah dari graf dan sisi yang berhubungan dengannya.
    // Prekondisi: matkul pasti terdapat dalam graf.
    void removeMataKuliah(String matkul) {
        adjMataKuliah.values().stream().forEach(i -> i.remove(matkul));
        adjMataKuliah.remove(matkul);
    }

    // Method untuk menambah sebuah matkul menjadi prasyarat matkul lainnya
    // Prekondisi: matkul pasti terdapat dalam graf. MatkulPrasyarat terdefinisi.
    void addMatkulsbgPrasyarat(String matkul, String matkulPrasyarat) {
        adjMataKuliah.get(matkul).add(matkulPrasyarat);
    }

    // Menghapus matkul sebagai prasyarat dari matkul lainnya.
    // Prekondisi: matkul dan matkulPrasyarat pasti terdapat dalam graf.
    void removeMatkulsbgPrasyarat(String matkul, String matkulPrasyarat) {
        List<String> listOfPrasyarat = adjMataKuliah.get(matkul);
        if (listOfPrasyarat != null) {
            listOfPrasyarat.remove(matkulPrasyarat);
        }
    }

    // Mengembalikan sebuah list of String yang berisi seluruh matkul prasyarat dari sebuah masukan matkul tertentu.
    // Prekondisi: matkul pasti terdapat dalam graf.
    List<String> getAllMatkulPrasyarat(String matkul) {
        return adjMataKuliah.get(matkul);
    }

    // Mengembalikan jumlah matkul prasyarat dari sebuah masukan matkul tertentu.
    // Prekondisi: matkul pasti terdapat dalam graf.
    int getJumlahMatkulPrasyarat(String matkul) {
        return adjMataKuliah.get(matkul).size();
    }

    // Mengecek apakah graf kosong. Mengembalikan true jika graf kosong, dan false jika sebaliknya
    boolean isEmpty() {
        return adjMataKuliah.size() == 0;
    }

    // Membuat Rencana Mata Kuliah dari masukan File txt
    static RencanaKuliah createNewFromFile () {
        RencanaKuliah rencanaKuliah = new RencanaKuliah();

        try {
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.next();
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arr = data.split(",");
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = arr[i].replace(" ", "");
                    arr[i] = arr[i].replaceAll("\\.", "");
                    if (i == 0) {
                        rencanaKuliah.addMataKuliah(arr[i]);
                    } else {
                        rencanaKuliah.addMataKuliah(arr[i]);
                        rencanaKuliah.addMatkulsbgPrasyarat(arr[0], arr[i]);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return rencanaKuliah;
    }

    // Implementasi Topological Sort
    static void TopologicalSort(RencanaKuliah rencanaKuliah, ArrayList<List<String>> result) {
        while (!rencanaKuliah.isEmpty()) {
            ArrayList<String> currSmt = new ArrayList<>();  // List of string untuk menyimpan mata kuliah yang akan dieliminasi
            String[] keys = rencanaKuliah.adjMataKuliah.keySet().toArray(new String[0]);    // Array of string yang berisi seluruh mata kuliah dalam graf

            for (String matkul : keys) {
                if (rencanaKuliah.getJumlahMatkulPrasyarat(matkul) == 0) {      // Pemilihan mata kuliah yang tidak memiliki prasyarat
                    currSmt.add(matkul);        // Mata kuliah yang tidak memiliki prasyarat dimasukkan ke dalam list
                }
            }

            if (currSmt.isEmpty()) {        // Error handle jika graf yang dimasukkan bukanlah DAG
                throw new Error("\nRencana kuliah yang diberikan tidak memenuhi prekondisi. \nKuliah dan prerequisitenya tidak berupa Directed Acyclic Graph.");
            }

            for (String matkul : currSmt) {     // Mengeliminasi seluruh mata kuliah yang tidak memiliki prasyarat
                rencanaKuliah.removeMataKuliah(matkul);
            }
            result.add(currSmt);        // Menambahkan list of mata kuliah yang tidak memiliki prasyarat ke dalam list hasil
            TopologicalSort(rencanaKuliah, result);
        }
    }

    // Menampilkan logo selamat datang saat memulai program
    static void printLogo() {
        System.out.print("                                                                                                                                                                \n" +
                "8 8888     ,88' 8 8888                  .8.          b.             8 8 888888888o.      8 8888888888     d888888o. 8888888 8888888888  8 8888 b.             8 \n" +
                "8 8888    ,88'  8 8888                 .888.         888o.          8 8 8888    `^888.   8 8888         .`8888:' `88.     8 8888        8 8888 888o.          8 \n" +
                "8 8888   ,88'   8 8888                :88888.        Y88888o.       8 8 8888        `88. 8 8888         8.`8888.   Y8     8 8888        8 8888 Y88888o.       8 \n" +
                "8 8888  ,88'    8 8888               . `88888.       .`Y888888o.    8 8 8888         `88 8 8888         `8.`8888.         8 8888        8 8888 .`Y888888o.    8 \n" +
                "8 8888 ,88'     8 8888              .8. `88888.      8o. `Y888888o. 8 8 8888          88 8 888888888888  `8.`8888.        8 8888        8 8888 8o. `Y888888o. 8 \n" +
                "8 8888 88'      8 8888             .8`8. `88888.     8`Y8o. `Y88888o8 8 8888          88 8 8888           `8.`8888.       8 8888        8 8888 8`Y8o. `Y88888o8 \n" +
                "8 888888<       8 8888            .8' `8. `88888.    8   `Y8o. `Y8888 8 8888         ,88 8 8888            `8.`8888.      8 8888        8 8888 8   `Y8o. `Y8888 \n" +
                "8 8888 `Y8.     8 8888           .8'   `8. `88888.   8      `Y8o. `Y8 8 8888        ,88' 8 8888        8b   `8.`8888.     8 8888        8 8888 8      `Y8o. `Y8 \n" +
                "8 8888   `Y8.   8 8888          .888888888. `88888.  8         `Y8o.` 8 8888    ,o88P'   8 8888        `8b.  ;8.`8888     8 8888        8 8888 8         `Y8o.` \n" +
                "8 8888     `Y8. 8 888888888888 .8'       `8. `88888. 8            `Yo 8 888888888P'      8 888888888888 `Y8888P ,88P'     8 8888        8 8888 8            `Yo \n");

        System.out.println("  ___                                                         _ \n" +
                 " / _ \\                                                       | |\n" +
                 "/ /_\\ \\  ___  _   _  _ __    ___  _ __    __ _  _ __   _ __  | |\n" +
                 "|  _  | / __|| | | || '_ \\  / _ \\| '__|  / _` || '_ \\ | '_ \\ | |\n" +
                 "| | | | \\__ \\| |_| || |_) ||  __/| |    | (_| || |_) || |_) ||_|\n" +
                 "\\_| |_/ |___/ \\__,_|| .__/  \\___||_|     \\__,_|| .__/ | .__/ (_)\n" +
                 "                    | |                        | |    | |       \n" +
                 "                    |_|                        |_|    |_|       \n");

        System.out.println("Selamat datang!");
        System.out.println("Masukkan nama beserta direktori file daftar kuliah Anda: ");
    }

    // Mengembalikan angka Romawi dari sebuah integer
    static String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }

    // Print hasil sesuai dengan format yang ada
    static void printSolution(ArrayList<List<String>> result) {
        System.out.println("Berikut adalah rencana kuliah Anda: ");
        for (int cnt = 0; cnt < result.size(); cnt++) {
            System.out.print("Semester " + IntegerToRomanNumeral(cnt + 1) + ": ");
            List<String> currSmt = result.get(cnt);
            for (int i = 0; i < currSmt.size(); i++) {
                if (i == 0) {
                    System.out.print(currSmt.get(i));
                } else {
                    System.out.print(", " + currSmt.get(i));
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        printLogo();
        RencanaKuliah rencanaKuliah = createNewFromFile();
        ArrayList<List<String>> result = new ArrayList<>();
        TopologicalSort(rencanaKuliah, result);
        printSolution(result);
    }
}
