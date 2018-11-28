package org.secfirst.umbrella.whitelabel.feature.tent.interactor

import org.secfirst.umbrella.whitelabel.data.disk.TentRepo
import org.secfirst.umbrella.whitelabel.feature.base.interactor.BaseInteractorImp
import javax.inject.Inject

class TentInteractorImp @Inject constructor(private val tentRepo: TentRepo)
    : BaseInteractorImp(), TentBaseInteractor {

    override suspend fun updateRepository() = tentRepo.updateRepository()

    override suspend fun fetchRepository() = tentRepo.fetchRepository()

    override suspend fun loadElementsFile() = tentRepo.loadElementsFile()

    override fun loadCategoryImage(imgName: String) = tentRepo.loadCategoryImage(imgName)

    override fun loadFile() = tentRepo.loadFile()


}