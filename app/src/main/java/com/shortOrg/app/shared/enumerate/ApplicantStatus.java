package com.shortOrg.app.shared.enumerate;

import com.shortOrg.app.domain.Applicant;

public enum ApplicantStatus {
    HOST, MEMBER, REJECTED, PENDING;
    
    public static String toString(ApplicantStatus s) {
        if (s == null) { return "NONE"; }
        else { return s.name(); }
    }
}
