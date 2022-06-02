package data
import java.io.Serializable

data class VoleiConfig (
    var nomePartida : String,
    var comTemporizador: Boolean,
    var pontosPorSet : Int = 25,
    var qtdSetParaGanhar : Int = 3,
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
    var com_temporizador : Boolean
):Serializable

enum class VOLEI_POINT {
    NULL_POINT,
    NORMAL_POINT,
    SET_POINT,
    GAME_POINT,
}


class VoleiJogo (var nomePartida : String, var temporizador : Boolean, var pontuacaoMinimaGanharSet : Int = 25, var quantidadeDeSetsParaGanharJogo : Int = 3){
    var voleiPlacar = VoleiPlacar(nomePartida,"Time A", "Time B", 0, 0, 0, 0, "", temporizador)
    var jogoFinalizado = false

    fun pontoTimeA() : VOLEI_POINT{
        if(jogoFinalizado) return VOLEI_POINT.NULL_POINT

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
}
