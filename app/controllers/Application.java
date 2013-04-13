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
    	List <CandidateContributions> cc = CandidateContributions.findByRecipientDonarYear(recipient, donar, year);
    	render(cc);
    }

    public static void query() {
      render();
    }

}