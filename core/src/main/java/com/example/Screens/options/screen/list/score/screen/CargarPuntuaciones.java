package com.example.Screens.options.screen.list.score.screen;

import com.badlogic.gdx.utils.Array;

import java.io.*;

/**
 * Created by Nono on 24/11/2022.
 */
public class CargarPuntuaciones {
    private final File fichero = new File("Scores.txt");

    public Array<String> getScore() {
        Array<String> scores = new Array<>();
        if (fichero.exists()) {
            try {
                FileReader fr = new FileReader(fichero);
                BufferedReader br = new BufferedReader(fr);
                String linea;

                while ((linea = br.readLine()) != null) {
                    scores.add(linea);
                }
                fr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                FileWriter fw = new FileWriter(fichero);
                PrintWriter pw = new PrintWriter(fw);
                String linea = "No hay puntuaciones";
                pw.println(linea);

                fw.close();
                pw.close();

                scores.add(linea);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return scores;
    }
}
