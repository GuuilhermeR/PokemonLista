package br.org.catolicasc.leitorrss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class RSSListAdapter extends ArrayAdapter {
    private static final String TAG = "RSSListAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater; // Faz conversão layout resource em objeto
    private List<RSSEntry> aplicativos; //Lista que passa pro parametro la na aplicação.

    public RSSListAdapter(Context context, int resource, List<RSSEntry> aplicativos) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.aplicativos = aplicativos;
    }

    @Override
    public int getCount() {
        return aplicativos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG, "getView: chamada com um convertView null");
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: recebeu um convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RSSEntry appAtual = aplicativos.get(position); //muda

        viewHolder.tvNome.setText(appAtual.getNome()); //muda
        viewHolder.tvArtista.setText(appAtual.getArtista()); //muda
        viewHolder.tvData.setText(appAtual.getDataLancamento()); //muda

        new DownloadImageTask(viewHolder.ivAppImg).execute(appAtual.getUrlImagem());

        return convertView;
    }

    private class ViewHolder { //mudar os componentes para o pokemon
        final TextView tvNome;
        final TextView tvArtista;
        final TextView tvData;
        final ImageView ivAppImg;

        ViewHolder(View v) {
            this.tvNome = v.findViewById(R.id.tvNome);
            this.tvArtista = v.findViewById(R.id.tvArtista);
            this.tvData = v.findViewById(R.id.tvData);
            this.ivAppImg = v.findViewById(R.id.ivAppImg);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
