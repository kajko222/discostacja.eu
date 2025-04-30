package eu.discostacja.di

import android.content.Context
import coil3.ImageLoader
import coil3.util.DebugLogger
import com.google.gson.GsonBuilder
import eu.discostacja.player.RadioPlayer
import eu.discostacja.service.MarqueeFetcher
import eu.discostacja.ui.screen.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RadioPlayer(get()) }

    single { GsonBuilder().create() }

    single { MarqueeFetcher(get()) }

    viewModel { MainViewModel(get()) }

    single {
        ImageLoader.Builder(get<Context>())
            .logger(DebugLogger())
            .build()
    }
}
