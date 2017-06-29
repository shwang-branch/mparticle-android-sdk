package com.mparticle.identity;

import com.mparticle.MParticle;
import com.mparticle.internal.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class IdentityApiRequest {
    private Map<MParticle.IdentityType, String> userIdentities = new HashMap<MParticle.IdentityType, String>();
    private boolean isCopyUserAttributes = false;
    private Long mpId;

    private IdentityApiRequest(IdentityApiRequest.Builder builder) {
        if (builder.userIdentities != null){
            this.userIdentities = builder.userIdentities;
        }
        if (builder.isCopyUserAttributes != null) {
            this.isCopyUserAttributes = builder.isCopyUserAttributes;
        }
        if (builder.mpId != null) {
            this.mpId = builder.mpId;
        }
    }

    public static Builder withEmptyUser() {
        return new IdentityApiRequest.Builder();
    }

    public static Builder withUser(MParticleUser currentUser) {
        return new IdentityApiRequest.Builder(currentUser);
    }

    public boolean shouldCopyUserAttributes() {
        return isCopyUserAttributes;
    }

    public Long getMpId() {
        return mpId;
    }

    public Map<MParticle.IdentityType, String> getUserIdentities() {
        return userIdentities;
    }

    public static class Builder {
        private Map<MParticle.IdentityType, String> userIdentities = new HashMap<MParticle.IdentityType, String>();
        private Boolean isCopyUserAttributes = null;
        private Long mpId = null;

        public Builder(MParticleUser currentUser) {
            userIdentities = currentUser.getUserIdentities();
            mpId = currentUser.getId();
        }

        public Builder() {

        }

        public Builder email(String email) {
            return userIdentity(MParticle.IdentityType.Email, email);
        }

        public Builder customerId(String customerId) {
            return userIdentity(MParticle.IdentityType.CustomerId, customerId);
        }

        public Builder userIdentity(MParticle.IdentityType identityType, String identityValue) {
            if (userIdentities.containsKey(identityType)) {
                Logger.warning("IdentityApiRequest already contains field with IdentityType of:" + identityType + ". It will be overwritten");
            }
            userIdentities.put(identityType, identityValue);
            return this;
        }

        public Builder userIdentities(Map<MParticle.IdentityType, String> userIdentities) {
            for (Map.Entry<MParticle.IdentityType, String> entry: userIdentities.entrySet()) {
                userIdentity(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public IdentityApiRequest build() {
            return new IdentityApiRequest(this);
        }

        public Builder copyUserAttributes(boolean copyUserAttributes) {
            this.isCopyUserAttributes = copyUserAttributes;
            return this;
        }
    }
}