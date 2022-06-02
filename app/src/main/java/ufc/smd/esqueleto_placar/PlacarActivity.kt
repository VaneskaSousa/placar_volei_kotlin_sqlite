package ufc.smd.esqueleto_placar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import data.VOLEI_POINT
import data.VoleiConfig
import data.VoleiJogo


class PlacarActivity : AppCompatActivity() {
    /// TEXT COMPONENTS
    lateinit var etNomeTimeA : EditText
    lateinit var etNomeTimeB : EditText
    lateinit var setsTimeA : TextView
    lateinit var setsTimeB : TextView
    lateinit var btPontoTimeA : Button
    lateinit var btPontoTimeB : Button
    lateinit var tvGanhador : TextView

    /// VOLEI CONFIG
    lateinit var voleiConfig : VoleiConfig
    lateinit var voleiJogo : VoleiJogo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)

        // Resgatando configurações da Activity anterior
        voleiConfig = getIntent().getExtras()?.getSerializable("voleiConfig") as VoleiConfig
        voleiJogo = VoleiJogo(voleiConfig.nomePartida, voleiConfig.comTemporizador, voleiConfig.pontosPorSet, voleiConfig.qtdSetParaGanhar)

        // Referencia dos componentes
        etNomeTimeA = findViewById(R.id.etTimeA)
        etNomeTimeB = findViewById(R.id.etTimeB)
        setsTimeA = findViewById(R.id.tvSetTimeA)
        setsTimeB = findViewById(R.id.tvSetTimeB)
        btPontoTimeA = findViewById(R.id.btPontoTimeA)
        btPontoTimeB = findViewById(R.id.btPontoTimeB)
        tvGanhador = findViewById(R.id.tvGanhadorFim)

        // Setando nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        tvNomePartida.text = voleiConfig.nomePartida

        etNomeTimeA.setText(voleiJogo.voleiPlacar.NomeTimeA)
        etNomeTimeB.setText(voleiJogo.voleiPlacar.NomeTimeB)
        atualizarSets()
        atualizarPlacar()
    }

    fun adicionarPontoTimeA(v : View){ voleiState(voleiJogo.pontoTimeA()) }
    fun adicionarPontoTimeB(v : View){ voleiState(voleiJogo.pontoTimeB()) }
    fun voleiState(volei_point : VOLEI_POINT){
        when(volei_point){
            VOLEI_POINT.NORMAL_POINT -> atualizarPlacar()
            VOLEI_POINT.SET_POINT -> {
                atualizarPlacar()
                atualizarSets()
            }
            VOLEI_POINT.GAME_POINT -> {
                atualizarPlacar()
                atualizarSets()
                finalizarJogo()
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
        var ganhadorString = voleiJogo.getGanhadorString()

        if(ganhadorString == "EMPATE"){
            tvGanhador.text = ganhadorString;
        }
        else
            tvGanhador.text = ganhadorString + " VENCEU!!!"

        btPontoTimeA.isEnabled = false
        btPontoTimeB.isEnabled = false
    }

    fun modificarNomeTimeA(v : View){}
    fun modificarNomeTimeB(v : View){}

/*
    fun saveGame(v: View) {

        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()
        //Salvar o número de jogos já armazenados
        edShared.putInt("numberMatch", 1)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match1"
        edShared.putString("match1", dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit() //Não esqueçam de comitar!!!

    }

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
