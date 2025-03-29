package Test;



public class Salle {
	 private Batiment batiment;
	 private String Nom_Salle;
	
	 public Salle(Batiment batiment, String Nom_Salle)
	 {
		 this.Nom_Salle=Nom_Salle;
		 this.batiment=batiment;
	 }
	 

	
	public Batiment getBatiment() {
		return batiment;
	}
	public void setBatiment(Batiment batiment) {
		this.batiment = batiment;
	}



	public String getNom_Salle() {
		return Nom_Salle;
	}



	public void setNom_Salle(String nom_Salle) {
		Nom_Salle = nom_Salle;
	}
	
	
}
