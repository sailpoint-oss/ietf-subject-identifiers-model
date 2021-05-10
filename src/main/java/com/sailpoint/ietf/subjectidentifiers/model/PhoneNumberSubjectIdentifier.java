/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import java.text.ParseException;

public class PhoneNumberSubjectIdentifier extends SubjectIdentifier {

    /**
     * The Phone Number Identifier Format identifies a subject using a telephone number. Subject Identifiers in this
     * format MUST contain a phone_number member whose value is a string containing the full telephone number of the
     * subject, including international dialing prefix, formatted according to E.164. The phone_number member is
     * REQUIRED and MUST NOT be null or empty. The Phone Number Identifier Format is identified by the name
     * phone_number.
     *
     * @throws SIValidationException - if value is improper per above
     */
    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();

        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.FORMAT.toString());

        String format = this.getString(SubjectIdentifierMembers.FORMAT);
        if (null == format || !(format.equals(SubjectIdentifierFormats.PHONE_NUMBER.toString()))) {
            throw new SIValidationException("PhoneNumberSubjectIdentifier format must be phone_number");
        }

        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.PHONE_NUMBER.toString());
        // TODO: Validate that the phone number string is proper per E.164
    }

    public static class Builder {

        private final PhoneNumberSubjectIdentifier members = new PhoneNumberSubjectIdentifier();

        public PhoneNumberSubjectIdentifier.Builder phoneNumber(final String phoneNumber) {
            members.put(SubjectIdentifierMembers.PHONE_NUMBER, phoneNumber);
            return this;
        }

        public PhoneNumberSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.PHONE_NUMBER.toString());
            return members;
        }

    }
}



