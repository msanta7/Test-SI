package Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FonctionnalitesReservation {

	private List<Reservation> reservations = new ArrayList<>();
	
	public FonctionnalitesReservation()
	{
		reservations = new ArrayList<>();
		
	}
	public void addReservation(Reservation reservation) throws DejaReservéeException {
        for (Reservation res : reservations) {
            if (res.getSalle().equals(reservation.getSalle()) &&
                res.getDate().equals(reservation.getDate())) {               //hna teni zedt te3 la date
                throw new DejaReservéeException("Salle déjà réservée à cette date.");
            }
        }
        reservations.add(reservation);
    }
	
	
		public void ModifierReservation(String IdResrvation,
		 String Nom_Employe,
		 LocalDateTime Date,
		 double Durée,
		 Salle salle)throws ReservationNonTrouvéeException
		{
			for(Reservation reservation : reservations)
			{
				if(reservation.getIdResrvation().equals(IdResrvation))
				{
					reservation.setIdResrvation(IdResrvation); //Chaque réservation doit être attribuée à un numéro unique généré automatiquement. 
					reservation.setNom_Employe(Nom_Employe);
					reservation.setDate(Date);
					reservation.setDurée(Durée);
					reservation.setSalle(salle);
					return;
				}
			}
			throw new ReservationNonTrouvéeException("Reservation non-trouvée");
			
		}
	
		public void removeReservation(String id) throws ReservationNonTrouvéeException
		{
			for (Reservation reservation : reservations) {
	            if (reservation.getIdResrvation().equals(id)) {
	                reservations.remove(reservation);
	                return;
	            }
	        }
			
				throw new ReservationNonTrouvéeException("Reservation non-trouvée");
			
		}
		
		public List<Reservation> afficherReservations() {
	        return new ArrayList<>(reservations);
	    }
	}


