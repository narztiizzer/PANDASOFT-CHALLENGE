package com.tiizzer.narz.pandasoft.challenge

import android.app.Application
import com.tiizzer.narz.pandasoft.challenge.authentication.VMLogin
import com.tiizzer.narz.pandasoft.challenge.details.VMDetails
import com.tiizzer.narz.pandasoft.challenge.home.viewmodel.VMHome
import com.tiizzer.narz.pandasoft.challenge.main.VMMain
import com.tiizzer.narz.pandasoft.challenge.repository.AppApiRepository
import com.tiizzer.narz.pandasoft.challenge.repository.AppRequestInterceptor
import com.tiizzer.narz.pandasoft.challenge.utils.InteractionTimeoutHelper
import com.tiizzer.narz.pandasoft.challenge.utils.SharePreferencesHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseApplication: Application() {
    private val interaction: InteractionTimeoutHelper by lazy { InteractionTimeoutHelper.getInstance() }

    override fun onCreate() {
        super.onCreate()
        this.setupKoin()
    }

    private fun prepareModule(): List<Module> {
        val main = module {
            factory {
                SharePreferencesHelper(get())
            }

            factory {
                val interceptor = HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
                val client = OkHttpClient
                    .Builder()
                    .addInterceptor(AppRequestInterceptor(get()))
                    .addInterceptor(interceptor)
                    .build()

                Retrofit.Builder()
                    .baseUrl("https://5c065a3fc16e1200139479cc.mockapi.io/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AppApiRepository::class.java)
            }

            viewModel { VMMain(get(), get(), get()) }
        }

        val login = module {
            viewModel { VMLogin(get(), get(), get()) }
        }

        val home = module {
            viewModel {
                VMHome(
                    get(),
                    get(),
                    get()
                )
            }
        }

        val detail = module {
            viewModel { VMDetails(get(), get(), get()) }
        }

        return listOf(main, login, home, detail)
    }

    private fun setupKoin(){
        startKoin {
            androidContext(this@BaseApplication)
            modules(prepareModule())
        }
    }

    fun getInteractionHelper() = this.interaction
}