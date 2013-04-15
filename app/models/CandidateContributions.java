package models;

import play.*;
import play.db.jpa.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Scope.Params;
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


	public static String getOrEmpty(Params params, String key) {
		return getOrDefault(params, key, "");
	}

	public static String getOrDefault(Params params, String key, String default_) {
		String obj = params.get(key);
		return obj == null ? default_ : obj;
	}

  private static final int LIMIT = 100;
	public static List<CandidateContributions> get(Params params) {
		String recipient = getOrEmpty(params, "recipient");
		String donor = getOrEmpty(params, "donor");
		if (recipient.isEmpty() && donor.isEmpty()) {
			return new ArrayList<CandidateContributions>();
		}
    
		String date_start = getOrEmpty(params, "date-start");
		String date_end = getOrEmpty(params, "date-end");
		String location_from = getOrEmpty(params, "location-from");
		String location_to = getOrEmpty(params, "location-to");

		List<String> wheres = new LinkedList<String>();
		List<Object> finders = new LinkedList<Object>();
		if (!recipient.isEmpty()) {
    		if (recipient.equals("__anyone")) {
    			// nothing
    		} else if (recipient.equals("__current")) {
    			wheres.add("Current = True");
    		} else {
    			String[] recipients = recipient.toLowerCase().split("\\s*,\\s*");
    			StringBuffer recBuf = new StringBuffer("lower(c.RecipientCandidateNameNormalized) IN (");
    			boolean isFirst = true;
    			for (String r : recipients) {
    				if (!isFirst) { recBuf.append(", ");}
    				isFirst = false;
    				recBuf.append("?");
    				finders.add(r.toLowerCase());
    			}
    			recBuf.append(")");
    			wheres.add(recBuf.toString());
    		}
    	}
    	if (!donor.isEmpty()) {
    		wheres.add("(c.DonorNameNormalized like ? OR c.DonorOrganization like ?)");
    		finders.add("%" + donor + "%");
    		finders.add("%" + donor + "%");
    	}
    	if (!date_start.isEmpty()) {
    		wheres.add("TransactionDate >= ?");
    		finders.add(date_start);
    	}
    	if (!date_end.isEmpty()) {
    		wheres.add("TransactionDate <= ?");
    		finders.add(date_end);
    	}
    	if (!location_from.isEmpty()) {
    		wheres.add("(lower(DonorState) = ? OR lower(DonorCity) = ?)");
    		finders.add(location_from.toLowerCase());
    		finders.add(location_from.toLowerCase());
    	}
    	if (!location_to.isEmpty()) {
    		wheres.add("lower(RecipientCandidateOfficeState) = ");
    		finders.add(location_to.toLowerCase());
    	}
    	
      String order = getOrEmpty(params, "order");
      if (order.isEmpty()) {
        order = "TransactionAmount DESC";
      }

      int limit = LIMIT;
      try {
        limit = Integer.valueOf(getOrEmpty(params, "limit"));
        if (limit <= 0) {
          limit = LIMIT;
        }
      } catch (Exception _) {
        // leave default value
      }
    	
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT c FROM CandidateContributions c ");
    	if (wheres.size() > 0) {
    		sql.append("\nWHERE ");
    		sql.append(JavaExtensions.join(wheres, " AND "));
    	}
      sql.append("\nORDER BY ?");
      finders.add(order);
    	Logger.info(sql.toString().replace("?", "'%s'"), finders.toArray());
    	return find(sql.toString(), finders.toArray()).fetch(limit);
	}
}
