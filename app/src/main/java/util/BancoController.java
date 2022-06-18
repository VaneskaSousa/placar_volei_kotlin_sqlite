package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context){
        banco = new CriaBanco(context);
    }

    public String insereDados(String nome_partida, String nome_time_A, String nome_time_B, int pontos_por_set,
                             int total_time_a, int total_time_b, String vencedor){

        System.out.println("cheguei no insereDados");

        String mensagem = "vazio";

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();

        valores.put(CriaBanco.COLUNA_1, nome_partida);
        valores.put(CriaBanco.COLUNA_2, nome_time_A);
        valores.put(CriaBanco.COLUNA_3, nome_time_B);
        valores.put(CriaBanco.COLUNA_4, pontos_por_set);
        valores.put(CriaBanco.COLUNA_5, total_time_a);
        valores.put(CriaBanco.COLUNA_6, total_time_b);
        valores.put(CriaBanco.COLUNA_7, vencedor);

        resultado = db.insert(CriaBanco.TABELA, null, valores);
        db.close();

        if (resultado ==-1) {
            mensagem = "Erro ao inserir registro "+resultado;
        }else {
            mensagem = "Dados cadastrado com sucesso";
        }

        return mensagem;
    }
}
