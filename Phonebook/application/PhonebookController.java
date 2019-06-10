package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXB;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class PhonebookController {
	
	private RecordList records;
	private int recordsSize;
	private int counter = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtNameField;

    @FXML
    private TextField txtStateField;
    
    @FXML
    private Label txtCounter;

    @FXML
    private Button btnMinus;

    @FXML
    private Button btnPlus;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnNext;

    @FXML
    private Label txtFile;

    @FXML
    private TextField txtPhoneField;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnSerialize;

    @FXML
    private Button btnExit;
    
    @FXML
    private Button btnOK; //on PopupWindow.FXML
    
    @FXML
    private Button btnCancel; //on PopupWindow.FXML
    
    @FXML
    void clickedLoadButton(MouseEvent event) throws IOException {
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select File");
		File file = fileChooser.showOpenDialog(btnLoad.getScene().getWindow());
			
		String fileName = file.getName();
	//	String longFileName = file.getAbsolutePath();
		
		BufferedReader br = null;
		
		//Unmarshall & Loading XML File
		try {
			
		br = Files.newBufferedReader(Paths.get(fileName));
		br.readLine();
			if(br.readLine() == null) {
				throw new IOException();
			}

			
	        records = JAXB.unmarshal(br, RecordList.class);			
			recordsSize = records.getRecordList().size();
			counter = recordsSize;
			
			//RecordsSize is not 0
			btnPlus.disableProperty().set(false);
	    	btnMinus.disableProperty().set(false);
	    	btnSerialize.disableProperty().set(false);
	    	btnNext.disableProperty().set(false);
	    	btnPrevious.disableProperty().set(false);
		}
		catch(IOException e) {
			records = new RecordList();
			recordsSize = 0;
			counter = 0;
		//	System.out.println("Creating new List...");
			btnPlus.disableProperty().set(false);
		}
		
		txtCounter.setText(counter +" of " + recordsSize);
		
		txtFile.setText("File: " + fileName);
		
    }

    @FXML
    void clickedSerializeButton(MouseEvent event) throws IOException {
    	btnPlus.disableProperty().set(false);
    	
    	
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Select File");
		File file = fileChooser.showOpenDialog(btnLoad.getScene().getWindow());
		String fileName = file.getName();
		String longFileName = file.getAbsolutePath();

		
		//Marshall & Loading XML File
		try {
		//	Record record = new Record(txtNameField.getText(),txtStateField.getText(),txtPhoneField.getText());
		//	records.getRecordList().add(record);
			BufferedWriter bw = Files.newBufferedWriter(Paths.get(longFileName));			
			JAXB.marshal(records, bw);	
			
			bw.flush();
			bw.close();
			}
		catch(IOException e){
			System.err.println("Error opening file. Terminating.");
		}
		
		txtFile.setText("File: " + fileName);
    }
    
    @FXML
    void clickedPlusButton(MouseEvent event) {
    	if(!(txtNameField.getText().matches("([A-Z][a-zA-Z]{2,}\\s{0,1}){0,}")) || (txtNameField.getText().split(" ").length < 1)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Value");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Name. Names should start with an uppercase letter followed by at least two characters");
            alert.showAndWait();
            }
    	else if(!(txtStateField.getText().matches("([A-Z][a-zA-Z]{2,}\\s{0,1}){0,}")) || (txtStateField.getText().split(" ").length > 2)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Value");
            alert.setHeaderText(null);
            alert.setContentText("Invalid State. States should consist of one or two words.");
            alert.showAndWait();
    	}
    	else if(!(txtPhoneField.getText().matches("\\([1-9]\\d{2}\\)\\s[1-9]\\d{2}\\s{0,1}-\\s{0,1}\\d{4}"))) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Value");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Phone Number. Ex (212) 555-1234");
            alert.showAndWait();
    	}
    	else{
	    	Record record = new Record(txtNameField.getText(),txtStateField.getText(),txtPhoneField.getText());
	    	records.getRecordList().add(record);
	    	
	    	counter++;
	    	recordsSize = records.getRecordList().size();
	    	
	    	if(counter == recordsSize) {
	    		txtCounter.setText(counter +" of " + (recordsSize)); // Increment Counter
	    	}
	    	else {
	    		txtCounter.setText((counter+1) +" of " + (recordsSize)); // Increment Counter
	
	    	}
	    	if((counter) == 1) {
		    	btnMinus.disableProperty().set(false);
		    	btnSerialize.disableProperty().set(false);
		    	btnNext.disableProperty().set(false);
		    	btnPrevious.disableProperty().set(false);
	    	}
    	}
    }
    
    @FXML
    void clickedMinusButton(MouseEvent event) {
    	
    	records.getRecordList().remove(counter);
    	counter--;
    	recordsSize = records.getRecordList().size();
    	Record record = records.getRecordList().get(counter);
    	
    	txtNameField.setText(record.getName());
    	txtStateField.setText(record.getState());
    	txtPhoneField.setText(record.getPhone());
    	
		txtCounter.setText((counter+1) +" of " + recordsSize); // Decrement Counter
		
		if((counter) == 0) {
	    	btnMinus.disableProperty().set(true);
	    	btnSerialize.disableProperty().set(false);
	    	btnNext.disableProperty().set(true);
	    	btnPrevious.disableProperty().set(true);
		}
    }
    
    @FXML
    void clickedNextButton(MouseEvent event) {
    	ArrayList<Record> showRecords = records.getRecordList();
    	recordsSize = records.getRecordList().size();
    	
    	if(counter == recordsSize) {
    		System.err.println("Invalid");
    	}
    	else {
    		counter++;
    		Record record = showRecords.get(counter);
        	txtNameField.setText(record.getName());
        	txtStateField.setText(record.getState());
        	txtPhoneField.setText(record.getPhone());
        	
    		txtCounter.setText((counter+1) +" of " + recordsSize);
    		
    	}
    }
    
    @FXML
    void clickedPreviousButton(MouseEvent event) {
    	ArrayList<Record> showRecords = records.getRecordList();
    	recordsSize = records.getRecordList().size();
    	
    	if(recordsSize == 0) {
    		System.err.println("Invalid");
    	}
    	else {
    		counter--;
    		Record record = showRecords.get(counter);
        	txtNameField.setText(record.getName());
        	txtStateField.setText(record.getState());
        	txtPhoneField.setText(record.getPhone());
        	
    		txtCounter.setText((counter+1) +" of " + recordsSize);
    		
    	}
    }
    
    @FXML
    void clickedExitButton(MouseEvent event) throws IOException {
    	
    	 Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Exit");
         alert.setHeaderText(null);
         alert.setContentText("Are you sure you want to exit?");
         alert.showAndWait();
         if(alert.getResult() == ButtonType.OK) {
        	Platform.exit();
         }
    }

/*
    @FXML
    void initialize() {
    	 assert txtNameField != null : "fx:id=\"txtNameField\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert txtStateField != null : "fx:id=\"txtStateField\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert txtCounter != null : "fx:id=\"txtCounter\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnMinus != null : "fx:id=\"btnMinus\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnPlus != null : "fx:id=\"btnPlus\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnPrevious != null : "fx:id=\"btnPrevious\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnNext != null : "fx:id=\"btnNext\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert txtPhoneField != null : "fx:id=\"txtPhoneField\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnLoad != null : "fx:id=\"btnLoad\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnSerialize != null : "fx:id=\"btnSerialize\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert txtFile != null : "fx:id=\"txtFile\" was not injected: check your FXML file 'Phonebook.fxml'.";
         assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'Phonebook.fxml'.";

    }
    */
}
