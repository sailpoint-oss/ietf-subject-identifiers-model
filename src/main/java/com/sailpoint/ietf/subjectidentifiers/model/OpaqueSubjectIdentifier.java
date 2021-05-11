/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import com.nimbusds.jose.shaded.json.JSONObject;

import java.text.ParseException;

public class OpaqueSubjectIdentifier extends SubjectIdentifier {

    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();
        final String format = getString(SubjectIdentifierMembers.FORMAT);
        if (!(SubjectIdentifierFormats.OPAQUE.toString().equals(format))) {
            throw new SIValidationException("OpaqueSubjectIdentifier must have format opaque.");
        }
        final String id = getString(SubjectIdentifierMembers.ID);
        if (null == id || id.isEmpty()) {
            throw new SIValidationException("OpaqueSubjectIdentifier member id must be non-null and a non-empty String.");
        }
    }

    @Override
    protected void convertChildSubjects(final JSONObject subjectJO) {
        // no op
    }

    public static class Builder {

        private final OpaqueSubjectIdentifier members = new OpaqueSubjectIdentifier();

        public Builder id(final String id) {
            members.put(SubjectIdentifierMembers.ID.toString(), id);
            return this;
        }

        public OpaqueSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.OPAQUE.toString());
            return members;
        }
    }
}



