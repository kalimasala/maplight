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
//Test edit
//my Test Branch
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

  public static List<CandidateContributions> get(Params params) {
    String recipient = getOrEmpty(params, "recipient");
    String donor = getOrEmpty(params, "donor");
    if (recipient.isEmpty() && donor.isEmpty()) {
      return new ArrayList<CandidateContributions>();
    }
    WhereData where = constructWhereClauseFromParams(params);
//    TODO(rkj): ORDER cannot be parametrized in MySQL, I do not have time
//      to escape this on my own...
//    String order = getOrDefault(params, "order", "TransactionAmount DESC");
    String sql = "SELECT c FROM CandidateContributions c\n"
      + where.create();
    Logger.info(sql.replace("?", "'%s'"), where.data.toArray());
    System.err.printf(sql.replace("?", "'%s'") + "\n", where.data.toArray());
    JPAQuery query = find(sql, where.data.toArray());
    return query.fetch(getLimit(params));
  }

  public static int getTotal(Params params) {
    WhereData where = constructWhereClauseFromParams(params);
    String result = find(
        "SELECT SUM(c.TransactionAmount) FROM CandidateContributions c " + where.create(),
        where.data.toArray()).first();
    return Integer.parseInt(result);
  }

  private static final int LIMIT = 100;
  private static int getLimit(Params params) {
      if (!getOrEmpty(params, "download").isEmpty()) {
        return Integer.MAX_VALUE;
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
        data.append("Current = True", new Object[0]);
      } else {
        String[] recipients = recipient.split("\\s*,\\s*");
        List<String> dummy = new LinkedList<String>();
        for (String _ : recipients) {
          dummy.add("?");
        }
        data.append(
          "c.RecipientCandidateNameNormalized IN (" + JavaExtensions.join(dummy, ", ") + ")",
          recipients
        );
      }
    }
    if (!donor.isEmpty()) {
      String closeDonor = donor + "%";
      data.append(
        "(c.DonorNameNormalized = ? OR c.DonorOrganization = ?)",
        donor, donor
      );
    }
    if (!date_start.isEmpty()) {
      data.append("TransactionDate >= ?", date_start);
    }
    if (!date_end.isEmpty()) {
      data.append("TransactionDate <= ?", date_end);
    }
    if (!location_from.isEmpty()) {
      data.append("(DonorState = ? OR DonorCity = ?)", location_from, location_from);
    }
    if (!location_to.isEmpty()) {
      data.append("RecipientCandidateOfficeState = ?", location_to);
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
