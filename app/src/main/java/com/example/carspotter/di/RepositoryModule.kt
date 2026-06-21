package com.example.carspotter.di

import com.example.carspotter.data.repository.AuthRepository
import com.example.carspotter.data.repository.AuthRepositoryImpl
import com.example.carspotter.data.repository.CarModelRepository
import com.example.carspotter.data.repository.CarModelRepositoryImpl
import com.example.carspotter.data.repository.CommentRepository
import com.example.carspotter.data.repository.CommentRepositoryImpl
import com.example.carspotter.data.repository.FriendRepositoryImpl
import com.example.carspotter.data.repository.FriendRepository
import com.example.carspotter.data.repository.FriendRequestRepositoryImpl
import com.example.carspotter.data.repository.FriendRequestRepository
import com.example.carspotter.data.repository.ImageRepository
import com.example.carspotter.data.repository.ImageRepositoryImpl
import com.example.carspotter.data.repository.LikeRepository
import com.example.carspotter.data.repository.LikeRepositoryImpl
import com.example.carspotter.data.repository.LocationRepository
import com.example.carspotter.data.repository.LocationRepositoryImpl
import com.example.carspotter.data.repository.PostRepository
import com.example.carspotter.data.repository.PostRepositoryImpl
import com.example.carspotter.data.repository.ReportRepository
import com.example.carspotter.data.repository.ReportRepositoryImpl
import com.example.carspotter.data.repository.UserCarRepository
import com.example.carspotter.data.repository.UserCarRepositoryImpl
import com.example.carspotter.data.repository.UserRepository
import com.example.carspotter.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCarModelRepository(
        impl: CarModelRepositoryImpl
    ): CarModelRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        impl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindFriendRepository(
        impl: FriendRepositoryImpl
    ): FriendRepository

    @Binds
    @Singleton
    abstract fun bindFriendRequestRepository(
        impl: FriendRequestRepositoryImpl
    ): FriendRequestRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    @Singleton
    abstract fun bindLikeRepository(
        impl: LikeRepositoryImpl
    ): LikeRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindUserCarRepository(
        impl: UserCarRepositoryImpl
    ): UserCarRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}