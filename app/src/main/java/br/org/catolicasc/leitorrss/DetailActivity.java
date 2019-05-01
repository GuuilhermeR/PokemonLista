package br.org.catolicasc.leitorrss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView tvId;
    private TextView tvNome;
    private TextView tvPeso;
    private TextView tvAltura;
    private ImageView imvPokemon;
    private Bitmap imagem;

    public void voltar(View view) {
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvId = findViewById(R.id.tvID);
        tvNome = findViewById(R.id.tvNome);
        tvPeso = findViewById(R.id.tvPeso);
        tvAltura = findViewById(R.id.tvAltura);
        imvPokemon = findViewById(R.id.imageView2);

        Intent intent = getIntent();
        Pokemon p = (Pokemon) intent.getSerializableExtra("pokemon");

        tvId.setText(p.getId());
        tvNome.setText(p.getNome());
        tvPeso.setText(p.getPeso());
        tvAltura.setText(p.getAltura());

        ArrayList<Pokemon> pokemonArray = new ArrayList<>();

        RSSListAdapter pokemonAdapter = new RSSListAdapter(
                DetailActivity.this, R.layout.activity_detail, pokemonArray
        );

        imagem = pokemonAdapter.returnImagemTask(p.getImgUrl());

        imvPokemon.setImageBitmap(imagem);

    }
}