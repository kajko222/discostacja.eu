package eu.discostacja

import android.app.Application
import eu.discostacja.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DiscoStacjaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DiscoStacjaApp)
            modules(appModule)
        }
    }
}