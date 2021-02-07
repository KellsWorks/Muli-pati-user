package app.mulipati.db.repositories

import app.mulipati.db.daos.UsersDao
import app.mulipati.db.remote.UsersRemoteDataSource
import app.mulipati.util.performGetOperation
import javax.inject.Inject

class UsersRepository @Inject constructor(
        private val remoteDataSource: UsersRemoteDataSource,
        private val localDataSource: UsersDao
) {

    fun getUsers() = performGetOperation(
        databaseQuery = { localDataSource.getUsers() },
        networkCall = { remoteDataSource.getUsers() },
        saveCallResult = {

            localDataSource.deleteUsers()
            localDataSource.insertUsers(it.app_users)

        }
    )

}