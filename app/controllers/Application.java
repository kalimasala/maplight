package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void listByRecipientDonorYear(String recipient, String donor, int year,
    		boolean download) {
    	recipient = recipient.toLowerCase();
    	donor = donor.toLowerCase();
    	if (recipient.isEmpty() && donor.isEmpty()) {
    		flash.error("both recipient and donor cannot be empty");
    		index();
		}
    	List <CandidateContributions> cc = null;
    	if (!recipient.isEmpty() && !donor.isEmpty()) {
    		cc = CandidateContributions.findByRecipientDonor(recipient, donor, year);
        } else if (!recipient.isEmpty()) {
    		cc = CandidateContributions.findByRecipient(recipient, year);
    	} 
    	else {
    		cc = CandidateContributions.findByDonor(donor, year);
    	}
    	if (download) {
    		response.setHeader("Content-Disposition", "attachment; filename=download.csv");
    		renderTemplate("Application/listByRecipientDonorYear.csv", cc);
    	}
    	else {
        	render(cc, recipient, donor, year);
    	}
    }
    
  public static void byDonors() {
	List<CandidateContributions> cc = CandidateContributions.get(params);
	renderTemplate("Application/CandidateContributions.html", cc);
  }
}
