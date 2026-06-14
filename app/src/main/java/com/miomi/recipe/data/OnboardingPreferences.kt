package com.miomi.recipe.data

import android.content.Context

class OnboardingPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun hasSeenOnboarding(): Boolean = prefs.getBoolean("has_seen", false)

    fun markOnboardingComplete() {
        prefs.edit().putBoolean("has_seen", true).apply()
    }
}
