/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Map;

public class SubjectIdentifier extends JSONObject {

    public final String getString(SubjectIdentifierMembers member) throws ParseException {
        return JSONObjectUtils.getString(this, member.toString());
    }

    public void put(final SubjectIdentifierMembers member, final Object o) {
        this.put(member.toString(), o);
    }

    public void validateFormat() throws ParseException, SIValidationException {
        final String format = getString(SubjectIdentifierMembers.FORMAT);
        if (null == format) {
            throw new SIValidationException("SubjectIdentifier member format must be present and non-null.");
        }
        if (SubjectIdentifierFormats.contains(format) || format.startsWith("x-")) {
            return;
        }
        throw new SIValidationException("Subject Identifier member format "
                + "must be defined by specification or begin with x-.");
    }

    public void validate() throws ParseException, SIValidationException {
        validateFormat();

        // Subject Identifiers can be complex (thus recursive)
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof SubjectIdentifier) {
                SubjectIdentifier si = (SubjectIdentifier) entry.getValue();
                si.validate();
            }
        }
    }

    protected void validateMemberPresentNotNullNotEmptyString(final String member) throws ParseException, SIValidationException {
        String s = JSONObjectUtils.getString(this, member);
        if (null == s) {
            throw new SIValidationException("SubjectIdentifier member " + member + " must not be null.");
        }
        if (s.isEmpty()) {
            throw new SIValidationException("SubjectIdentifier member " + member + " must not be an empty String.");
        }
    }

    protected void convertChildSubjects(final JSONObject subjectJO) throws ParseException, SIValidationException {
        // Recursively create child SIs with specific object types
        for (Map.Entry<String, Object> entry : subjectJO.entrySet()) {
            String k = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                this.put(k, convertSubjects((JSONObject) value));
            }
        }
    }

    private static SubjectIdentifier constructSubjectIdentifier(final JSONObject subjectJO) throws SIValidationException {
        SubjectIdentifierFormats format;
        String formatName = (String) subjectJO.get(SubjectIdentifierMembers.FORMAT.toString());

        if (null == formatName
                || null == (format = SubjectIdentifierFormats.enumByName(formatName))
                || null == format.getCls()) {
            // No format member, no standardized format, or no specialized class for that format.
            // Return a base SubjectIdentifier.
            return new SubjectIdentifier();
        }

        Class<? extends SubjectIdentifier> cls = format.getCls();
        Constructor<? extends SubjectIdentifier> ctor;

        try {
            ctor = cls.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new SIValidationException("Cannot find a constructor() for " + cls.getName());
        }

        try {
            return ctor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SIValidationException("Cannot instantiate SubjectIdentifier subclass");
        }
    }

    public static SubjectIdentifier convertSubjects(final JSONObject subjectJO) throws ParseException, SIValidationException {
        if (null == subjectJO) { return null; }

        SubjectIdentifier subj = constructSubjectIdentifier(subjectJO);
        subj.merge(subjectJO);
        subj.convertChildSubjects(subjectJO);
        subj.validate();
        return subj;
    }

    public static class Builder {

        private final SubjectIdentifier members = new SubjectIdentifier();

        public Builder format(final SubjectIdentifierFormats format) {
            members.put(SubjectIdentifierMembers.FORMAT, format.toString());
            return this;
        }

        public Builder issuer(final String iss) throws ParseException {
            final String subjectTypeMember = members.getString(SubjectIdentifierMembers.FORMAT);
            if (SubjectIdentifierFormats.SAML_ASSERTION_ID.equalsName(subjectTypeMember)) {
                members.put(SubjectIdentifierMembers.SAML_ISSUER, iss);
            } else {
                members.put(SubjectIdentifierMembers.ISSUER, iss);
            }
            return this;
        }

        public Builder subject(final String sub) {

            members.put(SubjectIdentifierMembers.SUBJECT, sub);
            return this;
        }

        public Builder email(final String email) {

            members.put(SubjectIdentifierMembers.EMAIL, email);
            return this;
        }

        public Builder jwtID(final String jwt_id) {

            members.put(SubjectIdentifierMembers.JWT_ID, jwt_id);
            return this;
        }

        public Builder samlAssertionID(final String assertionId) {

            members.put(SubjectIdentifierMembers.SAML_ASSERTION_ID, assertionId);
            return this;
        }

        public Builder id(final String id) {

            members.put(SubjectIdentifierMembers.ID, id);
            return this;
        }

        public Builder user(final SubjectIdentifier user) {
            members.put(SubjectIdentifierMembers.USER, user);
            return this;
        }

        public Builder device(final SubjectIdentifier device) {
            members.put(SubjectIdentifierMembers.DEVICE, device);
            return this;
        }

        public Builder session(final SubjectIdentifier session) {
            members.put(SubjectIdentifierMembers.SESSION, session);
            return this;
        }

        public Builder tenant(final SubjectIdentifier tenant) {
            members.put(SubjectIdentifierMembers.TENANT, tenant);
            return this;
        }


        public Builder member(final String name, final Object value) {
            members.put(name, value);
            return this;
        }

        public SubjectIdentifier build() {
            return members;
        }

    }
}



