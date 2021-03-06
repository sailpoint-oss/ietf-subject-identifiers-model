/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;

import java.util.HashMap;
import java.util.Map;

public enum SubjectIdentifierMembers {

    FORMAT("format"),
    ISSUER("iss"),
    SUBJECT("sub"),
    EMAIL("email"),
    PHONE_NUMBER("phone_number"),
    JWT_ID("jti"),
    SAML_ISSUER("issuer"),
    SAML_ASSERTION_ID("assertion_id"),
    USER("user"),
    DEVICE("device"),
    SESSION("session"),
    APPLICATION("application"),
    TENANT("tenant"),
    ORG_UNIT("org-unit"),
    GROUP("group"),
    ID("id"),
    IDENTIFIERS("identifiers"),

    //https://bitbucket.org/openid/risc/src/master/oauth-event-types-1_0.txt
    SUBJECT_TYPE("subject_type"),
    TOKEN_TYPE("token_type"),
    TOKEN_IDENTIFIER_ALG("token_identifier_alg"),
    TOKEN("token"),
    URL("url"),
    URI("uri");

    private static final Map<String, SubjectIdentifierMembers> BY_NAME = new HashMap<>();

    static {
        for (SubjectIdentifierMembers t : values()) {
            BY_NAME.put(t.name, t);
        }
    }

    private final String name;

    SubjectIdentifierMembers(final String s) {
        name = s;
    }

    public static SubjectIdentifierMembers valueOfLabel(String name) {
        return BY_NAME.get(name);
    }

    public static boolean contains(final String name) {
        return BY_NAME.containsKey(name);
    }

    public boolean equalsName(final String otherName) {
        return name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }

}
