package app.mulipati

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.db.entities.Messages
import app.mulipati.db.repositories.MessagesRepository
import app.mulipati.util.Resource

class MessagesViewModel @ViewModelInject constructor(
        repository: MessagesRepository
) : ViewModel() {

    private val repo = repository

    fun getMessages(from: Int, to: Int) : LiveData<Resource<List<Messages>>> {
        return repo.getMessages(from, to)
    }

    fun insertMessages(from: Int, to: Int){
        repo.getMessages(from , to)
    }


}