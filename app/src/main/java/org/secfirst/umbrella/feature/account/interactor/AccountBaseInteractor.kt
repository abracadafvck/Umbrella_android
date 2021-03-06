package org.secfirst.umbrella.feature.account.interactor

import org.secfirst.umbrella.data.database.reader.FeedLocation
import org.secfirst.umbrella.data.database.reader.FeedSource
import org.secfirst.umbrella.feature.base.interactor.BaseInteractor

interface AccountBaseInteractor : BaseInteractor {

    suspend fun accessDatabase(userToken: String)

    suspend fun changeDatabaseAccess(userToken: String): Boolean

    suspend fun insertFeedLocation(feedLocation: FeedLocation)

    suspend fun insertAllFeedSources(feedSources: List<FeedSource>)

    suspend fun fetchFeedSources(): List<FeedSource>

    suspend fun fetchFeedLocation(): FeedLocation?

    suspend fun fetchRefreshInterval(): Int

    suspend fun putRefreshInterval(position: Int): Boolean

    fun setMaskApp(value: Boolean): Boolean

    fun isMaskApp(): Boolean

    fun setFakeView(isShowFakeView: Boolean) : Boolean

    fun getMaskApp() : Boolean

    suspend fun serializeNewContent(path : String) : Boolean
}