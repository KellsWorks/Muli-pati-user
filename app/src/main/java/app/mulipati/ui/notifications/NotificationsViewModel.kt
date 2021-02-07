package app.mulipati.ui.notifications

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.db.repositories.NotificationsRepository
import app.mulipati.network.responses.notifications.Notification
import app.mulipati.util.Resource

class NotificationsViewModel @ViewModelInject constructor(
    repository: NotificationsRepository
) : ViewModel() {

    val notications: LiveData<Resource<List<Notification>>> = repository.getNotifications()

}