package plic;

import plic.repint.primaire.TDS;

import java.io.File;

public class PlicMultiple {
    public static void main(String[] args) {
        File folder = new File("src/plic/sources/masse");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            System.out.println("Fichier " + file.getName());
            if (file.isFile()) {
                try {
                    new Plic(file.getAbsolutePath());
                    System.out.println("Réussi: " + file.getName());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
                TDS.reset();
            }
            System.out.println("-------------");
        }
    }
}