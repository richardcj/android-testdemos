package com.cj.myteaculture.fragment;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.R.raw;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cj.myteaculture.activity.R;
import com.cj.myteaculture.adapter.AdvertisementAdapter;
import com.cj.myteaculture.config.UrlConfig;
import com.cj.myteaculture.fragment.base.BaseListFragment;
import com.cj.myteaculture.util.HttpClientHelper;
import com.cj.myteaculture.util.JsonHelper;
import com.cj.myteaculture.widget.ImageRotationViewPager;
import com.cj.myteaculture.widget.ImageRotationViewPager.OnSingleClickListener;

/**
 * ����ҳ���Fragment����
 * 
 * @author caojian
 * 
 */
@SuppressLint("ValidFragment")
public class ContentFragment extends BaseListFragment implements
		OnSingleClickListener {

	private static final String TAG = "ContentFragment===";
	/* ListView����Դ */
	private List<Map<String, Object>> pageDataList;
	/* ÿ��ListView����Դ��Ӧ��URL */
	private String urlStr;
	/* fragment������ListView�������� */
	private ContentAdapter contentAdapter;
	/* ȫ�ֱ���֮ͼƬ����map */
	private Map<String, Bitmap> cacheImageMap;
	/* �������ݼ��ص�ҳ�� */
	private int page = 1;
	/* ��ʶfragment�Ƿ�����ҳ 1����ҳ 0������ҳ */
	private int fragmentIndex;	
	// ==============================һ����ֻ�е�һҳ�Ż���ֵĹ��ҳ
	/* ��ʾͼƬ�ֻ���СԲ��ĸ����� */
	private RadioGroup mRadioGroup_fragment;
	/* ��ʾ�ֻ�ͼƬ���Զ���ViewPager */
	private ImageRotationViewPager mImageRotationViewPager;
	/* ViewPager����Դ */
	private List<View> views;
	/* ViewPager������ */
	private AdvertisementAdapter advertisementAdapter;
	/* �ֻ�ͼƬ�ĸ��ؼ���Ϊ��ȷ����height */
	private RelativeLayout mAdsLayout_fragment;
	/* �ֻ�ͼƬ���ص�json���� */
	private List<Map<String, Object>> imagesJsondata;
	/* �ֻ�ͼƬ��Ӧ�������������� */
	private String[] arrtitles = new String[3];
	/* ��ʾ�ֻ�ͼƬ���ֵĿؼ� */
	private TextView mTitleView;
	/*��ǰ�ֻ�ͼƬ��ʶ*/
	private int currentIndex=0;

	public ContentFragment() {
		fragmentIndex = 1;
	}

	/**
	 * ��ʼ�����췽��
	 * 
	 * @param urlStr
	 *            �����url
	 * @param list
	 *            ����url��Ӧ������
	 * @param cacheImageMap
	 *            ͼƬ����
	 */
	public ContentFragment(String urlStr, List<Map<String, Object>> list,
			Map<String, Bitmap> cacheImageMap) {
		// Log.i(TAG, "====list�������ݣ�"+list.size());
		this.urlStr = urlStr;
		this.pageDataList = list;
		this.cacheImageMap = cacheImageMap;
		if (urlStr.equals(UrlConfig.JSON_LIST_DATA_0)) {
			fragmentIndex = 1;
		} else {
			fragmentIndex = 0;
		}
	}

	/**
	 * ��ʼ��ListView����Դ
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (pageDataList == null) {
			pageDataList = new ArrayList<Map<String, Object>>();
		}
		contentAdapter = new ContentAdapter(getActivity(), pageDataList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("���Ի�������===", "diaoyong111");
		super.onCreateView(inflater, container, savedInstanceState);
		if (urlStr.equals("")) {
			/* ����ǵ������棬��ô����ʾListView���� */
			listView.setPullLoadEnable(false);
		}
		mAdsLayout_fragment = (RelativeLayout) view
				.findViewById(R.id.rl_frgcontent_container);
		mTitleView = (TextView) view.findViewById(R.id.tv_frgcontent_title);
		/* 1��ʾ��һҳ��Ҫ��ʾ�ֻ�ͼƬ 0����ʾ����ҳ */
		if (fragmentIndex == 1) {
			/* �����ֻ�ͼƬjson���� */
			new ContentTask(0).execute(UrlConfig.JSON_URL);
			mAdsLayout_fragment.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, getHeightViewPager()));

			/* �õ�viewPager���󣬲���������ҳ�� */
			mImageRotationViewPager = (ImageRotationViewPager) view
					.findViewById(R.id.vp_frgcontent_ImageRotation);
			mImageRotationViewPager.setOnSingleClickListener(this);
			
			views = new ArrayList<View>();
			for (int i = 0; i < 3; i++) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setImageResource(R.drawable.ic_launcher);
//				imageView.setOnClickListener(this);
				views.add(imageView);
			}
			advertisementAdapter = new AdvertisementAdapter(views);
			mImageRotationViewPager.setAdapter(advertisementAdapter);
			/* ViewPager����ʱ�ļ��� */
			mImageRotationViewPager
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							/*��¼��ǰѡ��ͼƬ������*/
							currentIndex=position;
							mRadioGroup_fragment.getChildAt(position)
									.performClick();
						}

						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {

						}

						@Override
						public void onPageScrollStateChanged(int state) {

						}
					});

			/* �õ�RadioGroup����������ť�鼯�� */
			mRadioGroup_fragment = (RadioGroup) view
					.findViewById(R.id.radio_frgcontent_showdot);
			for (int i = 0; i < mRadioGroup_fragment.getChildCount(); i++) {
				RadioButton radioButton = (RadioButton) mRadioGroup_fragment
						.getChildAt(i);
				radioButton.setTag(i);
				radioButton.setBackgroundResource(R.drawable.slide_image_dot2);
				/* ����״̬���ã�ȥ����ѡ��ťǰ��ĵ���ɫΪ�� */
				radioButton.setButtonDrawable(new ColorDrawable(
						Color.TRANSPARENT));
			}
			/* ��ѡ��ť�鱻ѡ��ʱ�ļ��� */
			mRadioGroup_fragment
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							RadioButton radioButton = (RadioButton) group
									.findViewById(checkedId);
							int index = (Integer) radioButton.getTag();
							mImageRotationViewPager.setCurrentItem(index);
							mTitleView.setText(arrtitles[index]);
						}
					});
			/* Ĭ�ϵ�һҳ��ѡ�� */
			mRadioGroup_fragment.getChildAt(0).performClick();

		} else {
			/* ����ҳ�������ֻ�ͼƬ */
			mAdsLayout_fragment.setVisibility(View.GONE);
		}

		listView.setXListViewListener(this);/* ���ü������������� */
		listView.setAdapter(contentAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String idStr = pageDataList.get((int) id).get("id").toString();
				goContentActivity(idStr);
			}
		});

		return view;
	}

	/* �����ֻ��ߴ���ViewPager�߶� */
	private int getHeightViewPager() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int height = 0;
		height = display.getWidth() / 2;
		return height;
	}

	/**
	 * ����ˢ��
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	/**
	 * ��������
	 */
	@Override
	public void onLoadMore() {
		new ContentTask(2).execute(urlStr + "&page=" + ++page);
	}
	
	/**
	 * �ֻ�ͼƬ����¼�
	 */
	@Override
	public void onSingleClick() {
		View view = views.get(currentIndex);
		goContentActivity(view.getTag().toString());
	}
	

	/**
	 * �첽���񣬷����������ݻ�ͼƬ�������� ContentTask��������:
	 * 1����ҳ�ֻ�ͼƬ��Json���� 2�����б��������е�ͼƬ
	 * 3����ҳ�ֻ�ͼƬ��Bitmap��Դ 4����ҳ���ݼ���
	 * */
	private class ContentTask extends AsyncTask<String, Void, Object> {
		/* ��ʾͼƬ��ImageView�ؼ� */
		private ImageView imageView;
		/* ��Ϊ�б���ͼƬ�������ַ�ַ��� */
		private String urlStr;
		/* ������ǣ�0�����ֻ�ͼƬ��Դ��1�������б����е�ͼƬ */
		private int flag;

		public ContentTask(int flag) {
			this.flag = flag;
		}

		public ContentTask(int flag, String urlStr) {
			this.flag = flag;
			this.urlStr = urlStr;
		}

		public ContentTask(ImageView imageView, String urlStr, int flag) {
			this.imageView = imageView;
			this.urlStr = urlStr;
			this.flag = flag;
		}

		@Override
		protected Object doInBackground(String... params) {
			Object obj = null;
			byte[] bitmapByte = HttpClientHelper
					.loadByteFromUrlByGet(params[0]);
			switch (flag) {
			case 0: // ��ҳ�ֻ�ͼƬ
				Log.i(TAG, "==��ȡ�ֻ�ͼƬ��json����");
				// imageJsonDataHandler(bitmapByte, new String[] { "image_s",
				// "id", "title" }, "data");
				obj = bitmapByte;
				break;
			case 1: // �����б��е�ͼƬ
				obj = bitmapByte;
				break;
			case 2: // ���ظ�������
				String retStr = null;
				try {
					retStr = new String(bitmapByte, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				List<Map<String, Object>> list = JsonHelper.jsonStringToList(
						retStr, new String[] { "title", "source", "nickname",
								"create_time", "wap_thumb", "id" }, "data");
				obj = list;
				break;

			default: // ��ҳ�ֻ�ͼƬ��Bitmap��Դ
				obj = bitmapByte;
				break;
			}
			return obj;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result == null) {
				return;
			}
			switch (flag) {
			case 0:
				byte[] retStr = (byte[]) result;
				imageJsonDataHandler(retStr, new String[] { "image_s", "id",
						"title" }, "data");
				break;
			case 1: // �����б����е�ͼƬ
				byte[] retByte = (byte[]) result;
				Bitmap bitmap = BitmapFactory.decodeByteArray(retByte, 0,
						retByte.length);
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
				cacheImageMap.put(urlStr, bitmap);
				break;
			case 2: // ���ظ�������
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) result;
				contentAdapter.addList(list);
				contentAdapter.notifyDataSetChanged();
				/* ���listû�������ˣ�ֹͣ�������� */
				if (list.size() == 0) {
					listView.setPullLoadEnable(false);
				}
				break;
			case 3: // //��ҳ�ֻ�ͼƬ(��1��)
				setImageBitmap(urlStr, (byte[]) result, 0);
				break;
			case 4: // ��ҳ�ֻ�ͼƬ(��2��)
				Log.i(TAG, "==ÿ���䲥ͼƬ�����ݼ���");
				setImageBitmap(urlStr, (byte[]) result, 1);
				break;
			case 5: // ��ҳ�ֻ�ͼƬ(��3��)
				Log.i(TAG, "==ÿ���䲥ͼƬ�����ݼ���");
				setImageBitmap(urlStr, (byte[]) result, 2);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * ��ҳ�ֻ�ͼƬ��json���ݽ�������
	 * 
	 * @param result
	 *            �������󷵻ص��ֽ�����
	 * @param arrCulumn
	 *            ��Ҫ����������
	 * @param key
	 *            json���ݶ�Ӧ��key
	 */
	private void imageJsonDataHandler(byte[] result, String[] arrCulumn,
			String key) {
		/* ���ص�json�ַ��� */
		String retStr = null;
		/* json�ַ����ж�Ӧ��ͼƬ��ַ */
		String imageUrl = null;
		try {
			retStr = new String(result, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		imagesJsondata = JsonHelper.jsonStringToList(retStr, arrCulumn, key);
		Log.i(TAG, "====imagesJsondata:" + imagesJsondata.size());
		for (int i = 0; i < imagesJsondata.size(); i++) {
			imageUrl = imagesJsondata.get(i).get("image_s").toString();
			String id = imagesJsondata.get(i).get("id").toString();
			String title = imagesJsondata.get(i).get("title").toString();
			arrtitles[i] = title;
			ImageView imageView = (ImageView) views.get(i);
			imageView.setTag(id);
			Log.i(TAG, "imageUrl=" + imageUrl);
			if (cacheImageMap.get(imageUrl) == null) {
				/* ����ÿһ��ͼƬ��bitmap��Դ */
				new ContentTask(3 + i, imageUrl).execute(imageUrl);
			} else {
				/* ���ػ���ͼƬ��Դ��ˢ�� */
				imageView.setImageBitmap(cacheImageMap.get(imageUrl));
				advertisementAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * ������ҳ�ֻ�ͼƬ��bitmap��Դ
	 * 
	 * @param imageUrl
	 * @param result
	 * @param i
	 */
	private void setImageBitmap(String imageUrl, byte[] result, int i) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
		ImageView imageView = (ImageView) views.get(i);
		imageView.setImageBitmap(bitmap);
		cacheImageMap.put(imageUrl, bitmap);
		advertisementAdapter.notifyDataSetChanged();
		// ���õ�һҳ��ʾ��ͼƬ������
		mRadioGroup_fragment.getChildAt(0).performClick();
		mTitleView.setText(arrtitles[0]);
	}	
	

	/**
	 * ListView����Դ������
	 * 
	 * @author caojian
	 * 
	 */
	private class ContentAdapter extends BaseAdapter {

		private Context context;
		private List<Map<String, Object>> pageDataList;

		public ContentAdapter(Context context,
				List<Map<String, Object>> pageDataList) {
			this.context = context;
			this.pageDataList = pageDataList;
		}

		/**
		 * �����������list����
		 * 
		 * @param list
		 */
		public void addList(List<Map<String, Object>> list) {
			pageDataList.addAll(list);
		}

		@Override
		public int getCount() {
			return pageDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return pageDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_frgcontent_listview, null);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.tv_frgcontent_itemtitle);
				viewHolder.source = (TextView) convertView
						.findViewById(R.id.tv_frgcontent_itemSource);
				viewHolder.nickname = (TextView) convertView
						.findViewById(R.id.tv_frgcontent_itemNickName);
				viewHolder.create_time = (TextView) convertView
						.findViewById(R.id.tv_frgcontent_itemCreateTime);
				viewHolder.wap_thumb = (ImageView) convertView
						.findViewById(R.id.iv_frgcontent_thumb);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Map<String, Object> map = pageDataList.get(position);
			String title = (String) map.get("title");
			String source = (String) map.get("source");
			String nickname = (String) map.get("nickname");
			String create_time = (String) map.get("create_time");
			String wap_thumb = map.get("wap_thumb").toString();

			viewHolder.title.setText(title);
			viewHolder.source.setText(source);
			viewHolder.nickname.setText(nickname);
			viewHolder.create_time.setText(create_time);

			/* �ж��Ƿ��ͼƬ��[λ��] */
			if (wap_thumb == null || wap_thumb.equals("")) {
				viewHolder.wap_thumb.setVisibility(View.GONE);
			} else {
				viewHolder.wap_thumb.setVisibility(View.INVISIBLE);
			}
			/* ���ͼƬ����Ϊ�գ�����ͼƬ���� */
			if (cacheImageMap.get(wap_thumb) == null) {
				/* �첽����ͼƬ��Դ */
				// Log.i(TAG, "===��ֵ��urlStr:"+urlStr);
				new ContentTask(viewHolder.wap_thumb, wap_thumb, 1)
						.execute(wap_thumb);
			} else {
				viewHolder.wap_thumb.setImageBitmap(cacheImageMap
						.get(wap_thumb));
				viewHolder.wap_thumb.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		/**
		 * ListView��������
		 * 
		 * @author caojian
		 * 
		 */
		public class ViewHolder {
			private TextView title;
			private TextView source;
			private TextView nickname;
			private TextView create_time;
			private ImageView wap_thumb;
		}

	}


	
}
