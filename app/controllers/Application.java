package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void listByRecipientDonarYear(String recipient, String donar, int year) {
    	if (recipient.isEmpty() && donar.isEmpty()) {
    		flash.error("both recipient and donar cannot be empty");
    		index();
		}
    	List <CandidateContributions> cc = null;
    	if (!recipient.isEmpty() && !donar.isEmpty())
    		cc = CandidateContributions.findByRecipientDonar(recipient, donar, year);
    	else if (!recipient.isEmpty()) {
    		cc = CandidateContributions.findByRecipient(recipient, year);
    	} 
    	else {
    		cc = CandidateContributions.findByDonor(donar, year);
    	}
    	render(cc);
    }

  public static void byDonors(String from, String to, String date_start, String date_end) {
	if (from == null) from = "";
	if (to == null) to = "";
  	if (from.isEmpty() && to.isEmpty()) {
		flash.error("both recipient and donor cannot be empty");
		return;
	}
	renderTemplate("CandidateContributions.html", CandidateContributions.get(from, to, date_start, date_end));
  }
}
