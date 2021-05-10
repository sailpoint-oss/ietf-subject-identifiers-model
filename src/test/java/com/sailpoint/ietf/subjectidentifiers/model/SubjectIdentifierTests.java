/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SubjectIdentifierTests {
    /*
     *  https://github.com/richanna/secevent/pull/2
     */
    @Test
    public void DIDTest1() throws ParseException {
        DIDSubjectIdentifier subj = new DIDSubjectIdentifier.Builder()
                .uri("did:example:123456")
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"did\",\n" +
                "  \"uri\": \"did:example:123456\"\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        assertEquals(figureJson, subj);
    }

    @Test
    public void DIDTest2() throws SIValidationException {
        DIDSubjectIdentifier subj = new DIDSubjectIdentifier.Builder()
                .uri("did:example:123456")
                .build();
        subj.validate();
    }

    @Test
    public void DIDTest3()  {
        DIDSubjectIdentifier subj = new DIDSubjectIdentifier.Builder()
                .uri("example:123456")
                .build();
        assertThrows(SIValidationException.class, subj::validate);
    }

    @Test
    public void DIDTest4()  {
        DIDSubjectIdentifier subj = new DIDSubjectIdentifier.Builder()
                .uri("http://example:123456")
                .build();
        assertThrows(SIValidationException.class, subj::validate);
    }



    @Test
    public void AccountTest() throws ParseException {
        AccountSubjectIdentifier subj = new AccountSubjectIdentifier.Builder()
                .uri("acct:example.user@service.example.com")
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"account\",\n" +
                "  \"uri\": \"acct:example.user@service.example.com\"\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        assertEquals(figureJson, subj);
    }

    @Test
    public void AccountTest2() throws SIValidationException {
        AccountSubjectIdentifier subj = new AccountSubjectIdentifier.Builder()
                .uri("acct:example.user@service.example.com")
                .build();
        subj.validate();
    }



    @Test
    public void NegativeAccountTest()  {
        AccountSubjectIdentifier subj = new AccountSubjectIdentifier.Builder()
                .uri("example.user@service.example.com")
                .build();
        assertThrows(SIValidationException.class, subj::validate);
    }


}
