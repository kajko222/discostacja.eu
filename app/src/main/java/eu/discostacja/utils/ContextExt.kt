package eu.discostacja.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import eu.discostacja.model.BackgroundProblemFlags

fun Context.openNotificationSettings() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    }
    startActivity(intent)
}

fun Context.requestIgnoreBatteryOptimizations() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = Uri.parse("package:$packageName")
    }
    startActivity(intent)
}

fun Context.checkBackgroundRestrictions(): BackgroundProblemFlags {
    val notificationsDisabled = !NotificationManagerCompat.from(this).areNotificationsEnabled()

    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    val batteryOptimizationsEnabled = !powerManager.isIgnoringBatteryOptimizations(packageName)

    return BackgroundProblemFlags(
        notificationsDisabled = notificationsDisabled,
        batteryOptimizationsEnabled = batteryOptimizationsEnabled
    )
}
