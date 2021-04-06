package uz.revolution.mydictionary.database.app

import android.app.Application
import android.graphics.BlurMaskFilter
import androidx.appcompat.app.AppCompatDelegate
import io.alterac.blurkit.BlurKit
import uz.revolution.mydictionary.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)

        // disable night mode settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // init blurLayout
        BlurKit.init(this)
    }
}