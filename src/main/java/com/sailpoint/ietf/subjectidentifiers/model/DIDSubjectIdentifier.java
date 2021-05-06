/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import java.net.URISyntaxException;
import java.net.URI;

public class DIDSubjectIdentifier extends SubjectIdentifier {



    private static void validateUri(final DIDSubjectIdentifier subj) throws SIValidationException {
        final Object o = subj.get(SubjectIdentifierMembers.URI.toString());
        String value;
        if (null == o) {
            throw new SIValidationException(subj.getClass().getName() + " member uri is missing or null.");
        }
        if (o instanceof String){
            value = (String) o;
            if (value.isEmpty()) {
                throw new SIValidationException(subj.getClass().getName() + " member uri is empty.");
            }
        }
        else {
            throw new SIValidationException(subj.getClass().getName() + " member uri is not a String.");
        }

        URI uri;
        try {
            uri = new URI(value);
        }  catch (URISyntaxException e) {
            throw new SIValidationException("DIDSubjectIdentifier member uri invalid URI.");
        }
        String scheme = uri.getScheme();
        if (null == scheme) {
            throw new SIValidationException("DIDSubjectIdentifier member uri must begin with did: scheme.");
        }
        if (!scheme.equals("did")) {
            throw new SIValidationException("AccountSubjectIdentifier member uri must have did: scheme.");
        }
    }

    @Override
    public void validate() throws SIValidationException {
        super.validate();
        final String format = (String) get(SubjectIdentifierMembers.FORMAT.toString());
        if (!format.equals(SubjectIdentifierFormats.DID.toString())) {
            throw new SIValidationException("DID Subject Identifiers must have format did.");
        }
        validateUri(this);
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
            return members;
        }
    }
}



