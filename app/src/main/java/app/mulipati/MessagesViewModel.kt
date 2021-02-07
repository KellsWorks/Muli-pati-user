package app.mulipati

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import app.mulipati.db.repositories.MessagesRepository

class MessagesViewModel @ViewModelInject constructor(
        repository: MessagesRepository
) : ViewModel() {

    private val repo = repository


}