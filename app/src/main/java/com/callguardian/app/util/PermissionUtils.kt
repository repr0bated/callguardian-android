package com.callguardian.app.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Utility class for handling runtime permissions with user-friendly explanations.
 * Provides rationale messages for sensitive permissions required by CallGuardian.
 */
object PermissionUtils {

    /**
     * Permission codes for request tracking
     */
    const val REQUEST_CODE_CALL_LOG = 1001
    const val REQUEST_CODE_PHONE_STATE = 1002
    const val REQUEST_CODE_READ_SMS = 1003
    const val REQUEST_CODE_WRITE_CONTACTS = 1004
    const val REQUEST_CODE_ANSWER_PHONE_CALLS = 1005

    /**
     * All permissions required for CallGuardian functionality
     */
    val REQUIRED_PERMISSIONS = listOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.ANSWER_PHONE_CALLS
    )

    /**
     * Check if all required permissions are granted
     */
    fun hasAllPermissions(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Check if a specific permission is granted
     */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request permissions with rationale if needed
     */
    fun requestPermissionsWithRationale(
        activity: Activity,
        permissions: Array<String>,
        rationaleMessage: String,
        requestCode: Int
    ) {
        val shouldShowRationale = permissions.any { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }

        if (shouldShowRationale) {
            // Show rationale dialog before requesting permission
            showPermissionRationaleDialog(activity, rationaleMessage) { granted ->
                if (granted) {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode)
                }
            }
        } else {
            // No rationale needed, request directly
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
    }

    /**
     * Get user-friendly permission rationale message
     */
    fun getPermissionRationale(permission: String): String {
        return when (permission) {
            Manifest.permission.READ_CALL_LOG -> 
                "CallGuardian needs access to your call log to analyze incoming and outgoing calls, " +
                "provide call screening, and maintain interaction history for better spam detection."
            
            Manifest.permission.READ_PHONE_STATE ->
                "CallGuardian needs access to phone state to identify incoming calls, " +
                "determine call status, and provide real-time caller information."
            
            Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS ->
                "CallGuardian needs access to SMS messages to filter spam messages, " +
                "analyze message content, and provide message screening capabilities."
            
            Manifest.permission.WRITE_CONTACTS ->
                "CallGuardian can save unknown numbers to your contacts for easier identification. " +
                "This permission allows the app to add new contacts when you choose to save a number."
            
            Manifest.permission.ANSWER_PHONE_CALLS ->
                "CallGuardian needs permission to answer calls so it can automatically reject " +
                "spam calls or connect legitimate calls based on your settings."
            
            else -> "This permission is required for CallGuardian to function properly."
        }
    }

    /**
     * Get permission title for rationale dialog
     */
    fun getPermissionTitle(permission: String): String {
        return when (permission) {
            Manifest.permission.READ_CALL_LOG -> "Call Log Access"
            Manifest.permission.READ_PHONE_STATE -> "Phone State Access"
            Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS -> "SMS Access"
            Manifest.permission.WRITE_CONTACTS -> "Contacts Access"
            Manifest.permission.ANSWER_PHONE_CALLS -> "Call Answering"
            else -> "Permission Required"
        }
    }

    /**
     * Show permission rationale dialog
     * Note: This would need to be implemented with Compose or traditional Android dialogs
     */
    private fun showPermissionRationaleDialog(
        activity: Activity,
        message: String,
        onResult: (Boolean) -> Unit
    ) {
        // This is a placeholder for the actual dialog implementation
        // In a real implementation, this would show a Material Design dialog
        // with the rationale message and options to proceed or cancel
        onResult(true) // Auto-proceed for now
    }

    /**
     * Check if any required permissions are permanently denied
     */
    fun hasPermanentlyDeniedPermissions(activity: Activity): Boolean {
        return REQUIRED_PERMISSIONS.any { permission ->
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) &&
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Get message for permanently denied permissions
     */
    fun getPermanentlyDeniedMessage(context: Context): String {
        return "Some permissions are permanently denied. " +
               "Please go to Settings > Apps > CallGuardian > Permissions " +
               "and enable all required permissions for full functionality."
    }

    /**
     * Open app settings for permission management
     */
    fun openAppSettings(context: Context) {
        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    /**
     * Validate that all critical permissions are available
     * Returns true if all permissions are granted, false otherwise
     */
    fun validateCriticalPermissions(context: Context): Boolean {
        val criticalPermissions = listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )

        return criticalPermissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Get list of missing permissions
     */
    fun getMissingPermissions(context: Context): List<String> {
        return REQUIRED_PERMISSIONS.filter { permission ->
            ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Request all missing permissions with appropriate rationale
     */
    fun requestMissingPermissions(activity: Activity) {
        val missingPermissions = getMissingPermissions(activity)
        
        if (missingPermissions.isEmpty()) {
            return
        }

        // Group permissions by category for better user experience
        val callPermissions = missingPermissions.filter { 
            it == Manifest.permission.READ_CALL_LOG || 
            it == Manifest.permission.READ_PHONE_STATE ||
            it == Manifest.permission.ANSWER_PHONE_CALLS
        }

        val smsPermissions = missingPermissions.filter { 
            it == Manifest.permission.RECEIVE_SMS || 
            it == Manifest.permission.READ_SMS
        }

        val contactPermissions = missingPermissions.filter { 
            it == Manifest.permission.WRITE_CONTACTS
        }

        // Request call permissions
        if (callPermissions.isNotEmpty()) {
            requestPermissionsWithRationale(
                activity,
                callPermissions.toTypedArray(),
                getPermissionRationale(Manifest.permission.READ_CALL_LOG),
                REQUEST_CODE_CALL_LOG
            )
        }

        // Request SMS permissions
        if (smsPermissions.isNotEmpty()) {
            requestPermissionsWithRationale(
                activity,
                smsPermissions.toTypedArray(),
                getPermissionRationale(Manifest.permission.RECEIVE_SMS),
                REQUEST_CODE_READ_SMS
            )
        }

        // Request contact permissions
        if (contactPermissions.isNotEmpty()) {
            requestPermissionsWithRationale(
                activity,
                contactPermissions.toTypedArray(),
                getPermissionRationale(Manifest.permission.WRITE_CONTACTS),
                REQUEST_CODE_WRITE_CONTACTS
            )
        }
    }
}