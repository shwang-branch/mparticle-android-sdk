package com.mparticle.internal.listeners;

import android.content.ContentValues;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.mparticle.SdkListener;
import com.mparticle.identity.AliasResponse;
import com.mparticle.identity.MParticleUser;
import com.mparticle.internal.InternalSession;

import org.json.JSONObject;

public interface InternalListener {

    /**
     * This method should be called within the body of a public API method. Generally
     * we only want to instrument API methods which "do something", i.e log an event or change
     * a User's state, not simple getters
     *
     * @param objects the arguments passed into the API method
     */
    void onApiCalled(Object... objects);

    /**
     * to be called when a Kit's API method is invoked. This overloaded variant should be used when
     * the name of the method containing this method's invocation (in KitManagerImpl) matches the name of the
     * Kit's method being invoked
     * @param kitId the Id of the kit
     * @param used whether the Kit's method returned ReportingMessages, or null if return type is void
     * @param objects the arguments supplied to the Kit
     */
    void onKitApiCalled(int kitId, Boolean used, Object... objects);

    /**
     * to be called when a Kit's API method is invoked, and the name of the Kit's method is different
     * then the method containing this method's invocation
     * @param methodName the name of the Kit's method being called
     * @param kitId the Id of the kit
     * @param used whether the Kit's method returned ReportingMessages, or null if return type is void
     * @param objects the arguments supplied to the Kit
     */
    void onKitApiCalled(String methodName, int kitId, Boolean used, Object... objects);

    /**
     * establishes a child-parent relationship between two objects. It is not necessary to call this
     * method for objects headed to the MessageHandler from the public API, or objects headed to kits
     * @param child the child object
     * @param parent the parent object
     */
    void onCompositeObjects(@Nullable Object child, @Nullable Object parent);

    /**
     * denotes that an object is going to be passed to a new Thread, and is a candidate to be a "composite"
     * This method should be called twice per thread change, once before the shift to the new Thread,
     * and once as soon as it lands on the new Thread
     *
     * @param handlerName the Name of the Handler class, for example "com.mparticle.internal.MessageHandler"
     * @param msg the Message object
     */
    void onThreadMessage(@NonNull String handlerName, @NonNull Message msg, boolean onNewThread);

    /**
     * indicates that an entry has been stored in the Database
     * @param rowId the rowId denoted by the "_id" column value
     * @param tableName the name of the database table
     * @param contentValues the ContentValues object to be inserted
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    void onEntityStored(Long rowId, String tableName, ContentValues contentValues);

    /**
     * indicates that a Network Request has been started
     * @param type the type of Request, should either be "CONFIG", "EVENTS" or "Identity.Login/Logout/Identify/Modify"
     * @param url the request url
     * @param body the request body
     * @param objects any underlying objects that the request body is derived from, for example, an IdentityApiRequest instance
     */
    void onNetworkRequestStarted(SdkListener.Endpoint type, String url, JSONObject body, Object... objects);

    /**
     * indicates that a NetworkRequest has been finished
     * @param url the request url
     * @param response the response body
     * @param responseCode the response code
     */
    void onNetworkRequestFinished(SdkListener.Endpoint type, String url, JSONObject response, int responseCode);

    /**
     * this should be called when the current Session changes, for example, it starts, stops or the
     * event count changes
     * @param internalSession
     */
    void onSessionUpdated(InternalSession internalSession);

    /**
     * indicates that a Kit dependency is present
     * @param kitId
     */
    void onKitDetected(int kitId);

    /**
     * indicates that we have received a configuration for a Kit
     * @param kitId
     * @param configuration
     */
    void onKitConfigReceived(int kitId, String configuration);

    /**
     * indicates that a Kit was present, and a configuration was received for it, but it was not started,
     * or it was stopped. This could be because it crashed, or because a User's logged in status required
     * that we shut it down
     * @param kitId
     * @param reason
     */
    void onKitExcluded(int kitId, String reason);

    /**
     * indicates that a Kit successfully executed it's onKitCreate() method
     * @param kitId
     */
    void onKitStarted(int kitId);

    void onAliasRequestFinished(AliasResponse aliasResponse);

    InternalListener EMPTY = new InternalListener() {
        public void onApiCalled(Object... objects) { /* stub */}
        public void onKitApiCalled(int kitId, Boolean used, Object... objects) { /* stub */}
        public void onKitApiCalled(String methodName, int kitId, Boolean used, Object... objects) { /* stub */}
        public void onEntityStored(Long rowId, String tableName, ContentValues contentValues) { /* stub */}
        public void onNetworkRequestStarted(SdkListener.Endpoint type, String url, JSONObject body, Object... objects) { /* stub */}
        public void onNetworkRequestFinished(SdkListener.Endpoint type, String url, JSONObject response, int responseCode) { /* stub */}
        public void onSessionUpdated(InternalSession internalSession) { /* stub */}
        public void onKitDetected(int kitId) { /* stub */}
        public void onKitConfigReceived(int kitId, String configuration) { /* stub */}
        public void onKitExcluded(int kitId, String reason) { /* stub */}
        public void onKitStarted(int kitId) { /* stub */}
        public void onAliasRequestFinished(AliasResponse aliasResponse) { /* stub */}

        public void onCompositeObjects(Object child, Object parent) { /* stub */}
        public void onThreadMessage(String handlerName, Message msg, boolean onNewThread) { /* stub */ }
    };
}
