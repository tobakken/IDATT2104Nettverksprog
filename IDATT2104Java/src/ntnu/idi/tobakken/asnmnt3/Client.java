package ntnu.idi.tobakken.asnmnt3;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("State name of machine: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Set up connection */
        Socket forbindelse = new Socket(tjenermaskin, PORTNR);
        System.out.println("Connected.");

        /* Åpner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse
                = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        System.out.println(innledning1);

        /* Leser tekst fra kommandovinduet (brukeren) */
        String enLinje = "n";
        String respons = ":";

        while (!enLinje.equals("")) {
            while (respons.charAt(respons.length() - 1) == ':') {
                respons = leseren.readLine();// mottar respons fra tjeneren
                System.out.println(respons);
                enLinje = leserFraKommandovindu.nextLine();
                skriveren.println(enLinje);  // sender teksten til tjeneren
            }
            respons = leseren.readLine();
            String respons2 = leseren.readLine();
            System.out.println(respons + "\n" + respons2);
            enLinje = leserFraKommandovindu.nextLine();
            if (enLinje.equals("yes")) {respons = ":";}
            else {enLinje = "";}
            skriveren.println(enLinje);
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}

