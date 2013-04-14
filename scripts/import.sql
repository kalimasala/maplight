CREATE TABLE Import (
    TransactionTypeCode VARCHAR,
    TransactionType VARCHAR,
    ElectionCycle VARCHAR,
    ReportingCommitteeMLID VARCHAR,
    ReportingCommitteeFECID VARCHAR,
    ReportingCommitteeName VARCHAR,
    ReportingCommitteeNameNormalized VARCHAR,
    PrimaryGeneralIndicator VARCHAR,
    TransactionID VARCHAR,
    FileNumber VARCHAR,
    RecordNumberML VARCHAR,
    RecordNumberFEC VARCHAR,
    TransactionDate VARCHAR,
    TransactionAmount VARCHAR,
    RecipientName VARCHAR,
    RecipientNameNormalized VARCHAR,
    RecipientCity VARCHAR,
    RecipientState VARCHAR,
    RecipientZipCode VARCHAR,
    RecipientEmployer VARCHAR,
    RecipientEmployerNormalized VARCHAR,
    RecipientOccupation VARCHAR,
    RecipientOccupationNormalized VARCHAR,
    RecipientOrganization VARCHAR,
    RecipientEntityTypeCode VARCHAR,
    RecipientEntityType VARCHAR,
    RecipientCommitteeMLID VARCHAR,
    RecipientCommitteeFECID VARCHAR,
    RecipientCommitteeName VARCHAR,
    RecipientCommitteeNameNormalized VARCHAR,
    RecipientCommitteeTreasurer VARCHAR,
    RecipientCommitteeDesignationCode VARCHAR,
    RecipientCommitteeDesignation VARCHAR,
    RecipientCommitteeTypeCode VARCHAR,
    RecipientCommitteeType VARCHAR,
    RecipientCommitteeParty VARCHAR,
    RecipientCandidateMLID VARCHAR,
    RecipientCandidateFECID VARCHAR,
    RecipientCandidateName VARCHAR,
    RecipientCandidateNameNormalized VARCHAR,
    RecipientCandidateParty VARCHAR,
    RecipientCandidateICO VARCHAR,
    RecipientCandidateStatus VARCHAR,
    RecipientCandidateOfficeState VARCHAR,
    RecipientCandidateOffice VARCHAR,
    RecipientCandidateDistrict VARCHAR,
    RecipientCandidateGender VARCHAR,
    DonorName VARCHAR,
    DonorNameNormalized VARCHAR,
    DonorCity VARCHAR,
    DonorState VARCHAR,
    DonorZipCode VARCHAR,
    DonorEmployer VARCHAR,
    DonorEmployerNormalized VARCHAR,
    DonorOccupation VARCHAR,
    DonorOccupationNormalized VARCHAR,
    DonorOrganization VARCHAR,
    DonorEntityTypeCode VARCHAR,
    DonorEntityType VARCHAR,
    DonorCommitteeMLID VARCHAR,
    DonorCommitteeFECID VARCHAR,
    DonorCommitteeName VARCHAR,
    DonorCommitteeNameNormalized VARCHAR,
    DonorCommitteeTreasurer VARCHAR,
    DonorCommitteeDesignationCode VARCHAR,
    DonorCommitteeDesignation VARCHAR,
    DonorCommitteeTypeCode VARCHAR,
    DonorCommitteeType VARCHAR,
    DonorCommitteeParty VARCHAR,
    DonorCandidateMLID VARCHAR,
    DonorCandidateFECID VARCHAR,
    DonorCandidateName VARCHAR,
    DonorCandidateNameNormalized VARCHAR,
    DonorCandidateParty VARCHAR,
    DonorCandidateICO VARCHAR,
    DonorCandidateStatus VARCHAR,
    DonorCandidateOfficeState VARCHAR,
    DonorCandidateOffice VARCHAR,
    DonorCandidateDistrict VARCHAR,
    DonorCandidateGender VARCHAR,
    UpdateTimestamp VARCHAR);

LOAD DATA INFILE 'data.csv' INTO TABLE Import FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 LINES (
    TransactionTypeCode,
    TransactionType,
    ElectionCycle,
    ReportingCommitteeMLID,
    ReportingCommitteeFECID,
    ReportingCommitteeName,
    ReportingCommitteeNameNormalized,
    PrimaryGeneralIndicator,
    TransactionID,
    FileNumber,
    RecordNumberML,
    RecordNumberFEC,
    TransactionDate,
    TransactionAmount,
    RecipientName,
    RecipientNameNormalized,
    RecipientCity,
    RecipientState,
    RecipientZipCode,
    RecipientEmployer,
    RecipientEmployerNormalized,
    RecipientOccupation,
    RecipientOccupationNormalized,
    RecipientOrganization,
    RecipientEntityTypeCode,
    RecipientEntityType,
    RecipientCommitteeMLID,
    RecipientCommitteeFECID,
    RecipientCommitteeName,
    RecipientCommitteeNameNormalized,
    RecipientCommitteeTreasurer,
    RecipientCommitteeDesignationCode,
    RecipientCommitteeDesignation,
    RecipientCommitteeTypeCode,
    RecipientCommitteeType,
    RecipientCommitteeParty,
    RecipientCandidateMLID,
    RecipientCandidateFECID,
    RecipientCandidateName,
    RecipientCandidateNameNormalized,
    RecipientCandidateParty,
    RecipientCandidateICO,
    RecipientCandidateStatus,
    RecipientCandidateOfficeState,
    RecipientCandidateOffice,
    RecipientCandidateDistrict,
    RecipientCandidateGender,
    DonorName,
    DonorNameNormalized,
    DonorCity,
    DonorState,
    DonorZipCode,
    DonorEmployer,
    DonorEmployerNormalized,
    DonorOccupation,
    DonorOccupationNormalized,
    DonorOrganization,
    DonorEntityTypeCode,
    DonorEntityType,
    DonorCommitteeMLID,
    DonorCommitteeFECID,
    DonorCommitteeName,
    DonorCommitteeNameNormalized,
    DonorCommitteeTreasurer,
    DonorCommitteeDesignationCode,
    DonorCommitteeDesignation,
    DonorCommitteeTypeCode,
    DonorCommitteeType,
    DonorCommitteeParty,
    DonorCandidateMLID,
    DonorCandidateFECID,
    DonorCandidateName,
    DonorCandidateNameNormalized,
    DonorCandidateParty,
    DonorCandidateICO,
    DonorCandidateStatus,
    DonorCandidateOfficeState,
    DonorCandidateOffice,
    DonorCandidateDistrict,
    DonorCandidateGender,
    UpdateTimestamp);

ALTER TABLE Import CHANGE TransactionAmount TransactionAmount Integer;
ALTER TABLE Import ADD Current Boolean;