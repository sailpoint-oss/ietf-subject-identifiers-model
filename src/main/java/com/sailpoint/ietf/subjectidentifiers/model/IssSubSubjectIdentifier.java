/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import java.net.URI;
import java.net.URISyntaxException;

public class IssSubSubjectIdentifier extends SubjectIdentifier {

    /**
     * https://tools.ietf.org/html/rfc7519
     * StringOrURI
     *       A JSON string value, with the additional requirement that while
     *       arbitrary string values MAY be used, any value containing a ":"
     *       character MUST be a URI [RFC3986].  StringOrURI values are
     *       compared as case-sensitive strings with no transformations or
     *       canonicalizations applied.
     *
     * @param member - Map key
     * @throws SIValidationException - if value is improper per above
     */
    private void validateStringOrURI(final String member) throws SIValidationException {
        Object o = this.get(member);
        String s;
        if (null == o) {
            throw new SIValidationException("IssSubSubjectIdentifier member " + member + " must be present.");
        }
        if (!((o instanceof String) || (o instanceof URI))) {
            throw new SIValidationException("IssSubSubjectIdentifier member " + member + " must be a String or URI");
        }
        if (o instanceof String) {
            s = (String) o;
            if (s.equals("")) {
                throw new SIValidationException("IssSubSubjectIdentifier member " + member + " must not be an empty String.");
            }

            if (s.indexOf(':') < 0) return; // No :, not a URI. Plain strings are OK.

            try {
                new URI(s);
            } catch (URISyntaxException e) {
                throw new SIValidationException("IssSubSubjectIdentifier member " + member + " invalid URI");
            }
        }
    }

    @Override
    public void validate() throws SIValidationException {
        super.validate();
        validateStringOrURI(SubjectIdentifierMembers.SUBJECT.toString());
        validateStringOrURI(SubjectIdentifierMembers.ISSUER.toString());
    }

    public static class Builder {

        private final IssSubSubjectIdentifier members = new IssSubSubjectIdentifier();

        public Builder subject(final String sub) {
            members.put(SubjectIdentifierMembers.SUBJECT, sub);
            return this;
        }

        public Builder issuer(final String iss) {
            members.put(SubjectIdentifierMembers.ISSUER, iss);
            return this;
        }

        public IssSubSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.ISSUER_SUBJECT.toString());
            return members;
        }

    }
}



