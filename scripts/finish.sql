DROP TABLE IF EXISTS OldData;
ALTER TABLE CandidateContributions RENAME OldData;
ALTER TABLE Import RENAME CandidateContributions;
