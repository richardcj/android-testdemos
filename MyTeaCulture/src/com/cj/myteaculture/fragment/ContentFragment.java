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
 * 各主页面的Fragment内容
 * 
 * @author caojian
 * 
 */
@SuppressLint("ValidFragment")
public class ContentFragment extends BaseListFragment implements
		OnSingleClickListener {

	private static final String TAG = "ContentFragment===";
	/* ListView数据源 */
	private List<Map<String, Object>> pageDataList;
	/* 每个ListView数据源对应的URL */
	private String urlStr;
	/* fragment布局中ListView的适配器 */
	private ContentAdapter contentAdapter;
	/* 全局变量之图片缓存map */
	private Map<String, Bitmap> cacheImageMap;
	/* 请求数据加载的页数 */
	private int page = 1;
	/* 标识fragment是否是首页 1、首页 0、其它页 */
	private int fragmentIndex;	
	// ==============================一下是只有第一页才会出现的广告页
	/* 显示图片轮换的小圆点的父容器 */
	private RadioGroup mRadioGroup_fragment;
	/* 显示轮换图片的自定义ViewPager */
	private ImageRotationViewPager mImageRotationViewPager;
	/* ViewPager数据源 */
	private List<View> views;
	/* ViewPager适配器 */
	private AdvertisementAdapter advertisementAdapter;
	/* 轮换图片的父控件，为了确定其height */
	private RelativeLayout mAdsLayout_fragment;
	/* 轮换图片返回的json数据 */
	private List<Map<String, Object>> imagesJsondata;
	/* 轮换图片对应的文字描述数组 */
	private String[] arrtitles = new String[3];
	/* 显示轮换图片文字的控件 */
	private TextView mTitleView;
	/*当前轮换图片标识*/
	private int currentIndex=0;

	public ContentFragment() {
		fragmentIndex = 1;
	}

	/**
	 * 初始化构造方法
	 * 
	 * @param urlStr
	 *            请求的url
	 * @param list
	 *            请求url对应的数据
	 * @param cacheImageMap
	 *            图片缓存
	 */
	public ContentFragment(String urlStr, List<Map<String, Object>> list,
			Map<String, Bitmap> cacheImageMap) {
		// Log.i(TAG, "====list集合数据："+list.size());
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
	 * 初始化ListView数据源
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
		Log.i("测试滑动调用===", "diaoyong111");
		super.onCreateView(inflater, container, savedInstanceState);
		if (urlStr.equals("")) {
			/* 如果是第六界面，那么不显示ListView加载 */
			listView.setPullLoadEnable(false);
		}
		mAdsLayout_fragment = (RelativeLayout) view
				.findViewById(R.id.rl_frgcontent_container);
		mTitleView = (TextView) view.findViewById(R.id.tv_frgcontent_title);
		/* 1表示第一页：要显示轮换图片 0：表示其它页 */
		if (fragmentIndex == 1) {
			/* 加载轮换图片json数据 */
			new ContentTask(0).execute(UrlConfig.JSON_URL);
			mAdsLayout_fragment.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, getHeightViewPager()));

			/* 得到viewPager对象，并设置其子页面 */
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
			/* ViewPager滑动时的监听 */
			mImageRotationViewPager
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							/*记录当前选择图片的索引*/
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

			/* 得到RadioGroup，并创建按钮组集合 */
			mRadioGroup_fragment = (RadioGroup) view
					.findViewById(R.id.radio_frgcontent_showdot);
			for (int i = 0; i < mRadioGroup_fragment.getChildCount(); i++) {
				RadioButton radioButton = (RadioButton) mRadioGroup_fragment
						.getChildAt(i);
				radioButton.setTag(i);
				radioButton.setBackgroundResource(R.drawable.slide_image_dot2);
				/* 背景状态设置，去掉单选按钮前面的点颜色为空 */
				radioButton.setButtonDrawable(new ColorDrawable(
						Color.TRANSPARENT));
			}
			/* 单选按钮组被选中时的监听 */
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
			/* 默认第一页被选中 */
			mRadioGroup_fragment.getChildAt(0).performClick();

		} else {
			/* 其它页面隐藏轮换图片 */
			mAdsLayout_fragment.setVisibility(View.GONE);
		}

		listView.setXListViewListener(this);/* 设置监听由这个类完成 */
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

	/* 根据手机尺寸获得ViewPager高度 */
	private int getHeightViewPager() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int height = 0;
		height = display.getWidth() / 2;
		return height;
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {
		new ContentTask(2).execute(urlStr + "&page=" + ++page);
	}
	
	/**
	 * 轮换图片点击事件
	 */
	@Override
	public void onSingleClick() {
		View view = views.get(currentIndex);
		goContentActivity(view.getTag().toString());
	}
	

	/**
	 * 异步任务，访问网络数据或图片，并设置 ContentTask被作用于:
	 * 1、首页轮换图片的Json数据 2、各列表项数据中的图片
	 * 3、首页轮换图片的Bitmap资源 4、分页数据加载
	 * */
	private class ContentTask extends AsyncTask<String, Void, Object> {
		/* 显示图片的ImageView控件 */
		private ImageView imageView;
		/* 作为列表项图片缓存的网址字符串 */
		private String urlStr;
		/* 操作标记：0加载轮换图片资源，1：数据列表项中的图片 */
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
			case 0: // 首页轮换图片
				Log.i(TAG, "==获取轮换图片的json数据");
				// imageJsonDataHandler(bitmapByte, new String[] { "image_s",
				// "id", "title" }, "data");
				obj = bitmapByte;
				break;
			case 1: // 数据列表中的图片
				obj = bitmapByte;
				break;
			case 2: // 加载更多数据
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

			default: // 首页轮换图片的Bitmap资源
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
			case 1: // 数据列表项中的图片
				byte[] retByte = (byte[]) result;
				Bitmap bitmap = BitmapFactory.decodeByteArray(retByte, 0,
						retByte.length);
				imageView.setImageBitmap(bitmap);
				imageView.setVisibility(View.VISIBLE);
				cacheImageMap.put(urlStr, bitmap);
				break;
			case 2: // 加载更多数据
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) result;
				contentAdapter.addList(list);
				contentAdapter.notifyDataSetChanged();
				/* 如果list没有数据了，停止下拉加载 */
				if (list.size() == 0) {
					listView.setPullLoadEnable(false);
				}
				break;
			case 3: // //首页轮换图片(第1张)
				setImageBitmap(urlStr, (byte[]) result, 0);
				break;
			case 4: // 首页轮换图片(第2张)
				Log.i(TAG, "==每个输播图片的数据加载");
				setImageBitmap(urlStr, (byte[]) result, 1);
				break;
			case 5: // 首页轮换图片(第3张)
				Log.i(TAG, "==每个输播图片的数据加载");
				setImageBitmap(urlStr, (byte[]) result, 2);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 首页轮换图片的json数据解析处理
	 * 
	 * @param result
	 *            网络请求返回的字节数组
	 * @param arrCulumn
	 *            需要的数据列名
	 * @param key
	 *            json数据对应的key
	 */
	private void imageJsonDataHandler(byte[] result, String[] arrCulumn,
			String key) {
		/* 返回的json字符串 */
		String retStr = null;
		/* json字符串中对应的图片地址 */
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
				/* 加载每一张图片的bitmap资源 */
				new ContentTask(3 + i, imageUrl).execute(imageUrl);
			} else {
				/* 加载缓存图片资源并刷新 */
				imageView.setImageBitmap(cacheImageMap.get(imageUrl));
				advertisementAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 设置首页轮换图片的bitmap资源
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
		// 设置第一页显示的图片和文字
		mRadioGroup_fragment.getChildAt(0).performClick();
		mTitleView.setText(arrtitles[0]);
	}	
	

	/**
	 * ListView数据源适配器
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
		 * 往集合里添加list数据
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

			/* 判断是否给图片留[位置] */
			if (wap_thumb == null || wap_thumb.equals("")) {
				viewHolder.wap_thumb.setVisibility(View.GONE);
			} else {
				viewHolder.wap_thumb.setVisibility(View.INVISIBLE);
			}
			/* 如果图片缓存为空，加载图片数据 */
			if (cacheImageMap.get(wap_thumb) == null) {
				/* 异步加载图片资源 */
				// Log.i(TAG, "===赋值给urlStr:"+urlStr);
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
		 * ListView数据项类
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
