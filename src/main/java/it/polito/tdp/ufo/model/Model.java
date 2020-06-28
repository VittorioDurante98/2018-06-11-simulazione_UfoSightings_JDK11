package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;


import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	private Graph<String, DefaultEdge> grafo;
	private List<String> best;
	private List<String> parziale;
	
	
	public Model() {
		this.dao= new SightingsDAO();
	}
	
	public List<AnnoAvvistamento> getYearSightings() {
		return dao.getAnnoAvvistamento();
	}
	
	public void creaGrafo(AnnoAvvistamento aa) {
		this.grafo= new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, dao.getStati(aa.getAnno()));
		
		for (Arco arco : dao.getArchi(aa.getAnno())) {
			Graphs.addEdgeWithVertices(grafo, arco.getStato1(), arco.getStato2());
		}
	}
	
	public Graph<String, DefaultEdge> getGrafo(){
		return grafo;
	}
	
	public List<String> getSuccessivi(String stato) {
		return Graphs.successorListOf(grafo, stato);
	}
	
	public List<String> getPrecedenti(String stato) {
		return Graphs.predecessorListOf(grafo, stato);
	}
	
	public List<String> getVisita(String partenza) {
        List<String> stati = new ArrayList<>();
		final Map<String, String> visita = new HashMap<String, String>();
		DepthFirstIterator<String, DefaultEdge> it = new DepthFirstIterator<String, DefaultEdge>(this.grafo, partenza);
		
		visita.put(partenza, null);
		it.addTraversalListener(new TraversalListener<String, DefaultEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<String> e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void vertexFinished(VertexTraversalEvent<String> e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				String sorgente = grafo.getEdgeSource(e.getEdge());
				String destinazione = grafo.getEdgeTarget(e.getEdge());
				
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				}else if(visita.containsKey(destinazione) && !visita.containsKey(sorgente)) {
					visita.put(sorgente, destinazione);
				}
			}
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while (it.hasNext()) {
			it.next();
		}
		
		for (String step: visita.keySet()) {
			stati.add(step);
		}
		stati.remove(partenza);
		
		Collections.sort(stati);
		
		return stati;
	}
	   /* public List<String> getVisita(String partenza){
		final Map<String	,String> albero = new HashMap<>();
		List<String> result = new ArrayList<String>();

		
		BreadthFirstIterator<String,DefaultEdge> it =new BreadthFirstIterator<>(this.grafo,partenza);
		albero.put(partenza,null);
		
		it.addTraversalListener(new TraversalListener<String, DefaultEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<String> e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<String> e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				String sorgente= grafo.getEdgeSource(e.getEdge());
				String destinazione= grafo.getEdgeTarget(e.getEdge());
				if( !albero.containsKey(destinazione) && albero.containsKey(sorgente)) {
					albero.put(destinazione, sorgente);
				}else if( !albero.containsKey(sorgente) && albero.containsKey(destinazione)) {
					albero.put(sorgente, destinazione);
				}
				
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

			
			
	
		while(it.hasNext()) {
			it.next();
		}
		
		for(String a:albero.keySet()) {
			result.add(a);
		}
		result.remove(partenza);
		
		Collections.sort(result);


		return result;
	}*/
	
	public List<String> trovaRicorsione(String partenza) {
		this.parziale = new ArrayList<String>();
		this.best = new ArrayList<String>();
		parziale.add(partenza);
		int livello = 1;
		cerca(livello, parziale);
		return best;
	}

	private void cerca(int livello, List<String> parziale) {
		
		for(String s: Graphs.successorListOf(grafo, parziale.get(parziale.size()-1))) {
			if (!parziale.contains(s)) {
				parziale.add(s);
				cerca(livello+1, parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		if (best.size()<parziale.size()) {
			best= new ArrayList<String>(parziale);
		}
		
	}

}
