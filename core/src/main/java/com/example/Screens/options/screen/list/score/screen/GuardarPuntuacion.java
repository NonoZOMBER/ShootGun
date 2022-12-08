package com.example.Screens.options.screen.list.score.screen;

import java.io.*;

/**
 * Created by Nono on 08/12/2022.
 */
public class GuardarPuntuacion {
    private final File fichero = new File("Scores.txt");
    public void saveScore(String name, int score) {
        if (fichero.exists()) {
            try {
                FileWriter fw = new FileWriter(fichero, true);
                PrintWriter pw = new PrintWriter(fw);
                String linea = String.format("%s.................................%s", name, score);
                pw.append(linea).append("\n");
                fw.close();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
