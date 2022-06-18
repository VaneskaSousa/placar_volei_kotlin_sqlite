package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vaneska Sousa.
 */

public class CriaBanco extends SQLiteOpenHelper {
    /*
     * Aqui você cria as constantes sobre o banco de dados, como nome, tabela, id e versão.
     * O construtor CriaBanco irá criar o banco automaticamente com os dados passados
     */
    public static final String NOME_BANCO = "banco.db";
    public static final String TABELA = "partida";
    public static final String ID = "_id";
    public static final Integer VERSAO = 1;

    /*
     * Essa parte é opcional, a ideia aqui é voce criar o nome das colunas, afim de so mudar o nome depois
     * porem voce pode colocar esses dados direto no codigo dentro do metodo onCreate
     */
    public static final String COLUNA_1 = "nome_partida";
    public static final String COLUNA_2 = "nome_time_A";
    public static final String COLUNA_3 = "nome_time_B";
    public static final String COLUNA_4 = "pontos_por_set";
    public static final String COLUNA_5 = "total_time_a";
    public static final String COLUNA_6 = "total_time_b";
    public static final String COLUNA_7 = "vencedor";


    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE"+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + COLUNA_1 + " TEXT,"
                + COLUNA_2 + " TEXT,"
                + COLUNA_3 + " TEXT,"
                + COLUNA_4 + " INTEGER,"
                + COLUNA_5 + " INTEGER,"
                + COLUNA_6 + " INTEGER,"
                + COLUNA_7 + " text,"
                +")";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}