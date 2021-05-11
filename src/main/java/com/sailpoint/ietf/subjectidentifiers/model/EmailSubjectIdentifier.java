/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;

import com.nimbusds.jose.shaded.json.JSONObject;

import java.text.ParseException;

public class EmailSubjectIdentifier extends SubjectIdentifier {

    /**
     * The Email Identifier Format identifies a subject using an email address. Subject Identifiers in this format
     * MUST contain an email member whose value is a string containing the email address of the subject, formatted as
     * an addr-spec as defined in Section 3.4.1 of {{!RFC5322}}. The email member is REQUIRED and MUST NOT be null or
     * empty. The value of the email member SHOULD identify a mailbox to which email may be delivered, in accordance
     * with {{!RFC5321}}. The Email Identifier Format is identified by the name email.
     *
     * @throws SIValidationException - if value is improper per above
     */
    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();

        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.FORMAT.toString());

        String format = this.getString(SubjectIdentifierMembers.FORMAT);
        if (null == format || !(format.equals(SubjectIdentifierFormats.EMAIL.toString()))) {
            throw new SIValidationException("EmailSubjectIdentifier format must be email");
        }

        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.EMAIL.toString());
    }

    @Override
    protected void convertChildSubjects(final JSONObject subjectJO) {
        // no op
    }


    public static class Builder {

        private final EmailSubjectIdentifier members = new EmailSubjectIdentifier();

        public Builder email(final String email) {
            members.put(SubjectIdentifierMembers.EMAIL, email);
            return this;
        }

        public EmailSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.EMAIL.toString());
            return members;
        }
    }
}



