package br.org.catolicasc.leitorrss;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView rssListView;
    private ImageView imgView;
    private ListView lvPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssListView = findViewById(R.id.lvPokemon);

        Log.d(TAG, "onCreate: começando AsyncTask");
        DownloadDeDados downloadDeDados = new DownloadDeDados();
        downloadDeDados.execute("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json");
        Log.d(TAG, "onCreate: final do onCreate");

        lvPokemon = findViewById(R.id.lvPokemon);

        final ArrayList<String> pokemons = new ArrayList<>();

        pokemons.add("Pikachu");
        pokemons.add("Tokepi");
        pokemons.add("Ekans");
        pokemons.add("Raichu");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this, android.R.layout.simple_list_item_1, pokemons
        );

        lvPokemon.setAdapter(arrayAdapter);
        lvPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("nome", pokemons.get(position));
                startActivity(intent);
            }
        });
    }


    private class DownloadDeDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: começa com o parâmetro: " + strings[0]);
            String rssFeed = downloadRSS(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Erro baixando RSS");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parâmetro é: " + s);
            ParseJson parseJson = new ParseJson();
            parseJson.parse(s);
//            ArrayAdapter<RSSEntry> arrayAdapter = new ArrayAdapter<>(
//                MainActivity.this, R.layout.list_item, parseRSS.getAplicacoes()
//            );
//            rssListView.setAdapter(arrayAdapter);
            RSSListAdapter rssListAdapter = new RSSListAdapter(MainActivity.this,
                    R.layout.list_complex_item, parseJson.getPokemons());
            rssListView.setAdapter(rssListAdapter);
        }

        private String downloadRSS(String urlString) {
            StringBuilder xmlRSS = new StringBuilder();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int resposta = connection.getResponseCode();
                Log.d(TAG, "downloadRSS: O código de resposta foi: " + resposta);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                int charsLidos;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsLidos = reader.read(inputBuffer);
                    if (charsLidos < 0) {
                        break;
                    }
                    if (charsLidos > 0) {
                        xmlRSS.append(
                                String.copyValueOf(inputBuffer, 0, charsLidos));
                    }
                }
                reader.close();
                return xmlRSS.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadRSS: URL é inválida " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadRSS: Ocorreu um erro de IO ao baixar dados: "
                    + e.getMessage());
            }
            return null;
        }
    }
}
