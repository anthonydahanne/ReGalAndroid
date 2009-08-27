/*
 * G2Android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.g2android.activity;

import java.util.List;

import net.dahanne.android.g2android.activity.ShowGallery.ImageAdapter;
import net.dahanne.android.g2android.model.G2Picture;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * This class is NOT currently used
 * 
 * @author Anthony Dahanne
 * 
 */
public class AlbumGallery extends Activity implements OnClickListener {
	private static final String TAG = "AlbumGallery";
	private Context mContext;
	private List<ImageView> mImageViews;
	private List<G2Picture> albumPictures;
	private ImageAdapter imageAdapter;

	public void onClick(View v) {

	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// Album album = (Album) getIntent().getSerializableExtra(
	// "net.dahanne.android.g2android.Album");
	// setContentView(R.layout.albumgallery);
	//
	// HashMap<String, String> imagesProperties = G2Utils.fetchImages(Settings
	// .getGalleryUrl(this), album.getName());
	// albumPictures = new ArrayList<G2Picture>();
	// albumPictures.addAll(G2Utils
	// .extractG2PicturesFromProperties(imagesProperties));
	// mImageViews = new ArrayList<ImageView>();
	//
	// // drawables = getDrawablesFromG2Pictures(albumPictures);
	//
	// GridView gridview = (GridView) findViewById(R.id.gridview);
	// imageAdapter = new ImageAdapter(this);
	// gridview.setAdapter(imageAdapter);
	//
	// Log.d(TAG, album.toString());
	//
	// }
	//
	// public class ImageAdapter extends BaseAdapter {
	//
	// public ImageAdapter(Context c) {
	// mContext = c;
	// }
	//
	// public int getCount() {
	// return albumPictures.size();
	// }
	//
	// public Object getItem(int position) {
	// return albumPictures.get(position);
	// }
	//
	// public long getItemId(int position) {
	// return albumPictures.get(position).getId();
	// }
	//
	// // create a new ImageView for each item referenced by the Adapter
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ImageView imageView;
	// if (convertView == null) { // if it's not recycled, initialize some
	// // attributes
	// imageView = new ImageView(mContext);
	// imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	// imageView.setPadding(8, 8, 8, 8);
	// // mImageViews.add(imageView);
	// } else {
	// imageView = (ImageView) convertView;
	// // imageView.setAlpha(125);
	//
	// }
	// new DownloadImageTask().execute(Settings
	// .getBaseUrl(AlbumGallery.this)
	// + albumPictures.get(position).getThumbName(), imageView);
	// //
	// imageView.setImageBitmap(downloadImage(Settings.getBaseUrl(AlbumGallery.this)+
	// // albumPictures.get(position).getThumbName()));
	// imageView.setId(position);
	// imageView.setOnClickListener(AlbumGallery.this);
	// return imageView;
	// }
	//
	// }
	//
	// private class DownloadImageTask extends AsyncTask {
	// private ImageView imageView = null;
	//
	// @Override
	// protected Bitmap doInBackground(Object... urls) {
	// imageView = (ImageView) urls[1];
	// Log.i(TAG, "Downloading image in background " + (String) urls[0]);
	// return downloadImage((String) urls[0]);
	// }
	//
	// @Override
	// protected void onPostExecute(Object result) {
	// // for (ImageView imageView : mImageViews) {
	// // if(imageView.getDrawable()==null){
	// // imageView.setImageBitmap((Bitmap) result);
	// // break;
	// // }
	// // }
	// imageView.setImageBitmap((Bitmap) result);
	// // request to refresh the attached gridView
	// // imageAdapter.notifyDataSetChanged();
	// Log.i(TAG, "POST EXE " + imageAdapter.getCount());
	// }
	// }
	//
	// private Bitmap downloadImage(String urlTyped2) {
	// HttpURLConnection con = null;
	// URL url;
	// InputStream is = null;
	// try {
	// url = new URL(urlTyped2);
	// con = (HttpURLConnection) url.openConnection();
	// con.setReadTimeout(10000 /* milliseconds */);
	// con.setConnectTimeout(15000 /* milliseconds */);
	// con.setRequestMethod("GET");
	// con.setDoInput(true);
	// con.addRequestProperty("Referer", "http://blog.dahanne.net");
	// // Start the query
	// con.connect();
	// is = con.getInputStream();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// Bitmap bm = BitmapFactory.decodeStream(is);
	// return bm;
	// }
	//
	// private List<Drawable> getDrawablesFromG2Pictures(
	// List<G2Picture> albumPictures) {
	// URL url;
	// List<Drawable> drawables = new ArrayList<Drawable>();
	// for (G2Picture picture : albumPictures) {
	// InputStream is = null;
	// try {
	// url = new URL(Settings.getBaseUrl(this)
	// + picture.getThumbName());
	// HttpURLConnection conn = (HttpURLConnection) url
	// .openConnection();
	// conn.setDoOutput(true);
	// conn.setRequestMethod("GET");
	//
	// conn.setDoInput(true);
	// // conn.addRequestProperty("Referer",
	// // "http://blog.dahanne.net");
	// // Start the query
	// conn.connect();
	// is = conn.getInputStream();
	// drawables.add(Drawable.createFromStream(is, picture
	// .getThumbName()));
	// } catch (Exception e) {
	// // Log.d(G,e.getMessage());
	// }
	// }
	//
	// return drawables;
	// }
	//
	// @Override
	// public void onClick(View v) {
	//
	// Intent intent = new Intent(this, FullImage.class);
	// intent.putExtra("g2Picture", albumPictures.get(v.getId()));
	// startActivity(intent);
	//
	// }

}
