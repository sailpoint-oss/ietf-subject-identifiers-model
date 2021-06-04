/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import com.nimbusds.jose.shaded.json.JSONObject;

import java.text.ParseException;

public class DIDSubjectIdentifier extends SubjectIdentifier {

    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();
        final String format = (String) get(SubjectIdentifierMembers.FORMAT.toString());
        if (null == format || !format.equals(SubjectIdentifierFormats.DID.toString())) {
            throw new SIValidationException("DIDSubjectIdentifier must have format did.");
        }
        // Minimal validation of the url field, because relative URLs and other valid strings can be here.
        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.URL.toString());
    }

    @Override
    protected void convertChildSubjects(final JSONObject subjectJO) {
        // no op
    }

    public static class Builder {

        private final DIDSubjectIdentifier members = new DIDSubjectIdentifier();

        // Redefining format is easier than genericizing SubjectIdentifier
        public Builder format(final SubjectIdentifierFormats format) {
            members.put(SubjectIdentifierMembers.FORMAT, format.toString());
            return this;
        }

        public Builder url(final String url) {
            members.put(SubjectIdentifierMembers.URL.toString(), url);
            return this;
        }

        public DIDSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.DID.toString());
            return members;
        }
    }
}



