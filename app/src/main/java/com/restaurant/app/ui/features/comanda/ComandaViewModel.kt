package com.restaurant.app.ui.features.comanda

import androidx.lifecycle.ViewModel
import com.restaurant.app.domain.model.ComandaModel
import com.restaurant.app.domain.model.EstadoComanda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ComandaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ComandaUiState(comandas = getMockComandas())
    )
    val uiState: StateFlow<ComandaUiState> = _uiState.asStateFlow()

    fun onFiltroSelected(filtro: String) {
        _uiState.update { it.copy(filtroEstado = filtro) }
    }

    private companion object {
        fun getMockComandas() = listOf(
            ComandaModel(
                id = "124",
                numeroMesa = "03",
                camareroNombre = "Camila Gómez",
                cantidadItems = 3,
                hora = "10:25",
                total = 28600.0,
                estado = EstadoComanda.EN_PREPARACION
            ),
            ComandaModel(
                id = "123",
                numeroMesa = "02",
                camareroNombre = "Camila Gómez",
                cantidadItems = 2,
                hora = "10:10",
                total = 12400.0,
                estado = EstadoComanda.ENTREGADO
            ),
            ComandaModel(
                id = "122",
                numeroMesa = "06",
                camareroNombre = "Juan Pérez",
                cantidadItems = 4,
                hora = "09:45",
                total = 42000.0,
                estado = EstadoComanda.COBRADO
            )
        )
    }
}