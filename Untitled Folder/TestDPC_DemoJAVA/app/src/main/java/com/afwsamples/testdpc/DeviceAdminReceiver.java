/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.afwsamples.testdpc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION_CODES;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.afwsamples.testdpc.common.NotificationUtil;
import com.afwsamples.testdpc.common.Util;
import com.afwsamples.testdpc.provision.PostProvisioningTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Handles events related to the managed profile.
 */
public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    private static final String TAG = "DeviceAdminReceiver";

    public static final String ACTION_PASSWORD_REQUIREMENTS_CHANGED =
            "com.afwsamples.testdpc.policy.PASSWORD_REQUIREMENTS_CHANGED";

    private static final String LOGS_DIR = "logs";

    private static final String FAILED_PASSWORD_LOG_FILE =
            "failed_pw_attempts_timestamps.log";

    private static final int CHANGE_PASSWORD_NOTIFICATION_ID = 101;
    private static final int PASSWORD_FAILED_NOTIFICATION_ID = 102;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_PASSWORD_REQUIREMENTS_CHANGED:
            case Intent.ACTION_BOOT_COMPLETED:
                updatePasswordConstraintNotification(context);
                break;
            case DevicePolicyManager.ACTION_PROFILE_OWNER_CHANGED:
                onProfileOwnerChanged(context);
                break;
            case DevicePolicyManager.ACTION_DEVICE_OWNER_CHANGED:
                onDeviceOwnerChanged(context);
                break;
            default:
               super.onReceive(context, intent);
               break;
        }
    }

    @TargetApi(VERSION_CODES.N)
    @Override
    public void onSecurityLogsAvailable(Context context, Intent intent) {
        Log.i(TAG, "onSecurityLogsAvailable() called");
        Toast.makeText(context,
                context.getString(R.string.on_security_logs_available),
                Toast.LENGTH_LONG)
                .show();
    }


    /*
     * TODO: reconsider how to store and present the logs in the future, e.g. save the file into
     * internal memory and show the content in a ListView
     */
    @TargetApi(VERSION_CODES.O)
    @Override
    public void onNetworkLogsAvailable(Context context, Intent intent, long batchToken,
            int networkLogsCount) {
        CommonReceiverOperations.onNetworkLogsAvailable(context, getComponentName(context),
                batchToken, networkLogsCount);
    }

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        PostProvisioningTask task = new PostProvisioningTask(context);
        if (!task.performPostProvisioningOperations(intent)) {
            return;
        }

        final Intent launchIntent = task.getPostProvisioningLaunchIntent(intent);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            Log.e(TAG, "DeviceAdminReceiver.onProvisioningComplete() invoked, but ownership "
                + "not assigned");
            Toast.makeText(context, R.string.device_admin_receiver_failure, Toast.LENGTH_LONG)
                .show();
        }
    }

    @TargetApi(VERSION_CODES.N)
    @Override
    public void onBugreportSharingDeclined(Context context, Intent intent) {
        Log.i(TAG, "Bugreport sharing declined");
        NotificationUtil.showNotification(context, R.string.bugreport_title,
                context.getString(R.string.bugreport_sharing_declined),
                NotificationUtil.BUGREPORT_NOTIFICATION_ID);
    }

    @SuppressLint("StaticFieldLeak")
    @TargetApi(VERSION_CODES.N)
    @Override
    public void onBugreportShared(final Context context, Intent intent,
            final String bugreportFileHash) {
        Log.i(TAG, "Bugreport shared, hash: " + bugreportFileHash);
        final Uri bugreportUri = intent.getData();

        final PendingResult result = goAsync();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                File outputBugreportFile;
                String message;
                InputStream in;
                OutputStream out;
                try {
                    ParcelFileDescriptor mInputPfd = context.getContentResolver()
                            .openFileDescriptor(bugreportUri, "r");
                    in = new FileInputStream(mInputPfd.getFileDescriptor());
                    outputBugreportFile = new File(context.getExternalFilesDir(null),
                            bugreportUri.getLastPathSegment());
                    out = new FileOutputStream(outputBugreportFile);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();/**
 * Provides several device management functions.
 *
 * These include:
 * <ul>
 * <li> {@link DevicePolicyManager#setLockTaskPackages(android.content.ComponentName, String[])} </li>
 * <li> {@link DevicePolicyManager#isLockTaskPermitted(String)} </li>
 * <li> {@link UserManager#DISALLOW_DEBUGGING_FEATURES} </li>
 * <li> {@link UserManager#DISALLOW_INSTALL_UNKNOWN_SOURCES} </li>
 * <li> {@link UserManager#DISALLOW_REMOVE_USER} </li>
 * <li> {@link UserManager#DISALLOW_ADD_USER} </li>
 * <li> {@link UserManager#DISALLOW_FACTORY_RESET} </li>
 * <li> {@link UserManager#DISALLOW_CONFIG_CREDENTIALS} </li>
 * <li> {@link UserManager#DISALLOW_SHARE_LOCATION} </li>
 * <li> {@link UserManager#DISALLOW_CONFIG_TETHERING} </li>
 * <li> {@link UserManager#DISALLOW_ADJUST_VOLUME} </li>
 * <li> {@link UserManager#DISALLOW_UNMUTE_MICROPHONE} </li>
 * <li> {@link UserManager#DISALLOW_MODIFY_ACCOUNTS} </li>
 * <li> {@link UserManager#DISALLOW_SAFE_BOOT} </li>
 * <li> {@link UserManager#DISALLOW_OUTGOING_BEAM}} </li>
 * <li> {@link UserManager#DISALLOW_CREATE_WINDOWS}} </li>
 * <li> {@link DevicePolicyManager#clearDeviceOwnerApp(String)} </li>
 * <li> {@link DevicePolicyManager#getPermittedAccessibilityServices(android.content.ComponentName)}
 * </li>
 * <li> {@link DevicePolicyManager#getPermittedInputMethods(android.content.ComponentName)} </li>
 * <li> {@link DevicePolicyManager#setAccountManagementDisabled(android.content.ComponentName,
 *             String, boolean)} </li>
 * <li> {@link DevicePolicyManager#getAccountTypesWithManagementDisabled()} </li>
 * <li> {@link DevicePolicyManager#removeUser(ComponentName, UserHandle)} </li>
 * <li> {@link DevicePolicyManager#switchUser(ComponentName, UserHandle)} </li>
 * <li> {@link DevicePolicyManager#stopUser(ComponentName, UserHandle)} </li>
 * <li> {@link DevicePolicyManager#setLogoutEnabled(ComponentName, boolean)} </li>
 * <li> {@link DevicePolicyManager#isLogoutEnabled()} </li>
 * <li> {@link DevicePolicyManager#isAffiliatedUser()} </li>
 * <li> {@link DevicePolicyManager#isEphemeralUser(ComponentName)} </li>
 * <li> {@link DevicePolicyManager#setUninstallBlocked(android.content.ComponentName, String,
 *             boolean)} </li>
 * <li> {@link DevicePolicyManager#isUninstallBlocked(android.content.ComponentName, String)} </li>
 * <li> {@link DevicePolicyManager#setCameraDisabled(android.content.ComponentName, boolean)} </li>
 * <li> {@link DevicePolicyManager#getCameraDisabled(android.content.ComponentName)} </li>
 * <li> {@link DevicePolicyManager#enableSystemApp(android.content.ComponentName,
 *             android.content.Intent)} </li>
 * <li> {@link DevicePolicyManager#enableSystemApp(android.content.ComponentName, String)} </li>
 * <li> {@link DevicePolicyManager#setApplicationRestrictions(android.content.ComponentName, String,
 *       android.os.Bundle)} </li>
 * <li> {@link DevicePolicyManager#installKeyPair(android.content.ComponentName,
 *             java.security.PrivateKey, java.security.cert.Certificate, String)} </li>
 * <li> {@link DevicePolicyManager#removeKeyPair(android.content.ComponentName, String)} </li>
 * <li> {@link DevicePolicyManager#installCaCert(android.content.ComponentName, byte[])} </li>
 * <li> {@link DevicePolicyManager#uninstallAllUserCaCerts(android.content.ComponentName)} </li>
 * <li> {@link DevicePolicyManager#getInstalledCaCerts(android.content.ComponentName)} </li>
 * <li> {@link DevicePolicyManager#setStatusBarDisabled(ComponentName, boolean)} </li>
 * <li> {@link DevicePolicyManager#setKeyguardDisabled(ComponentName, boolean)} </li>
 * <li> {@link DevicePolicyManager#setPermissionPolicy(android.content.ComponentName, int)} </li>
 * <li> {@link DevicePolicyManager#getPermissionPolicy(android.content.ComponentName)} </li>
 * <li> {@link DevicePolicyManager#setPermissionGrantState(ComponentName, String, String, int) (
 *        android.content.ComponentName, String, String, boolean)} </li>
 * <li> {@link DevicePolicyManager#setScreenCaptureDisabled(ComponentName, boolean)} </li>
 * <li> {@link DevicePolicyManager#getScreenCaptureDisabled(ComponentName)} </li>
 * <li> {@link DevicePolicyManager#setMaximumTimeToLock(ComponentName, long)} </li>
 * <li> {@link DevicePolicyManager#setMaximumFailedPasswordsForWipe(ComponentName, int)} </li>
 * <li> {@link DevicePolicyManager#setAffiliationIds(ComponentName, Set)} </li>
 * <li> {@link DevicePolicyManager#setApplicationHidden(ComponentName, String, boolean)} </li>
 * <li> {@link DevicePolicyManager#setShortSupportMessage(ComponentName, CharSequence)} </li>
 * <li> {@link DevicePolicyManager#setLongSupportMessage(ComponentName, CharSequence)} </li>
 * <li> {@link UserManager#DISALLOW_CONFIG_WIFI} </li>
 * <li> {@link DevicePolicyManager#installExistingPackage(ComponentName, String)} </li>
 * <li> {@link DevicePolicyManager#getPasswordComplexity()}
 * <li> {@link DevicePolicyManager#ACTION_SET_NEW_PASSWORD}
 * <li> {@link DevicePolicyManager#EXTRA_PASSWORD_COMPLEXITY}
 * </ul>
 */
                    out.close();
                    message = context.getString(R.string.received_bugreport,
                            outputBugreportFile.getPath(), bugreportFileHash);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    message = context.getString(R.string.received_bugreport_failed_retrieval);
                }
                return message;
            }

            @Override
            protected void onPostExecute(String message) {
                NotificationUtil.showNotification(context, R.string.bugreport_title,
                        message, NotificationUtil.BUGREPORT_NOTIFICATION_ID);
                result.finish();
            }

        }.execute();
    }

    @TargetApi(VERSION_CODES.N)
    @Override
    public void onBugreportFailed(Context context, Intent intent, int failureCode) {
        String failureReason;
        switch (failureCode) {
            case BUGREPORT_FAILURE_FILE_NO_LONGER_AVAILABLE:
                failureReason = context.getString(
                        R.string.bugreport_failure_file_no_longer_available);
                break;
            case BUGREPORT_FAILURE_FAILED_COMPLETING:
                //fall through
            default:
                failureReason = context.getString(
                        R.string.bugreport_failure_failed_completing);
        }
        Log.i(TAG, "Bugreport failed: " + failureReason);
        NotificationUtil.showNotification(context, R.string.bugreport_title,
                context.getString(R.string.bugreport_failure_message, failureReason),
                NotificationUtil.BUGREPORT_NOTIFICATION_ID);
    }


    @TargetApi(VERSION_CODES.O)
    @Override
    public void onUserAdded(Context context, Intent intent, UserHandle newUser) {
        handleUserAction(context, newUser, R.string.on_user_added_title,
                R.string.on_user_added_message, NotificationUtil.USER_ADDED_NOTIFICATION_ID);
    }

    @TargetApi(VERSION_CODES.O)
    @Override
    public void onUserRemoved(Context context, Intent intent, UserHandle removedUser) {
        handleUserAction(context, removedUser, R.string.on_user_removed_title,
                R.string.on_user_removed_message, NotificationUtil.USER_REMOVED_NOTIFICATION_ID);
    }

    @TargetApi(VERSION_CODES.P)
    @Override
    public void onUserStarted(Context context, Intent intent, UserHandle startedUser) {
        handleUserAction(context, startedUser, R.string.on_user_started_title,
                R.string.on_user_started_message, NotificationUtil.USER_STARTED_NOTIFICATION_ID);
    }

    @TargetApi(VERSION_CODES.P)
    @Override
    public void onUserStopped(Context context, Intent intent, UserHandle stoppedUser) {
        handleUserAction(context, stoppedUser, R.string.on_user_stopped_title,
                R.string.on_user_stopped_message, NotificationUtil.USER_STOPPED_NOTIFICATION_ID);
    }

    @TargetApi(VERSION_CODES.P)
    @Override
    public void onUserSwitched(Context context, Intent intent, UserHandle switchedUser) {
        handleUserAction(context, switchedUser, R.string.on_user_switched_title,
                R.string.on_user_switched_message, NotificationUtil.USER_SWITCHED_NOTIFICATION_ID);
    }

    @TargetApi(VERSION_CODES.M)
    @Override
    public void onSystemUpdatePending(Context context, Intent intent, long receivedTime) {
        if (receivedTime != -1) {
            DateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
            String timeString = sdf.format(new Date(receivedTime));
            Toast.makeText(context, "System update received at: " + timeString,
                    Toast.LENGTH_LONG).show();
        } else {
            // No system update is currently available on this device.
        }
    }

    @TargetApi(VERSION_CODES.M)
    @Override
    public String onChoosePrivateKeyAlias(Context context, Intent intent, int uid, Uri uri,
            String alias) {
        return CommonReceiverOperations.onChoosePrivateKeyAlias(context, uid);
    }

    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), DeviceAdminReceiver.class);
    }

    @Deprecated
    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        onPasswordExpiring(context, intent, Process.myUserHandle());
    }

    @TargetApi(VERSION_CODES.O)
    // @Override
    public void onPasswordExpiring(Context context, Intent intent, UserHandle user) {
        if (!Process.myUserHandle().equals(user)) {
            // This password expiration was on another user, for example a parent profile. Skip it.
            return;
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);

        final long timeNow = System.currentTimeMillis();
        final long timeAdminExpires =
                devicePolicyManager.getPasswordExpiration(getComponentName(context));
        final boolean expiredBySelf = (timeNow >= timeAdminExpires && timeAdminExpires != 0);

        NotificationUtil.showNotification(context, R.string.password_expired_title,
                context.getString(expiredBySelf
                        ? R.string.password_expired_by_self
                        : R.string.password_expired_by_others),
                NotificationUtil.PASSWORD_EXPIRATION_NOTIFICATION_ID);
    }

    @Deprecated
    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        onPasswordFailed(context, intent, Process.myUserHandle());
    }

    @TargetApi(VERSION_CODES.O)
    // @Override
    public void onPasswordFailed(Context context, Intent intent, UserHandle user) {
        if (!Process.myUserHandle().equals(user)) {
            // This password failure was on another user, for example a parent profile. Ignore it.
            return;
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        /*
         * Post a notification to show:
         *  - how many wrong passwords have been entered;
         *  - how many wrong passwords need to be entered for the device to be wiped.
         */
        int attempts = devicePolicyManager.getCurrentFailedPasswordAttempts();
        int maxAttempts = devicePolicyManager.getMaximumFailedPasswordsForWipe(null);

        String title = context.getResources().getQuantityString(
                R.plurals.password_failed_attempts_title, attempts, attempts);

        ArrayList<Date> previousFailedAttempts = getFailedPasswordAttempts(context);
        Date date = new Date();
        previousFailedAttempts.add(date);
        Collections.sort(previousFailedAttempts, Collections.<Date>reverseOrder());
        try {
            saveFailedPasswordAttempts(context, previousFailedAttempts);
        } catch (IOException e) {
            Log.e(TAG, "Unable to save failed password attempts", e);
        }

        String content = maxAttempts == 0
                ? context.getString(R.string.password_failed_no_limit_set)
                : context.getResources().getQuantityString(
                        R.plurals.password_failed_attempts_content, maxAttempts, maxAttempts);

        NotificationCompat.Builder warn = NotificationUtil.getNotificationBuilder(context);
        warn.setSmallIcon(R.drawable.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(PendingIntent.getActivity(context, /* requestCode */ -1,
                        new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD), /* flags */ 0));

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);

        final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        for(Date d : previousFailedAttempts) {
            inboxStyle.addLine(dateFormat.format(d));
        }
        warn.setStyle(inboxStyle);

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(PASSWORD_FAILED_NOTIFICATION_ID, warn.build());
    }

    @Deprecated
    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        onPasswordSucceeded(context, intent, Process.myUserHandle());
    }

    @TargetApi(VERSION_CODES.O)
    // @Override
    public void onPasswordSucceeded(Context context, Intent intent, UserHandle user) {
        if (Process.myUserHandle().equals(user)) {
            logFile(context).delete();
        }
    }

    @Deprecated
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        onPasswordChanged(context, intent, Process.myUserHandle());
    }

    @TargetApi(VERSION_CODES.O)
    // @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle user) {
        if (Process.myUserHandle().equals(user)) {
            updatePasswordConstraintNotification(context);
        }
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        long serialNumber = userManager.getSerialNumberForUser(Binder.getCallingUserHandle());
        Log.i(TAG, "Device admin enabled in user with serial number: " + serialNumber);
    }

    private static File logFile(Context context) {
        File parent = context.getDir(LOGS_DIR, Context.MODE_PRIVATE);
        return new File(parent, FAILED_PASSWORD_LOG_FILE);
    }

    private static ArrayList<Date> getFailedPasswordAttempts(Context context) {
        File logFile = logFile(context);
        ArrayList<Date> result = new ArrayList<Date>();

        if(!logFile.exists()) {
            return result;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null && line.length() > 0) {
                result.add(new Date(Long.parseLong(line)));
            }

            br.close();
        } catch (IOException e) {
            Log.e(TAG, "Unable to read failed password attempts", e);
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to close failed password attempts log file", e);
                }
            }
        }

        return result;
    }

    private static void saveFailedPasswordAttempts(Context context, ArrayList<Date> attempts)
            throws IOException {
        File logFile = logFile(context);

        if(!logFile.exists()) {
            logFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(logFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(Date date : attempts) {
            bw.write(Long.toString(date.getTime()));
            bw.newLine();
        }

        bw.close();
    }

    private static void updatePasswordConstraintNotification(Context context) {
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        final UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);

        if (!dpm.isProfileOwnerApp(context.getPackageName())
                && !dpm.isDeviceOwnerApp(context.getPackageName())) {
            // Only try to update the notification if we are a profile or device owner.
            return;
        }

        final NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        final ArrayList<CharSequence> problems = new ArrayList<>();
        if (!dpm.isActivePasswordSufficient()) {
            problems.add(context.getText(R.string.password_not_compliant_title));
        }

        if (um.hasUserRestriction(UserManager.DISALLOW_UNIFIED_PASSWORD)
                && Util.isManagedProfileOwner(context)
                && isUsingUnifiedPassword(context)) {
            problems.add(context.getText(R.string.separate_challenge_required_title));
        }

        if (!problems.isEmpty()) {
            final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            style.setBigContentTitle(
                    context.getText(R.string.set_new_password_notification_content));
            for (final CharSequence problem : problems) {
                style.addLine(problem);
            }
            final NotificationCompat.Builder warn =
                    NotificationUtil.getNotificationBuilder(context);
            warn.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setStyle(style)
                    .setContentIntent(PendingIntent.getActivity(context, /*requestCode*/ -1,
                            new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD), /*flags*/ 0));
            nm.notify(CHANGE_PASSWORD_NOTIFICATION_ID, warn.getNotification());
        } else {
            nm.cancel(CHANGE_PASSWORD_NOTIFICATION_ID);
        }
    }

    @TargetApi(VERSION_CODES.P)
    private static Boolean isUsingUnifiedPassword(Context context) {
        if (Util.SDK_INT < VERSION_CODES.P) {
            return false;
        }
        final DevicePolicyManager dpm = context.getSystemService(DevicePolicyManager.class);
        return dpm.isUsingUnifiedPassword(getComponentName(context));
    }

    /**
     * Notify the admin receiver that something about the password has changed, e.g. quality
     * constraints or separate challenge requirements.
     *
     * This has to be sent manually because the system server only sends broadcasts for changes to
     * the actual password, not any of the constraints related it it.
     *
     * <p>May trigger a show/hide of the notification warning to change the password through
     * Settings.
     */
    public static void sendPasswordRequirementsChanged(Context context) {
        final Intent changedIntent =
                new Intent(DeviceAdminReceiver.ACTION_PASSWORD_REQUIREMENTS_CHANGED);
        changedIntent.setComponent(getComponentName(context));
        context.sendBroadcast(changedIntent);
    }

    private void onProfileOwnerChanged(Context context) {
        Log.i(TAG, "onProfileOwnerChanged");
        NotificationUtil.showNotification(context,
                R.string.transfer_ownership_profile_owner_changed_title,
                context.getString(R.string.transfer_ownership_profile_owner_changed_title),
                NotificationUtil.PROFILE_OWNER_CHANGED_ID);
    }

    private void onDeviceOwnerChanged(Context context) {
        Log.i(TAG, "onDeviceOwnerChanged");
        NotificationUtil.showNotification(context,
                R.string.transfer_ownership_device_owner_changed_title,
                context.getString(R.string.transfer_ownership_device_owner_changed_title),
                NotificationUtil.DEVICE_OWNER_CHANGED_ID);
    }

    @TargetApi(VERSION_CODES.P)
    public void onTransferOwnershipComplete(Context context, PersistableBundle bundle) {
        Log.i(TAG, "onTransferOwnershipComplete");
        NotificationUtil.showNotification(context,
                R.string.transfer_ownership_complete_title,
                context.getString(R.string.transfer_ownership_complete_message,
                        getComponentName(context)),
                NotificationUtil.TRANSFER_OWNERSHIP_COMPLETE_ID);
    }

    @TargetApi(VERSION_CODES.P)
    public void onTransferAffiliatedProfileOwnershipComplete(Context context, UserHandle user) {
        Log.i(TAG, "onTransferAffiliatedProfileOwnershipComplete");
        NotificationUtil.showNotification(context,
                R.string.transfer_ownership_affiliated_complete_title,
                context.getString(R.string.transfer_ownership_affiliated_complete_message, user),
                NotificationUtil.TRANSFER_AFFILIATED_PROFILE_OWNERSHIP_COMPLETE_ID);
    }

    private void handleUserAction(Context context, UserHandle userHandle, int titleResId,
            int messageResId, int notificationId) {
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        String message = context.getString(messageResId,
                userManager.getSerialNumberForUser(userHandle));
        Log.i(TAG, message);
        NotificationUtil.showNotification(context, titleResId, message, notificationId);
    }

    @Override
    public void onLockTaskModeExiting(@NonNull Context context, @NonNull Intent intent) {
        super.onLockTaskModeExiting(context, intent);
    }
}
