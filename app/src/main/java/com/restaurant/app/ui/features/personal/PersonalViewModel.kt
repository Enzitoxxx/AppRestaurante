package com.restaurant.app.ui.features.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
//import com.restaurant.app.data.repository.PersonalRepositoryDatos
import com.restaurant.app.domain.model.Empleado
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalViewModel(
    //private val personalRepository: PersonalRepository = PersonalRepositoryDatos()
    private val personalRepository: PersonalRepository = Repositorios.personal,
    private val mesaRepository: MesaRepository = Repositorios.mesas
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalUiState())
    val uiState: StateFlow<PersonalUiState> = _uiState.asStateFlow()

    private val _events = Channel<PersonalEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Escuchamos la lista de personal en tiempo real
        viewModelScope.launch {
            personalRepository.observarPersonal().collect { lista ->
                _uiState.update { it.copy(empleados = lista) }
            }
        }
    }

    fun onAgregarEmpleadoClick() {
        _events.trySend(PersonalEvent.AgregarEmpleado)
    }

    fun onEditarEmpleadoClick(empleado: Empleado) {
        _events.trySend(PersonalEvent.EditarEmpleado(empleado))
    }

    fun onAsignarMesasClick(empleado: Empleado) {
        _events.trySend(PersonalEvent.AsignarMesasEmpleado(empleado))
    }

    fun onSolicitarBajaEmpleado(empleado: Empleado) {
        _uiState.update { it.copy(empleadoADarDeBaja = empleado) }
    }

    fun onCancelarBajaEmpleado() {
        _uiState.update { it.copy(empleadoADarDeBaja = null) }
    }

    fun onConfirmarBajaEmpleado() {
        val empleado = _uiState.value.empleadoADarDeBaja ?: return
        viewModelScope.launch {
            personalRepository.guardarEmpleado(empleado.copy(estaActivo = false))
            mesaRepository.liberarMesasFueraDeZona(empleado.id, null)   // le quitamos todas las mesas
            _uiState.update { it.copy(empleadoADarDeBaja = null) }
        }
    }
}
