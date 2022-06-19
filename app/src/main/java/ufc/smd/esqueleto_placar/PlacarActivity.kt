package ufc.smd.esqueleto_placar

import android.content.Context
import android.content.SharedPreferences
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import data.VOLEI_POINT
import data.VoleiConfig
import data.VoleiJogo
import util.BancoController
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets


class PlacarActivity : AppCompatActivity() {
    /// TEXT COMPONENTS
    lateinit var tvNomeTimeA : TextView
    lateinit var tvNomeTimeB : TextView
    lateinit var setsTimeA : TextView
    lateinit var setsTimeB : TextView
    lateinit var tvGanhador : TextView

    /// Buttons
    lateinit var ibtUndo : ImageButton
    lateinit var btPontoTimeA : Button
    lateinit var btPontoTimeB : Button
    lateinit var btSave : Button
    lateinit var chronoTempoJogo : Chronometer

    /// VOLEI CONFIG
    lateinit var voleiConfig : VoleiConfig
    lateinit var voleiJogo : VoleiJogo
    var chronoRunning : Boolean = false
    var pauseOffset : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)

        // Resgatando configurações da Activity anterior
        voleiConfig = getIntent().getExtras()?.getSerializable("voleiConfig") as VoleiConfig
        voleiJogo = VoleiJogo(voleiConfig.nomePartida, voleiConfig.comTemporizador, voleiConfig.pontosPorSet, voleiConfig.qtdSetParaGanhar)
        voleiJogo.voleiPlacar.NomeTimeA = voleiConfig.nomeTimeA
        voleiJogo.voleiPlacar.NomeTimeB = voleiConfig.nomeTimeB
        voleiJogo.voleiPlacar.dataJogo = voleiConfig.dataInicioJogo

        // Referencia dos componentes
        tvNomeTimeA = findViewById(R.id.tvTimeA)
        tvNomeTimeB = findViewById(R.id.tvTimeB)
        setsTimeA = findViewById(R.id.tvSetTimeA)
        setsTimeB = findViewById(R.id.tvSetTimeB)
        btPontoTimeA = findViewById(R.id.btPontoTimeA)
        btPontoTimeB = findViewById(R.id.btPontoTimeB)
        tvGanhador = findViewById(R.id.tvGanhadorFim)
        ibtUndo = findViewById(R.id.ibtUndo)
        btSave = findViewById(R.id.btSalvarPlacar)
        chronoTempoJogo = findViewById(R.id.cronoTempoJogo)

        // Setando nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        tvNomePartida.text = voleiConfig.nomePartida

        // Iniciando a tela de placar com informações do jogo
        btSave.isEnabled = false
        tvNomeTimeA.setText(voleiConfig.nomeTimeA)
        tvNomeTimeB.setText(voleiConfig.nomeTimeB)
        atualizarSets()
        atualizarPlacar()

        // Cronometro iniciar
        chronoRunning = true
        chronoTempoJogo.setOnClickListener{
            if(chronoRunning)
                cronometroPausar()
            else
                cronometroIniciar()

            chronoRunning = !chronoRunning
        }
        cronometroIniciar()
    }
    fun adicionarPontoTimeA(v : View){ verificarEstadoJogoVolei(voleiJogo.pontoTimeA()) }
    fun adicionarPontoTimeB(v : View){ verificarEstadoJogoVolei(voleiJogo.pontoTimeB()) }
    fun verificarEstadoJogoVolei(volei_point : VOLEI_POINT){
        when(volei_point){
            VOLEI_POINT.NORMAL_POINT -> atualizarPlacar()
            VOLEI_POINT.SET_POINT -> {
                atualizarPlacar()
                atualizarSets()
                vibrar()
            }
            VOLEI_POINT.GAME_POINT -> {
                atualizarPlacar()
                atualizarSets()
                finalizarJogo()
                vibrar()
            }
        }
    }

    fun atualizarPlacar(){
        btPontoTimeA.text = voleiJogo.voleiPlacar.placarTimeA.toString()
        btPontoTimeB.text = voleiJogo.voleiPlacar.placarTimeB.toString()
    }
    fun atualizarSets(){
        setsTimeA.text = "Sets: " + voleiJogo.voleiPlacar.setsTimeA.toString()
        setsTimeB.text = "Sets: " + voleiJogo.voleiPlacar.setsTimeB.toString()
    }
    fun finalizarJogo(){
        voleiJogo.voleiPlacar.tempoDeJogo = chronoTempoJogo.text.toString()

        var ganhadorString = voleiJogo.getGanhadorString()

        if(ganhadorString == "EMPATE"){
            tvGanhador.text = ganhadorString;
        } else {
            tvGanhador.text = ganhadorString + " VENCEU!!!"
        }



        btSave.isEnabled = true

        ibtUndo.isEnabled = false
        btPontoTimeA.isEnabled = false
        btPontoTimeB.isEnabled = false
        tvNomeTimeA.isEnabled = false
        tvNomeTimeB.isEnabled = false
        chronoTempoJogo.isEnabled = false
        chronoRunning = false
        cronometroPausar()
    }

    fun undo(v: View){
        voleiJogo.voltarAcao()
        atualizarSets()
        atualizarPlacar()
    }
    fun vibrar(){
        val buzzer = this.getSystemService<Vibrator>()
        val pattern = longArrayOf(0, 200, 100, 300)
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

    fun saveGame(v: View) {
        btSave.isEnabled = false

        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()

        //Salvar o número de jogos já armazenados
        var quantidadePartidas = sp.getInt("quantidadePartidas", 0) + 1
        edShared.putInt("quantidadePartidas", quantidadePartidas)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(voleiJogo.voleiPlacar);

        //Salvar como "match1"
        edShared.putString("match" + quantidadePartidas.toString(), dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit()

        /*
        * SAVE GAME - SQLITE
        * 18/06 - Vaneska Sousa - O envio dos dados está dando erro, está retornando -1
        */
        println("pontos por set: "+voleiConfig.pontosPorSet)

        val crud = BancoController(baseContext)
        var resultado = "";
        resultado = crud.insereDados(voleiConfig.nomePartida, voleiConfig.nomeTimeA, voleiConfig.nomeTimeB,
            voleiConfig.pontosPorSet, voleiJogo.quantidadeDeSetsParaGanharJogo, voleiJogo.getGanhadorString());

        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

    }

    fun cronometroIniciar(){
        chronoTempoJogo.base = SystemClock.elapsedRealtime() - pauseOffset
        chronoTempoJogo.start()
    }
    fun cronometroPausar(){
        chronoTempoJogo.stop()
        pauseOffset = SystemClock.elapsedRealtime() - chronoTempoJogo.base
    }

/*

    fun lerUltimosJogos(v: View){
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)

        var meuObjString:String= sp.getString("match1","").toString()
        if (meuObjString.length >=1) {
            var dis = ByteArrayInputStream(meuObjString.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var placarAntigo:Placar=oos.readObject() as Placar
            Log.v("SMD26",placar.resultado)
        }
    }

    fun ultimoJogos () {
        val sharedFilename = "PreviousGames"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var matchStr:String=sp.getString("match1","").toString()
       // Log.v("PDM22", matchStr)
        if (matchStr.length >=1){
            var dis = ByteArrayInputStream(matchStr.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var prevPlacar:Placar = oos.readObject() as Placar
            Log.v("PDM22", "Jogo Salvo:"+ prevPlacar.resultado)
        }

    }
*/
}
