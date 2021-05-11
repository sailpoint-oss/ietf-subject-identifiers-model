/*
 * Copyright (c) 2021 SailPoint Technologies, Inc.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.sailpoint.ietf.subjectidentifiers.model;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.plaf.OptionPaneUI;
import java.text.ParseException;


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
        Assert.assertEquals(figureJson, subj);
    }

    @Test
    public void DIDTest2() throws ParseException, SIValidationException {
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
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    @Test
    public void DIDTest4()  {
        DIDSubjectIdentifier subj = new DIDSubjectIdentifier.Builder()
                .uri("http://example:123456")
                .build();
        Assert.assertThrows(SIValidationException.class, subj::validate);
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
        Assert.assertEquals(figureJson, subj);
    }

    @Test
    public void AccountTest2() throws ParseException, SIValidationException {
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
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    @Test
    public void EmailTest() throws ParseException {
        EmailSubjectIdentifier subj = new EmailSubjectIdentifier.Builder()
                .email("user@example.com")
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"email\",\n" +
                "  \"email\": \"user@example.com\"\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        Assert.assertEquals(figureJson, subj);
    }

    // no email member
    @Test
    public void EmailNegativeTest1() {
        EmailSubjectIdentifier subj = new EmailSubjectIdentifier.Builder()
                .build();
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // No format member
    @Test
    public void EmailNegativeTest2()  {
        EmailSubjectIdentifier subj = new EmailSubjectIdentifier.Builder()
                .email("user@example.com")
                .build();
        subj.remove(SubjectIdentifierMembers.FORMAT.toString());
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // Wrong format member
    @Test
    public void EmailNegativeTest3() {
        EmailSubjectIdentifier subj = new EmailSubjectIdentifier.Builder()
                .email("user@example.com")
                .build();
        subj.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.ACCOUNT.toString());
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }


    @Test
    public void PhoneNumberTest() throws ParseException {
        PhoneNumberSubjectIdentifier subj = new PhoneNumberSubjectIdentifier.Builder()
                .phoneNumber("+12065550100")
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"phone_number\",\n" +
                "  \"phone_number\": \"+12065550100\"\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        Assert.assertEquals(figureJson, subj);
    }

    // no phone_number member
    @Test
    public void PhoneNumberNegativeTest1() {
        PhoneNumberSubjectIdentifier subj = new PhoneNumberSubjectIdentifier.Builder()
                .build();
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // No format member
    @Test
    public void PhoneNumberNegativeTest2() {
        PhoneNumberSubjectIdentifier subj = new PhoneNumberSubjectIdentifier.Builder()
                .phoneNumber("+12065550100")
                .build();
        subj.remove(SubjectIdentifierMembers.FORMAT.toString());
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // Wrong format member
    @Test
    public void PhoneNumberNegativeTest3() {
        PhoneNumberSubjectIdentifier subj = new PhoneNumberSubjectIdentifier.Builder()
                .phoneNumber("+12065550100")
                .build();
        subj.put(SubjectIdentifierMembers.FORMAT, SubjectIdentifierFormats.ACCOUNT.toString());
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    @Test
    public void AliasesTest() throws ParseException {
        EmailSubjectIdentifier email1 = new EmailSubjectIdentifier.Builder()
                .email("user@example.com")
                .build();
        PhoneNumberSubjectIdentifier phone = new PhoneNumberSubjectIdentifier.Builder()
                .phoneNumber("+12065550100")
                .build();
        EmailSubjectIdentifier email2 = new EmailSubjectIdentifier.Builder()
                .email("user+qualifier@example.com")
                .build();
        JSONArray a = new JSONArray();
        a.add(email1);
        a.add(phone);
        a.add(email2);

        AliasesSubjectIdentifier subj = new AliasesSubjectIdentifier.Builder()
                .identifiers(a)
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"aliases\",\n" +
                "  \"identifiers\": [\n" +
                "    {\n" +
                "      \"format\": \"email\",\n" +
                "      \"email\": \"user@example.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"format\": \"phone_number\",\n" +
                "      \"phone_number\": \"+12065550100\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"format\": \"email\",\n" +
                "      \"email\": \"user+qualifier@example.com\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        Assert.assertEquals(figureJson, subj);
    }

    @Test
    public void ConvertSubjectsTest() throws ParseException, SIValidationException {
        final String figure_text = "{\n" +
                "  \"format\": \"aliases\",\n" +
                "  \"identifiers\": [\n" +
                "    {\n" +
                "      \"format\": \"email\",\n" +
                "      \"email\": \"user@example.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"format\": \"phone_number\",\n" +
                "      \"phone_number\": \"+12065550100\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"format\": \"email\",\n" +
                "      \"email\": \"user+qualifier@example.com\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        SubjectIdentifier subj = SubjectIdentifier.convertSubjects(figureJson);
        JSONArray identifiers = (JSONArray) subj.get(SubjectIdentifierMembers.IDENTIFIERS.toString());
        Assert.assertTrue(identifiers.get(0) instanceof EmailSubjectIdentifier);
        Assert.assertTrue(identifiers.get(1) instanceof PhoneNumberSubjectIdentifier);
        Assert.assertTrue(identifiers.get(2) instanceof EmailSubjectIdentifier);
    }

    @Test
    public void ConvertAliasesSubjectsNegativeTest() throws ParseException {
        final String figure_text = "{\n" +
                "  \"format\": \"aliases\",\n" +
                "  \"identifiers\": [\n" +
                "    {\n" +
                "      \"format\": \"aliases\",\n" +
                "      \"identifiers\": [\n" +
                "        {\n" +
                "          \"format\": \"phone_number\",\n" +
                "          \"phone_number\": \"+12065550100\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"format\": \"email\",\n" +
                "          \"email\": \"user+qualifier@example.com\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }" +
                "  ]\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        Assert.assertThrows(SIValidationException.class, () -> SubjectIdentifier.convertSubjects(figureJson));
    }

    @Test
    public void OpaqueSubjectsTest() throws ParseException {
        final OpaqueSubjectIdentifier subj = new OpaqueSubjectIdentifier.Builder()
                .id("11112222333344445555")
                .build();

        final String figure_text = "{\n" +
                "  \"format\": \"opaque\",\n" +
                "  \"id\": \"11112222333344445555\"\n" +
                "}";

        final JSONObject figureJson = new JSONObject(JSONObjectUtils.parse(figure_text));
        Assert.assertEquals(figureJson, subj);
    }

    // No id member
    @Test
    public void OpaqueSubjectsNegativeTest1()  {
        final OpaqueSubjectIdentifier subj = new OpaqueSubjectIdentifier.Builder()
                .build();
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // Empty string id member
    @Test
    public void OpaqueSubjectsNegativeTest2()  {
        final OpaqueSubjectIdentifier subj = new OpaqueSubjectIdentifier.Builder()
                .id("")
                .build();
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }

    // id member not a string
    @Test
    public void OpaqueSubjectsNegativeTest3()  {
        final OpaqueSubjectIdentifier subj = new OpaqueSubjectIdentifier.Builder()
                .build();
        subj.put("id", 1234L);
        Assert.assertThrows(ParseException.class, subj::validate);
    }

    // No format member
    @Test
    public void OpaqueSubjectsNegativeTest4()  {
        final OpaqueSubjectIdentifier subj = new OpaqueSubjectIdentifier.Builder()
                .id("11112222333344445555")
                .build();
        subj.remove("format");
        Assert.assertThrows(SIValidationException.class, subj::validate);
    }
}
