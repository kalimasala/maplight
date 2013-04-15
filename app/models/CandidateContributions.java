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
    
    String order = getOrDefault(params, "order", "TransactionAmount DESC");

    StringBuffer sql = new StringBuffer();
    WhereData where = constructWhereClauseFromParams(params);
    sql.append("SELECT c FROM CandidateContributions c\n");
    sql.append(where.create());
    sql.append("\nORDER BY ?");
    where.data.add(order);
    Logger.info(sql.toString().replace("?", "'%s'"), where.data.toArray());
    return find(sql.toString(), where.data.toArray()).fetch(getLimit(params));
  }

  public static Object getTotal(Params params) {
    WhereData where = constructWhereClauseFromParams(params);
    return find(
        "SELECT SUM(c.TransactionAmount) FROM CandidateContributions c " + where.create(),
        where.data.toArray())
      .fetch(getLimit(params)).get(0);
  }

  private static int getLimit(Params params) {
      int limit = LIMIT;
      try {
        limit = Integer.valueOf(getOrEmpty(params, "limit"));
        if (limit <= 0) {
          limit = LIMIT;
        }
      } catch (Exception _) {
        // leave default value
      }
      return limit;
  }

  private static WhereData constructWhereClauseFromParams(Params params) {
    String recipient = getOrEmpty(params, "recipient");
    String donor = getOrEmpty(params, "donor");
    String date_start = getOrEmpty(params, "date-start");
    String date_end = getOrEmpty(params, "date-end");
    String location_from = getOrEmpty(params, "location-from");
    String location_to = getOrEmpty(params, "location-to");

    WhereData data = new WhereData();
    if (!recipient.isEmpty()) {
      if (recipient.equals("__anyone")) {
        // nothing
      } else if (recipient.equals("__current")) {
        data.append("Current = True", null);
      } else {
        String[] recipients = recipient.toLowerCase().split("\\s*,\\s*");
        List<String> dummy = new LinkedList<String>();
        for (String r : recipients) {
          dummy.add("?");
        }
        data.append(
          "lower(c.RecipientCandidateNameNormalized) IN (" + JavaExtensions.join(dummy, ", ") + ")",
          recipients
        );
      }
    }
    if (!donor.isEmpty()) {
      String closeDonor = "%" + donor + "%";
      data.append(
        "(c.DonorNameNormalized like ? OR c.DonorOrganization like ?)",
        closeDonor, closeDonor
      );
    }
    if (!date_start.isEmpty()) {
      data.append("TransactionDate >= ?", date_start);
    }
    if (!date_end.isEmpty()) {
      data.append("TransactionDate <= ?", date_end);
    }
    if (!location_from.isEmpty()) {
      String lowerLocation = location_from.toLowerCase();
      data.append("(lower(DonorState) = ? OR lower(DonorCity) = ?)", lowerLocation, lowerLocation);
    }
    if (!location_to.isEmpty()) {
      data.append("lower(RecipientCandidateOfficeState) = ", location_to.toLowerCase());
    }
    return data;
  }

  private final static class WhereData {
    List<String> conditions = new LinkedList<String>();
    List<Object> data = new LinkedList<Object>();

    public WhereData append(String condition, Object... objects) {
      conditions.add(condition);
      if (objects != null) {
        data.addAll(Arrays.asList(objects));
      }
      return this;
    }

    public String create() {
      if (conditions.size() <= 0) {
        return "";
      }
      return "WHERE " + JavaExtensions.join(conditions, " AND ");
    }
  }
}
