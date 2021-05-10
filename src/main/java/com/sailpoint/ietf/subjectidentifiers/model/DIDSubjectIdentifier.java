/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import com.nimbusds.jose.shaded.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URI;
import java.text.ParseException;

public class DIDSubjectIdentifier extends SubjectIdentifier {

    private void validateUri() throws ParseException, SIValidationException {
        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.URI.toString());

        final Object o = this.get(SubjectIdentifierMembers.URI.toString());

        URI uri;
        try {
            uri = new URI((String)o);
        }  catch (URISyntaxException e) {
            throw new SIValidationException("DIDSubjectIdentifier member uri invalid URI.");
        }
        String scheme = uri.getScheme();
        if (null == scheme) {
            throw new SIValidationException("DIDSubjectIdentifier member uri must begin with a scheme.");
        }
        if (!scheme.equals("did")) {
            throw new SIValidationException("DIDSubjectIdentifier member uri must have did: scheme.");
        }
    }

    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();
        final String format = (String) get(SubjectIdentifierMembers.FORMAT.toString());
        if (null == format || !format.equals(SubjectIdentifierFormats.DID.toString())) {
            throw new SIValidationException("DIDSubjectIdentifier must have format did.");
        }
        validateUri();
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

        public Builder uri(final String uri) {
            members.put(SubjectIdentifierMembers.URI.toString(), uri);
            return this;
        }

        public DIDSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.DID.toString());
            return members;
        }
    }
}



