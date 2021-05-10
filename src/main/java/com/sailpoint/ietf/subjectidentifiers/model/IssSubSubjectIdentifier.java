/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import com.nimbusds.jose.shaded.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

public class IssSubSubjectIdentifier extends SubjectIdentifier {

    /**
     *
     * The Issuer and Subject Identifier Format identifies a subject using a pair of iss and sub members, analagous
     * to how subjects are identified using the iss and sub claims in OpenID Connect ID Tokens. These members MUST
     * follow the formats of the iss member and sub member defined by {{!RFC7519}}, respectively. Both the iss member
     * and the sub member are REQUIRED and MUST NOT be null or empty. The Issuer and Subject Identifier Format is
     * identified by the name iss_sub.
     *
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
    private void validateStringOrURI(final String member) throws ParseException, SIValidationException {
        validateMemberPresentNotNullNotEmptyString(member);
        Object o = this.get(member);
        String s;
        if (!((o instanceof String) || (o instanceof URI))) {
            throw new SIValidationException("IssSubSubjectIdentifier member " + member + " must be a String or URI");
        }
        if (o instanceof String) {
            s = (String) o;
            if (s.indexOf(':') < 0) return; // No :, not a URI. Plain strings are OK.

            try {
                new URI(s);
            } catch (URISyntaxException e) {
                throw new SIValidationException("IssSubSubjectIdentifier member " + member + " invalid URI");
            }
        }
    }

    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();
        validateStringOrURI(SubjectIdentifierMembers.SUBJECT.toString());
        validateStringOrURI(SubjectIdentifierMembers.ISSUER.toString());
    }

    @Override
    protected void convertChildSubjects(final JSONObject subjectJO) {
        // no op
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



