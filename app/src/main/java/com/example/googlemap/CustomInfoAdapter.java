package com.example.googlemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mwindow;
    private Context mcontext;

    public CustomInfoAdapter(Context mcontext) {
        this.mcontext = mcontext;
        mwindow= LayoutInflater.from(mcontext).inflate(R.layout.custom_locinfo,null);
    }

  private void rendowWindowText(Marker marker,View view)
  {
      String title=marker.getTitle();
      TextView title_txt=(TextView) view.findViewById(R.id.title_place);
      Toast.makeText(view.getContext(), "k"+title,Toast.LENGTH_SHORT).show();

      if(!title.equals(""))
      {
          title_txt.setText(title);
      }
      String snippet=marker.getSnippet();
      TextView tvSnippet=(TextView) view.findViewById(R.id.info);

      if(!snippet.equals(""))
      {
          tvSnippet.setText(snippet);
      }
  }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        rendowWindowText(marker,mwindow);
        return mwindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        rendowWindowText(marker,mwindow);

        return mwindow;
    }
}
