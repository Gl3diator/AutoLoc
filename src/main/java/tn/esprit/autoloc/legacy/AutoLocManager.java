package tn.esprit.autoloc.legacy;

public class AutoLocManager {

    public void louerVehicule(
            String typeVehicule,
            String typeReservation,
            int nombreJours,
            String clientEmail) {

        double prix = 0;

        if (typeReservation.equals("LongDuree")) {
            prix = nombreJours * 120;
        } else if (typeReservation.equals("Weekend")) {
            prix = nombreJours * 180;
        } else if (typeReservation.equals("Standard")) {
            prix = nombreJours * 150;
        }

        System.out.println("Contrat créé");
        System.out.println("Prix total : " + prix);

        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    private void envoyerEmail(String email) {
        System.out.println("Email envoyé à " + email);
    }

    private void envoyerSMS(String sms) {
        System.out.println("SMS envoyé à " + sms);
    }

    private void enregistrerPaiement() {
        System.out.println("Paiement enregistré");
    }

    private void sauvegarderContrat() {
        System.out.println("Contrat sauvegardé");
    }
}
