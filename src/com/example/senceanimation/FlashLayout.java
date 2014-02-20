package com.example.senceanimation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class FlashLayout extends SurfaceView implements Callback,Runnable{
	private int paoCount = 0;
	private Paint paint;
	private Canvas canvas;
	private int width;
	private int height;
	private Bitmap bitmap = null;
	private SurfaceHolder holder;
	List<BallInfo> balls = new ArrayList<BallInfo>();
	private int ballHeight = 0;
	private int ballWidth = 0;
	private boolean isRunning = true;
	public FlashLayout(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.qipao);
		ballHeight = bitmap.getHeight();
		ballWidth = bitmap.getWidth();
	}
	
	private void initBalls() {
        //随机个数和步长
        double random = Math.random();
        paoCount = (int) (random * 10);
        for(int i = 0;i< paoCount;i++) {
            double bpRandom = Math.random();
            balls.add(new BallInfo((float) (width * bpRandom), (float) (height * bpRandom),true,true, (float) (bpRandom*10),(float) (bpRandom * 10)));
        }
	}

	class BallInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private float w;
		private float h;
		private boolean isWidAdd = true;
		private boolean isHeiAdd = true;
		private float wStep; //横向移动距离
		private float hStep;//纵向移动距离
		public float getW() {
			return w;
		}
		public void setW(float w) {
			this.w = w;
		}
		public float getH() {
			return h;
		}
		public void setH(float h) {
			this.h = h;
		}
		public boolean isWidAdd() {
			return isWidAdd;
		}
		public void setWidAdd(boolean isWidAdd) {
			this.isWidAdd = isWidAdd;
		}
		public boolean isHeiAdd() {
			return isHeiAdd;
		}
		public void setHeiAdd(boolean isHeiAdd) {
			this.isHeiAdd = isHeiAdd;
		}
		public float getwStep() {
			return wStep;
		}
		public void setwStep(float wStep) {
			this.wStep = wStep;
		}
		public float gethStep() {
			return hStep;
		}
		public void sethStep(float hStep) {
			this.hStep = hStep;
		}
		private BallInfo(float w, float h, boolean isWidAdd, boolean isHeiAdd,
				float wStep, float hStep) {
			super();
			this.w = w;
			this.h = h;
			this.isWidAdd = isWidAdd;
			this.isHeiAdd = isHeiAdd;
			this.wStep = wStep;
			this.hStep = hStep;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		 //获取屏幕宽度   
        width = getWidth() - ballWidth;  
        //获取屏幕高度   
        height = getHeight() - ballHeight;
        initBalls();
        //启动绘图线程   
        new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRunning = false;
	}
	
	@Override
	public void run() {
		 while(isRunning){  
             try{  
                 myDraw();   
                 Thread.sleep(50);  
             }catch(InterruptedException e){  
                 e.printStackTrace();  
             }  
         }  
	}

	private void myDraw() {
		paint = new Paint();
		paint.setAntiAlias(true);
		canvas = holder.lockCanvas();
		canvas.drawColor(Color.WHITE);
        for(BallInfo b :balls) {
            drawBall(b);
        }
		holder.unlockCanvasAndPost(canvas);
	}
	
	private void drawBall(BallInfo ballInfo) {
		if(ballInfo.getW() >= width) {//到width尾
			ballInfo.setWidAdd(false);
		}else if(ballInfo.getW() <=0) {//到width头
			ballInfo.setWidAdd(true);
		}
		
		if(ballInfo.getH() >= height) {//到height尾
			ballInfo.setHeiAdd(false);
		}else if(ballInfo.getH()<=0) {//到height头
			ballInfo.setHeiAdd(true);
		} 
		
		if(ballInfo.isWidAdd) {
			ballInfo.setW(ballInfo.getW()+ ballInfo.getwStep());
		} else {
			ballInfo.setW(ballInfo.getW()- ballInfo.getwStep());
		}
		if(ballInfo.isHeiAdd) {
			ballInfo.setH(ballInfo.getH() + ballInfo.gethStep());
		} else {
			ballInfo.setH(ballInfo.getH() - ballInfo.gethStep());
		}
		canvas.drawBitmap(bitmap, ballInfo.getW(), ballInfo.getH(), paint);
	}

}
