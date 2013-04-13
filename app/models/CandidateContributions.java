package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
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
		return find("SELECT DISTINCT c.RecipientNameNormalized FROM CandidateContributions c").fetch();
	}
	public static List<CandidateContributions> findByRecipientDonarYear(String recipient, String donar, int year) {
		final String y = new Integer(year).toString();
		return find("byRecipientNameNormalizedAndDonorNameNormalizedAndElectionCycle", recipient, donar, y).fetch();
	}
	
}
