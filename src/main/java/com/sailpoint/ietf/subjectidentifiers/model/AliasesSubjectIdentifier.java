/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;


import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.text.ParseException;

public class AliasesSubjectIdentifier extends SubjectIdentifier {

    /**
     * The Aliases Identifier Format describes a subject that is identified with a list of different Subject
     * Identifiers. It is intended for use when a variety of identifiers have been shared with the party that will be
     * interpreting the Subject Identifier, and it is unknown which of those identifiers they will recognize or
     * support. Subject Identifiers in this format MUST contain an identifiers member whose value is a JSON array
     * containing one or more Subject Identifiers. Each Subject Identifier in the array MUST identify the same entity.
     * The identifiers member is REQUIRED and MUST NOT be null or empty. It MAY contain multiple instances of the same
     * Identifier Format (e.g., multiple Email Subject Identifiers), but SHOULD NOT contain exact duplicates. This type
     * is identified by the name aliases.
     *
     * alias Subject Identifiers MUST NOT be nested; i.e., the identifiers member of an alias Subject Identifier MUST
     * NOT contain a Subject Identifier of type aliases.
     *
     * @throws SIValidationException - if value is improper per above
     */
    @Override
    public void validate() throws ParseException, SIValidationException {
        super.validate();

        validateMemberPresentNotNullNotEmptyString(SubjectIdentifierMembers.FORMAT.toString());

        String format = this.getString(SubjectIdentifierMembers.FORMAT);
        if (!(format.equals(SubjectIdentifierFormats.ALIASES.toString()))) {
            throw new SIValidationException("AliasesSubjectIdentifier format must be aliases");
        }

        if (!this.containsKey(SubjectIdentifierMembers.IDENTIFIERS.toString())) {
            throw new SIValidationException("AliasesSubjectIdentifier must contain an identifiers member");
        }

        Object o = this.get(SubjectIdentifierMembers.IDENTIFIERS.toString());
        if (! (o instanceof JSONArray)) {
            throw new SIValidationException("AliasesSubjectIdentifier identifiers member must be a JSON Array");
        }
        JSONArray identifiers = (JSONArray) o;
        // Items in this array are other subject identifiers
        // Validate each of them.
        for (Object si : identifiers) {
            if (si instanceof SubjectIdentifier) {
                // Alias SIs cannot be recursive
                if (JSONObjectUtils.getString((JSONObject)si, SubjectIdentifierMembers.FORMAT.toString())
                        .equals(SubjectIdentifierFormats.ALIASES.toString()))
                    throw new SIValidationException("AliasesSubjectIdentifier identifiers member must not be an AliasSI.");
                ((SubjectIdentifier)si).validate();
            }
        }
    }

    @Override
    protected void convertChildSubjects(final JSONObject subjectJO) throws ParseException, SIValidationException {
        // Recursively create child SIs with specific object types
        Object o = this.get(SubjectIdentifierMembers.IDENTIFIERS.toString());
        if (!(o instanceof JSONArray)) {
            return;
        }
        JSONArray identifiers = (JSONArray) o;
        // Items in this array are other subject identifiers
        // Replace each of them.
        int index;
        for (index = 0; index < identifiers.size(); index++) {
            Object item = identifiers.get(index);
            if (item instanceof JSONObject) {
                identifiers.set(index, SubjectIdentifier.convertSubjects((JSONObject) item));
            }
        }
    }


    public static class Builder {

        private final AliasesSubjectIdentifier members = new AliasesSubjectIdentifier();

        public AliasesSubjectIdentifier.Builder identifiers(final JSONArray array) {
            members.put(SubjectIdentifierMembers.IDENTIFIERS.toString(), array);
            return this;
        }

        public AliasesSubjectIdentifier build() {
            members.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.ALIASES.toString());
            return members;
        }

    }
}



