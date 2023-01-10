package ru.esstu.android.domain.datasources.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.esstu.android.domain.datasources.download_worker.FileDownloadRepositoryImpl
import ru.esstu.android.domain.datasources.download_worker.IFileDownloadRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadRepoModule{
    @Singleton
    @Binds
    abstract fun bindDownloadRepository(
        fileDownloadRepositoryImpl: FileDownloadRepositoryImpl
    ): IFileDownloadRepository
}