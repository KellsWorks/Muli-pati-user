package app.mulipati.db.module

import android.content.Context
import app.mulipati.db.AppDatabase
import app.mulipati.db.daos.TripsDao
import app.mulipati.db.remote.TripRemoteDataSource
import app.mulipati.db.repositories.TripsRepository
import app.mulipati.network.services.TripsService
import app.mulipati.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    private var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideTripsService(retrofit: Retrofit): TripsService = retrofit.create(TripsService::class.java)

    @Singleton
    @Provides
    fun provideTripsRemoteDataSource(tripsService: TripsService) = TripRemoteDataSource(tripsService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideTripsDao(db: AppDatabase) = db.tripsDao()

    @Singleton
    @Provides
    fun provideTripsRepository(remoteDataSource: TripRemoteDataSource,
                          localDataSource: TripsDao) =
        TripsRepository(remoteDataSource, localDataSource)
}