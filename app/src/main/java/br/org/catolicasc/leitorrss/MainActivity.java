package br.org.catolicasc.leitorrss;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private ListView lvPokemon;
    private RSSListAdapter rssListAdapter;
    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvPokemon = findViewById(R.id.lvPokemon);

        ParseJson pjson = new ParseJson();

        final DownloadDeDados downloadDeDados = new DownloadDeDados();
        downloadDeDados.execute("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json");

        lvPokemon = findViewById(R.id.lvPokemon);

        lvPokemon.setAdapter(rssListAdapter);
        lvPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("pokemon", pokemons.get(position));
                startActivity(intent);
            }
        });

    }

    private class DownloadDeDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadRSS(strings[0]);
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parâmetro é: " + s);
            ParseJson parseJson = new ParseJson();
            parseJson.parse(s);
            //ArrayAdapter<RSSEntry> arrayAdapter = new ArrayAdapter<>(
            //        MainActivity.this, R.layout.list_item, parseRSS.getAplicacoes());
           // rssListView.setAdapter(arrayAdapter);
            RSSListAdapter rssListAdapter = new RSSListAdapter(MainActivity.this,
                    R.layout.list_complex_item, parseJson.getPokemons());
            lvPokemon.setAdapter(rssListAdapter);
            pokemons = (ArrayList<Pokemon>) parseJson.getPokemons();

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
