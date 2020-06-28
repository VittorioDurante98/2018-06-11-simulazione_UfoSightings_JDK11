package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoAvvistamento;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<AnnoAvvistamento> boxAnno;

    @FXML
    private ComboBox<String> boxStato;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleAnalizza(ActionEvent event) {
    	txtResult.clear();
    	AnnoAvvistamento aa= null;
    	aa= boxAnno.getValue();
    	if(aa==null) {
    		txtResult.appendText("Seleziona un anno!");
    		return;
    	}
    	String stato = boxStato.getValue();
    	txtResult.appendText("PRECEDENTI:\n");
    	for (String s1 : model.getPrecedenti(stato)) {
			txtResult.appendText(s1+"\n");
		}
    	txtResult.appendText("\nSUCCESSORI:\n");
    	for (String s2 : model.getSuccessivi(stato)) {
    		txtResult.appendText(s2+"\n");
		}
    	txtResult.appendText("\nVISITA:\n");
    	for (String s3 : model.getVisita(stato)) {
    		txtResult.appendText(s3+"\n");
    	}
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	txtResult.clear();
    	AnnoAvvistamento aa= null;
    	aa= boxAnno.getValue();
    	if(aa==null) {
    		txtResult.appendText("Seleziona un anno!");
    		return;
    	}
    	model.creaGrafo(aa);
    	List<String > stati = new ArrayList<String>(model.getGrafo().vertexSet());
    	boxStato.getItems().addAll(stati);
    	txtResult.appendText("GRAFO CREATO!\nVERTICI: "+model.getGrafo().vertexSet().size()+"\nARCHI: "+model.getGrafo().edgeSet().size());
    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	AnnoAvvistamento aa= null;
    	aa= boxAnno.getValue();
    	if(aa==null) {
    		txtResult.appendText("Seleziona un anno!");
    		return;
    	}
    	if (boxStato.getValue()==null) {
    		txtResult.appendText("Seleziona un anno!");
    		return;
		}
    	txtResult.appendText("CAMMINO MASSIMO:\n");
    	for (String s : model.trovaRicorsione(boxStato.getValue())) {
			txtResult.appendText(s+" ");
		}
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		txtResult.setEditable(false);
		
		boxAnno.getItems().addAll(model.getYearSightings());

	}
}
