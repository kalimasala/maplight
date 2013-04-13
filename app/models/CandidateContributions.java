package models;

import play.*;
import play.db.jpa.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.templates.JavaExtensions;

import javax.persistence.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

@Entity
public class CandidateContributions extends Model {
	public String TransactionTypeCode;
	public String TransactionType;
	public String ElectionCycle;
	public String ReportingCommitteeMLID;
	public String ReportingCommitteeFECID;
	public String ReportingCommitteeName;
	public String ReportingCommitteeNameNormalized;
	public String PrimaryGeneralIndicator;
	public String TransactionID;
	public String FileNumber;
	public String RecordNumberML;
	public String RecordNumberFEC;
	public String TransactionDate;
	public String TransactionAmount;
	public String RecipientName;
	public String RecipientNameNormalized;
	public String RecipientCity;
	public String RecipientState;
	public String RecipientZipCode;
	public String RecipientEmployer;
	public String RecipientEmployerNormalized;
	public String RecipientOccupation;
	public String RecipientOccupationNormalized;
	public String RecipientOrganization;
	public String RecipientEntityTypeCode;
	public String RecipientEntityType;
	public String RecipientCommitteeMLID;
	public String RecipientCommitteeFECID;
	public String RecipientCommitteeName;
	public String RecipientCommitteeNameNormalized;
	public String RecipientCommitteeTreasurer;
	public String RecipientCommitteeDesignationCode;
	public String RecipientCommitteeDesignation;
	public String RecipientCommitteeTypeCode;
	public String RecipientCommitteeType;
	public String RecipientCommitteeParty;
	public String RecipientCandidateMLID;
	public String RecipientCandidateFECID;
	public String RecipientCandidateName;
	public String RecipientCandidateNameNormalized;
	public String RecipientCandidateParty;
	public String RecipientCandidateICO;
	public String RecipientCandidateStatus;
	public String RecipientCandidateOfficeState;
	public String RecipientCandidateOffice;
	public String RecipientCandidateDistrict;
	public String RecipientCandidateGender;
	public String DonorName;
	public String DonorNameNormalized;
	public String DonorCity;
	public String DonorState;
	public String DonorZipCode;
	public String DonorEmployer;
	public String DonorEmployerNormalized;
	public String DonorOccupation;
	public String DonorOccupationNormalized;
	public String DonorOrganization;
	public String DonorEntityTypeCode;
	public String DonorEntityType;
	public String DonorCommitteeMLID;
	public String DonorCommitteeFECID;
	public String DonorCommitteeName;
	public String DonorCommitteeNameNormalized;
	public String DonorCommitteeTreasurer;
	public String DonorCommitteeDesignationCode;
	public String DonorCommitteeDesignation;
	public String DonorCommitteeTypeCode;
	public String DonorCommitteeType;
	public String DonorCommitteeParty;
	public String DonorCandidateMLID;
	public String DonorCandidateFECID;
	public String DonorCandidateName;
	public String DonorCandidateNameNormalized;
	public String DonorCandidateParty;
	public String DonorCandidateICO;
	public String DonorCandidateStatus;
	public String DonorCandidateOfficeState;
	public String DonorCandidateOffice;
	public String DonorCandidateDistrict;
	public String DonorCandidateGender;
	public String UpdateTimestamp;

	public static List<String> getCandidatesNames() {
		return find("SELECT DISTINCT RecipientCandidateNameNormalized FROM CandidateContributions").fetch();
	}
	
	//TODO(rkj): cache this
	public static List<JsonObject> getCurrentCandidates() {
		String url = "http://data.maplight.org/FEC/active_incumbents.json";
		HttpResponse res = WS.url(url).get();
		JsonElement json = res.getJson();
		List<JsonObject> ret = new LinkedList<JsonObject>();
		for (JsonElement el : json.getAsJsonArray()) {
			JsonObject obj = el.getAsJsonObject();
			ret.add(obj);
		}
		return ret;
	}
	
	public static List<String> getCompaniesNames() {
		return find("SELECT DISTINCT DonorOrganization FROM CandidateContributions").fetch();
	}


	public static List<CandidateContributions> findByRecipientDonor(String recipient, String donor, int year) {
		donor += "%";
		if (year == 0) 
			return find("select c from CandidateContributions c where (lower(c.DonorNameNormalized) like ?" +
						"or lower(c.DonorOrganization) like ?) and lower(c.RecipientCandidateNameNormalized) = ?", 
						donor, donor, recipient
					).fetch();
		else {
			final String y = new Integer(year).toString();
			Logger.info("donor = %s, r = %s, year = %s", donor, recipient, y);
			return find("select c from CandidateContributions c where (lower(c.DonorNameNormalized) like ?" +
					"or lower(c.DonorOrganization) like ?) and lower(c.RecipientCandidateNameNormalized) = ? " +
					"and c.ElectionCycle = ?", 
					donor, donor, recipient, y
				).fetch();
		}
	}

	public static List<CandidateContributions> findByRecipient(String recipient, int year) {
		if (year == 0) {
			return find("select c from CandidateContributions c where lower(c.RecipientCandidateNameNormalized) = ?", 
					recipient).fetch();			
		}
		else {
			final String y = new Integer(year).toString();
			return find("select c from CandidateContributions c where "+
						"lower(c.RecipientCandidateNameNormalized) = ? and ElectionCycle = ?", 
					recipient, y).fetch();			
		}
	}

	public static List<CandidateContributions> findByDonor(String donor, int year) {
		donor += "%";
		if (year == 0)
			return find("select c from CandidateContributions c where lower(c.DonorNameNormalized) like ?" +
						"or lower(c.DonorOrganization) like ?", donor, donor
					).fetch();
		else {
			final String y = new Integer(year).toString();
			return find("select c from CandidateContributions c where ( lower(c.DonorNameNormalized) like ?" +
					"or lower(c.DonorOrganization) like ?) and c.ElectionCycle = ?", donor, donor, y
				).fetch();
		}
	}

	public static List<CandidateContributions> get(
			String recipient,
			String donor,
			String date_start,
			String date_end) {
		List<String> wheres = new LinkedList<String>();
    	if (!recipient.isEmpty()) {
    		wheres.add("RecipientCandidateNameNormalized = " + recipient);
    	}
    	if (!donor.isEmpty()) {
    		wheres.add("DonorNameNormalized = " + donor);
    	}
    	if (!date_start.isEmpty()) {
    		wheres.add("TransactionDate >= " + date_start);
    	}
    	if (!date_end.isEmpty()) {
    		wheres.add("TransactionDate <= " + date_end);
    	}
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT * FROM CandidateContributions");
    	if (wheres.size() > 0) {
    		sql.append("\nWHERE ");
   			sql.append(JavaExtensions.join(wheres, ", "));
    	}
    	return find(sql.toString()).fetch();
	}
}
