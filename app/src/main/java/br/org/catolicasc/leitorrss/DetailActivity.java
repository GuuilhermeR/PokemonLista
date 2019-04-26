package br.org.catolicasc.leitorrss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView tvPokemon;

    public void voltar(View view) {
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvPokemon = findViewById(R.id.tvID);
        tvPokemon = findViewById(R.id.tvNome);
        tvPokemon = findViewById(R.id.tvPeso);
        tvPokemon = findViewById(R.id.tvAltura);

        Intent intent = getIntent();
        tvPokemon.setText(intent.getStringExtra("id"));


    }
}
