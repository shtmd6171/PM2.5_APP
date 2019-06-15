package com.example.assortment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CustomDustMapView extends View {

    // 상수
    // 지동상의 지역정보 표시 위치를 위해 지도위를 블럭화 시킨다.
    private static final float POSITION_MATRIX_ROW = 40F;   // 지도상 세로 블럭 크기
    private static final float POSITION_MATRIX_LINE = 20F;  // 지도상 가로 블럭 크기

    // 레이아웃 관련 변수
    private Context mRootContext = null;
    private Rect mMapArea = new Rect(); // 지도가 그려지는 영역
    private float mPosMatrixLineOne = -1, mPosMatrixRowOne = -1;    // 지도의 위치블럭의 1칸의 간격들
    private int mViewWidth = -1, mViewHeight = -1;  // 현재 뷰의 크기(가로, 세로)

    private Bitmap mMapBitmap = null;   // 그려저야하는 지도 이미지 (화면의 크기에 맞게 리사이즈 후 이미지)
    private ArrayList<DustInfo> mArrDustInfo = new ArrayList<DustInfo>();   // 전국을 표시하기 위한 미세먼지 정보 리스트

    // 선, 객체 색상 정보
    private Paint mTestBarPaint = new Paint();
    private Paint mBoxDustPatin = new Paint();

    // 각 위치정보에 대한 수집정보
    public class DustInfo {
        public String DUST_TITLE = null;
        public int DUST_VALUE_10 = -1;
        public int DUST_VALUE_25 = -1;
        public PointF SHOW_POS = null;
        public Boolean IS_INITED = false;

        public DustInfo(String title, int value_10, int value_25, PointF pos) {
            this.DUST_TITLE = title;
            this.DUST_VALUE_10 = value_10;
            this.DUST_VALUE_25 = value_25;
            this.SHOW_POS = pos;
        }

        public void updateValue(int value_10, int value_25) {
            this.DUST_VALUE_10 = value_10;
            this.DUST_VALUE_25 = value_25;
            this.IS_INITED = true;
        }

        public String getDustValue() {
            return (DUST_VALUE_10 +"㎍/㎥" +  "\n" +  DUST_VALUE_25 +"㎍/㎥\n");
        }
    }

    // 생성자
    public CustomDustMapView(@NonNull Context context) {
        super(context);

        mRootContext = context;
        initLayout();
    }

    // 생성자
    public CustomDustMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mRootContext = context;
        initLayout();
    }

    // 생성자
    public CustomDustMapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRootContext = context;
        initLayout();
    }

    // 레이아웃 초기화
    private void initLayout() {

        // 지역정보 초기화
        mArrDustInfo.add(new DustInfo("서울", 0, 0, new PointF(5, 5)));           // 서울
        mArrDustInfo.add(new DustInfo("경기도", 0, 0, new PointF(6, 10)));        // 경기도
        mArrDustInfo.add(new DustInfo("강원도", 0, 0, new PointF(11, 7)));        // 강원도
        mArrDustInfo.add(new DustInfo("충청북도", 0, 0, new PointF(8, 14)));      // 충청북도
        mArrDustInfo.add(new DustInfo("충청남도", 0, 0, new PointF(4, 17)));      // 충청남도
        mArrDustInfo.add(new DustInfo("경상북도", 0, 0, new PointF(12, 17)));     // 경상북도
        mArrDustInfo.add(new DustInfo("경상남도", 0, 0, new PointF(11, 26)));     // 경상남도
        mArrDustInfo.add(new DustInfo("전라북도", 0, 0, new PointF(5, 22)));      // 전라북도
        mArrDustInfo.add(new DustInfo("전라남도", 0, 0, new PointF(4, 28)));      // 전라남도
        mArrDustInfo.add(new DustInfo("제주도", 0, 0, new PointF(3, 36)));        // 제주도

        // 배경 색 설정
        this.setBackgroundColor(0xFFb3e0ff);

        // 페인트 초기화
        mTestBarPaint.setColor(Color.GREEN);
        mTestBarPaint.setStyle(Paint.Style.FILL);
        mTestBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mTestBarPaint.setAntiAlias(true);

        mBoxDustPatin.setColor(Color.rgb(0, 0, 0));
        mBoxDustPatin.setTextAlign(Paint.Align.CENTER);
        mBoxDustPatin.setTextSize(DpToPx(14));
        mBoxDustPatin.setAntiAlias(true);
    }

    // 가로 세로 계산 하기
    private int getResizeableHeight(int img_width, int img_height, int target_size, boolean isg_width_fix_mode) {

        try {
            if (isg_width_fix_mode) {
                float rate = target_size / (float) img_width;
                int res_size = (int) (img_height * rate);
                return res_size;

            } else {
                float rate = target_size / (float) img_height;
                int res_size = (int) (img_width * rate);
                return res_size;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    // 지도 이미지 지정된 크기로 리사이징
    private Bitmap resizeMap(Bitmap oriBitmap, int width, int height) {

        try {
            Bitmap resize = Bitmap.createScaledBitmap(oriBitmap, width, height, true);
            return resize;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // 특정 위치 그리기
    private void drawDustInfo(Canvas canvas, DustInfo dustInfo) {
        float pos_x = mMapArea.left + (dustInfo.SHOW_POS.x * mPosMatrixLineOne);
        float pos_y = mMapArea.top + (dustInfo.SHOW_POS.y * mPosMatrixRowOne);

        Rect textRect = new Rect();
        mBoxDustPatin.getTextBounds(dustInfo.DUST_TITLE, 0, dustInfo.DUST_TITLE.length(), textRect);
        canvas.drawText(dustInfo.DUST_TITLE, pos_x + (textRect.width() / 2), pos_y + textRect.height(), mBoxDustPatin);

        if (dustInfo.IS_INITED) {
            Rect textRect2 = new Rect();
            pos_y += textRect.height() + DpToPx(0);
            mBoxDustPatin.getTextBounds(dustInfo.getDustValue(), 0, dustInfo.getDustValue().length(), textRect2);
            canvas.drawText(dustInfo.getDustValue(), pos_x + (textRect.width() / 2), pos_y + textRect2.height(), mBoxDustPatin);
        }
    }

    // DP 정수 값을 PX로 변한하여 값을 변환함.
    private static int DpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // 값 업데이트
    public void updateDustInfo(String area, int value_10, int value_25) {

        for (DustInfo info : mArrDustInfo) {
            if (info.DUST_TITLE.compareTo(area) == 0) {
                info.updateValue(value_10, value_25);
                invalidate();
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 드로잉 최초에 화면의 크기를 구하고 이미지의 크기 및 이미지의 박스 사이즈룰 설정하고
        // 위치 분활을 통해 각 위치 정보를 표시할 점을 획득한다.
        if (mViewWidth == -1 && mViewHeight == -1) {
            Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.m_map);
            if (map != null) {
                int imgWidth = getWidth();
                int imgHeight = getResizeableHeight(map.getWidth(), map.getHeight(), imgWidth, true);
                if (getHeight() < imgHeight) {
                    imgHeight = getHeight();
                    imgWidth = getResizeableHeight(map.getWidth(), map.getHeight(), imgHeight, false);
                }

                mMapBitmap = resizeMap(map, imgWidth, imgHeight);
                if (mMapBitmap != null) {
                    mViewWidth = getWidth();
                    mViewHeight = getHeight();

                    mMapArea.left = ((mViewWidth - imgWidth) / 2);
                    mMapArea.right = mViewWidth - ((mViewWidth - imgWidth) / 2);
                    mMapArea.top = ((mViewHeight - imgHeight) / 2);
                    mMapArea.bottom = mViewHeight - ((mViewHeight - imgHeight) / 2);

                    mPosMatrixLineOne = (float) mMapArea.width() / POSITION_MATRIX_LINE;
                    mPosMatrixRowOne = (float) mMapArea.height() / POSITION_MATRIX_ROW;
                }
            }
        }

        // 지도 그리기
        if (mMapBitmap != null) {
            canvas.drawBitmap(mMapBitmap, mMapArea.left, mMapArea.top, null);

            // 매트릭스 그리기 확인용
            // 지도상의 지역 글씨 위치를 잡기 위한 테스트용 라인 그리기
//            int pos_x = mMapArea.left;
//            for (int pos = 0; pos < (int) POSITION_MATRIX_LINE; pos++) {
//                pos_x += mPosMatrixLineOne;
//                canvas.drawLine(pos_x, mMapArea.top, pos_x, mMapArea.bottom, mTestBarPaint);
//            }
//            int pos_y = mMapArea.top;
//            for (int pos = 0; pos < (int) POSITION_MATRIX_ROW; pos++) {
//                pos_y += mPosMatrixRowOne;
//                canvas.drawLine(mMapArea.left, pos_y, mMapArea.right, pos_y, mTestBarPaint);
//            }
        }

        // 각 지역 그리기
        for (DustInfo info : mArrDustInfo) {
            drawDustInfo(canvas, info);
        }
    }
}
