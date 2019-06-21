package it.polito.tdp.model;

import java.util.*;

import it.polito.tdp.dao.EsameDAO;


public class Model {

	//esami letti dal DB
	private List<Esame> esami;
	
	//gestione della ricorsione
	private List<Esame> best;
	private double media_best;
	
	public Model() {
		
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
		
	}
	
	/**
	 * Trova la combinazione di corsi avente somma dei crediti richiesta
	 * che abbia la media massima.
	 * 
	 * @param numeroCrediti
	 * @return l'elenco dei corsi ottimale oppure {@code null} se non esiste alcuna combinazione di corsi
	 * la cui somma di crediti è il parametro passato
	 * 
	 */
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		best = null;	
		media_best=0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}

	private void cerca(Set <Esame> parziale, int L, int m) {
		
		//casi terminali
		int crediti = sommaCrediti(parziale);
		if(crediti>m)
			return;
		
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			
			if(media>media_best) {
				//evvia
				best = new ArrayList<Esame>(parziale);			
				media_best=media;
				return;
			}
			else
				return;
		}
		
		if(L==esami.size()) {
			return;
		}
		
		//caso normale
		//QUI E' DIVERSO PERCHE' NON CONTA L'ORDINE
		//esame L è da aggiungere o no?
		
		//provo a non aggiungerlo
		cerca(parziale, L+1,m);
		
		//provo ad aggiungerlo
		
		parziale.add(esami.get(L));
		
		cerca(parziale, L+1,m);
		
	    parziale.remove(esami.get(L));
		
	}

	private double calcolaMedia(Set<Esame> parziale) {
	
		double media = 0.0;
		int crediti = 0;
		
		for(Esame e : parziale) {
			media+=e.getCrediti()*e.getVoto();
			crediti+=e.getCrediti();
		}
		
		
		return media/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
	    
		int somma = 0;
		
		for(Esame e : parziale)
			somma+=e.getCrediti();
	
		return somma;
	}
}
