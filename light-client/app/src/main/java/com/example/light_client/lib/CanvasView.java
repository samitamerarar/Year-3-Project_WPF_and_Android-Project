/**
 * CanvasView.java
 *
 * Copyright (c) 2014 Tomohiro IKEDA (Korilakkuma)
 * Released under the MIT license
 */

package com.example.light_client.lib;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;
import kotlin.Triple;
import com.example.light_client.Application;
import com.example.light_client.src.services.GameService;
import com.example.light_client.ui.game.AllGameModesState;

/**
 * This class defines fields and methods for drawing.
 */
public class CanvasView extends View {

    // Enumeration for Mode
    public enum Mode {
        DRAW,
        TEXT,
        ERASER;
    }

    // Enumeration for Drawer
    public enum Drawer {
        PEN,
        LINE,
        RECTANGLE_VERTICAL, // ADDED for Projet-3
        RECTANGLE_HORIZONTAL, // ADDED for Projet-3
        ERASER_SEGMENT, // ADDED for Projet-3
        CIRCLE,
        ELLIPSE,
        QUADRATIC_BEZIER,
        QUBIC_BEZIER;
    }

    float historicalX = 0F; // ADDED for Projet-3
    float historicalY = 0F; // ADDED for Projet-3
    float rectangleWidth = 0F; // ADDED for Projet-3
    float rectangleHeight = 0F; // ADDED for Projet-3
    float rectangleStrokeWeight = 10F; // ADDED for Projet-3
    List<List<Float>>  pointsListOfPathsList  = new ArrayList<>(); // ADDED for Projet-3
    List<Triple<Paint.Cap, Integer, Float>> paintOfPathsList = new ArrayList<>(); // ADDED for Projet-3

    // ADDED for Projet-3
    public List<List<Float>> getPointsListOfPathsList() { return this.pointsListOfPathsList; }
    // ADDED for Projet-3
    public void setPointsListOfPathsList(List<List<Float>> p) { this.pointsListOfPathsList = p; }

    // ADDED for Projet-3
    public List<Triple<Paint.Cap, Integer, Float>> getPaintOfPathsList() { return this.paintOfPathsList; }
    // ADDED for Projet-3
    public void setPaintOfPathsList(List<Triple<Paint.Cap, Integer, Float>> p) { this.paintOfPathsList = p; }

    public class Point {
        public float x;
        public float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private Boolean forOneSegment = false;

    private Canvas canvas   = null;
    private Bitmap bitmap   = null;

    private List<Path>  pathLists  = new ArrayList<Path>();
    private List<Paint> paintLists = new ArrayList<Paint>();

    private final Paint emptyPaint = new Paint();

    // for Eraser
    private int baseColor = Color.WHITE;

    // for Undo, Redo
    private int historyPointer = 0;

    // ADDED for Projet-3
    public int getHistoryPointer() { return this.historyPointer; }
    // ADDED for Projet-3
    public void setHistoryPointer(int h) { this.historyPointer = h; }

    // Flags
    private Mode mode      = Mode.DRAW;
    private Drawer drawer  = Drawer.PEN;
    private boolean isDown = false;

    // for Paint
    private Paint.Style paintStyle    = Paint.Style.STROKE;
    private int paintStrokeColor      = Color.BLACK;
    private int paintFillColor        = Color.BLACK;
    private float paintStrokeWidth    = 10F;
    private int opacity               = 255;
    private float blur                = 0F;
    private Paint.Cap lineCap         = Paint.Cap.ROUND;
    private PathEffect drawPathEffect = null;

    // for Text
    private String text           = "";
    private Typeface fontFamily   = Typeface.DEFAULT;
    private float fontSize        = 32F;
    private Paint.Align textAlign = Paint.Align.RIGHT;  // fixed
    private Paint textPaint       = new Paint();
    private float textX           = 0F;
    private float textY           = 0F;

    // for Drawer
    private float startX   = 0F;
    private float startY   = 0F;
    private float controlX = 0F;
    private float controlY = 0F;

    /**
     * Copy Constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setup();
    }

    /**
     * Copy Constructor
     *
     * @param context
     * @param attrs
     */
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    /**
     * Copy Constructor
     *
     * @param context
     */
    public CanvasView(Context context) {
        super(context);
        this.setup();
    }

    /**
     * Common initialization.
     *
     */
    public void setup() {

        this.pathLists.add(new Path());
        this.paintLists.add(this.createPaint());
        this.historyPointer++;

        this.textPaint.setARGB(0, 255, 255, 255);
    }

    /**
     * This method creates the instance of Paint.
     * In addition, this method sets styles for Paint.
     *
     * @return paint This is returned as the instance of Paint
     */
    private Paint createPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(this.paintStyle);
        paint.setStrokeWidth(this.paintStrokeWidth);
        paint.setStrokeCap(this.lineCap);
        paint.setStrokeJoin(Paint.Join.ROUND);  // fixed

        // for Text
        if (this.mode == Mode.TEXT) {
            paint.setTypeface(this.fontFamily);
            paint.setTextSize(this.fontSize);
            paint.setTextAlign(this.textAlign);
            paint.setStrokeWidth(0F);
        }

        if (this.mode == Mode.ERASER) {
            // Eraser
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paint.setARGB(0, 0, 0, 0);

            // paint.setColor(this.baseColor);
            // paint.setShadowLayer(this.blur, 0F, 0F, this.baseColor);
        } else {
            // Otherwise
            paint.setColor(this.paintStrokeColor);
            paint.setShadowLayer(this.blur, 0F, 0F, this.paintStrokeColor);
            paint.setAlpha(this.opacity);
            paint.setPathEffect(this.drawPathEffect);
        }

        return paint;
    }

    /**
     * This method initialize Path.
     * Namely, this method creates the instance of Path,
     * and moves current position.
     *
     * @param event This is argument of onTouchEvent method
     * @return path This is returned as the instance of Path
     */
    private Path createPath(MotionEvent event) {
        Path path = new Path();

        // Save for ACTION_MOVE
        this.startX = event.getX();
        this.startY = event.getY();

        path.moveTo(this.startX, this.startY);

        return path;
    }

    /**
     * This method updates the lists for the instance of Path and Paint.
     * "Undo" and "Redo" are enabled by this method.
     *
     * @param path the instance of Path
     */
    private void updateHistory(Path path) {
        if (this.historyPointer == this.pathLists.size()) {
            this.pathLists.add(path);
            this.paintLists.add(this.createPaint());
            this.historyPointer++;
        } else {
            // On the way of Undo or Redo
            this.pathLists.set(this.historyPointer, path);
            this.paintLists.set(this.historyPointer, this.createPaint());
            this.historyPointer++;

            for (int i = this.historyPointer, size = this.paintLists.size(); i < size; i++) {
                this.pathLists.remove(this.historyPointer);
                this.paintLists.remove(this.historyPointer);
            }
        }
    }

    /**
     * This method gets the instance of Path that pointer indicates.
     *
     * @return the instance of Path
     */
    private Path getCurrentPath() {
        return this.pathLists.get(this.historyPointer - 1);
    }

    /**
     * This method draws text.
     *
     * @param canvas the instance of Canvas
     */
    private void drawText(Canvas canvas) {
        if (this.text.length() <= 0) {
            return;
        }

        if (this.mode == Mode.TEXT) {
            this.textX = this.startX;
            this.textY = this.startY;

            this.textPaint = this.createPaint();
        }

        float textX = this.textX;
        float textY = this.textY;

        Paint paintForMeasureText = new Paint();

        // Line break automatically
        float textLength   = paintForMeasureText.measureText(this.text);
        float lengthOfChar = textLength / (float)this.text.length();
        float restWidth    = this.canvas.getWidth() - textX;  // text-align : right
        int numChars       = (lengthOfChar <= 0) ? 1 : (int)Math.floor((double)(restWidth / lengthOfChar));  // The number of characters at 1 line
        int modNumChars    = (numChars < 1) ? 1 : numChars;
        float y            = textY;

        for (int i = 0, len = this.text.length(); i < len; i += modNumChars) {
            String substring = "";

            if ((i + modNumChars) < len) {
                substring = this.text.substring(i, (i + modNumChars));
            } else {
                substring = this.text.substring(i, len);
            }

            y += this.fontSize;

            canvas.drawText(substring, textX, y, this.textPaint);
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionDown(MotionEvent event) {
        forOneSegment = true; // ADDED for Projet-3
        switch (this.mode) {
            case DRAW   :
            case ERASER :
                // ADDED for Projet-3
                if (this.drawer == Drawer.ERASER_SEGMENT) {
                    // outer : for (int i = 0; i < this.historyPointer-1; i++) {
                    outer : for (int i = this.historyPointer-2; i >= 0; i--) {
                        for (int j = 0; j < pointsListOfPathsList.get(i).size(); j+=2) {
                            float point = pointsListOfPathsList.get(i).get(j);
                            if ((point > event.getX() - 4f) && (point < event.getX() + 4f)) {
                                for (int k = 1; k < pointsListOfPathsList.get(i).size(); k+=2) {
                                    point = pointsListOfPathsList.get(i).get(k);
                                    if ((point > event.getY() - 10f) && (point < event.getY() + 10f)) {
                                        if (this.paintLists.get(i+1).getColor() != Color.WHITE) {
                                            this.pathLists.set(i+1, new Path());
                                            this.paintLists.set(i+1, new Paint());
                                            pointsListOfPathsList.set(i, new ArrayList<Float>());

                                            Application app = (Application) getActivity().getApplication();
                                            AllGameModesState state = app.getStateManager().get("gamemodes", AllGameModesState.class);
                                            GameService.DrawAction del = GameService.DrawAction.DEL;
                                            int idSegment = i;
                                            GameService.PointNature cap = GameService.PointNature.ellipse;
                                            GameService.PointParams pointParams = new GameService.PointParams(cap.name(), null, null,null);
                                            state.gameService.sendPoint(del, idSegment, pointParams, new Point(0f, 0f));

                                            break outer;
                                        }
                                        break outer;
                                    }
                                }
                            }
                        }
                    }
                    this.invalidate(); // ADDED for Projet-3
                }
                else {
                    if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                        // Oherwise
                        this.updateHistory(this.createPath(event));
                        // ADDED for Projet-3
                        if (historyPointer-1 > pointsListOfPathsList.size() || pointsListOfPathsList.isEmpty()) {
                            pointsListOfPathsList.add(new ArrayList<Float>());
                        }
                        this.isDown = true;
                    } else {
                        // Bezier
                        if ((this.startX == 0F) && (this.startY == 0F)) {
                            // The 1st tap
                            this.updateHistory(this.createPath(event));
                        } else {
                            // The 2nd tap
                            this.controlX = event.getX();
                            this.controlY = event.getY();

                            this.isDown = true;
                        }
                    }
                }

                break;
            case TEXT   :
                this.startX = event.getX();
                this.startY = event.getY();

                break;
            default :
                break;
        }
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    /**
     * This method defines processes on MotionEvent.ACTION_MOVE
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        // ADDED For Projet-3
        if (this.drawer != Drawer.ERASER_SEGMENT) {
            pointsListOfPathsList.get(historyPointer - 2).add(x);
            pointsListOfPathsList.get(historyPointer - 2).add(y);
            Application app = (Application) getActivity().getApplication();
            AllGameModesState state = app.getStateManager().get("gamemodes", AllGameModesState.class);

            GameService.DrawAction add = GameService.DrawAction.ADD;
            int idSegment = historyPointer - 2;
            GameService.PointNature cap = GameService.PointNature.ellipse;
            if (lineCap == Paint.Cap.SQUARE) cap = GameService.PointNature.rectangle;
            String color = "#" + Integer.toHexString(paintStrokeColor).toUpperCase(); // output = "00000000"
            GameService.PointParams pointParams = new GameService.PointParams(cap.name(), color, paintStrokeWidth,null);

            state.gameService.sendPoint(add, idSegment, pointParams, new Point(x,y));
        }

        switch (this.mode) {
            case DRAW   :
            case ERASER :

                if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                    if (!isDown) {
                        return;
                    }

                    Path path = this.getCurrentPath();
                    int historySize = event.getHistorySize(); // ADDED For Projet-3

                    switch (this.drawer) {
                        case PEN :
                            path.lineTo(x, y);
                            break;
                        case LINE :
                            path.reset();
                            path.moveTo(this.startX, this.startY);
                            path.lineTo(x, y);
                            break;
                        // ADDED For Projet-3
                        case RECTANGLE_HORIZONTAL :
                            x = event.getX();
                            y = event.getY();
                            rectangleWidth = x+rectangleStrokeWeight;
                            rectangleHeight = y;
                            path.addRect(x, y, rectangleWidth+20, rectangleHeight+5, Path.Direction.CCW);
                            path.addRect(x, y, rectangleWidth+40, rectangleHeight+10, Path.Direction.CCW);
                            if (historySize > 0) {
                                for (int i = 0; i < historySize; i++) {
                                    historicalX = event.getHistoricalX(i);
                                    historicalY = event.getHistoricalY(i);
                                    x = historicalX;
                                    y = historicalY;
                                    rectangleWidth = x+rectangleStrokeWeight;
                                    rectangleHeight = y;
                                    path.addRect(x, y, rectangleWidth+20, rectangleHeight+5, Path.Direction.CCW);
                                    path.addRect(x, y, rectangleWidth+40, rectangleHeight+10, Path.Direction.CCW);
                                /*path.addRect(x, y, rectangleWidth+60, rectangleHeight+15, Path.Direction.CCW);
                                path.addRect(x, y, rectangleWidth+80, rectangleHeight+20, Path.Direction.CCW);
                                path.addRect(x, y, rectangleWidth+100, rectangleHeight+25, Path.Direction.CCW);*/
                                }
                            }
                            break;
                        // ADDED For Projet-3
                        case RECTANGLE_VERTICAL :
                            x = event.getX();
                            y = event.getY();
                            rectangleWidth = x;
                            rectangleHeight = y + rectangleStrokeWeight;
                            path.addRect(x, y, rectangleWidth + 5, rectangleHeight + 20, Path.Direction.CCW);
                            path.addRect(x, y, rectangleWidth + 10, rectangleHeight + 40, Path.Direction.CCW);
                            if (historySize > 0) {
                                for (int i = 0; i < historySize; i++) {
                                    historicalX = event.getHistoricalX(i);
                                    historicalY = event.getHistoricalY(i);
                                    x = historicalX;
                                    y = historicalY;
                                    rectangleWidth = x;
                                    rectangleHeight = y + rectangleStrokeWeight;
                                    path.addRect(x, y, rectangleWidth + 5, rectangleHeight + 20, Path.Direction.CCW);
                                    path.addRect(x, y, rectangleWidth + 10, rectangleHeight + 40, Path.Direction.CCW);
                                /*path.addRect(x, y, rectangleWidth+15, rectangleHeight+60, Path.Direction.CCW);
                                path.addRect(x, y, rectangleWidth+20, rectangleHeight+80, Path.Direction.CCW);
                                path.addRect(x, y, rectangleWidth+25, rectangleHeight+100, Path.Direction.CCW);*/
                                }
                            }
                            break;
                        case CIRCLE :
                            double distanceX = Math.abs((double)(this.startX - x));
                            double distanceY = Math.abs((double)(this.startX - y));
                            double radius    = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

                            path.reset();
                            path.addCircle(this.startX, this.startY, (float)radius, Path.Direction.CCW);
                            break;
                        case ELLIPSE :
                            RectF rect = new RectF(this.startX, this.startY, x, y);

                            path.reset();
                            path.addOval(rect, Path.Direction.CCW);
                            break;
                        default :
                            break;
                    }
                } else {
                    if (!isDown) {
                        return;
                    }

                    Path path = this.getCurrentPath();

                    path.reset();
                    path.moveTo(this.startX, this.startY);
                    path.quadTo(this.controlX, this.controlY, x, y);
                }

                break;
            case TEXT :
                this.startX = x;
                this.startY = y;

                break;
            default :
                break;
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionUp(MotionEvent event) {
        if (isDown) {
            this.startX = 0F;
            this.startY = 0F;
            this.isDown = false;
        }
    }

    /**
     * This method updates the instance of Canvas (View)
     *
     * @param canvas the new instance of Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Before "drawPath"
        canvas.drawColor(this.baseColor);

        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, 0F, 0F, emptyPaint);
        }

        for (int i = 0; i < this.historyPointer; i++) {
            Path path   = this.pathLists.get(i);
            Paint paint = this.paintLists.get(i);

            canvas.drawPath(path, paint);
        }

        this.drawText(canvas);

        this.canvas = canvas;
    }

    /**
     * This method set event listener for drawing.
     *
     * @param event the instance of MotionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE :
                this.onActionMove(event);
                break;
            case MotionEvent.ACTION_UP :
                this.onActionUp(event);
                break;
            default :
                break;
        }

        // Re gif_draw
        this.invalidate();

        return true;
    }

    /**
     * This method is getter for mode.
     *
     * @return
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * This method is setter for mode.
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * This method is getter for drawer.
     *
     * @return
     */
    public Drawer getDrawer() {
        return this.drawer;
    }

    /**
     * This method is setter for drawer.
     *
     * @param drawer
     */
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    /**
     * This method checks if Undo is available
     *
     * @return If Undo is available, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean canUndo() {
        return this.historyPointer > 1;
    }

    /**
     * This method checks if Redo is available
     *
     * @return If Redo is available, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean canRedo() {
        return this.historyPointer < this.pathLists.size();
    }

    /**
     * This method draws canvas again for Undo.
     *
     * @return If Undo is enabled, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean undo() {
        if (canUndo()) {
            this.historyPointer--;
            this.invalidate();

            return true;
        } else {
            return false;
        }
    }

    /**
     * This method draws canvas again for Redo.
     *
     * @return If Redo is enabled, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean redo() {
        if (canRedo()) {
            this.historyPointer++;
            this.invalidate();

            return true;
        } else {
            return false;
        }
    }

    /**
     * This method initializes canvas.
     *
     * @return
     */
    public void clear() {
        Path path = new Path();
        path.moveTo(0F, 0F);
        path.addRect(0F, 0F, 1000F, 1000F, Path.Direction.CCW);
        path.close();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        if (this.historyPointer == this.pathLists.size()) {
            this.pathLists.add(path);
            this.paintLists.add(paint);
            this.historyPointer++;
        } else {
            // On the way of Undo or Redo
            this.pathLists.set(this.historyPointer, path);
            this.paintLists.set(this.historyPointer, paint);
            this.historyPointer++;

            for (int i = this.historyPointer, size = this.paintLists.size(); i < size; i++) {
                this.pathLists.remove(this.historyPointer);
                this.paintLists.remove(this.historyPointer);
            }
        }

        this.text = "";

        // Clear
        this.invalidate();
    }

    /**
     * This method is getter for canvas background color
     *
     * @return
     */
    public int getBaseColor() {
        return this.baseColor;
    }

    /**
     * This method is setter for canvas background color
     *
     * @param color
     */
    public void setBaseColor(int color) {
        this.baseColor = color;
    }

    /**
     * This method is getter for drawn text.
     *
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     * This method is setter for drawn text.
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method is getter for stroke or fill.
     *
     * @return
     */
    public Paint.Style getPaintStyle() {
        return this.paintStyle;
    }

    /**
     * This method is setter for stroke or fill.
     *
     * @param style
     */
    public void setPaintStyle(Paint.Style style) {
        this.paintStyle = style;
    }

    /**
     * This method is getter for stroke color.
     *
     * @return
     */
    public int getPaintStrokeColor() {
        return this.paintStrokeColor;
    }

    /**
     * This method is setter for stroke color.
     *
     * @param color
     */
    public void setPaintStrokeColor(int color) {
        this.paintStrokeColor = color;
    }

    /**
     * This method is getter for fill color.
     * But, current Android API cannot set fill color (?).
     *
     * @return
     */
    public int getPaintFillColor() {
        return this.paintFillColor;
    };

    /**
     * This method is setter for fill color.
     * But, current Android API cannot set fill color (?).
     *
     * @param color
     */
    public void setPaintFillColor(int color) {
        this.paintFillColor = color;
    }

    /**
     * This method is getter for stroke width.
     *
     * @return
     */
    public float getPaintStrokeWidth() {
        return this.paintStrokeWidth;
    }

    /**
     * This method is setter for stroke width.
     *
     * @param width
     */
    public void setPaintStrokeWidth(float width) {
        if (width >= 0) {
            this.paintStrokeWidth = width;
        } else {
            this.paintStrokeWidth = 3F;
        }
    }

    // ADDED for Projet-3
    public void setRectangleStrokeWeight(float weight) {
        if (weight >= 0) {
            this.rectangleStrokeWeight = weight;
        } else {
            this.rectangleStrokeWeight = 3F;
        }
    }

    public float getRectangleStrokeWeight() {
        return this.rectangleStrokeWeight;
    }

    /**
     * This method is getter for alpha.
     *
     * @return
     */
    public int getOpacity() {
        return this.opacity;
    }

    /**
     * This method is setter for alpha.
     * The 1st argument must be between 0 and 255.
     *
     * @param opacity
     */
    public void setOpacity(int opacity) {
        if ((opacity >= 0) && (opacity <= 255)) {
            this.opacity = opacity;
        } else {
            this.opacity= 255;
        }
    }

    /**
     * This method is getter for amount of blur.
     *
     * @return
     */
    public float getBlur() {
        return this.blur;
    }

    /**
     * This method is setter for amount of blur.
     * The 1st argument is greater than or equal to 0.0.
     *
     * @param blur
     */
    public void setBlur(float blur) {
        if (blur >= 0) {
            this.blur = blur;
        } else {
            this.blur = 0F;
        }
    }

    public float[] measurePath(Path path) {
        PathMeasure pm = new PathMeasure(path, false);
        //coordinates will be here
        float aCoordinates[] = {0f, 0f};

        //get point from the middle
        pm.getPosTan(pm.getLength(), aCoordinates, null);

        return aCoordinates;
    }

    /**
     * This method is getter for line cap.
     *
     * @return
     */
    public Paint.Cap getLineCap() {
        return this.lineCap;
    }

    /**
     * This method is setter for line cap.
     *
     * @param cap
     */
    public void setLineCap(Paint.Cap cap) {
        this.lineCap = cap;
    }

    /**
     * This method is getter for path effect of drawing.
     *
     * @return drawPathEffect
     */
    public PathEffect getDrawPathEffect() {
        return drawPathEffect;
    }

    /**
     * This method is setter for path effect of drawing.
     *
     * @param drawPathEffect
     */
    public void setDrawPathEffect(PathEffect drawPathEffect) {
        this.drawPathEffect = drawPathEffect;
    }
    /**
     * This method is getter for font size,
     *
     * @return
     */
    public float getFontSize() {
        return this.fontSize;
    }

    /**
     * This method is setter for font size.
     * The 1st argument is greater than or equal to 0.0.
     *
     * @param size
     */
    public void setFontSize(float size) {
        if (size >= 0F) {
            this.fontSize = size;
        } else {
            this.fontSize = 32F;
        }
    }

    /**
     * This method is getter for font-family.
     *
     * @return
     */
    public Typeface getFontFamily() {
        return this.fontFamily;
    }

    /**
     * This method is setter for font-family.
     *
     * @param face
     */
    public void setFontFamily(Typeface face) {
        this.fontFamily = face;
    }

    /**
     * This method gets current canvas as bitmap.
     *
     * @return This is returned as bitmap.
     */
    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(false);
        this.setDrawingCacheEnabled(true);

        return Bitmap.createBitmap(this.getDrawingCache());
    }

    /**
     * This method gets current canvas as scaled bitmap.
     *
     * @return This is returned as scaled bitmap.
     */
    public Bitmap getScaleBitmap(int w, int h) {
        this.setDrawingCacheEnabled(false);
        this.setDrawingCacheEnabled(true);

        return Bitmap.createScaledBitmap(this.getDrawingCache(), w, h, true);
    }

    /**
     * This method draws the designated bitmap to canvas.
     *
     * @param bitmap
     */
    public void drawBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.invalidate();
    }

    /**
     * This method draws the designated byte array of bitmap to canvas.
     *
     * @param byteArray This is returned as byte array of bitmap.
     */
    public void drawBitmap(byte[] byteArray) {
        this.drawBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    /**
     * This static method gets the designated bitmap as byte array.
     *
     * @param bitmap
     * @param format
     * @param quality
     * @return This is returned as byte array of bitmap.
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap, CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This method gets the bitmap as byte array.
     *
     * @param format
     * @param quality
     * @return This is returned as byte array of bitmap.
     */
    public byte[] getBitmapAsByteArray(CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.getBitmap().compress(format, quality, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This method gets the bitmap as byte array.
     * Bitmap format is PNG, and quality is 100.
     *
     * @return This is returned as byte array of bitmap.
     */
    public byte[] getBitmapAsByteArray() {
        return this.getBitmapAsByteArray(CompressFormat.PNG, 100);
    }

}