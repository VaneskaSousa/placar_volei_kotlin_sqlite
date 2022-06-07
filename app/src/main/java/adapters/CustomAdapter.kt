package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import data.VoleiPlacar
import ufc.smd.esqueleto_placar.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun getDate(milliSeconds: Long, dateFormat: String?): String? {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.getTime())
}

class CustomAdapter(private val mList: ArrayList<VoleiPlacar>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    // Criação de Novos ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // infla o card_previous_games
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_previous_game, parent, false)

        return ViewHolder(view)
    }

    // Ligando o Recycler view a um View Holder
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = ItemView.findViewById(R.id.imageview)
        val tvNomePartida: TextView = ItemView.findViewById(R.id.tvNomePartida)
        val tvDataPartida: TextView = ItemView.findViewById(R.id.tvDataPartida)
        val tvResultadoJogo: TextView = ItemView.findViewById(R.id.tvResultadoJogo)
        val lnCell: LinearLayout = ItemView.findViewById(R.id.lnCell)
    }

    // faz o bind de uma ViewHolder a um Objeto da Lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placarAnterior = mList[position]

        //alimentando os elementos a partir do objeto placar
        holder.tvNomePartida.text = placarAnterior.NomePartida
        holder.tvResultadoJogo.text =
            placarAnterior.NomeTimeA + " " +
            placarAnterior.setsTimeA + " x " +
            placarAnterior.setsTimeB + " " +
            placarAnterior.NomeTimeB

        holder.tvDataPartida.text = getDate(placarAnterior.dataJogo, "dd/MM/yyyy hh:mm:ss")

        holder.lnCell.setOnClickListener{
            val duration= Snackbar.LENGTH_LONG
            val text = "Duração do jogo: " + placarAnterior.tempoDeJogo

            val snack= Snackbar.make(holder.lnCell,text,duration)
            snack.show()

        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
}
