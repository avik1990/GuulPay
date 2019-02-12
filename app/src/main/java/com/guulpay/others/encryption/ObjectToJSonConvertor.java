package com.guulpay.others.encryption;

import android.util.Base64;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ObjectToJSonConvertor {
    static String jsonInString = "";
    static String request_body = "";
    /*public static String getRandomString(Object T) {
        RandomStringGenerator randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        randomStringGenerator.generate(12);
        return request_body;
    }*/

    public static String base64Encode(String sttobase64) {
        String encodedBytes = Base64.encodeToString(sttobase64.getBytes(), Base64.DEFAULT);
        return encodedBytes;
    }


    public static String getEncryptedString(Object T, String randomkey) {
        Gson gson = new Gson();
        String json = gson.toJson(T);
        try {
            jsonInString = json;
            request_body = AESCrypt.encrypt(jsonInString.replace("\\", ""), randomkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request_body;
    }

    public static String getRequestJson(Object T) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert object to JSON string
            jsonInString = mapper.writeValueAsString(T);
            // request_body = AESCrypt.encrypt(jsonInString);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }


/*
    public static String getEncryptedUserID(Context mContext) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserId mUserId= new UserId();
            mUserId.setUserId(MomentaPrefs.getUserId(mContext));
            jsonInString = mapper.writeValueAsString(mUserId);
            jsonInString=jsonInString.replace("userId","user_id");
            request_body = com.app.momenta.data.network.utils.AESCrypt.encrypt(jsonInString);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return request_body;
    }

    public static LoginResponse getLoginObjectFromJson(String encryptedString) {
        LoginResponse mLoginresponse = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mLoginresponse = gson.fromJson(jsonInString, LoginResponse.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mLoginresponse;
    }

    public static BaseResponse getForgetPasswordObjectFromJson(String encryptedString) {
        BaseResponse mBaseResponse = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mBaseResponse = gson.fromJson(jsonInString, BaseResponse.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mBaseResponse;
    }

    public static ProfileResponse getProfileFromJson(String encryptedString) {
        ProfileResponse mProfileResponse = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mProfileResponse = gson.fromJson(jsonInString, ProfileResponse.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mProfileResponse;
    }

    public static FriendList getFriendList(String encryptedString) {
        FriendList mFriendList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mFriendList = gson.fromJson(jsonInString, FriendList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mFriendList;
    }

    public static FamilyList getFamilyList(String encryptedString) {
        FamilyList mFamilyList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mFamilyList = gson.fromJson(jsonInString, FamilyList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mFamilyList;
    }

    public static ReligionList getReligion(String encryptedString) {
        ReligionList mReligionList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mReligionList = gson.fromJson(jsonInString, ReligionList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mReligionList;
    }

    public static CountryList getCountry(String encryptedString) {
        CountryList mCountryList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mCountryList = gson.fromJson(jsonInString, CountryList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mCountryList;
    }

    public static EducationStateList getEducationStateList(String encryptedString) {
        EducationStateList mEducationStateList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mEducationStateList = gson.fromJson(jsonInString, EducationStateList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mEducationStateList;
    }

    public static RelationList getRelationList(String encryptedString) {
        RelationList mRelationList = null;
        try {
            jsonInString = com.app.momenta.data.network.utils.AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mRelationList = gson.fromJson(jsonInString, RelationList.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mRelationList;
    }

    public static CreateEventTypeResponse getEventTypeList(String encryptedString) {
        CreateEventTypeResponse mEventTypeList = null;
        try {
            jsonInString = AESCrypt.decrypt(encryptedString);
            Gson gson = new Gson();
            mEventTypeList = gson.fromJson(jsonInString, CreateEventTypeResponse.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return mEventTypeList;
    }*/
}
