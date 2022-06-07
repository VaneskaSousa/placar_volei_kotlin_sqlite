package ufc.smd.esqueleto_placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import data.VoleiConfig

class ConfigActivity : AppCompatActivity() {

    var voleiConfig : VoleiConfig = VoleiConfig("Nome", false)
    lateinit var etPoints : EditText
    lateinit var etSets : EditText
    lateinit var etNameTimeA : EditText
    lateinit var etNameTimeB : EditText
    lateinit var etNomePartida : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        /// GET REFERENCES FROM VIEW
        etPoints = findViewById<EditText>(R.id.etPontosPorSet)
        etSets = findViewById<EditText>(R.id.etSets)
        etNameTimeA = findViewById<EditText>(R.id.editTextGameName2)
        etNameTimeB = findViewById<EditText>(R.id.editTextGameName3)
        etNomePartida = findViewById<EditText>(R.id.editTextGameName)

        /// SETUP BEHAVIORS
        etPoints.doAfterTextChanged { text ->
            var amount = text.toString()
            if(amount != "" && amount.toInt() <= 0)
                etPoints.setText("1")
        }
        etSets.doAfterTextChanged{ text ->
            var amount = text.toString()
            if(amount != "" && amount.toInt() <= 0)
                etSets.setText("1")
        }
        etPoints.setOnFocusChangeListener { view, b ->
            if(etPoints.text.toString() == "")
                etPoints.setText("1")
        }
        etSets.setOnFocusChangeListener { view, b ->
            if(etSets.text.toString() == "")
                etSets.setText("1")
        }
        etNameTimeA.setOnFocusChangeListener { view, b ->
            if(etNameTimeA.text.toString() == "")
                etNameTimeA.setText("Time A")
        }
        etNameTimeB.setOnFocusChangeListener { view, b ->
            if(etNameTimeB.text.toString() == "")
                etNameTimeB.setText("Time A")
        }
        etNomePartida.setText(voleiConfig.nomePartida)

        openConfig()
    }
    fun saveConfig(){
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var edShared = sp.edit()

        edShared.putString("nomePartida",voleiConfig.nomePartida)
        edShared.putBoolean("comTemporizador",voleiConfig.comTemporizador)
        edShared.putInt("pontos", voleiConfig.pontosPorSet)
        edShared.putInt("sets", voleiConfig.qtdSetParaGanhar)
        edShared.putString("nomeTimeA", voleiConfig.nomeTimeA)
        edShared.putString("nomeTimeB", voleiConfig.nomeTimeB)

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
    fun updatePlacarConfig(){
        voleiConfig.nomePartida = etNomePartida.text.toString()
        voleiConfig.pontosPorSet = etPoints.text.toString().toInt()
        voleiConfig.qtdSetParaGanhar = etSets.text.toString().toInt()
        voleiConfig.nomeTimeA = etNameTimeA.text.toString()
        voleiConfig.nomeTimeB = etNameTimeB.text.toString()
        voleiConfig.dataInicioJogo = System.currentTimeMillis()
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