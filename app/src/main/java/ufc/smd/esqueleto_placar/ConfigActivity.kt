package ufc.smd.esqueleto_placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import data.VoleiConfig

class ConfigActivity : AppCompatActivity() {

    var voleiConfig : VoleiConfig = VoleiConfig("Nome da Partida", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        openConfig()
        initInterface()
    }
    fun saveConfig(){
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var edShared = sp.edit()


        edShared.putString("nomePartida",voleiConfig.nomePartida)
        edShared.putBoolean("comTemporizador",voleiConfig.comTemporizador)
        edShared.putInt("pontos", voleiConfig.pontosPorSet)
        edShared.putInt("sets", voleiConfig.qtdSetParaGanhar)
        edShared.commit()
    }
    fun openConfig() {
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        voleiConfig.nomePartida = sp.getString("nomePartida","Jogo Padrão").toString()
        voleiConfig.comTemporizador = sp.getBoolean("comTemporizador",false)
        voleiConfig.pontosPorSet = sp.getInt("pontos", voleiConfig.pontosPorSet)
        voleiConfig.qtdSetParaGanhar = sp.getInt("sets", voleiConfig.qtdSetParaGanhar)
    }
    fun initInterface(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        tv.setText(voleiConfig.nomePartida)
    }
    fun updatePlacarConfig(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        voleiConfig.nomePartida = tv.text.toString()

        val  tePoints = findViewById<EditText>(R.id.etPontosPorSet)
        voleiConfig.pontosPorSet = tePoints.text.toString().toInt()

        val etSets = findViewById<EditText>(R.id.etSets)
        voleiConfig.qtdSetParaGanhar = etSets.text.toString().toInt()
    }
    fun openPlacar(v: View){ //Executa ao click do Iniciar Jogo
        updatePlacarConfig() //Pega da Interface e joga no placar
        saveConfig() //Salva no Shared preferences

        val intent = Intent(this, PlacarActivity::class.java).apply{
            putExtra("voleiConfig", voleiConfig)
        }
        startActivity(intent)
    }
}