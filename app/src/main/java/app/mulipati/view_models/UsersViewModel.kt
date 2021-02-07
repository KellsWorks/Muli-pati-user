package app.mulipati.view_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.db.repositories.UsersRepository
import app.mulipati.network.responses.users.AppUser
import app.mulipati.util.Resource

class UsersViewModel @ViewModelInject constructor(
    repository: UsersRepository
) : ViewModel() {

    val users: LiveData<Resource<List<AppUser>>> = repository.getUsers()

}