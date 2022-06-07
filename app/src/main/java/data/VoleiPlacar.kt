package data
import java.io.Serializable

enum class VOLEI_POINT {
    NULL_POINT,
    NORMAL_POINT,
    SET_POINT,
    GAME_POINT,
}

data class VoleiConfig (
    var nomePartida : String,
    var comTemporizador: Boolean,
    var nomeTimeA : String = "Time A",
    var nomeTimeB : String = "Time B",
    var pontosPorSet : Int = 25,
    var qtdSetParaGanhar : Int = 3,
    var dataInicioJogo : Long = 0

) : Serializable

data class VoleiPlacar (
    var NomePartida : String,
    var NomeTimeA : String,
    var NomeTimeB : String,
    var placarTimeA : Int,
    var placarTimeB : Int,
    var setsTimeA : Int,
    var setsTimeB : Int,
    var tempoDeJogo : String,
    var com_temporizador : Boolean,
    var dataJogo : Long,
):Serializable

data class VoleiPlacarUndo(
    val pointTimeA : Int,
    val pointTimeB : Int,
    val setTimeA : Int,
    val SetTimeB : Int
)

class VoleiJogo (var nomePartida : String, var temporizador : Boolean, var pontuacaoMinimaGanharSet : Int = 25, var quantidadeDeSetsParaGanharJogo : Int = 3){
    var voleiPlacar = VoleiPlacar(nomePartida,"Time A", "Time B", 0, 0, 0, 0, "", temporizador, 0)
    var undo : MutableList<VoleiPlacarUndo> = mutableListOf()
    var jogoFinalizado = false

    fun pontoTimeA() : VOLEI_POINT{
        if(jogoFinalizado) return VOLEI_POINT.NULL_POINT

        undo.add(VoleiPlacarUndo(voleiPlacar.placarTimeA, voleiPlacar.placarTimeB, voleiPlacar.setsTimeA, voleiPlacar.setsTimeB))

        if(++voleiPlacar.placarTimeA >= pontuacaoMinimaGanharSet && checarDiferencaPlacarMaiorQueDois()){
            if(++voleiPlacar.setsTimeA >= quantidadeDeSetsParaGanharJogo){
                jogoFinalizado = true
                return VOLEI_POINT.GAME_POINT
            }
            else {
                zerarPlacarPontos()
                return VOLEI_POINT.SET_POINT
            }
        }

        return VOLEI_POINT.NORMAL_POINT
    }

    fun pontoTimeB() : VOLEI_POINT{
        if(jogoFinalizado) return VOLEI_POINT.NULL_POINT

        undo.add(VoleiPlacarUndo(voleiPlacar.placarTimeA, voleiPlacar.placarTimeB, voleiPlacar.setsTimeA, voleiPlacar.setsTimeB))

        if(++voleiPlacar.placarTimeB >= pontuacaoMinimaGanharSet && checarDiferencaPlacarMaiorQueDois()) {
            if(++voleiPlacar.setsTimeB >= quantidadeDeSetsParaGanharJogo){
                jogoFinalizado = true
                return VOLEI_POINT.GAME_POINT

            }
            else {
                zerarPlacarPontos()
                return VOLEI_POINT.SET_POINT
            }
        }

        return VOLEI_POINT.NORMAL_POINT
    }

    fun zerarPlacarPontos(){
        voleiPlacar.placarTimeA = 0
        voleiPlacar.placarTimeB = 0
    }

    fun checarDiferencaPlacarMaiorQueDois() : Boolean{
        return Math.abs(voleiPlacar.placarTimeA - voleiPlacar.placarTimeB) >= 2
    }

    fun getGanhadorString() : String{
        if(voleiPlacar.setsTimeA > voleiPlacar.setsTimeB)
            return voleiPlacar.NomeTimeA
        else if(voleiPlacar.setsTimeA < voleiPlacar.setsTimeB)
            return voleiPlacar.NomeTimeB

        else{
            if(voleiPlacar.placarTimeA > voleiPlacar.placarTimeB)
                return voleiPlacar.NomeTimeA
            else if (voleiPlacar.placarTimeA < voleiPlacar.placarTimeB)
                return voleiPlacar.NomeTimeB
        }

        return "EMPATADO"
    }

    fun voltarAcao(){
        if(undo.isEmpty())
            return

        var placarData = undo.get(undo.lastIndex)
        undo.removeLast()

        voleiPlacar.placarTimeA = placarData.pointTimeA
        voleiPlacar.placarTimeB = placarData.pointTimeB
        voleiPlacar.setsTimeA = placarData.setTimeA
        voleiPlacar.setsTimeB = placarData.SetTimeB
    }
}
