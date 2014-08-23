package com.example.android.BluetoothChat;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;



import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemListBaseAdapter extends ArrayAdapter<Profile> {

	private Activity activity;
	private List<Profile> items;
	private Profile objBean;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public ItemListBaseAdapter(Activity act, int resource, List<Profile> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvTitle = (TextView) view.findViewById(R.id.tvtitle);
		holder.tvDesc = (TextView) view.findViewById(R.id.tvdesc);
		holder.tvDate = (TextView) view.findViewById(R.id.tvdate);
		holder.imgView = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

		if (holder.tvTitle != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tvTitle.setText(Html.fromHtml(objBean.getName()));
		}
		if (holder.tvDesc != null && null != objBean.getEmail()
				&& objBean.getEmail().trim().length() > 0) {
			holder.tvDesc.setText(Html.fromHtml(objBean.getEmail()));
		}
		if (holder.tvDate != null && null != objBean.getMobilePhoneNum()
				&& objBean.getMobilePhoneNum().trim().length() > 0) {
			holder.tvDate.setText(Html.fromHtml(objBean.getMobilePhoneNum()));
		}
		if (holder.imgView != null) {
			if (null != objBean.getPhotoLink()
					&& objBean.getPhotoLink().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(activity));
				imageLoader.displayImage(objBean.getPhotoLink(), holder.imgView,
						options, new ImageLoadingListener() {
							@Override
							public void onLoadingComplete() {
								pbar.setVisibility(View.INVISIBLE);

							}

							@Override
							public void onLoadingFailed() {
								pbar.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onLoadingStarted() {
								pbar.setVisibility(View.VISIBLE);

							}
						});

			} else {
				holder.imgView.setImageResource(R.drawable.profile);
			}
		}

		return view;
	}

	public class ViewHolder {

		public TextView tvTitle, tvDesc, tvDate;
		private ImageView imgView;
		private ProgressBar pbar;

	}

}