package com.zh_weir.videoplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class VideoPlayerArc extends Activity  implements OnClickListener, OnBufferingUpdateListener, OnPreparedListener, OnCompletionListener{

		ImageView btnplay;
        SurfaceView surfaceView;
        MediaPlayer mediaPlayer;
        VideoView vv;
        int position;
        private Activity activity=this;
		private SurfaceHolder surfaceHolder;
		private int width;
		private int height;
		private ArrayList<Particle> particles=new ArrayList<Particle>();
        final int particle_count = 1;
		private int itemWidth;
		private int itemHeight;
		private int nbWidth;
		private int nbHeight;
		private int bitWidth;
		private int bitHeight;
		private int itemScreenWidth;
		private int itemScreenHeight;
		private double scale;
		public static int IMAGE_ENGINE_CORETHREAD = 5; // 可以同步加载图片的个数
		public static int IMAGE_ENGINE_COMPRESS_QUALITY = 100; // 对图片的压缩比例0~100数值约大越清晰，越小越模糊
																// Can slow
																// ImageLoader, use
																// it carefully
																// (Better don't use
																// it)
		public static int IMAGE_ENGINE_FREQ_LIMITED_MEMECACHE = 2 * 1024 * 1024; // 设置缓存内存的大小,在此设置2M的缓存
		public static int IMAGE_ENGINE_SCALE_FOR_SDCARD = 500; // 缓存图片的大小
		public static int IMAGE_ENGINE_SCALE_FOR_MEMECACHE = 500;
        public static final String IMAGE_ENGIN_PATH = "test/cache"; // 图片持久化的位置(本地sdcard和机身ROM)
        
        
    	public static DisplayImageOptions getDisplayImageOptions(int uri,
    			int display) {
    		DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions.Builder() // 圆角边处理的头像
    				.cacheInMemory(true) // 缓存到内存，设置则缓存到内存
    				.cacheOnDisc(true) // 缓存到本地磁盘,设置则缓存到磁盘
    				.showImageForEmptyUri(uri) // 设置尚未加载，或者无图片的默认图片
    				.showImageOnFail(uri) // 设置加载图片失败时的默认图片
    				.showImageOnLoading(uri) //设置加载中的图片
    				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 
    				.displayer(new FadeInBitmapDisplayer(3000)) 
    	//			.displayer(new RoundedBitmapDisplayer(display)) // 可以继承BitmapDisplayer接口来实现bitmap的其他特效，再此是实现圆角边特效
    				.build();
    		return AVATAR_OPTIONS;
    	}
    	
    	public static DisplayImageOptions getDisplayImageOptions2(int uri,
    			int display) {
    		DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions.Builder() // 圆角边处理的头像
    				.cacheInMemory(true) // 缓存到内存，设置则缓存到内存
    				.cacheOnDisc(true) // 缓存到本地磁盘,设置则缓存到磁盘
    				.showImageForEmptyUri(uri) // 设置尚未加载，或者无图片的默认图片
    				.showImageOnFail(uri) // 设置加载图片失败时的默认图片
    				.showImageOnLoading(uri) //设置加载中的图片
    				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 
    				.displayer(new FadeInBitmapDisplayer(3000)) 
    				.displayer(new RoundedVignetteBitmapDisplayer(15,0)) // 可以继承BitmapDisplayer接口来实现bitmap的其他特效，再此是实现圆角边特效
    				.build();
    		return AVATAR_OPTIONS;
    	}
        
        
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.user_video_play);   
                final GridView gv_Pic_List = (GridView) findViewById(R.id.gridView);  
               gv_Pic_List.setVisibility(View.INVISIBLE);
                final ImageView iv= (ImageView) this.findViewById(R.id.imageView);
//                WebView webView=(WebView) this.findViewById(R.id.webView);
//                webView.getSettings().setDefaultTextEncodingName("gbk");  
//                webView.setBackgroundColor(Color.TRANSPARENT);
//                webView.getBackground().setAlpha(2);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                ArrayList<Bitmap> picList = new ArrayList<Bitmap>(); 
                
                Bitmap picSelected = BitmapFactory.decodeResource(getResources(),R.drawable.img);  
                bitWidth = picSelected.getWidth();
                bitHeight = picSelected.getHeight();
                scale=bitHeight/(bitWidth*1.0);
                nbWidth=2;
                nbHeight=4;
                itemWidth = picSelected.getWidth() / nbWidth;  
                itemHeight = picSelected.getHeight() / nbHeight; 
                gv_Pic_List.setNumColumns(nbWidth);
                DisplayMetrics dm = new DisplayMetrics();
        		dm = getResources().getDisplayMetrics();
        		int screenWidth = dm.widthPixels;
        		int screenHeight = dm.heightPixels;
                
                itemScreenWidth = (int) (screenWidth*0.97 / nbWidth);    
                itemScreenHeight = (int) (screenWidth*scale / nbHeight);    
                for (int i = 1; i <= nbHeight; i++) {  
                    for (int j = 1; j <= nbWidth; j++) {  
                    Bitmap bitmap = Bitmap.createBitmap(picSelected, (j - 1) * itemWidth, (i - 1) * itemHeight, itemWidth, itemHeight);  
                    picList.add(bitmap);
                    }  
                }  
//
                final GridPicListAdapter gridAdapter = new GridPicListAdapter(this, picList);
                gv_Pic_List.setAdapter(gridAdapter); 

//		webView.loadDataWithBaseURL(
//				null,
//				"<head><script src='file:///android_asset/js/jquery.js'></script><script src='file:///android_asset/js/index.js'></script></head><body><canvas id='surface'></canvas></body>", "text/html", "UTF-8", null);
		//webView.loadUrl(url);  
     //         webView.loadUrl("http://www.baidu.com"); 
              iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.v("ting","onClick");
					 gv_Pic_List.setVisibility(View.VISIBLE);
					 iv.setVisibility(View.GONE);
					gridAdapter.notifyDataSetInvalidated();
					 initShareIntent(activity);
		           //   propertyValuesHolder(iv);
				}
			});
//                btnplay=(ImageView) this.findViewById(R.id.iv_video_control);
//            //    btnstop=(ImageButton)this.findViewById(R.id.btnplay);
//             //   btnpause=(ImageButton)this.findViewById(R.id.btnplay);
//                
//             //   btnstop.setOnClickListener(this);
//                btnplay.setOnClickListener(this);
//             //   btnpause.setOnClickListener(this);
//                
//                mediaPlayer=new MediaPlayer();\
                File cacheDir = StorageUtils.getOwnCacheDirectory(activity,
        				IMAGE_ENGIN_PATH); 
            	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
        				activity)
        				// .memoryCacheExtraOptions(, ) ///设置缓存图片时候的宽高最大值，默认为屏幕宽高
        		//尝试 无压缩 无限制大小
//        				.discCacheExtraOptions(IMAGE_ENGINE_SCALE_FOR_SDCARD,
//        						IMAGE_ENGINE_SCALE_FOR_SDCARD, CompressFormat.JPEG,
//        						IMAGE_ENGINE_COMPRESS_QUALITY, null)
        				// /设置硬盘缓存，默认格式jpeg，压缩质量
        				.threadPoolSize(IMAGE_ENGINE_CORETHREAD)
        				// 设置线程池的最高线程数量
        				.threadPriority(Thread.NORM_PRIORITY - 1)
        				// 设置线程优先级
        				.denyCacheImageMultipleSizesInMemory()
        				// //自动缩放
        				.memoryCache(
        						new UsingFreqLimitedMemoryCache(
        								IMAGE_ENGINE_FREQ_LIMITED_MEMECACHE))
        				// //设置缓存大小
        				.discCache(new UnlimitedDiskCache(cacheDir))
        				// //设置硬盘缓存
        				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
        				.build();
        		// Initialize ImageLoader with configuration.
        		ImageLoader.getInstance().init(config);
                
        		DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions.Builder() // 圆角边处理的头像
						.cacheInMemory(true) // 缓存到内存，设置则缓存到内存
						.cacheOnDisc(true) // 缓存到本地磁盘,设置则缓存到磁盘
					//	.showImageForEmptyUri(uri) // 设置尚未加载，或者无图片的默认图片
				//		.showImageOnFail(uri) // 设置加载图片失败时的默认图片
		//				.showImageOnLoading(uri) //设置加载中的图片
				//		.displayer(new RoundedBitmapDisplayer(display)) // 可以继承BitmapDisplayer接口来实现bitmap的其他特效，再此是实现圆角边特效
						.build();
        		DisplayImageOptions dio = getDisplayImageOptions(R.drawable.home_my_image_default,5);
        		DisplayImageOptions dio2 = getDisplayImageOptions2(R.drawable.home_my_image_default,5);
         //       ImageView imageView1 = (ImageView) this.findViewById(R.id.imageView1);
                
              // imageView1.getScaleType()
         //       ImageView imageView2 = (ImageView) this.findViewById(R.id.imageView2);
               //"http://staticnova.ruoogle.com/default/photonosee.jpg?imageMogr2/rotate/90"
                String url = "http://f.aiwaya.cn/group1/M00/07/D2/cHxxDlRcmbeAdQzoAAB_nLFCPT8260.jpg";
           //    ImageLoader.getInstance().displayImage(url, imageView1, dio2);
            //    ImageLoader.getInstance().displayImage(url, imageView2, dio2);
                
//                surfaceView=(SurfaceView) this.findViewById(R.id.surfaceView);
//                surfaceHolder =surfaceView.getHolder();
                
//                
//                Log.v("msg", width+" "+(surfaceHolder==null));
//                particles=new ArrayList<Particle>();
//                
//
//				this.findViewById(R.id.rl_video_control).setVisibility(View.INVISIBLE);
//                surfaceHolder.addCallback(new Callback() {
//
//					@Override
//					public void surfaceCreated(SurfaceHolder holder) {
//						// TODO Auto-generated method stub
//			        	 	width=surfaceView.getWidth();
//			                height=surfaceView.getHeight();
//			                Log.v("msg", width+" "+height);
//			                for(int i = 0; i < particle_count; i++) {
//			                	particles.add(new Particle(width,height));
//			              	}
//			        	 	mHandler.sendEmptyMessage(1);
//					}
//
//					@Override
//					public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void surfaceDestroyed(SurfaceHolder holder) {
//						// TODO Auto-generated method stub
//						
//					}});
      //          this.findViewById(R.id.rl_video_control).setVisibility(View.INVISIBLE);
       // 	 	mHandler.sendEmptyMessage(1);
                
//                Log.v("msg", ( surfaceView==null)+"");
//                String path = "http://staticnova.ruoogle.com/video/2644197_avatar_201410171200001";
//                //设置SurfaceView自己不管理的缓冲区
//                surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);                
//                surfaceView.getHolder().addCallback(new Callback() {                
//                        @Override
//                        public void surfaceDestroyed(SurfaceHolder holder) {
//                
//                        }
//                
//                        @Override
//                        public void surfaceCreated(SurfaceHolder holder) {
//                                if (position>0) {
//                                        try {
//                                                //开始播放
//                                                play();
//                                                //并直接从指定位置开始播放
//                                                mediaPlayer.seekTo(position);
//                                                position=0;                                                
//                                        } catch (Exception e) {
//                                                // TODO: handle exception
//                                        }
//                                }
//                        }                        
//                        @Override
//                        public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                                        int height) {
//
//                        }
//                });                
        }
        
        

        public Handler mHandler=new Handler(){
        	
        	
        	@Override
    		public void handleMessage(Message msg) {
        		
        		
        		switch (msg.what) {
				case 1:
				//	ClearDraw();
					SimpleDraw();
				//	mHandler.sendEmptyMessageDelayed(1,20);
					break;

				default:
					break;
				}
        		
        	}
        	
        }; 
        
        
        
        void SimpleDraw() {
            Canvas canvas = surfaceHolder.lockCanvas(new Rect(0, 0, width,height));// 关键:获取画布
            canvas.drawColor(Color.BLACK);
            Paint mPaint = new Paint();
       //     mPaint.setColor(Color.GREEN);// 画笔为绿色
        //    mPaint.setStrokeWidth(2);// 设置画笔粗细
     
         //   Log.v("msg","opacity "+ particles.size()+(canvas==null));
            for(int i = 0; i < particles.size(); i++)
    		{
    			 Particle p = particles.get(i);
            
    			 

     			p.opacity = p.death*255/p.life;
     			Log.v("msg","opacity "+p.g);
    			 int colorS=Color.argb(255,p.r,p.g,p.b);
                 int colorE=Color.argb(p.opacity,p.r,p.g,p.b);
    	            
    	     //     RadialGradient rg=new RadialGradient(p.locationX, p.locationY,p.radius,colorS, colorE, Shader.TileMode.REPEAT);
    	          
    	          RadialGradient mRadialGradient = new RadialGradient(p.locationX, p.locationY,p.radius, new int[] {  
    	        		  colorS,colorE}, new float[] {  
    	    	        		  0,1},  
    	                  Shader.TileMode.REPEAT); 
    	          int[] glassGradientColors = new int[5]; 
    	          float[] glassGradientPositions = new float[5]; 
    	          int glassColor = 245; 
    	          int px = width/2; 
    	          int py = height/2; 
    	          int radius = Math.min(px, py)-2; 
    	          Point center = new Point(px, py); 
    	          RectF boundingBox = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius); 
    	          int ringWidth=30;
				  RectF innerBoundingBox = new RectF(center.x - radius + ringWidth, center.y - radius + ringWidth, center.x + radius - ringWidth, center.y + radius - ringWidth);    	          
    	          float innerRadius = innerBoundingBox.height()/2;
    	          glassGradientColors[4]=Color.argb(100,glassColor,glassColor, glassColor); 
    	          glassGradientColors[3]=Color.argb(65,glassColor,glassColor,glassColor); 
    	          glassGradientColors[2]=Color.argb(50,glassColor,glassColor, glassColor); 
    	          glassGradientColors[1]=Color.argb(30,glassColor,glassColor, glassColor); 
    	          glassGradientColors[0]=Color.argb(10,glassColor,glassColor, glassColor); 
    	          glassGradientPositions[4] = 1-0.0f; 
    	          glassGradientPositions[3] = 1-0.06f; 
    	          glassGradientPositions[2] = 1-0.40f; 
    	          glassGradientPositions[1] = 1-0.60f; 
    	          glassGradientPositions[0] = 1-1.0f;
    	          Paint glassPaint = new Paint();
    	          RadialGradient glassShader = new RadialGradient(px, py, 2*(int)innerRadius, glassGradientColors, glassGradientPositions, TileMode.CLAMP); 
    	          glassPaint.setShader(glassShader); 
    	          
    	          
    	          // 设置光源的方向
    	          float[] direction = new float[]{ 1, 1, 1 };
    	          //设置环境光亮度
    	          float light = 0.4f;
    	          // 选择要应用的反射等级
    	          float specular = 6;
    	          // 向mask应用一定级别的模糊
    	          float blur = 3.5f;
    	          EmbossMaskFilter emboss=new EmbossMaskFilter(direction,light,specular,blur);
    	          // 应用mask myPaint.setMaskFilter(emboss);
    	          
    	          glassPaint.setMaskFilter(emboss);
    	          
    	          canvas.drawOval(innerBoundingBox, glassPaint);
    	          
    	          
    	          
    	          
    	       //   mPaint.setShader(mRadialGradient);
    	      //      canvas.drawCircle(p.locationX, p.locationY, p.radius, mPaint);
    	    //       RectF oval=new  RectF(p.locationX-p.radius/2,p.locationY-p.radius,p.locationX+p.radius/2,p.locationY+p.radius);
    	     //      canvas.drawArc(oval, 0, 360, false, mPaint);
    	            p.death--;
    	            p.g=p.g- p.death*75/p.life;
    				p.radius+=2;
    				p.locationX += p.speedX;
    				p.locationY += p.speedY;
    			if(p.death < 0 || p.radius < 0){
    				//a brand new particle replacing the dead one
    				particles.set(i,new Particle(width,height));
    			}
    		}
            surfaceHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        };
        void ClearDraw() {
            Canvas canvas = surfaceHolder.lockCanvas(null);
            canvas.drawColor(Color.BLACK);// 清除画布
            surfaceHolder.unlockCanvasAndPost(canvas);
     
        }
        
//        @Override
//        public void onClick(View v) {        
//                switch (v.getId()) {
//                case R.id.iv_video_control:
//                	 //	this.findViewById(R.id.video_masking).setVisibility(View.INVISIBLE);
//                	 	this.findViewById(R.id.rl_video_control).setVisibility(View.INVISIBLE);
//                	 	mHandler.sendEmptyMessage(1);
//                	 	// play();
//                       // vv.start();
//                        break;
//                        
////                case R.id.btnpause:
////                        if (mediaPlayer.isPlaying()) {
////                                mediaPlayer.pause();
////                        }else{
////                                mediaPlayer.start();
////                        }
////                        break;
////
////                case R.id.btnstop:
////                        if (mediaPlayer.isPlaying()) {
////                                mediaPlayer.stop();
////                        }
////                        
////                        break;
//                default:
//                        break;
//                }
//
//        }
        @Override
        protected void onPause() {        
//                //先判断是否正在播放
//                if (mediaPlayer.isPlaying()) {
//                        //如果正在播放我们就先保存这个播放位置
//                        position=mediaPlayer.getCurrentPosition()
//                                        ;
//                        mediaPlayer.stop();
//                }
            super.onPause();
        }

        private void play() {
                try {
                        mediaPlayer.reset();
                        mediaPlayer
                        .setAudioStreamType(AudioManager.STREAM_MUSIC);
                        //设置需要播放的视频
                        String path="http://staticnova.ruoogle.com/video/2644197_avatar_201410171200001";
                        Uri url = Uri.parse(path);
                        mediaPlayer.setDataSource(this,url);
                        //把视频画面输出到SurfaceView
                        mediaPlayer.setDisplay(surfaceView.getHolder());
                        mediaPlayer.setOnBufferingUpdateListener(this); 
                        mediaPlayer.setOnCompletionListener(this);
                        mediaPlayer.setOnPreparedListener(this); 
                        mediaPlayer.prepareAsync();
                        //播放         
                } catch (Exception e) {
                        // TODO: handle exception
                }
        }
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub

            mediaPlayer.start();       
		}
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			 //mediaPlayer.stop();
			 mediaPlayer.reset();
			  String path="http://staticnova.ruoogle.com/video/2644197_avatar_201410171200001";
              Uri url = Uri.parse(path);
              try {
				mediaPlayer.setDataSource(this,url);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              mediaPlayer.prepareAsync();
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		

		public class GridPicListAdapter extends BaseAdapter {

		    
			OnLongClickListener onLongClickListener=new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
//					 int sdkInt = Build.VERSION.SDK_INT;
//	                 if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
//	                         ClipboardManager copy = (ClipboardManager) VideoPlayerArc.this
//	                                         .getSystemService(Context.CLIPBOARD_SERVICE);
//	                         copy.setText(((TextView) v).getText());
//	                         Toast.makeText(VideoPlayerArc.this, "邀请码成功复制到粘贴板",
//	                                         Toast.LENGTH_SHORT).show();
//	                 } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
//	                         android.text.ClipboardManager copyq = (android.text.ClipboardManager) VideoPlayerArc.this
//	                                         .getSystemService(Context.CLIPBOARD_SERVICE);
//	                         copyq.setText(((TextView) v).getText());
//	                         Toast.makeText(VideoPlayerArc.this, "邀请码成功复制到粘贴板",
//	                                         Toast.LENGTH_SHORT).show();
//	                 }
					return false;
				}
				
			};
			
			// 映射List
		    private List<Bitmap> picList;
		    private Context context;
		    public LayoutInflater inflater;
		    public GridPicListAdapter(Context context, List<Bitmap> picList) {
			this.context = context;
			this.picList = picList;
			this.inflater =LayoutInflater.from(context);
		    }

		    @Override
		    public int getCount() {
			return picList.size();
		    }

		    @Override
		    public Object getItem(int position) {
			return picList.get(position);
		    }

		    @Override
		    public long getItemId(int position) {
			return position;
		    }

		    @Override
		    public View getView(int position, View convertView, ViewGroup arg2) {
		    	
		    	
		    	 convertView = inflater.inflate(R.layout.list, null);
		    	    EditText text1 = (EditText) convertView.findViewById(R.id.text);
		    	    text1.setText("sdfsdf速度发生的发生");
		    	    text1.setTag("erefdgfd");
		    	    text1.setFocusable(true);
		    	    text1.setFocusableInTouchMode(true);
		    	    text1.setTextIsSelectable(true);
	 //   	    	text1.performLongClick();
		//    	    text1.setText(text1.getText(),BufferType.SPANNABLE );
//		    	    text1.setOnLongClickListener(new OnLongClickListener() {
//						@Override
//						public boolean onLongClick(View v) {
//							// TODO Auto-generated method stub
//							TextView tv=(TextView) v;
//							
//					//		tv.performLongClick();
//							tv.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
////							ClipboardManager cmb = (ClipboardManager) context
////
////
////									.getSystemService(Context.CLIPBOARD_SERVICE);
////
////
////									cmb.setText((CharSequence) v.getTag());
////									
////
////									Toast.makeText(context, "复制成功", Toast.LENGTH_LONG).show();
//							
//							return false;
//						}
//					});
//		    	    text1.setOnClickListener(new OnClickListener() {
//		    	        @Override
//		    	        public void onClick(View v) {
//		    	            // you just put your Logic here And use this custom adapter to
//		    	            // load your Data By using this particular custom adapter to
//		    	            // your listview
//
//		    	        }
//		    	    });
		    	return convertView;
//			ImageView iv_pic_item = null;
//			if (convertView == null) {
//			    iv_pic_item = new ImageView(context);
//			    // 设置布局 图片
//			    iv_pic_item.setLayoutParams(new GridView.LayoutParams(itemScreenWidth, itemScreenHeight));
//			    // 设置显示比例类型
//			    iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
//			} else {
//			    iv_pic_item = (ImageView) convertView;
//			}
//			iv_pic_item.setImageBitmap(picList.get(position));
//			propertyValuesHolder(iv_pic_item,position);
//			return iv_pic_item;
		    }
		}
	       
        /**
    	 * 抛物线
    	 * @param view
    	 */
    	public void paowuxian(View view)
    	{

    		ValueAnimator valueAnimator = new ValueAnimator();
    		valueAnimator.setDuration(3000);
    		valueAnimator.setObjectValues(new PointF(0, 0));
    		valueAnimator.setInterpolator(new LinearInterpolator());
    		valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
    		{
    			// fraction = t / duration
    			@Override
    			public PointF evaluate(float fraction, PointF startValue,
    					PointF endValue)
    			{
    				// x方向200px/s ，则y方向0.5 * 10 * t
    				PointF point = new PointF();
    				point.x = 200 * fraction * 3;
    				point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
    				return point;
    			}
    		});

    		valueAnimator.start();
    	}
        
        public void propertyValuesHolder(View view,int position)
    	{
    		PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("alpha", 1f,
    				0f);
    		PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f,
    				0);
    		PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f,
    				0);
    		int y = (position/nbWidth)*itemScreenHeight;
    		int x=position%nbWidth*itemScreenWidth;
    		
    		PropertyValuesHolder pvh4 = PropertyValuesHolder.ofFloat("x", x,(float)(Math.random()*(Math.max(x, bitWidth-x))));  
    		PropertyValuesHolder pvh5 = PropertyValuesHolder.ofFloat("y", y,(float)(Math.random()*(Math.max(y, bitHeight-y)))); 
    		ObjectAnimator.ofPropertyValuesHolder(view, pvh1,pvh2,pvh3,pvh4,pvh5).setDuration(2000).start();
    	}
        
    	private void initShareIntent(final Activity activity) {
  	      Intent intent = new Intent(Intent.ACTION_SEND);
  	      intent.setType("text/plain");
  	      List<ResolveInfo> resInfo =activity.getPackageManager().queryIntentActivities(
  	           intent, 0);
  	      if (!resInfo.isEmpty()) {
  	        List<Intent> targetedShareIntents = new ArrayList<Intent>();
  	        for (ResolveInfo info : resInfo) {
  	           Intent targeted = new Intent(Intent.ACTION_SEND);
  	           targeted.setType("text/plain");
  	           ActivityInfo activityInfo = info.activityInfo;

  	           
  	           
  	           // judgments :activityInfo.packageName, activityInfo.name, etc.
  	           if (activityInfo.packageName.contains("com.sina.weibo")||
  	        		 activityInfo.packageName.contains("com.tencent.mm")
  	                ) {

  	  	           Log.v("ting",activityInfo.packageName);
  	              targeted.putExtra(Intent.EXTRA_TEXT, "分享内容");
  	              targeted.setPackage(activityInfo.packageName);
  	              targetedShareIntents.add(targeted);
  	           }

  	        }
  	        Intent chooserIntent = Intent.createChooser(
  	              targetedShareIntents.remove(0), "选择分享");
  	        if (chooserIntent == null) {
  	           return;
  	        }
  	        
  	      Log.v("ting",targetedShareIntents.size()+"size");
  	        
  	        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
  	              targetedShareIntents.toArray(new Parcelable[]{}));
  	        try {
  	        	activity.startActivity(chooserIntent);
  	        } catch (android.content.ActivityNotFoundException ex) {
  	           Toast.makeText(activity, "Can't find sharecomponent to share",
  	                 Toast.LENGTH_SHORT).show();
  	        }
  	      }
  	   }
		
		
}
